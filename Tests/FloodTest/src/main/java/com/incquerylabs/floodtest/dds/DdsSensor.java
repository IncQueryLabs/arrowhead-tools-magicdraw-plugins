package com.incquerylabs.floodtest.dds;

import java.time.Instant;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.incquerylabs.floodtest.Constants;
import com.incquerylabs.floodtest.Sensor;
import com.rti.dds.domain.DomainParticipant;
import com.rti.dds.domain.DomainParticipantFactory;
import com.rti.dds.infrastructure.ResourceLimitsQosPolicy;
import com.rti.dds.infrastructure.StatusKind;
import com.rti.dds.infrastructure.StringSeq;
import com.rti.dds.publication.Publisher;
import com.rti.dds.subscription.DataReader;
import com.rti.dds.subscription.DataReaderAdapter;
import com.rti.dds.subscription.InstanceStateKind;
import com.rti.dds.subscription.SampleInfoSeq;
import com.rti.dds.subscription.SampleStateKind;
import com.rti.dds.subscription.Subscriber;
import com.rti.dds.subscription.ViewStateKind;
import com.rti.dds.topic.Topic;
import com.rti.dds.type.builtin.StringDataReader;
import com.rti.dds.type.builtin.StringDataWriter;

public class DdsSensor extends Thread implements Sensor {

	DomainParticipant participant;
	Publisher publisher;
	Subscriber subscriber;
	StringDataReader reader;
	Topic topic;
	Instant start;
	Instant end;
	StringSeq dataSeq = new StringSeq();
	SampleInfoSeq infoSeq = new SampleInfoSeq();
	MqttClient mqc;
	MqttMessage emptyMessage = new MqttMessage();
	String type;
	String name;

	public DdsSensor(String type) {
		this.type = type;
		name = "DdsSensor" + type;
		try {
			if (type.equals("A")) {
				mqc = new MqttClient("tcp://" + Constants.SERVER_IP + ":" + Constants.MQTT_SERVER_PORT, name,
						new MemoryPersistence());
			} else {
				mqc = new MqttClient("tcp://" + Constants.SERVER_IP + ":" + Constants.MQTT_SERVER_PORT, name,
						new MemoryPersistence());
			}
			MqttConnectOptions options = new MqttConnectOptions();
			options.setAutomaticReconnect(true);
			options.setCleanSession(true);
			options.setConnectionTimeout(10);
			mqc.connect(options);
		} catch (MqttException e) {
			System.out.println("Excepton in MQTT creation in " + name);
		}
		participant = DomainParticipantFactory.TheParticipantFactory.create_participant(Constants.DDS_DOMAIN_NUMBER,
				DomainParticipantFactory.PARTICIPANT_QOS_DEFAULT, null, StatusKind.STATUS_MASK_NONE);
		participant.add_peer("udpv4://" + Constants.SERVER_IP);
		subscriber = participant.create_subscriber(DomainParticipant.SUBSCRIBER_QOS_DEFAULT, null,
				StatusKind.STATUS_MASK_NONE);
		publisher = participant.create_publisher(DomainParticipant.PUBLISHER_QOS_DEFAULT, null,
				StatusKind.STATUS_MASK_NONE);
		if (type.equals("A")) {
			topic = participant.create_topic(Constants.DDS_SENSOR_A_TOPIC_NAME, Constants.DDS_TYPE_NAME,
					DomainParticipant.TOPIC_QOS_DEFAULT, null, StatusKind.STATUS_MASK_NONE);
		} else {
			topic = participant.create_topic(Constants.DDS_SENSOR_B_TOPIC_NAME, Constants.DDS_TYPE_NAME,
					DomainParticipant.TOPIC_QOS_DEFAULT, null, StatusKind.STATUS_MASK_NONE);
		}
		reader = (StringDataReader) subscriber.create_datareader(topic, Subscriber.DATAREADER_QOS_DEFAULT,
				new DdsSensorListener(), StatusKind.STATUS_MASK_ALL);
	}

	@Override
	public void kill() {
		if (mqc != null) {
			try {
				mqc.disconnect();
				mqc.close();
			} catch (MqttException e) {
				System.out.println("Problem on closing MQTT connection in " + name);
			}
			mqc = null;
		}
		if (participant != null) {
			participant.delete_contained_entities();
			DomainParticipantFactory.TheParticipantFactory.delete_participant(participant);
			participant = null;
		}
		this.interrupt();
	}

	private class DdsSensorListener extends DataReaderAdapter {
		@Override
		public void on_data_available(DataReader arg0) {
			StringDataReader getter = (StringDataReader) arg0;
			try {
				getter.take(dataSeq, infoSeq, ResourceLimitsQosPolicy.LENGTH_UNLIMITED,
						SampleStateKind.ANY_SAMPLE_STATE, ViewStateKind.ANY_VIEW_STATE,
						InstanceStateKind.ALIVE_INSTANCE_STATE);
				for (int i = 0; i < dataSeq.size(); ++i) {
					mqc.publish(Constants.RECEIVED_TOPIC_NAME, emptyMessage);
					if (infoSeq.get(i).valid_data) {
						System.out.println(name + " received request from Processor.");
						String got = (String) dataSeq.get(i);
						Topic responseTopic = participant.create_topic(got, Constants.DDS_TYPE_NAME,
								DomainParticipant.TOPIC_QOS_DEFAULT, null, StatusKind.STATUS_MASK_NONE);
						StringDataWriter writer = (StringDataWriter) publisher.create_datawriter(responseTopic,
								Publisher.DATAWRITER_QOS_DEFAULT, null, StatusKind.STATUS_MASK_NONE);
						mqc.publish(Constants.SENT_TOPIC_NAME, emptyMessage);
						writer.write("", null);
					}
				}
			} catch (MqttException m) {
				System.out.println("Excepton in Aux publishing in DDS Consumer listener.");
			} catch (Exception e) {
				System.out.println("Exception in DDS Consumer listener");
			} finally {
				getter.return_loan(dataSeq, infoSeq);
			}
		}
	}

	@Override
	public void run() {
		System.out.println(name + " ready");
	}
}

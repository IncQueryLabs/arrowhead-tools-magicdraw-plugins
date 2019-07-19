package com.incquerylabs.floodtest.dds;

import java.time.Instant;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.incquerylabs.floodtest.Constants;
import com.incquerylabs.floodtest.Processor;
import com.rti.dds.domain.DomainParticipant;
import com.rti.dds.domain.DomainParticipantFactory;
import com.rti.dds.infrastructure.InstanceHandle_t;
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

public class DdsProcessor extends Thread implements Processor {

	DomainParticipant participant;
	Publisher publisher;
	Subscriber subscriber;
	StringDataReader processorReader;
	StringDataReader sensorAReader;
	StringDataReader sensorBReader;
	StringDataWriter processorWriter;
	StringDataWriter sensorAWriter;
	StringDataWriter sensorBWriter;
	Topic processorForwardTopic;
	Topic processorBackwardTopic;
	Topic sensorAForwardTopic;
	String backATopicName;
	Topic sensorABackwardTopic;
	Topic sensorBForwardTopic;
	String backBTopicName;
	Topic sensorBBackwardTopic;
	Instant start;
	Instant end;
	MqttClient mqc;
	MqttMessage emptyMessage = new MqttMessage();
	String type;
	volatile boolean a = false;
	volatile boolean b = false;
	String name;

	public DdsProcessor(int n) {
		name = "DdsProc" + n;
		backATopicName = Constants.DDS_SENSOR_A_TOPIC_NAME + n;
		backBTopicName = Constants.DDS_SENSOR_B_TOPIC_NAME + n;
		try {
			mqc = new MqttClient("tcp://" + Constants.SERVER_IP + ":" + Constants.MQTT_SERVER_PORT, name,
					new MemoryPersistence());
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

		processorForwardTopic = participant.create_topic(Constants.DDS_PROCESSOR_FORWARD_TOPIC_NAME,
				Constants.DDS_TYPE_NAME, DomainParticipant.TOPIC_QOS_DEFAULT, null, StatusKind.STATUS_MASK_NONE);
		processorBackwardTopic = participant.create_topic(Constants.DDS_PROCESSOR_BACKWARD_TOPIC_NAME,
				Constants.DDS_TYPE_NAME, DomainParticipant.TOPIC_QOS_DEFAULT, null, StatusKind.STATUS_MASK_NONE);
		sensorAForwardTopic = participant.create_topic(Constants.DDS_SENSOR_A_TOPIC_NAME, Constants.DDS_TYPE_NAME,
				DomainParticipant.TOPIC_QOS_DEFAULT, null, StatusKind.STATUS_MASK_NONE);
		sensorABackwardTopic = participant.create_topic(backATopicName, Constants.DDS_TYPE_NAME,
				DomainParticipant.TOPIC_QOS_DEFAULT, null, StatusKind.STATUS_MASK_NONE);
		sensorBForwardTopic = participant.create_topic(Constants.DDS_SENSOR_B_TOPIC_NAME, Constants.DDS_TYPE_NAME,
				DomainParticipant.TOPIC_QOS_DEFAULT, null, StatusKind.STATUS_MASK_NONE);
		sensorBBackwardTopic = participant.create_topic(backBTopicName, Constants.DDS_TYPE_NAME,
				DomainParticipant.TOPIC_QOS_DEFAULT, null, StatusKind.STATUS_MASK_NONE);

		processorReader = (StringDataReader) subscriber.create_datareader(processorForwardTopic,
				Subscriber.DATAREADER_QOS_DEFAULT, new DdsProcessorListener("P"), StatusKind.STATUS_MASK_ALL);
		sensorAReader = (StringDataReader) subscriber.create_datareader(sensorABackwardTopic,
				Subscriber.DATAREADER_QOS_DEFAULT, new DdsProcessorListener("A"), StatusKind.STATUS_MASK_ALL);
		sensorBReader = (StringDataReader) subscriber.create_datareader(sensorBBackwardTopic,
				Subscriber.DATAREADER_QOS_DEFAULT, new DdsProcessorListener("B"), StatusKind.STATUS_MASK_ALL);

		processorWriter = (StringDataWriter) publisher.create_datawriter(processorBackwardTopic,
				Publisher.DATAWRITER_QOS_DEFAULT, null, StatusKind.STATUS_MASK_NONE);
		sensorAWriter = (StringDataWriter) publisher.create_datawriter(sensorAForwardTopic,
				Publisher.DATAWRITER_QOS_DEFAULT, null, StatusKind.STATUS_MASK_NONE);
		sensorBWriter = (StringDataWriter) publisher.create_datawriter(sensorBForwardTopic,
				Publisher.DATAWRITER_QOS_DEFAULT, null, StatusKind.STATUS_MASK_NONE);
	}

	private class DdsProcessorListener extends DataReaderAdapter {

		StringSeq dataSeq = new StringSeq();
		SampleInfoSeq infoSeq = new SampleInfoSeq();
		String type;

		public DdsProcessorListener(String tip) {
			type = tip;
		}

		@Override
		public void on_data_available(DataReader arg0) {
			StringDataReader reader = (StringDataReader) arg0;
			try {
				reader.take(dataSeq, infoSeq, ResourceLimitsQosPolicy.LENGTH_UNLIMITED,
						SampleStateKind.ANY_SAMPLE_STATE, ViewStateKind.ANY_VIEW_STATE,
						InstanceStateKind.ALIVE_INSTANCE_STATE);
				for (int i = 0; i < dataSeq.size(); ++i) {
					mqc.publish(Constants.RECEIVED_TOPIC_NAME, emptyMessage);
					if (infoSeq.get(i).valid_data) {
						switch (type) {
						case "P":
							System.out.println(name + " received quest from consumer");
							mqc.publish(Constants.SENT_TOPIC_NAME, emptyMessage);
							mqc.publish(Constants.SENT_TOPIC_NAME, emptyMessage);
							sensorAWriter.write(backATopicName, InstanceHandle_t.HANDLE_NIL);
							sensorBWriter.write(backBTopicName, InstanceHandle_t.HANDLE_NIL);
							a = false;
							b = false;
							break;
						case "A":
							if (b && !a) {
								mqc.publish(Constants.SENT_TOPIC_NAME, emptyMessage);
								processorWriter.write("megvan", InstanceHandle_t.HANDLE_NIL);
								a = true;
							}
							break;
						case "B":
							if (a && !b) {
								mqc.publish(Constants.SENT_TOPIC_NAME, emptyMessage);
								processorWriter.write("megvan", InstanceHandle_t.HANDLE_NIL);
								b = true;
							}
							break;
						}
					}
				}
			} catch (MqttException m) {
				System.out.println("Excepton in Aux publishing in " + name);
			} finally {
				reader.return_loan(dataSeq, infoSeq);
			}
		}
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

	@Override
	public void run() {
		System.out.println(name + " ready");
	}
}

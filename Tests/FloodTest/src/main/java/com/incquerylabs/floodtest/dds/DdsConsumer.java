package com.incquerylabs.floodtest.dds;

import java.time.Instant;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.incquerylabs.floodtest.Constants;
import com.incquerylabs.floodtest.Consumer;
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
import com.rti.dds.type.builtin.StringTypeSupport;

public class DdsConsumer extends DataReaderAdapter implements Consumer {

	DomainParticipant participant;
	Publisher publisher;
	StringDataWriter writer;
	Subscriber subscriber;
	StringDataReader reader;
	Topic forwardTopic;
	Topic backwardTopic;
	Instant start;
	Instant end;
	private volatile boolean waitForResponse = true;
	StringSeq dataSeq = new StringSeq();
	SampleInfoSeq infoSeq = new SampleInfoSeq();
	MqttClient mqc;

	public DdsConsumer() {
		try {
			mqc = new MqttClient("tcp://" + Constants.SERVER_IP + ":" + Constants.MQTT_SERVER_PORT, "ddscon",
					new MemoryPersistence());
			MqttConnectOptions options = new MqttConnectOptions();
			options.setAutomaticReconnect(true);
			options.setCleanSession(true);
			options.setConnectionTimeout(10);
			mqc.connect(options);
		} catch (MqttException e) {
			System.out.println("Excepton in MQTT creation in DDS Consumer.");
		}
		participant = DomainParticipantFactory.TheParticipantFactory.create_participant(Constants.DDS_DOMAIN_NUMBER,
				DomainParticipantFactory.PARTICIPANT_QOS_DEFAULT, null, StatusKind.STATUS_MASK_NONE);
		subscriber = participant.create_subscriber(DomainParticipant.SUBSCRIBER_QOS_DEFAULT, null,
				StatusKind.STATUS_MASK_NONE);
		publisher = participant.create_publisher(DomainParticipant.PUBLISHER_QOS_DEFAULT, null,
				StatusKind.STATUS_MASK_NONE);
		String typeName = StringTypeSupport.get_type_name();
		forwardTopic = participant.create_topic(Constants.DDS_PROCESSOR_FORWARD_TOPIC_NAME, typeName,
				DomainParticipant.TOPIC_QOS_DEFAULT, null, StatusKind.STATUS_MASK_NONE);
		backwardTopic = participant.create_topic(Constants.DDS_PROCESSOR_BACKWARD_TOPIC_NAME, typeName,
				DomainParticipant.TOPIC_QOS_DEFAULT, null, StatusKind.STATUS_MASK_NONE);
		writer = (StringDataWriter) publisher.create_datawriter(forwardTopic, Publisher.DATAWRITER_QOS_DEFAULT, null,
				StatusKind.STATUS_MASK_NONE);
		reader = (StringDataReader) subscriber.create_datareader(backwardTopic, Subscriber.DATAREADER_QOS_DEFAULT, this,
				StatusKind.STATUS_MASK_NONE);
	}

	@Override
	public void go() {
		try {
			start = Instant.now();
			mqc.publish(Constants.SENT_TOPIC_NAME, null);
			writer.write("", null);
			while (waitForResponse) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					System.out.println("Waiting interrupted in DDS Consumer.");
				}
			}
			end = Instant.now();
		} catch (MqttException m) {
			System.out.println("Excepton in Aux publishing in DDS Consumer.");
		} finally {
			if (mqc != null) {
				try {
					mqc.close();
				} catch (MqttException e) {
					System.out.println("Problem on closing MQTT connection in Arrowhead Consumer.");
				}
				mqc = null;
			}
		}
	}

	@Override
	public void on_data_available(DataReader arg0) {
		StringDataReader getter = (StringDataReader) arg0;
		try {
			getter.take(dataSeq, infoSeq, ResourceLimitsQosPolicy.LENGTH_UNLIMITED, SampleStateKind.ANY_SAMPLE_STATE,
					ViewStateKind.ANY_VIEW_STATE, InstanceStateKind.ALIVE_INSTANCE_STATE);
			for (int i = 0; i < dataSeq.size(); ++i) {
				mqc.publish(Constants.RECEIVED_TOPIC_NAME, null);
				if (infoSeq.get(i).valid_data) {
					waitForResponse = false;
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

	@Override
	public Instant getEnd() {
		return end;
	}

	@Override
	public Instant getStart() {
		return start;
	}
}

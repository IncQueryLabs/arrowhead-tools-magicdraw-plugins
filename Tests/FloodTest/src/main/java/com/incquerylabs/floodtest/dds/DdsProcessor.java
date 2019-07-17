package com.incquerylabs.floodtest.dds;

import java.time.Instant;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.incquerylabs.floodtest.Constants;
import com.incquerylabs.floodtest.Processor;
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
	Topic sensorABackwardTopic;
	Topic sensorBForwardTopic;
	Topic sensorBBackwardTopic;
	Instant start;
	Instant end;
	StringSeq dataSeq = new StringSeq();
	SampleInfoSeq infoSeq = new SampleInfoSeq();
	MqttClient mqc;
	String type;
	String typeName;
	volatile boolean a = false;
	volatile boolean b = false;

	public DdsProcessor(int n) {

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
		typeName = StringTypeSupport.get_type_name();

		processorForwardTopic = participant.create_topic(Constants.DDS_PROCESSOR_FORWARD_TOPIC_NAME, typeName,
				DomainParticipant.TOPIC_QOS_DEFAULT, null, StatusKind.STATUS_MASK_NONE);
		processorBackwardTopic = participant.create_topic(Constants.DDS_PROCESSOR_BACKWARD_TOPIC_NAME, typeName,
				DomainParticipant.TOPIC_QOS_DEFAULT, null, StatusKind.STATUS_MASK_NONE);
		sensorAForwardTopic = participant.create_topic(Constants.DDS_SENSOR_A_TOPIC_NAME, typeName,
				DomainParticipant.TOPIC_QOS_DEFAULT, null, StatusKind.STATUS_MASK_NONE);
		sensorABackwardTopic = participant.create_topic(Constants.DDS_SENSOR_TOPIC_BASENAME + "A" + n, typeName,
				DomainParticipant.TOPIC_QOS_DEFAULT, null, StatusKind.STATUS_MASK_NONE);
		sensorBForwardTopic = participant.create_topic(Constants.DDS_SENSOR_B_TOPIC_NAME, typeName,
				DomainParticipant.TOPIC_QOS_DEFAULT, null, StatusKind.STATUS_MASK_NONE);
		sensorBBackwardTopic = participant.create_topic(Constants.DDS_SENSOR_TOPIC_BASENAME + "B" + n, typeName,
				DomainParticipant.TOPIC_QOS_DEFAULT, null, StatusKind.STATUS_MASK_NONE);

		processorReader = (StringDataReader) subscriber.create_datareader(processorForwardTopic,
				Subscriber.DATAREADER_QOS_DEFAULT, new DdsProceesorListener("P"), StatusKind.STATUS_MASK_NONE);
		sensorAReader = (StringDataReader) subscriber.create_datareader(sensorABackwardTopic,
				Subscriber.DATAREADER_QOS_DEFAULT, new DdsProceesorListener("A"), StatusKind.STATUS_MASK_NONE);
		sensorBReader = (StringDataReader) subscriber.create_datareader(sensorBBackwardTopic,
				Subscriber.DATAREADER_QOS_DEFAULT, new DdsProceesorListener("B"), StatusKind.STATUS_MASK_NONE);

		processorWriter = (StringDataWriter) publisher.create_datawriter(processorBackwardTopic,
				Publisher.DATAWRITER_QOS_DEFAULT, null, StatusKind.STATUS_MASK_NONE);
		sensorAWriter = (StringDataWriter) publisher.create_datawriter(sensorAForwardTopic,
				Publisher.DATAWRITER_QOS_DEFAULT, null, StatusKind.STATUS_MASK_NONE);
		sensorBWriter = (StringDataWriter) publisher.create_datawriter(sensorBForwardTopic,
				Publisher.DATAWRITER_QOS_DEFAULT, null, StatusKind.STATUS_MASK_NONE);
	}

	private class DdsProceesorListener extends DataReaderAdapter {
		String type;

		public DdsProceesorListener(String s) {
			type = s;
		}

		@Override
		public void on_data_available(DataReader arg0) {
			StringDataReader getter = (StringDataReader) arg0;
			try {
				getter.take(dataSeq, infoSeq, ResourceLimitsQosPolicy.LENGTH_UNLIMITED,
						SampleStateKind.ANY_SAMPLE_STATE, ViewStateKind.ANY_VIEW_STATE,
						InstanceStateKind.ALIVE_INSTANCE_STATE);
				for (int i = 0; i < dataSeq.size(); ++i) {
					mqc.publish(Constants.RECEIVED_TOPIC_NAME, null);
					switch (type) {
					case "P":
						mqc.publish(Constants.RECEIVED_TOPIC_NAME, null);
						mqc.publish(Constants.RECEIVED_TOPIC_NAME, null);
						sensorAWriter.write("", null);
						sensorBWriter.write("", null);
						break;
					case "A":
						if (b == true) {
							if(a != true) {
								mqc.publish(Constants.SENT_TOPIC_NAME, null);
								processorWriter.write("", null);
								a = true;
							}
						} else {
							a = true;
						}
						break;
					case "B":
						if (a == true) {
							if(b != true) {
								mqc.publish(Constants.SENT_TOPIC_NAME, null);
								processorWriter.write("", null);
								b = true;
							}
						} else {
							a = true;
						}
						break;
					}
				}
			} catch (MqttException m) {
				System.out.println("Excepton in Aux publishing in DDS Processor listener.");
			} catch (Exception e) {
				System.out.println("Exception in DDS Consumer listener");
			} finally {
				getter.return_loan(dataSeq, infoSeq);
			}
		}
	}

	@Override
	public void kill() {
		if(mqc != null) {
			try {
				mqc.close();
			} catch (MqttException e) {
				System.out.println("Problem on closing MQTT connection in MqttSensor");
			}
			mqc = null;
		}
		this.interrupt();
	}
}

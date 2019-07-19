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
import com.rti.dds.infrastructure.StatusKind;
import com.rti.dds.infrastructure.StringSeq;
import com.rti.dds.publication.Publisher;
import com.rti.dds.subscription.DataReader;
import com.rti.dds.subscription.DataReaderAdapter;
import com.rti.dds.subscription.SampleInfoSeq;
import com.rti.dds.subscription.Subscriber;
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
	MqttClient mqc;
	MqttMessage emptyMessage = new MqttMessage();
	String type;
	String typeName;
	volatile boolean a = false;
	volatile boolean b = false;
	String name;

	public DdsProcessor(int n) {
		name = "DdsProc" + n;
		try {
			mqc = new MqttClient("tcp://" + Constants.SERVER_IP + ":" + Constants.MQTT_SERVER_PORT, name,
					new MemoryPersistence());
			MqttConnectOptions options = new MqttConnectOptions();
			options.setAutomaticReconnect(true);
			options.setCleanSession(true);
			options.setConnectionTimeout(10);
			mqc.connect(options);
		} catch (MqttException e) {
			System.out.println("Excepton in MQTT creation in " + n);
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

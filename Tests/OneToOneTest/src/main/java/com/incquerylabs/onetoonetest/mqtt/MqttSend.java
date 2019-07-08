package com.incquerylabs.onetoonetest.mqtt;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.incquerylabs.onetoonetest.Sender;

public class MqttSend implements Sender {

	public static final String TOPIC = MqttRec.TOPIC;
	private static String IP = "127.0.0.1";
	private static int port = MqttRec.MOSQUITTO_PORT;
	private static String name = "MY life";
	MqttClient mqc;

	public MqttSend() {
		try {
			mqc = new MqttClient("tcp://" + IP + ":" + port, name, new MemoryPersistence());
			MqttConnectOptions options = new MqttConnectOptions();
			options.setAutomaticReconnect(true);
			options.setCleanSession(true);
			options.setConnectionTimeout(10);
		
			mqc.connect(options);
		} catch (MqttException e) {
			System.out.println("Excepton in MQTT creation.");
		}
	}

	@Override
	public void send(int n,  File file) {
		try {
			ByteBuffer buf = ByteBuffer.allocate((int) (file.length() + 5)).putInt(n)
					.put(Files.readAllBytes(file.toPath()));
			MqttMessage message = new MqttMessage(buf.array());
			message.setQos(2);
			mqc.publish(TOPIC, message);
		} catch (IOException e) {
			System.out.println("bad");
		} catch (MqttException e) {
			System.out.println("mqt?");
		}
	}
}

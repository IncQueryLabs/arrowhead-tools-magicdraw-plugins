package com.incquerylabs.conptest.mqtt;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.time.Instant;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.incquerylabs.conptest.Sender;

public class MqttSend extends MqttClient implements Sender{

	File file;
	public static final String TOPIC = MqttRec.TOPIC;
	private static String IP = "127.0.0.1";
    private static int port = MqttRec.MOSQUITTO_PORT;
    private static String name = "MY life";
    
	public MqttSend(File file) throws MqttException {
		super("tcp://" + IP + ":" + port, name, new MemoryPersistence());
		this.file = file;
		MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        
        connect(options);
	}
	
	@Override
	public Instant send(int n) {
		Instant mid = null;
		try {
			mid = Instant.now();
			ByteBuffer buf = ByteBuffer.allocate((int) (file.length() + 5)).putInt(n).put(Files.readAllBytes(file.toPath()));
			MqttMessage message = new MqttMessage(buf.array());
			message.setQos(2);
			publish(TOPIC, message);
		} catch (IOException e) {
			System.out.println("bad");
		} catch (MqttException e) {
			System.out.println("mqt?");
		}
		return mid;
	}
}

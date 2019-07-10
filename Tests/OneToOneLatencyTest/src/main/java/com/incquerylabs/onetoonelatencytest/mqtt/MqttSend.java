package com.incquerylabs.onetoonelatencytest.mqtt;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.incquerylabs.onetoonelatencytest.Constants;
import com.incquerylabs.onetoonelatencytest.Sender;

public class MqttSend implements MqttCallback, Sender {

	private static String name = "MY life";
	MqttClient mqc = null;
	Map<Integer, Instant> times = new HashMap<Integer, Instant>();
	volatile boolean unk = true;

	public MqttSend() {
		try {
			mqc = new MqttClient("tcp://" + Constants.MQTT_SERVER_IP + ":" + Constants.MQTT_SERVER_PORT, name, new MemoryPersistence());
			MqttConnectOptions options = new MqttConnectOptions();
			options.setAutomaticReconnect(true);
			options.setCleanSession(true);
			options.setConnectionTimeout(10);
			mqc.setCallback(this);
			
			
			mqc.connect(options);
			//mqc.subscribe(Constants.MQTT_BACKWARD_TOPIC_NAME, Constants.MQTT_QOS);
		} catch (MqttException e) {
			System.out.println("Excepton in MQTT creation.");
		}
	}

	@Override
	public void send(int n, File file) {
		try {
			ByteBuffer buf = ByteBuffer.allocate((int) (file.length())).put(Files.readAllBytes(file.toPath()));
			MqttMessage message = new MqttMessage(buf.array());
			message.setQos(2);
			times.put(0, Instant.now());
			for(int i = 0; i < n; ++i) {
				System.out.println("Start sending message " + (i + 1));
				mqc.publish(Constants.MQTT_FORWARD_TOPIC_NAME, message);
				message = new MqttMessage(buf.array());
				message.setQos(Constants.MQTT_QOS);
				while(unk) {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						;
					}
				}
				times.put(Integer.valueOf(i + 1), Instant.now());
				unk = true;
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					;
				}
			}
		} catch (IOException e) {
			System.out.println("bad");
		} catch (MqttException e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Override
	public void kill() {
		if(mqc != null) {
			try {
				mqc.disconnect();
			} catch (MqttException e) {
				System.out.println("MQTT rec unable to disconnect");
			}
			try {
				mqc.close();
			} catch (MqttException e) {
				System.out.println("MQTT rec unable to close");
			}
			mqc = null;
		}
	}

	@Override
	public void connectionLost(Throwable cause) {
		System.out.println("Connection lost because: " + cause);
		System.exit(1);
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		//TODO
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws MqttException {
		unk = false;
	}
	
	@Override
	public Map<Integer, Instant> getTimes() {
		return times;
	}
}

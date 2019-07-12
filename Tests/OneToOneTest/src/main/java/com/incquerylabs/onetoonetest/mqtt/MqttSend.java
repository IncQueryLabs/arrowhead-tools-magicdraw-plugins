package com.incquerylabs.onetoonetest.mqtt;

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

import com.incquerylabs.onetoonetest.Constants;
import com.incquerylabs.onetoonetest.Sender;

public class MqttSend implements MqttCallback, Sender {

	private static String name = "MY life";
	MqttClient mqc = null;
	Map<Integer, Instant> times = new HashMap<Integer, Instant>();
	private volatile boolean waitForResponse = true;

	public MqttSend() {
		try {
			mqc = new MqttClient("tcp://" + Constants.SERVER_IP + ":" + Constants.MQTT_SERVER_PORT, name,
					new MemoryPersistence());
			MqttConnectOptions options = new MqttConnectOptions();
			options.setAutomaticReconnect(true);
			options.setCleanSession(true);
			options.setConnectionTimeout(10);

			mqc.setCallback(this);
			mqc.connect(options);
			mqc.subscribe(Constants.MQTT_BACKWARD_TOPIC_NAME, Constants.MQTT_QOS);
		} catch (MqttException e) {
			System.out.println("Excepton in MQTT creation.");
		}
	}

	@Override
	public void send(int n, File file) {
		try {
			times.put(0, Instant.now());
			ByteBuffer buf = ByteBuffer.allocate((int) (file.length())).put(Files.readAllBytes(file.toPath()));
			for (int i = 0; i < n; ++i) {
				MqttMessage message = new MqttMessage(buf.array());
				message.setQos(Constants.MQTT_QOS);
				System.out.println("MQTT message number " + Integer.valueOf(i + 1) + " sent.");
				mqc.publish(Constants.MQTT_FORWARD_TOPIC_NAME, message);

				while (waitForResponse) {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						System.out.println("MQTT Send interrupted while waiting for confirmation.");
					}
				}

				waitForResponse = true;
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					System.out.println("MQTT Send interrupted while cleaning up.");
				}
				times.put(Integer.valueOf(i + 1), Instant.now());
			}

		} catch (IOException e) {
			System.out.println("bad");
		} catch (MqttException e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void kill() {
		if (mqc != null) {
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
	public Map<Integer, Instant> getTimes() {
		return times;
	}

	@Override
	public void connectionLost(Throwable cause) {
		System.out.println("Connection lost because: " + cause);
		System.exit(1);
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		waitForResponse = false;
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
	}
}

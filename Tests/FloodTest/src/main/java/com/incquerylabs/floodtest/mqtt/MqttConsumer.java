package com.incquerylabs.floodtest.mqtt;

import java.time.Instant;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.incquerylabs.floodtest.Constants;
import com.incquerylabs.floodtest.Consumer;

public class MqttConsumer implements Consumer, MqttCallback{

	Instant start;
	Instant end = null;
	MqttClient mqc = null;
	MqttMessage emptyMessage = new MqttMessage();
	volatile boolean waiting = true;
	String name = "MttqConsumer";
	
	public MqttConsumer() {
		try {
			mqc = new MqttClient("tcp://" + Constants.SERVER_IP + ":" + Constants.MQTT_SERVER_PORT, name,
					new MemoryPersistence());
			MqttConnectOptions options = new MqttConnectOptions();
			options.setAutomaticReconnect(true);
			options.setCleanSession(true);
			options.setConnectionTimeout(10);
			mqc.setCallback(this);
			mqc.connect(options);
			mqc.publish(Constants.SENT_TOPIC_NAME, emptyMessage);
			mqc.subscribe(Constants.MQTT_PROCESSOR_BACKWARD_TOPIC_NAME);
		} catch (MqttException e) {
			System.out.println("Excepton in MQTT creation in " + name);
		}
	}
	
	@Override
	public void go() {
		start = Instant.now();
		try {
			mqc.publish(Constants.SENT_TOPIC_NAME, emptyMessage);
			mqc.publish(Constants.MQTT_PROCESSOR_FORWARD_TOPIC_NAME, emptyMessage);
			System.out.println(name + " sent for Processor");
			while(waiting) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					System.out.println("Waiting interrupted in" + name);
				}
			}
		} catch (MqttException e) {
			System.out.println("Excepton in MQTT publishing in " + name);
		} finally {
			if(mqc != null) {
				try {
					mqc.disconnect();
					mqc.close();
				} catch (MqttException m) {
					System.out.println("Excepton in closing in " + name);
				}
			}
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

	@Override
	public void connectionLost(Throwable cause) {
		System.out.println("Connection lost because: " + cause);
		System.exit(1);
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		System.out.println(name + " received answer from Processor.");
		mqc.publish(Constants.RECEIVED_TOPIC_NAME, emptyMessage);
		if(end == null) {
			end = Instant.now();
			waiting = false;
		}
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
	}
}

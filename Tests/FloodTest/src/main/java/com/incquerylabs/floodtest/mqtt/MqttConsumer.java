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
	Instant end;
	MqttClient mqc = null;
	MqttMessage emptyMessage = new MqttMessage();
	volatile boolean waiting = true;
	
	public MqttConsumer() {
		try {
			mqc = new MqttClient("tcp://" + Constants.SERVER_IP + ":" + Constants.MQTT_SERVER_PORT, "MCONS",
					new MemoryPersistence());
			MqttConnectOptions options = new MqttConnectOptions();
			options.setAutomaticReconnect(true);
			options.setCleanSession(true);
			options.setConnectionTimeout(10);
			mqc.connect(options);
			mqc.publish(Constants.SENT_TOPIC_NAME, emptyMessage);
			mqc.subscribe(Constants.MQTT_PROCESSOR_BACKWARD_TOPIC_NAME);
		} catch (MqttException e) {
			System.out.println("Excepton in MQTT creation in MQTT Consumer.");
		}
	}
	
	@Override
	public void go() {
		try {
			mqc.publish(Constants.SENT_TOPIC_NAME, emptyMessage);
			mqc.publish(Constants.MQTT_PROCESSOR_FORWARD_TOPIC_NAME, emptyMessage);
			while(waiting) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					System.out.println("Waiting interrupted in MQTT Consumer.");
				}
			}
			end = Instant.now();
		} catch (MqttException e) {
			System.out.println("Excepton in MQTT publishing in MQTT Consumer.");
		} finally {
			if(mqc != null) {
				try {
					mqc.disconnect();
					mqc.close();
				} catch (MqttException m) {
					System.out.println("Excepton in closing in MQTT Consumer.");
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
		mqc.publish(Constants.RECEIVED_TOPIC_NAME, emptyMessage);
		end = Instant.now();
		waiting = false;
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
	}
}

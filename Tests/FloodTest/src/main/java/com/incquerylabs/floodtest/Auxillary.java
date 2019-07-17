package com.incquerylabs.floodtest;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Auxillary implements MqttCallback {

	MqttClient mqc = null;
	Integer sends = 0;
	Integer receits = 0;

	public Auxillary() {
		try {
			mqc = new MqttClient("tcp://" + Constants.SERVER_IP + ":" + Constants.MQTT_SERVER_PORT, "Aux",
					new MemoryPersistence());
			MqttConnectOptions options = new MqttConnectOptions();
			options.setAutomaticReconnect(true);
			options.setCleanSession(true);
			options.setConnectionTimeout(10);
			mqc.setCallback(this);
			mqc.connect(options);
			mqc.subscribe(Constants.SENT_TOPIC_NAME);
			mqc.subscribe(Constants.RECEIVED_TOPIC_NAME);
		} catch (MqttException e) {
			System.out.println("Excepton in MQTT creation in Aux");
		}
	}

	@Override
	public void connectionLost(Throwable cause) {
		System.out.println("Connection lost because: " + cause);
		System.exit(1);
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		switch (topic) {
		case Constants.SENT_TOPIC_NAME:
			sends = sends + 1;
			break;
		case Constants.RECEIVED_TOPIC_NAME:
			receits = receits + 1;
			break;
		}
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
	}
	
	public Integer getSends() {
		return sends;
	}
	
	public Integer getReceits() {
		return receits;
	}
	
	public void kill() {
		if(mqc != null) {
			try {
				mqc.close();
			} catch (MqttException e) {
				System.out.println("Problem on closing MQTT connection in Aux");
			}
			mqc = null;
		}
	}
}

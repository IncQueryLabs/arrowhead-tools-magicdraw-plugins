package com.incquerylabs.floodtest.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.incquerylabs.floodtest.Constants;
import com.incquerylabs.floodtest.Sensor;

public class MqttSensor extends Thread implements Sensor, MqttCallback {

	String type;
	MqttClient mqc = null;
	MqttMessage emptyMessage = new MqttMessage();
	String name;

	public MqttSensor(String type) {
		this.type = type;
		name = "MqttSensor" + type;
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
			if (type.equals("A")) {
				mqc.subscribe(Constants.MQTT_SENSOR_A_TOPIC_NAME);
			} else {
				mqc.subscribe(Constants.MQTT_SENSOR_B_TOPIC_NAME);
			}
		} catch (MqttException e) {
			System.out.println("Excepton in MQTT creation in " + name);
		}
	}

	@Override
	public void run() {
		System.out.println(name + " ready");
	}

	@Override
	public void kill() {
		if (mqc != null) {
			try {
				mqc.disconnect();
				mqc.close();
			} catch (MqttException e) {
				System.out.println("Problem on closing MQTT connection in MqttSensor");
			}
			mqc = null;
		}
		this.interrupt();
	}

	@Override
	public void connectionLost(Throwable cause) {
		System.out.println("Connection lost because: " + cause);
		System.exit(1);
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		mqc.publish(Constants.RECEIVED_TOPIC_NAME, emptyMessage);
		System.out.println(name + "received request from Processor.");
		mqc.publish(Constants.SENT_TOPIC_NAME, emptyMessage);
		String rect = new String(message.getPayload());
		mqc.publish(rect, emptyMessage);
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
	}
}

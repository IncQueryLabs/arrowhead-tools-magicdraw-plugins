package com.incquerylabs.raceconditiontest.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.incquerylabs.raceconditiontest.Constants;
import com.incquerylabs.raceconditiontest.Sensor;

public class MqttSensor extends Thread implements Sensor, MqttCallback {

	String type;
	MqttClient mqc = null;

	public MqttSensor(String type) {
		this.type = type;
		try {
			if (type.equals("A")) {
				mqc = new MqttClient("tcp://" + Constants.SERVER_IP + ":" + Constants.MQTT_SERVER_PORT, "MSensorA",
						new MemoryPersistence());
			} else {
				mqc = new MqttClient("tcp://" + Constants.SERVER_IP + ":" + Constants.MQTT_SERVER_PORT, "MSensorB",
						new MemoryPersistence());
			}
			MqttConnectOptions options = new MqttConnectOptions();
			options.setAutomaticReconnect(true);
			options.setCleanSession(true);
			options.setConnectionTimeout(10);
			mqc.setCallback(this);
			mqc.connect(options);
			mqc.publish(Constants.SENT_TOPIC_NAME, null);
			if (type.equals("A")) {
				mqc.subscribe(Constants.MQTT_SENSOR_A_TOPIC_NAME);
			} else {
				mqc.subscribe(Constants.MQTT_SENSOR_B_TOPIC_NAME);
			}
		} catch (MqttException e) {
			System.out.println("Excepton in MQTT creation in Arrowhead Sensor " + type + ".");
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

	@Override
	public void connectionLost(Throwable cause) {
		System.out.println("Connection lost because: " + cause);
		System.exit(1);
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		mqc.publish(Constants.RECEIVED_TOPIC_NAME, null);
		mqc.publish(Constants.SENT_TOPIC_NAME, null);
		String rect = new String(message.getPayload());
		mqc.publish(rect, null);
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
	}
}

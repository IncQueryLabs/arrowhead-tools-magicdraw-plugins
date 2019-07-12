package com.incquerylabs.onetoonetest.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.incquerylabs.onetoonetest.Constants;
import com.incquerylabs.onetoonetest.Receiver;

public class MqttRec extends Thread implements MqttCallback, Receiver {

	private MqttClient mqc = null;
	private String recName = "Mqtt rec";

	@Override
	public void run() {
		try {
			mqc = new MqttClient("tcp://" + Constants.SERVER_IP + ":" + Constants.MQTT_SERVER_PORT, recName,
					new MemoryPersistence());
			MqttConnectOptions options = new MqttConnectOptions();
			options.setAutomaticReconnect(true);
			options.setCleanSession(true);
			options.setConnectionTimeout(10);
			
			mqc.setCallback(this);
			mqc.connect(options);

			mqc.subscribe(Constants.MQTT_FORWARD_TOPIC_NAME, Constants.MQTT_QOS);
			System.out.println("MQTT Subscription succesful.");
		} catch (MqttException e) {
			System.out.println("MQtteX");
		}
	}

	@Override
	public void connectionLost(Throwable cause) {
		System.out.println("Connection lost because: " + cause);
		System.exit(1);
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws MqttException {
		System.out.println("MQTT message received.");
		
		MqttMessage response = new MqttMessage("gg".getBytes());
		response.setQos(Constants.MQTT_QOS);
		mqc.publish(Constants.MQTT_BACKWARD_TOPIC_NAME, response);
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
			this.interrupt();
		}
	}
}

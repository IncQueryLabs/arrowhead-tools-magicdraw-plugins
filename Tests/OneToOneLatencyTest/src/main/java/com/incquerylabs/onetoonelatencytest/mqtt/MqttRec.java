package com.incquerylabs.onetoonelatencytest.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.incquerylabs.onetoonelatencytest.Constants;
import com.incquerylabs.onetoonelatencytest.Receiver;

public class MqttRec extends Thread implements MqttCallback, Receiver {

	private MqttClient rec = null;
	private String recName = "Mqtt rec";

	@Override
	public void run() {
		MqttConnectOptions options = new MqttConnectOptions();
		options.setAutomaticReconnect(true);
		options.setCleanSession(true);
		options.setConnectionTimeout(10);
		String url = "tcp://" + Constants.MQTT_SERVER_IP + ":" + Constants.MQTT_SERVER_PORT;

		try {
			rec = new MqttClient(url, recName, new MemoryPersistence());
			rec.setCallback(this);
			rec.connect(options);

			rec.subscribe(Constants.MQTT_FORWARD_TOPIC_NAME, Constants.MQTT_QOS);
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
		//ByteBuffer buf = ByteBuffer.wrap(message.getPayload());
	}

	@Override
	public void kill() {
		if(rec != null) {
			try {
				rec.disconnect();
			} catch (MqttException e) {
				System.out.println("MQTT rec unable to disconnect");
			}
			try {
				rec.close();
			} catch (MqttException e) {
				System.out.println("MQTT rec unable to close");
			}
			rec = null;
			this.interrupt();
		}
	}
}

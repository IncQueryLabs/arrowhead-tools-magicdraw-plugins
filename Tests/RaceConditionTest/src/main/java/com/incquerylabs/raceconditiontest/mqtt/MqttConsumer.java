package com.incquerylabs.raceconditiontest.mqtt;

import java.time.Instant;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.incquerylabs.raceconditiontest.Constants;
import com.incquerylabs.raceconditiontest.Consumer;

public class MqttConsumer implements Consumer, MqttCallback{

	Instant start;
	Instant end;
	MqttClient mqc = null;
	
	public MqttConsumer() {
		try {
			mqc = new MqttClient("tcp://" + Constants.SERVER_IP + ":" + Constants.MQTT_SERVER_PORT, "MCONS",
					new MemoryPersistence());
			MqttConnectOptions options = new MqttConnectOptions();
			options.setAutomaticReconnect(true);
			options.setCleanSession(true);
			options.setConnectionTimeout(10);
			mqc.connect(options);
			mqc.subscribe(Constants.MQTT_PROCESSOR_BACKWARD_TOPIC_NAME);
		} catch (MqttException e) {
			System.out.println("Excepton in MQTT creation in Arrowhead Consumer.");
		}
	}
	
	@Override
	public void go() {
		
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
		mqc.publish(Constants.RECEIVED_TOPIC_NAME, null);
		end = Instant.now();
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
	}
}

package com.incquerylabs.floodtest;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.incquerylabs.floodtest.arrowhead.ArrowheadProcessor;
import com.incquerylabs.floodtest.dds.DdsProcessor;
import com.incquerylabs.floodtest.mqtt.MqttProcessor;

public class ProcessorFactory extends Thread implements MqttCallback {

	List<Processor> procs = new ArrayList<Processor>();
	MqttClient mqc = null;

	public ProcessorFactory() {
		try {

			mqc = new MqttClient("tcp://" + Constants.SERVER_IP + ":" + Constants.MQTT_SERVER_PORT, "ProcessorFactory",
					new MemoryPersistence());
			MqttConnectOptions options = new MqttConnectOptions();
			options.setAutomaticReconnect(true);
			options.setCleanSession(true);
			options.setConnectionTimeout(10);
			mqc.setCallback(this);
			mqc.connect(options);
			mqc.subscribe(Constants.CREATE_ARRPROC_TOPIC_NAME);
			mqc.subscribe(Constants.CREATE_DDSPROC_TOPIC_NAME);
			mqc.subscribe(Constants.CREATE_MQTTPROC_TOPIC_NAME);
			mqc.subscribe(Constants.KILLPROC_TOPIC_NAME);
		} catch (MqttException e) {
			System.out.println("Excepton in MQTT creation in ProcessorFactory");
		}
	}

	@Override
	public void connectionLost(Throwable cause) {
		System.out.println("Connection lost because: " + cause);
		System.exit(1);
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		Processor temp;
		switch (topic) {
		case Constants.CREATE_ARRPROC_TOPIC_NAME:
			temp = new ArrowheadProcessor(procs.size());
			temp.start();
			procs.add(temp);
			break;
		case Constants.CREATE_DDSPROC_TOPIC_NAME:
			temp = new DdsProcessor(procs.size());
			temp.start();
			procs.add(temp);
			break;
		case Constants.CREATE_MQTTPROC_TOPIC_NAME:
			temp = new MqttProcessor(procs.size());
			temp.start();
			procs.add(temp);
			break;
		case Constants.KILLPROC_TOPIC_NAME:
			for(Processor p : procs) {
				p.kill();
			}
			procs.clear();
			break;
		}
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
	}
}

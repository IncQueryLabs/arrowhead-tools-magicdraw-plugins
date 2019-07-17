package com.incquerylabs.floodtest.arrowhead;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.incquerylabs.floodtest.Constants;
import com.incquerylabs.floodtest.Sensor;

import eu.arrowhead.client.common.Utility;
import eu.arrowhead.client.common.exception.ArrowheadException;
import eu.arrowhead.client.common.exception.ExceptionType;
import eu.arrowhead.client.common.model.ArrowheadService;
import eu.arrowhead.client.common.model.ArrowheadSystem;
import eu.arrowhead.client.common.model.ServiceRegistryEntry;

public class ArrowheadSensor extends Thread implements Sensor {

	private static final String SR_REG_PATH = "serviceregistry/register";
	private static final String SR_UNREG_PATH = "serviceregistry/remove";
	ServerSocket serverSocket = null;
	String type;
	MqttClient mqc = null;
	ServiceRegistryEntry sre = null;
	MqttMessage emptyMessage = new MqttMessage();
	String name;

	public ArrowheadSensor(String type) {
		this.type = type;
		name = "ArrowheadSensor" + type;
		try {
			mqc = new MqttClient("tcp://" + Constants.SERVER_IP + ":" + Constants.MQTT_SERVER_PORT, name,
					new MemoryPersistence());
			MqttConnectOptions options = new MqttConnectOptions();
			options.setAutomaticReconnect(true);
			options.setCleanSession(true);
			options.setConnectionTimeout(10);
			mqc.connect(options);
		} catch (MqttException e) {
			System.out.println("Excepton in MQTT creation in " + name);
		}
		String srUri = Utility.getUri(Constants.SERVER_IP, Constants.ARROWHEAD_SERVICE_REGISTRY_PORT, SR_REG_PATH,
				false, true);
		try {
			if (type.equals("A")) {
				serverSocket = new ServerSocket(Constants.ARROWHEAD_SENSOR_A_PORT);
			} else {
				serverSocket = new ServerSocket(Constants.ARROWHEAD_SENSOR_B_PORT);
			}
		} catch (IOException e) {
			System.out.println("Excepton in server creation in " + name);
		}
		System.out.println(name + " listener started.");
		ArrowheadSystem me = new ArrowheadSystem(name, Constants.ARROWHEAD_SENSOR_IP, Constants.ARROWHEAD_SENSOR_A_PORT,
				null);
		Set<String> interfaces = new HashSet<String>();
		interfaces.add(Constants.ARROWHEAD_INTERFACE_NAME);
		Map<String, String> serviceMetadata = new HashMap<String, String>();
		ArrowheadService service;
		if (type.equals("A")) {
			service = new ArrowheadService(Constants.ARROWHEAD_SENSOR_A_SERVICE_NAME, interfaces, serviceMetadata);
		} else {
			service = new ArrowheadService(Constants.ARROWHEAD_SENSOR_B_SERVICE_NAME, interfaces, serviceMetadata);
		}
		sre = new ServiceRegistryEntry(service, me, "NOTRESTFUL");
		try {
			mqc.publish(Constants.SENT_TOPIC_NAME, emptyMessage);
			Utility.sendRequest(srUri, "POST", sre);
		} catch (ArrowheadException e) {
			if (e.getExceptionType() == ExceptionType.DUPLICATE_ENTRY) {
				System.out.println("Duplicate...");
				String unregUri = Utility.getUri(Constants.SERVER_IP, Constants.ARROWHEAD_SERVICE_REGISTRY_PORT,
						SR_UNREG_PATH, false, false);
				Utility.sendRequest(unregUri, "PUT", sre);
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					this.interrupt();
				}
				Utility.sendRequest(srUri, "POST", sre);
			} else {
				System.out.println(e.getMessage());
			}
		} catch (MqttException e) {
			System.out.println("Problem on aux publishing in " + name);
		}
	}

	@Override

	public void run() {
		System.out.println(name + " ready");
		try {
			while (true) {
				Socket socket = serverSocket.accept();
				System.out.println(name + "received request from Processor.");
				InputStream in = socket.getInputStream();
				OutputStream out = socket.getOutputStream();
				try {
					mqc.publish(Constants.RECEIVED_TOPIC_NAME, emptyMessage);
					in.read();
					out.write(111);
					mqc.publish(Constants.SENT_TOPIC_NAME, emptyMessage);
				} catch (MqttException e) {
					System.out.println("Excepton in aux publishing in Arrowhead Sensor " + type + ".");
				}
			}
		} catch (IOException e) {
			// Expected: the kill of organizer interrupts the severSocket.accept()
		} finally {
			if (serverSocket != null) {
				try {
					serverSocket.close();
				} catch (IOException e) {
					System.out.println("Exception in server socket closing in " + name);
				}
				serverSocket = null;
			}
		}
	}

	@Override
	public void kill() {
		if (sre != null) {
			String unregUri = Utility.getUri(Constants.SERVER_IP, Constants.ARROWHEAD_SERVICE_REGISTRY_PORT,
					SR_UNREG_PATH, false, false);
			try {
				mqc.publish(Constants.SENT_TOPIC_NAME, emptyMessage);
			} catch (MqttException e) {
				System.out.println("Excepton in aux publishing in " + name);
			}
			Utility.sendRequest(unregUri, "PUT", sre);
		}
		if (serverSocket != null) {
			try {
				serverSocket.close();
			} catch (IOException e) {
				System.out.println("Exception in server socket killing in " + name);
			}
			serverSocket = null;
		}
		if (mqc != null) {
			try {
				mqc.disconnect();
				mqc.close();
			} catch (MqttException e) {
				System.out.println("Problem on closing MQTT connection in " + name);
			}
			mqc = null;
		}
		this.interrupt();
	}
}

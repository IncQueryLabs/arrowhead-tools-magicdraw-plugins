package com.incquerylabs.raceconditiontest.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.incquerylabs.floodtest.Auxiliary;
import com.incquerylabs.floodtest.arrowhead.ArrowheadConsumer;
import com.incquerylabs.floodtest.arrowhead.ArrowheadSensor;
import com.incquerylabs.floodtest.dds.DdsConsumer;
import com.incquerylabs.floodtest.dds.DdsSensor;
import com.incquerylabs.floodtest.mqtt.MqttConsumer;
import com.incquerylabs.floodtest.mqtt.MqttSensor;
import com.incquerylabs.floodtest.Constants;

public class TestOrganizer {

	public void start() {
		boolean arr = true;
		boolean dds = true;
		boolean mqtt = true;
		File arrOut = new File("Out/Arrowhead.csv");
		File ddsOut = new File("Out/DDS.csv");
		File mqttOut = new File("Out/MQTT.csv");		

		FileWriter out = null;

		if (arr) {
			ArrowheadConsumer consumer = new ArrowheadConsumer();
			ArrowheadSensor sensorA = new ArrowheadSensor("A");
			sensorA.start();
			Auxiliary aux;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.out.println("Waiting interupted in Test Organizer");
			}
			for (int i = 0; i < Constants.MAX; ++i) {
				aux = new Auxiliary();
				aux.start();
				try {
					mqc.publish(Constants.CREATE_ARRPROC_TOPIC_NAME, emptyMessage);
				} catch (MqttException e) {
					System.out.println("Creation message interrupted");
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					System.out.println("Waiting interupted in Test Organizer");
				}
				consumer.go();
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					System.out.println("Waiting interupted in Test Organizer");
				}
				try {
					out = new FileWriter(arrOut, true);
				} catch (IOException e) {
					System.out.println("Writer down!!!?");
				}
				Duration time = Duration.between(consumer.getStart(), consumer.getEnd());
				try {
					out.write(i + ", " + consumer.getStart().toString() + ", " + consumer.getEnd().toString() + ", "
							+ time.toMillis() + ", " + aux.getSends().toString() + ", " + aux.getReceits());
					out.flush();
				} catch (IOException e) {
					System.out.println("Writer on fire");
				}
				aux.kill();
				sensorA.kill();
			}
			try {
				mqc.publish(Constants.KILLPROC_TOPIC_NAME, emptyMessage);
			} catch (MqttException e) {
				System.out.println("Kill message interrupted");
			}
		}
		if (dds) {
			DdsConsumer consumer = new DdsConsumer();
			DdsSensor sensorA = new DdsSensor("A");
			sensorA.start();
			Auxiliary aux;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.out.println("Waiting interupted in Test Organizer");
			}
			for (int i = 0; i < Constants.MAX; ++i) {
				aux = new Auxiliary();
				aux.start();
				try {
					mqc.publish(Constants.CREATE_DDSPROC_TOPIC_NAME, emptyMessage);
				} catch (MqttException e) {
					System.out.println("Creation message interrupted");
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					System.out.println("Waiting interupted in Test Organizer");
				}
				consumer.go();
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					System.out.println("Waiting interupted in Test Organizer");
				}
				try {
					out = new FileWriter(ddsOut, true);
				} catch (IOException e) {
					System.out.println("Writer down!!!?");
				}
				Duration time = Duration.between(consumer.getStart(), consumer.getEnd());
				try {
					out.write(i + ", " + consumer.getStart().toString() + ", " + consumer.getEnd().toString() + ", "
							+ time.toMillis() + ", " + aux.getSends().toString() + ", " + aux.getReceits());
					out.flush();
				} catch (IOException e) {
					System.out.println("Writer on fire");
				}
				aux.kill();
				sensorA.kill();
			}
			try {
				mqc.publish(Constants.KILLPROC_TOPIC_NAME, emptyMessage);
			} catch (MqttException e) {
				System.out.println("Kill message interrupted");
			}
		}
		if (mqtt) {
			MqttConsumer consumer = new MqttConsumer();
			MqttSensor sensorA = new MqttSensor("A");
			sensorA.start();
			Auxiliary aux;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.out.println("Waiting interupted in Test Organizer");
			}
			for (int i = 0; i < Constants.MAX; ++i) {
				aux = new Auxiliary();
				aux.start();
				try {
					mqc.publish(Constants.CREATE_MQTTPROC_TOPIC_NAME, emptyMessage);
				} catch (MqttException e) {
					System.out.println("Creation message interrupted");
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					System.out.println("Waiting interupted in Test Organizer");
				}
				consumer.go();
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					System.out.println("Waiting interupted in Test Organizer");
				}
				try {
					out = new FileWriter(mqttOut, true);
				} catch (IOException e) {
					System.out.println("Writer down!!!?");
				}
				Duration time = Duration.between(consumer.getStart(), consumer.getEnd());
				try {
					out.write(i + ", " + consumer.getStart().toString() + ", " + consumer.getEnd().toString() + ", "
							+ time.toMillis() + ", " + aux.getSends().toString() + ", " + aux.getReceits());
					out.flush();
				} catch (IOException e) {
					System.out.println("Writer on fire");
				}
				aux.kill();
				sensorA.kill();
			}
			try {
				mqc.publish(Constants.KILLPROC_TOPIC_NAME, emptyMessage);
			} catch (MqttException e) {
				System.out.println("Kill message interrupted");
			}
		}
	}

	MqttClient mqc = null;
	MqttMessage emptyMessage = new MqttMessage();

	public TestOrganizer() {
		try {
			mqc = new MqttClient("tcp://" + Constants.SERVER_IP + ":" + Constants.MQTT_SERVER_PORT, "TestOrganizer",
					new MemoryPersistence());
			MqttConnectOptions options = new MqttConnectOptions();
			options.setAutomaticReconnect(true);
			options.setCleanSession(true);
			options.setConnectionTimeout(10);
			mqc.connect(options);
		} catch (MqttException e) {
			System.out.println("Excepton in MQTT creation.");
		}
	}
}

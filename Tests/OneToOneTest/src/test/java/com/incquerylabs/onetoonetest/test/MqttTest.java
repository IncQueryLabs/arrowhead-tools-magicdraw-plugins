package com.incquerylabs.onetoonetest.test;

import java.io.File;

import org.eclipse.paho.client.mqttv3.MqttException;

import com.incquerylabs.onetoonetest.mqtt.MqttRec;
import com.incquerylabs.onetoonetest.mqtt.MqttSend;

public class MqttTest extends ArrowDirectTest {

	public MqttTest() {
		rec = new MqttRec();
		try {
			send = new MqttSend(file);
		} catch (MqttException e) {
			System.out.println("Problem in MqttSend creation.");
		}
	}

	public static void main(String[] args) {
		MqttTest test = new MqttTest();
		test.test(12, new File("Mqtt.csv"));
	}
}
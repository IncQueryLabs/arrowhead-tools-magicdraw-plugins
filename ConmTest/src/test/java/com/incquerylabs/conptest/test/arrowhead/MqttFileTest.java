package com.incquerylabs.conptest.test.arrowhead;

import org.eclipse.paho.client.mqttv3.MqttException;

import com.incquerylabs.conptest.mqtt.MqttRec;
import com.incquerylabs.conptest.mqtt.MqttSend;

public class MqttFileTest extends ArrowDirectTest {

	public MqttFileTest() {
		rec = new MqttRec();
		try {
			send = new MqttSend(file);
		} catch (MqttException e) {
			System.out.println("Problem in MqttSend creation.");
		}
	}

	public static void main(String[] args) {
		MqttFileTest test = new MqttFileTest();
		test.startup();
		for (Integer i = 0; i < 12; ++i) {
			test.sendFile(i);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		System.out.println("Wrapping up.");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		test.wrapup("MqttFileOut.csv");
	}
}

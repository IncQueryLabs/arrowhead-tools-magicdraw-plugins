package com.incquerylabs.onetoonetest.test;

import java.io.File;

import com.incquerylabs.onetoonetest.arrowheadevent.ArrowheadEventRec;
import com.incquerylabs.onetoonetest.arrowheadevent.ArrowheadEventSend;

public class ArrowEventTest extends ArrowDirectTest{
	
	public ArrowEventTest() {
		rec = new ArrowheadEventRec();
		send = new ArrowheadEventSend(file);
	}

	public static void main(String[] args) {
		MqttTest test = new MqttTest();
		test.test(12, new File("Mqtt.csv"));
	}
}

package com.incquerylabs.onetoonetest.test;

import java.io.File;

import com.incquerylabs.onetoonetest.arrowheaddirect.ArrowheadDirectRec;
import com.incquerylabs.onetoonetest.arrowheaddirect.ArrowheadDirectSend;
import com.incquerylabs.onetoonetest.arrowheadevent.ArrowheadEventRec;
import com.incquerylabs.onetoonetest.arrowheadevent.ArrowheadEventSend;
import com.incquerylabs.onetoonetest.mqtt.MqttRec;
import com.incquerylabs.onetoonetest.mqtt.MqttSend;

public class Organizer {
	public static void main(String[] args) {
		//Test test = new Test(new ArrowheadDirectRec(), new ArrowheadDirectSend() ,new File("L3.pdf") ,new File("Out/ArrowheadDirect.csv"));
		Test test = new Test(new ArrowheadEventRec(), new ArrowheadEventSend() ,new File("L3.pdf") ,new File("Out/ArrowheadEvent.csv"));
		//Test test = new Test(new MqttRec(), new MqttSend() ,new File("L3.pdf") ,new File("Out/Mqtt.csv"));
		test.test(12);
	}
}

package com.incquerylabs.onetoonelatencytest.test;

import java.io.File;

import com.incquerylabs.onetoonelatencytest.Constants;
import com.incquerylabs.onetoonelatencytest.Receiver;
import com.incquerylabs.onetoonelatencytest.Sender;
import com.incquerylabs.onetoonelatencytest.mqtt.MqttRec;
import com.incquerylabs.onetoonelatencytest.mqtt.MqttSend;

public class Temp {
	public static void main(String[] args) {
		Receiver rec = new MqttRec();
		Sender send = new MqttSend();
		rec.start();
		File inputFile = new File("src/test/resources/aaa.txt");
		File outputFile = new File("Out/ArrowheadDirect.csv");

		Test test = new Test(send, inputFile, outputFile);
		test.test(Constants.TEST_REPETITIONS);
		send.kill();
	}
}
package com.incquerylabs.onetoonelatencytest.test;

import java.io.File;

import com.incquerylabs.onetoonelatencytest.Constants;
import com.incquerylabs.onetoonelatencytest.Receiver;
import com.incquerylabs.onetoonelatencytest.Sender;
import com.incquerylabs.onetoonelatencytest.arrowheaddirect.ArrowheadDirectRec;
import com.incquerylabs.onetoonelatencytest.arrowheaddirect.ArrowheadDirectSend;
import com.incquerylabs.onetoonelatencytest.mqtt.MqttRec;
import com.incquerylabs.onetoonelatencytest.mqtt.MqttSend;

public class Temp {
	public static void main(String[] args) {
		Receiver rec = new ArrowheadDirectRec();
		Sender send = new ArrowheadDirectSend();
		rec.start();
		File inputFile = new File("src/test/resources/enwik8.txt");
		File outputFile = new File("Out/ArrowheadDirect.csv");

		Test test = new Test(send, inputFile, outputFile);
		test.test(Constants.TEST_REPETITIONS);
		rec.kill();
		send.kill();
	}
}
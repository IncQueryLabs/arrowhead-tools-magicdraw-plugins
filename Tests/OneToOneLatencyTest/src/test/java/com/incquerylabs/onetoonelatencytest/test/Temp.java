package com.incquerylabs.onetoonelatencytest.test;

import java.io.File;

import com.incquerylabs.onetoonelatencytest.Constants;
import com.incquerylabs.onetoonelatencytest.Receiver;
import com.incquerylabs.onetoonelatencytest.Sender;
import com.incquerylabs.onetoonelatencytest.arrowheaddirect.ArrowheadDirectRec;
import com.incquerylabs.onetoonelatencytest.arrowheaddirect.ArrowheadDirectSend;
import com.incquerylabs.onetoonelatencytest.dds.DdsRec;
import com.incquerylabs.onetoonelatencytest.dds.DdsSend;
import com.incquerylabs.onetoonelatencytest.mqtt.MqttRec;
import com.incquerylabs.onetoonelatencytest.mqtt.MqttSend;

@SuppressWarnings("unused")
public class Temp {
	public static void main(String[] args) {
		Receiver rec = new DdsRec();
		rec.start();
		while(true) {
			Sender send = new DdsSend();
			
			File inputFile = new File("src/test/resources/enwik8.txt");
			File outputFile = new File("Out/ArrowheadDirect.csv");

			Test test = new Test(send, inputFile, outputFile);
			test.test(Constants.TEST_REPETITIONS);
			send.kill();
		}
	}
}
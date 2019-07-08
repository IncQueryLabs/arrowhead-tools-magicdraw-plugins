package com.incquerylabs.onetoonetest.test;

import java.io.File;
import java.util.Random;

import com.incquerylabs.onetoonetest.arrowheaddirect.ArrowheadDirectRec;
import com.incquerylabs.onetoonetest.arrowheaddirect.ArrowheadDirectSend;
import com.incquerylabs.onetoonetest.mqtt.MqttRec;
import com.incquerylabs.onetoonetest.mqtt.MqttSend;

public class Organizer {
	public static void main(String[] args) {
		
		Test test = null;
		File file = null;
		Integer runs = 12;
		Integer testToRun = 2;
		Integer fileToSend = 1;
		
		while(true) {
			Random r = new Random();
			fileToSend = r.nextInt(4);
			testToRun = r.nextInt(3);
			
			
			switch (fileToSend) {
			case 0:
				file = new File("src/test/resources/a.txt");
				break;
			case 1:
				file = new File("src/test/resources/grammar.lsp");
				break;
			case 2: 
				file = new File("src/test/resources/pi.txt");
				break;
			case 3: 
				file = new File("src/test/resources/enwik8.txt");
				break;
			}
			
			switch (testToRun) {
			case 0:
				test = new Test(new ArrowheadDirectRec(), new ArrowheadDirectSend() , file, new File("Out/ArrowheadDirect.csv"));
				break;
			case 1:
				test = new Test(new ArrowheadDirectRec(), new ArrowheadDirectSend() , file, new File("Out/ArrowheadDirect.csv")); //TO be replaced with dds
				break;
			case 2: 
				test = new Test(new MqttRec(), new MqttSend() , file, new File("Out/Mqtt.csv"));
				break;
			}
			
			test.test(runs);
		}
	}
}

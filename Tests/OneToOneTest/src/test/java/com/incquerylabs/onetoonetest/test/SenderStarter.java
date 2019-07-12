package com.incquerylabs.onetoonetest.test;

import java.io.File;
import java.util.Random;

import com.incquerylabs.onetoonetest.Constants;
import com.incquerylabs.onetoonetest.Sender;
import com.incquerylabs.onetoonetest.arrowheaddirect.ArrowheadDirectSend;
import com.incquerylabs.onetoonetest.dds.DdsSend;
import com.incquerylabs.onetoonetest.mqtt.MqttSend;

public class SenderStarter{
	
	public static void main(String[] args) {
		Test test = null;
		File file = null;
		Integer testToRun = 0;
		Integer fileToSend = 3;
		
		while(true) {
			Random r = new Random();
			fileToSend = r.nextInt(5);
			testToRun = r.nextInt(3);
			Sender send = null;
			
			switch (fileToSend) {
			case 0:
				file = new File("src/test/resources/a.txt");
				break;
			case 1:
				file = new File("src/test/resources/aa.txt");
				break;
			case 2:
				file = new File("src/test/resources/aaa.txt");
				break;
			case 3: 
				file = new File("src/test/resources/pi.txt");
				break;
			case 4: 
				file = new File("src/test/resources/enwik8.txt");
				break;
			}
			
			switch (testToRun) {
			case 0:
				System.out.println("Arrowhead Direct Test.");
				send = new ArrowheadDirectSend();
				test = new Test(send, file, new File("Out/ArrowheadDirect.csv"));
				break;
			case 1:
				send = new DdsSend();
				System.out.println("DDS Test.");
				test = new Test(send, file, new File("Out/Dds.csv"));
				break;
			case 2:
				send = new MqttSend();
				System.out.println("MQTT Test.");
				test = new Test(send, file, new File("Out/Mqtt.csv"));
				break;
			}
			test.test(Constants.TEST_REPETITIONS);
			send.kill();
		}
	}	
}

package com.incquerylabs.onetoonetest.test;

import java.io.File;
import java.util.Random;

import com.incquerylabs.onetoonetest.Receiver;
import com.incquerylabs.onetoonetest.Sender;
import com.incquerylabs.onetoonetest.arrowheaddirect.ArrowheadDirectRec;
import com.incquerylabs.onetoonetest.arrowheaddirect.ArrowheadDirectSend;
import com.incquerylabs.onetoonetest.dds.DdsRec;
import com.incquerylabs.onetoonetest.dds.DdsSend;
import com.incquerylabs.onetoonetest.mqtt.MqttRec;
import com.incquerylabs.onetoonetest.mqtt.MqttSend;

public class Organizer{
	
	volatile static boolean run = true;
	
	public static void main(String[] args) {
		Organizer o = new Organizer();
		o.run();
	}

	public void run() {
		Test test = null;
		File file = null;
		Integer nOfRuns = 12;
		Integer testToRun = 1;
		Integer fileToSend = 2;
		
		while(run) {
			Random r = new Random();
			fileToSend = r.nextInt(4);
			//testToRun = r.nextInt(3);
			Receiver rec = null;
			Sender send = null;
			
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
				System.out.println("Arrowhead Direct Test.");
				rec = new ArrowheadDirectRec();
				send = new ArrowheadDirectSend();
				test = new Test(rec, send, file, new File("Out/ArrowheadDirect.csv"));
				break;
			case 1:
				rec = new DdsRec();
				send = new DdsSend();
				System.out.println("DDS Test.");
				test = new Test(rec, send, file, new File("Out/Dds.csv"));
				break;
			case 2:
				rec = new MqttRec();
				send = new MqttSend();
				System.out.println("MQTT Test.");
				test = new Test(rec, send, file, new File("Out/Mqtt.csv"));
				break;
			}
			test.test(nOfRuns);
			rec.kill();
			send.kill();
		}
	}
}

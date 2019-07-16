package com.incquerylabs.raceconditiontest.test;

import java.time.Duration;

import com.incquerylabs.raceconditiontest.Auxillary;
import com.incquerylabs.raceconditiontest.Consumer;
import com.incquerylabs.raceconditiontest.Processor;
import com.incquerylabs.raceconditiontest.Sensor;
import com.incquerylabs.raceconditiontest.arrowhead.ArrowheadConsumer;
import com.incquerylabs.raceconditiontest.arrowhead.ArrowheadProcessor;
import com.incquerylabs.raceconditiontest.arrowhead.ArrowheadSensor;
import com.incquerylabs.raceconditiontest.mqtt.MqttConsumer;
import com.incquerylabs.raceconditiontest.mqtt.MqttProcessor;
import com.incquerylabs.raceconditiontest.mqtt.MqttSensor;

public class UnifiedStarter {
	
	public static void main(String[] args) {
		String verse = "Arrowhead";
		Auxillary aux = new Auxillary();
		Consumer consumer = null;
		Sensor sensorA;
		Sensor sensorB;
		Processor[] procs;
		int q = 1;
		
		switch (verse) {
		case "Arrowhead":
			sensorA = new ArrowheadSensor("A");
			sensorB = new ArrowheadSensor("B");
			sensorA.start();
			sensorB.start();
			procs = new Processor[q];
			for(int i = 0; i < q; ++i) {
				Processor proc = new ArrowheadProcessor(i);
				procs[i] = proc;
				proc.start();
			}
			consumer = new ArrowheadConsumer();			
			break;
		case "MQTT":
			sensorA = new MqttSensor("A");
			sensorB = new MqttSensor("B");
			sensorA.start();
			sensorB.start();
			procs = new Processor[q];
			for(int i = 0; i < q; ++i) {
				Processor proc = new MqttProcessor(i);
				procs[i] = proc;
				proc.start();
			}
			consumer = new MqttConsumer();			
			break;			
		}
		if(consumer != null) {
			consumer.go();
			Duration d = Duration.between(consumer.getStart(), consumer.getEnd());
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Sends: " + aux.getSends());
			System.out.println("Receits: " + aux.getReceits());
			System.out.println("Time: " + d.toMillis());
		}
		//TODO kill
	}
}

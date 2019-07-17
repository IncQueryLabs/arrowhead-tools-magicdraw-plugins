package com.incquerylabs.raceconditiontest.test;

import java.time.Duration;

import com.incquerylabs.floodtest.Auxillary;
import com.incquerylabs.floodtest.Consumer;
import com.incquerylabs.floodtest.Processor;
import com.incquerylabs.floodtest.Sensor;
import com.incquerylabs.floodtest.arrowhead.ArrowheadConsumer;
import com.incquerylabs.floodtest.arrowhead.ArrowheadProcessor;
import com.incquerylabs.floodtest.arrowhead.ArrowheadSensor;
import com.incquerylabs.floodtest.dds.DdsConsumer;
import com.incquerylabs.floodtest.dds.DdsProcessor;
import com.incquerylabs.floodtest.dds.DdsSensor;
import com.incquerylabs.floodtest.mqtt.MqttConsumer;
import com.incquerylabs.floodtest.mqtt.MqttProcessor;
import com.incquerylabs.floodtest.mqtt.MqttSensor;

public class UnifiedStarter {

	public static void main(String[] args) {
		String verse = "DDS";
		int q = 1;

		Auxillary aux = new Auxillary();
		Consumer consumer = null;
		Sensor sensorA = null;
		Sensor sensorB = null;
		Processor[] procs = null;

		switch (verse) {
		case "Arrowhead":
			sensorA = new ArrowheadSensor("A");
			sensorB = new ArrowheadSensor("B");
			sensorA.start();
			sensorB.start();
			procs = new Processor[q];
			for (int i = 0; i < q; ++i) {
				Processor proc = new ArrowheadProcessor(i);
				procs[i] = proc;
				proc.start();
			}
			consumer = new ArrowheadConsumer();
			break;
		case "DDS":
			sensorA = new DdsSensor("A");
			sensorB = new DdsSensor("B");
			sensorA.start();
			sensorB.start();
			procs = new Processor[q];
			for (int i = 0; i < q; ++i) {
				Processor proc = new DdsProcessor(i);
				procs[i] = proc;
				proc.start();
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			consumer = new DdsConsumer();
			break;
		case "MQTT":
			sensorA = new MqttSensor("A");
			sensorB = new MqttSensor("B");
			sensorA.start();
			sensorB.start();
			procs = new Processor[q];
			for (int i = 0; i < q; ++i) {
				Processor proc = new MqttProcessor(i);
				procs[i] = proc;
				proc.start();
			}
			consumer = new MqttConsumer();
			break;
		}

		if (consumer != null) {
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
		if (sensorA != null) {
			sensorA.kill();
		}
		if (sensorA != null) {
			sensorB.kill();
		}
		if (procs != null) {
			for (int i = 0; i < q; ++i) {
				procs[i].kill();
			}
		}
		return;
	}
}

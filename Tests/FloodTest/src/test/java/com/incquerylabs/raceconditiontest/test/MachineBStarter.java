package com.incquerylabs.raceconditiontest.test;

import com.incquerylabs.floodtest.Sensor;
import com.incquerylabs.floodtest.arrowhead.ArrowheadSensor;
import com.incquerylabs.floodtest.dds.DdsSensor;
import com.incquerylabs.floodtest.mqtt.MqttSensor;

public class MachineBStarter {
	public static void main(String[] args) {
		Sensor b = new ArrowheadSensor("B");
		b.start();
		b = new DdsSensor("B");
		b.start();
		b = new MqttSensor("B");
		b.start();
	}
}

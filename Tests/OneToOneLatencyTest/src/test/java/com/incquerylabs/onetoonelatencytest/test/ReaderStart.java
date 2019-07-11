package com.incquerylabs.onetoonelatencytest.test;

import com.incquerylabs.onetoonelatencytest.Receiver;
import com.incquerylabs.onetoonelatencytest.arrowheaddirect.ArrowheadDirectRec;
import com.incquerylabs.onetoonelatencytest.dds.DdsRec;
import com.incquerylabs.onetoonelatencytest.mqtt.MqttRec;

public class ReaderStart {
	public static void main(String[] args) {
		Receiver reca = new ArrowheadDirectRec();
		reca.start();
		Receiver recd = new DdsRec();
		recd.start();
		Receiver recm = new MqttRec();
		recm.start();
	}
}

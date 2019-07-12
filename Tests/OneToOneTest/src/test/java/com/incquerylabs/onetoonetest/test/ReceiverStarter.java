package com.incquerylabs.onetoonetest.test;

import com.incquerylabs.onetoonetest.Receiver;
import com.incquerylabs.onetoonetest.arrowheaddirect.ArrowheadDirectRec;
import com.incquerylabs.onetoonetest.dds.DdsRec;
import com.incquerylabs.onetoonetest.mqtt.MqttRec;

public class ReceiverStarter {
	public static void main(String[] args) {
		Receiver reca = new ArrowheadDirectRec();
		reca.start();
		Receiver recd = new DdsRec();
		recd.start();
		Receiver recm = new MqttRec();
		recm.start();
	}
}

package com.incquerylabs.onetoonetest.arrowheadevent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import com.incquerylabs.onetoonetest.Sender;

import eu.arrowhead.client.common.Utility;
import eu.arrowhead.client.common.model.ArrowheadSystem;
import eu.arrowhead.client.common.model.Event;
import eu.arrowhead.client.common.model.PublishEvent;

public class ArrowheadEventSend implements Sender {

	File file;
	public static final String IP = "127.0.0.1";
	public static final int EH_PORT = 8454;
	public static final int PUB_PORT = 9023;
	public static final String EVENT_NAME = "file incoming";

	public ArrowheadEventSend(File File) {
		file = File;
	}

	@Override
	public void send(int n) {
		String ehUri = Utility.getUri(IP, EH_PORT, "eventhandler/publish", false, false);
		Map<String, String> metadata = new HashMap<String, String>();
		String payload = null;
		try {
			payload = Files.readString(file.toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Event event = new Event(EVENT_NAME, payload, ZonedDateTime.now(), metadata);
		ArrowheadSystem me = new ArrowheadSystem("cordek test pub", IP, PUB_PORT, null);
		PublishEvent pubE = new PublishEvent(me, event, null);

		Utility.sendRequest(ehUri, "POST", pubE);
	}
}

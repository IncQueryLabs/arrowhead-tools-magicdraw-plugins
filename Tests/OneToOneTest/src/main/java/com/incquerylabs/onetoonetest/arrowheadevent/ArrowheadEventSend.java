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

	public static final String IP = "127.0.0.1";
	public static final int EH_PORT = 8454;
	public static final int PUB_PORT = 9023;
	public static final String EVENT_NAME = "file incoming";

	@Override
	public void send(int n, File file) {
		String ehUri = Utility.getUri(IP, EH_PORT, "eventhandler/publish", false, false);
		Map<String, String> metadata = new HashMap<String, String>();
		String payload = null;
		try {
			String temp = Files.readString(file.toPath());
			payload = n + "\n";
			payload.concat(temp);
		} catch (IOException e) {
			System.out.println("File read wrong.");
		}
		Event event = new Event(EVENT_NAME, payload, ZonedDateTime.now(), metadata);
		ArrowheadSystem me = new ArrowheadSystem("cordek", IP, PUB_PORT, null);
		PublishEvent pubE = new PublishEvent(me, event, null);

		Utility.sendRequest(ehUri, "POST", pubE);
		System.out.println("Event sent with index: " + n);
	}
}

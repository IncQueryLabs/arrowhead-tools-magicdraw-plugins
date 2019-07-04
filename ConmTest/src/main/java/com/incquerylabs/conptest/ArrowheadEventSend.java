package com.incquerylabs.conptest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import eu.arrowhead.client.common.Utility;
import eu.arrowhead.client.common.model.ArrowheadSystem;
import eu.arrowhead.client.common.model.Event;
import eu.arrowhead.client.common.model.PublishEvent;


public class ArrowheadEventSend implements Sender {
	
	File file;
	public static final String EH_IP = "0.0.0.0";
	public static final int EH_PORT = 8454;
	public static final int PUB_PORT = 9023;
	public static final int TCP_PORT = 9024;
	public static final String EVENT_NAME = "file incoming";
	
	public ArrowheadEventSend(File File) {
		file = File;
	}
	
	@Override
	public void send() {
		String ehUri = Utility.getUri(EH_IP, EH_PORT, "eventhandler/publish", false, false);
		Map<String, String> metadata = new HashMap<String, String>();
		metadata.put("port", "9024");
		metadata.put("ip", "0.0.0.0");
		String payload = null;
		try {
			payload = Files.readString(file.toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Event event = new Event(EVENT_NAME, payload, ZonedDateTime.now(), metadata);
		ArrowheadSystem me = new ArrowheadSystem("cordek test pub", EH_IP, PUB_PORT, null);
		PublishEvent pubE = new PublishEvent(me, event, null);
		
		Utility.sendRequest(ehUri, "POST", pubE);
	}
}

package com.incquerylabs.onetoonetest.arrowheadevent;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import com.incquerylabs.onetoonetest.Receiver;
import eu.arrowhead.client.common.Utility;
import eu.arrowhead.client.common.model.ArrowheadSystem;
import eu.arrowhead.client.common.model.EventFilter;

public class ArrowheadEventRec extends Thread implements Receiver{
	
	public static final String EVENT_NAME = ArrowheadEventSend.EVENT_NAME;
	public static final String SH_PATH = "eventhandler/subscription";
	public static final int SH_PORT = 8454;
	public static final String IP = "127.0.0.1";
	public static final int SUB_PORT = 6435;
	Map<Integer, Instant> mid = new HashMap<Integer, Instant>();
	Map<Integer, Instant> end = new HashMap<Integer, Instant>();
	
	@Override
	public void run() {
		ArrowheadSystem sys = new ArrowheadSystem("Tiff", IP, SUB_PORT, "null");
		EventFilter ef = new EventFilter(EVENT_NAME, sys, null);
		String uri = Utility.getUri(IP, SH_PORT, SH_PATH, false, true);
		
		//TODO start server
		
		Utility.sendRequest(uri, "POST", ef);
	}
	
	public boolean pinged() {
		return true;
	}
	
	public boolean receiveFile() {
		
		
		
		return true;
	}
	
	@Override
	public Instant getEnd(Integer n) {
		return end.get(n);
	}

	@Override
	public Instant getMid(Integer n) {
		return mid.get(n);
	}
}

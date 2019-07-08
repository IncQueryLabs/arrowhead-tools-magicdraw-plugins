package com.incquerylabs.onetoonetest.arrowheadevent;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import com.incquerylabs.onetoonetest.Receiver;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

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
		
		try {
			HttpServer server = HttpServer.create(new InetSocketAddress(SUB_PORT), 0);
			server.createContext("/notify",   new ArrHttpHandler());
			server.setExecutor(null);
			server.start();			
		} catch (IOException e) {
			System.out.println("IOException in event rec.");
		}
		Utility.sendRequest(uri, "POST", ef);
	}

	private class ArrHttpHandler implements HttpHandler{

		@Override
		public void handle(HttpExchange exchange) throws IOException {
			String method = exchange.getRequestMethod();
			switch (method) {
			case "GET":
				exchange.sendResponseHeaders(200, 0);
				break;
			case "POST":
				DataInputStream dis = new DataInputStream(exchange.getRequestBody());
				Integer index = dis.readInt();
				mid.put(index, Instant.now());
				dis.readAllBytes();
				end.put(index, Instant.now());
			}
		}
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

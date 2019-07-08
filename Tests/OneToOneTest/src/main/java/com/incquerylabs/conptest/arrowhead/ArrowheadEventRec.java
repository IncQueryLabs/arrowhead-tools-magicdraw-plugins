package com.incquerylabs.conptest.arrowhead;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import com.incquerylabs.conptest.Receiver;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import eu.arrowhead.client.common.Utility;
import eu.arrowhead.client.common.model.ArrowheadSystem;
import eu.arrowhead.client.common.model.EventFilter;

public class ArrowheadEventRec extends Thread implements Receiver{
	
	public static final String NAME = "halfej";
	public static final String SH_PATH = "eventhandler/subscription";
	public static final int SH_PORT = 8454;
	public static final String IP = "0.0.0.0";
	public static final int SUB_PORT = 6435;
	Map<Integer, Instant> mid = new HashMap<Integer, Instant>();
	Map<Integer, Instant> end = new HashMap<Integer, Instant>();
	
	@Override
	public void run() {
		ArrowheadSystem sys = new ArrowheadSystem("Tiff", IP, SUB_PORT, "null");
		EventFilter ef = new EventFilter(NAME, sys, null);
		String uri = Utility.getUri(IP, SH_PORT, SH_PATH, false, true);
		
		try {
			HttpServer server = HttpServer.create(new InetSocketAddress(SUB_PORT), 0);
			server.createContext("/notify",   new ArrHttpHandler());
			server.setExecutor(null);
			server.start();			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
				InputStream is = exchange.getRequestBody();
				byte[] buf = is.readAllBytes();
				//buf.
				//exchange.sendResponseHeaders(200, 0);
			}
		}
	}
	@Override
	public Instant getEnd(Integer n) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public Instant getMid(Integer n) {
		// TODO Auto-generated method stub
		return null;
	}
}

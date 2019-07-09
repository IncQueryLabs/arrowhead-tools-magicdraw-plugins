package com.incquerylabs.onetoonetest.arrowheaddirect;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.incquerylabs.onetoonetest.Receiver;

import eu.arrowhead.client.common.Utility;
import eu.arrowhead.client.common.exception.ArrowheadException;
import eu.arrowhead.client.common.exception.ExceptionType;
import eu.arrowhead.client.common.model.ArrowheadService;
import eu.arrowhead.client.common.model.ArrowheadSystem;
import eu.arrowhead.client.common.model.ServiceRegistryEntry;

public class ArrowheadDirectRec extends Thread implements Receiver {

	static final String SR_IP = "127.0.0.1";
	static final int SR_PORT = 8442;
	static final int PROVIDER_PORT = 9112;
	public static final String SERVICE_NAME = ArrowheadDirectSend.SERVICE_NAME;
	public static final String INTERFACE = ArrowheadDirectSend.INTERFACE;
	static final String SR_PATH = "serviceregistry/register";
	Map<Integer, Instant> mid = new HashMap<Integer, Instant>();
	Map<Integer, Instant> end = new HashMap<Integer, Instant>();
	ServerSocket serverSocket = null;

	@Override
	public void run() {
		String srUri = Utility.getUri(SR_IP, SR_PORT, SR_PATH, false, true);
		ArrowheadSystem me = new ArrowheadSystem("arrdrec", "0.0.0.0", PROVIDER_PORT, "null");
		Set<String> interfaces = new HashSet<String>();
		interfaces.add(INTERFACE);
		Map<String, String> serviceMetadata = new HashMap<String, String>();

		ArrowheadService service = new ArrowheadService(SERVICE_NAME, interfaces, serviceMetadata);
		ServiceRegistryEntry sre = new ServiceRegistryEntry(service, me, "NOTRESTFUL");

		try {
			Utility.sendRequest(srUri, "POST", sre);
		} catch (ArrowheadException e) {
			if (e.getExceptionType() == ExceptionType.DUPLICATE_ENTRY) {
				System.out.println("Received DuplicateEntryException from SR, sending delete request and then registering again.");
				String unregUri = Utility.getUri(SR_IP, SR_PORT, "serviceregistry/remove", false, false);
				Utility.sendRequest(unregUri, "PUT", sre);
				Utility.sendRequest(srUri, "POST", sre);
			}
		}

		try {
			serverSocket = new ServerSocket(PROVIDER_PORT);
			System.out.println("Listener started.");
			while (true) {
				Socket socket = serverSocket.accept();
				System.out.println("Connection Received");
				DataInputStream in = new DataInputStream(socket.getInputStream());
				int index = in.readInt();
				mid.put(index, Instant.now());
				in.readAllBytes();
				System.out.println("Test " + index + " finished.");
				end.put(index, Instant.now());
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
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

	@Override
	public void kill() {
		if(serverSocket != null) {
			try {
				serverSocket.close();
				serverSocket = null;
			} catch (IOException e) {
				System.out.println("Arr Rec unable to kill serversocket");
			}
		}
		this.interrupt();
	}
}

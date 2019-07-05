package com.incquerylabs.conptest.arrowhead;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.incquerylabs.conptest.Receiver;

import eu.arrowhead.client.common.Utility;
import eu.arrowhead.client.common.exception.ArrowheadException;
import eu.arrowhead.client.common.exception.ExceptionType;
import eu.arrowhead.client.common.model.ArrowheadService;
import eu.arrowhead.client.common.model.ArrowheadSystem;
import eu.arrowhead.client.common.model.ServiceRegistryEntry;

public class ArrowheadDirectRec extends Thread implements Receiver{
	
	static final String SR_IP = "0.0.0.0";
	static final int SR_PORT =  8442;
	static final int PROVIDER_PORT =  9112;
	public static final String SERVICE_NAME = ArrowheadDirectSend.SERVICE_NAME;
	public static final String INTERFACE =  ArrowheadDirectSend.INTERFACE;
	static final String SR_PATH =  "serviceregistry/register";
	Instant endTime;
	
	
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
			if(e.getExceptionType() == ExceptionType.DUPLICATE_ENTRY) {
		        System.out.println("Received DuplicateEntryException from SR, sending delete request and then registering again.");
		        String unregUri = Utility.getUri(SR_IP, SR_PORT, "serviceregistry/remove", false, false);
		        Utility.sendRequest(unregUri, "PUT", sre);
		        Utility.sendRequest(srUri, "POST", sre);
			}
		}
		
		ServerSocket serverSocket = null;
		
		try {
			serverSocket = new ServerSocket(PROVIDER_PORT);
			
			while(true) {
				Socket socket = serverSocket.accept();
				DataInputStream in = new DataInputStream(socket.getInputStream());
				while(!socket.isClosed()) {
					in.read();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(serverSocket != null) {
				try {
					serverSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}


	@Override
	public Instant getEnd() {
		return endTime;
	}


	@Override
	public void setEnd(Instant in) {
		endTime = in;		
	}
}

package com.incquerylabs.onetoonelatencytest.arrowheaddirect;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.Response;

import com.incquerylabs.onetoonelatencytest.Sender;

import eu.arrowhead.client.common.Utility;
import eu.arrowhead.client.common.model.ArrowheadService;
import eu.arrowhead.client.common.model.ArrowheadSystem;
import eu.arrowhead.client.common.model.OrchestrationForm;
import eu.arrowhead.client.common.model.OrchestrationResponse;
import eu.arrowhead.client.common.model.ServiceRequestForm;

public class ArrowheadDirectSend implements Sender {

	public static final String ORCH_IP = "127.0.0.1";
	public static final int ORCH_PORT = 8440;
	public static final String SERVICE_NAME = "conptest";
	public static final String INTERFACE = "TCP";
	ArrowheadSystem provider = null;
	Socket socket = null;
	Map<Integer, Instant> times = new HashMap<Integer, Instant>();

	@Override
	public void send(int n, File file) {
		if (provider == null) {
			String orchUri = Utility.getUri(ORCH_IP, ORCH_PORT, "orchestrator/orchestration", false, false);
			ArrowheadSystem me = new ArrowheadSystem("ArrowheadDirectSender", "0.0.0.0", 1, null);
			Set<String> interfaces = new HashSet<String>();
			interfaces.add(INTERFACE);
			Map<String, String> serviceMetadata = new HashMap<String, String>();
			ArrowheadService service = new ArrowheadService(SERVICE_NAME, interfaces, serviceMetadata);
			Map<String, Boolean> flags = new HashMap<String, Boolean>();
			flags.put("overrideStore", true);
			ServiceRequestForm srf = new ServiceRequestForm.Builder(me).requestedService(service)
					.orchestrationFlags(flags).build();
			Response r = null;
			r = Utility.sendRequest(orchUri, "POST", srf);
			OrchestrationResponse or = r.readEntity(OrchestrationResponse.class);
			OrchestrationForm of = or.getResponse().get(0);
			provider = of.getProvider();
		}
		try {
			socket = new Socket(provider.getAddress(), provider.getPort());
			byte[] bytes = Files.readAllBytes(file.toPath());
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			times.put(0, Instant.now());
			for (int i = 0; i < n; ++i) {
				System.out.println("Start sending message " + (i + 1));
				out.write(bytes);
				in.readLine();
				times.put(Integer.valueOf(i + 1), Instant.now());
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					;
				}
			}
		} catch (UnknownHostException e) {
			System.out.println("What host?");
		} catch (IOException e) {
			System.out.println("IOException");
		} finally {
			if (socket != null) {
				try {
					socket.close();
					socket = null;
				} catch (IOException e) {
					System.out.println("IOException on closiong");
				}
			}
		}
	}

	@Override
	public void kill() {
		if (socket != null) {
			try {
				socket.close();
				socket = null;
			} catch (IOException e) {
				System.out.println("IOException on closiong");
			}
		}
	}

	@Override
	public Map<Integer, Instant> getTimes() {
		return times;
	}
}

package com.incquerylabs.onetoonelatencytest.arrowheaddirect;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.Response;

import com.incquerylabs.onetoonelatencytest.Constants;
import com.incquerylabs.onetoonelatencytest.Sender;

import eu.arrowhead.client.common.Utility;
import eu.arrowhead.client.common.model.ArrowheadService;
import eu.arrowhead.client.common.model.ArrowheadSystem;
import eu.arrowhead.client.common.model.OrchestrationForm;
import eu.arrowhead.client.common.model.OrchestrationResponse;
import eu.arrowhead.client.common.model.ServiceRequestForm;

public class ArrowheadDirectSend implements Sender {

	private static final String OR_PATH = "orchestrator/orchestration";
	ArrowheadSystem provider = null;
	Socket socket = null;
	Map<Integer, Instant> times = new HashMap<Integer, Instant>();
	private static final int BUFFER_SIZE = 65536;

	@Override
	public void send(int n, File file) {
		if (provider == null) {
			String orchUri = Utility.getUri(Constants.ARROWHEAD_ORCHESTRATOR_IP, Constants.ARROWHEAD_ORCHESTRATOR_PORT,
					OR_PATH, false, false);
			ArrowheadSystem me = new ArrowheadSystem("ArrowheadDirectSender", "0.0.0.0", 1, null);
			Set<String> interfaces = new HashSet<String>();
			interfaces.add(Constants.ARROWHEAD_INTERFACE_NAME);
			Map<String, String> serviceMetadata = new HashMap<String, String>();
			ArrowheadService service = new ArrowheadService(Constants.ARROWHEAD_SERVICE_NAME, interfaces,
					serviceMetadata);
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
			times.put(0, Instant.now());
			byte[] buff = new byte[BUFFER_SIZE];
			for (int i = 0; i < n; ++i) {
				socket = new Socket(provider.getAddress(), provider.getPort());
				OutputStream out = socket.getOutputStream();
				PrintWriter writer = new PrintWriter(out);
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				FileInputStream fip = new FileInputStream(file);
				System.out.println("Arrowhead message " + (i + 1) + " sent.");
				writer.println(file.length());
				writer.flush();
				int count;
				while ((count = fip.read(buff)) > 0) {
					out.write(buff, 0, count);
				}
				in.readLine();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					;
				}
				times.put(Integer.valueOf(i + 1), Instant.now());
				fip.close();
				try {
					socket.close();
				} catch (IOException e) {
					//expected?
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

package com.incquerylabs.floodtest.arrowhead;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.Response;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.incquerylabs.floodtest.Constants;
import com.incquerylabs.floodtest.Consumer;

import eu.arrowhead.client.common.Utility;
import eu.arrowhead.client.common.model.ArrowheadService;
import eu.arrowhead.client.common.model.ArrowheadSystem;
import eu.arrowhead.client.common.model.OrchestrationForm;
import eu.arrowhead.client.common.model.OrchestrationResponse;
import eu.arrowhead.client.common.model.ServiceRequestForm;

public class ArrowheadConsumer implements Consumer {

	private static final String ORCH_PATH = "orchestrator/orchestration";
	Instant start;
	Instant end;
	MqttClient mqc = null;
	MqttMessage emptyMessage = new MqttMessage();
	String name = "ArrowheadConsumer";

	public ArrowheadConsumer() {
		try {
			mqc = new MqttClient("tcp://" + Constants.SERVER_IP + ":" + Constants.MQTT_SERVER_PORT, name,
					new MemoryPersistence());
			MqttConnectOptions options = new MqttConnectOptions();
			options.setAutomaticReconnect(true);
			options.setCleanSession(true);
			options.setConnectionTimeout(10);
			mqc.connect(options);
		} catch (MqttException e) {
			System.out.println("Excepton in MQTT creation in " + name);
		}
	}

	@Override
	public void go() {
		start = Instant.now();
		String orchUri = Utility.getUri(Constants.SERVER_IP, Constants.ARROWHEAD_ORCHESTRATOR_PORT, ORCH_PATH, false,
				false);
		ArrowheadSystem me = new ArrowheadSystem(name, Constants.LOCALHOST_IP, 1, null);
		Set<String> interfaces = new HashSet<String>();
		interfaces.add(Constants.ARROWHEAD_INTERFACE_NAME);
		Map<String, String> serviceMetadata = new HashMap<String, String>();
		ArrowheadService service = new ArrowheadService(Constants.ARROWHEAD_PROCESSOR_SERVICE_NAME, interfaces,
				serviceMetadata);
		Map<String, Boolean> flags = new HashMap<String, Boolean>();
		flags.put("overrideStore", true);
		ServiceRequestForm srf = new ServiceRequestForm.Builder(me).requestedService(service).orchestrationFlags(flags)
				.build();
		Response r = Utility.sendRequest(orchUri, "POST", srf);
		Socket socket = null;
		try {
			mqc.publish(Constants.SENT_TOPIC_NAME, emptyMessage);
			OrchestrationResponse or = r.readEntity(OrchestrationResponse.class);
			mqc.publish(Constants.RECEIVED_TOPIC_NAME, emptyMessage);
			List<OrchestrationForm> forms = or.getResponse();
			boolean done = false;
			for (int i = 0; (i < forms.size()) && !done; ++i) {
				OrchestrationForm form = forms.get(i);
				ArrowheadSystem processor = form.getProvider();
				try {
					mqc.publish(Constants.SENT_TOPIC_NAME, emptyMessage);
					System.out.println(name + " sent for Processor");
					socket = new Socket(processor.getAddress(), processor.getPort());
					OutputStream out = socket.getOutputStream();
					InputStream in = socket.getInputStream();
					out.write(47);
					out.flush();
					in.read();
					end = Instant.now();
				} catch (SocketTimeoutException x) {
					System.out.println("Option " + i + " timed out.");
				}
			}
		} catch (MqttException e1) {
			System.out.println("Exception in aux publishing in " + name);
		} catch (IOException e2) {
			System.out.println("IOException in the big block in " + name);
			System.out.println(e2.getMessage());
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					System.out.println("Exception in closing socket in " + name);
				}
				socket = null;
			}
			if (mqc != null) {
				try {
					mqc.disconnect();
					mqc.close();
				} catch (MqttException e) {
					System.out.println("Exception in closing MQTT connection in " + name);
				}
				mqc = null;
			}
		}
	}

	@Override
	public Instant getEnd() {
		return end;
	}

	@Override
	public Instant getStart() {
		return start;
	}
}

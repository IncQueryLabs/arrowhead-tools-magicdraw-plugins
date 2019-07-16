package com.incquerylabs.raceconditiontest.arrowhead;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.Response;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.incquerylabs.raceconditiontest.Constants;
import com.incquerylabs.raceconditiontest.Processor;

import eu.arrowhead.client.common.Utility;
import eu.arrowhead.client.common.exception.ArrowheadException;
import eu.arrowhead.client.common.exception.ExceptionType;
import eu.arrowhead.client.common.model.ArrowheadService;
import eu.arrowhead.client.common.model.ArrowheadSystem;
import eu.arrowhead.client.common.model.OrchestrationForm;
import eu.arrowhead.client.common.model.OrchestrationResponse;
import eu.arrowhead.client.common.model.ServiceRegistryEntry;
import eu.arrowhead.client.common.model.ServiceRequestForm;

public class ArrowheadProcessor extends Thread implements Processor{

	private static final String SR_REG_PATH = "serviceregistry/register";
	private static final String SR_UNREG_PATH = "serviceregistry/remove";
	private static final String ORCH_PATH = "orchestrator/orchestration";
	MqttClient mqc = null;
	ServerSocket serverSocket = null;
	private static final String NAME = "arrProc";
	int n;
	ArrowheadSystem me;
	ServiceRegistryEntry sre = null;
	
	//RANDOM KILL ??
	public ArrowheadProcessor(int ft) {
		n = ft;
		try {
			mqc = new MqttClient("tcp://" + Constants.SERVER_IP + ":" + Constants.MQTT_SERVER_PORT, NAME + n,
					new MemoryPersistence());
			MqttConnectOptions options = new MqttConnectOptions();
			options.setAutomaticReconnect(true);
			options.setCleanSession(true);
			options.setConnectionTimeout(10);
			mqc.connect(options);
		} catch (MqttException e) {
			System.out.println("Excepton in MQTT creation in Arrowhead Processor " + n + ".");
		}
		try {
			serverSocket = new ServerSocket(Constants.ARROWHEAD_PROCESSOR_BASEPORT + n);
		} catch (IOException e) {
			System.out.println("Excepton in server creation in Arrowhead Processor " + n + ".");
		}
		String srUri = Utility.getUri(Constants.SERVER_IP, Constants.ARROWHEAD_SERVICE_REGISTRY_PORT, SR_REG_PATH,
				false, true);
		me = new ArrowheadSystem(Constants.ARROWHEAD_PROCESSOR_BASENAME + n, Constants.ARROWHEAD_PRCESSOR_IP,
				Constants.ARROWHEAD_PROCESSOR_BASEPORT + n, null);
		Set<String> interfaces = new HashSet<String>();
		interfaces.add(Constants.ARROWHEAD_INTERFACE_NAME);
		Map<String, String> serviceMetadata = new HashMap<String, String>();
		ArrowheadService service = new ArrowheadService(Constants.ARROWHEAD_PROCESSOR_SERVICE_NAME, interfaces,
				serviceMetadata);
		sre = new ServiceRegistryEntry(service, me, "NOTRESTFUL");
		try {
			mqc.publish(Constants.SENT_TOPIC_NAME, null);
			Utility.sendRequest(srUri, "POST", sre);
		} catch (ArrowheadException e) {
			if (e.getExceptionType() == ExceptionType.DUPLICATE_ENTRY) {
				System.out.println("Duplicate...");
				String unregUri = Utility.getUri(Constants.SERVER_IP, Constants.ARROWHEAD_SERVICE_REGISTRY_PORT,
						SR_UNREG_PATH, false, false);
				Utility.sendRequest(unregUri, "PUT", sre);
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					this.interrupt();
				}
				Utility.sendRequest(srUri, "POST", sre);
			} else {
				System.out.println(e.getMessage());
			}
		} catch (MqttException e) {
			System.out.println("Excepton in aux publishing in Arrowhead Processor " + n + " constuctor.");
		}
	}

	@Override
	public void run() {
		try {
			while (true) {
				Socket socket = serverSocket.accept();
				InputStream in = socket.getInputStream();
				OutputStream out = socket.getOutputStream();
				byte[] inBuff = new byte[10];
				in.read(inBuff);
				try {
					mqc.publish(Constants.RECEIVED_TOPIC_NAME, null);
					String orchUri = Utility.getUri(Constants.SERVER_IP, Constants.ARROWHEAD_ORCHESTRATOR_PORT,
							ORCH_PATH, false, false);
					Set<String> interfaces = new HashSet<String>();
					interfaces.add(Constants.ARROWHEAD_INTERFACE_NAME);
					Map<String, String> serviceMetadata = new HashMap<String, String>();
					ArrowheadService serviceA = new ArrowheadService(Constants.ARROWHEAD_SENSOR_A_SERVICE_NAME,
							interfaces, serviceMetadata);
					ArrowheadService serviceB = new ArrowheadService(Constants.ARROWHEAD_SENSOR_B_SERVICE_NAME,
							interfaces, serviceMetadata);
					Map<String, Boolean> flags = new HashMap<String, Boolean>();
					flags.put("overrideStore", true);
					ServiceRequestForm srfA = new ServiceRequestForm.Builder(me).requestedService(serviceA)
							.orchestrationFlags(flags).build();
					ServiceRequestForm srfB = new ServiceRequestForm.Builder(me).requestedService(serviceB)
							.orchestrationFlags(flags).build();
					Response responseA = Utility.sendRequest(orchUri, "POST", srfA);
					Response responseB = Utility.sendRequest(orchUri, "POST", srfB);
					OrchestrationResponse oA = responseA.readEntity(OrchestrationResponse.class);
					OrchestrationResponse oB = responseB.readEntity(OrchestrationResponse.class);
					OrchestrationForm fA = oA.getResponse().get(0);
					OrchestrationForm fB = oB.getResponse().get(0);

					Socket caller = null;
					try {
						ArrowheadSystem sensorA = fA.getProvider();
						caller = new Socket(sensorA.getAddress(), sensorA.getPort());
						InputStream inA = caller.getInputStream();
						OutputStream outA = caller.getOutputStream();
						mqc.publish(Constants.SENT_TOPIC_NAME, null);
						outA.write(11);
						inA.read();
						mqc.publish(Constants.RECEIVED_TOPIC_NAME, null);
					} catch (MqttException e) {
						System.out.println("Excepton in aux publishing in Arrowhead Processor " + n + ".");
					} catch (IOException e) {
						System.out.println("Excepton in sensor communication in Arrowhead Processor " + n + ".");
					} finally {
						if (caller != null) {
							try {
								caller.close();
							}catch (IOException e) {
								// probably expected
							}
							caller = null;
						}
					}
					try {
						ArrowheadSystem sensorB = fB.getProvider();
						caller = new Socket(sensorB.getAddress(), sensorB.getPort());
						InputStream inA = caller.getInputStream();
						OutputStream outA = caller.getOutputStream();
						mqc.publish(Constants.SENT_TOPIC_NAME, null);
						outA.write(11);
						inA.read();
						mqc.publish(Constants.RECEIVED_TOPIC_NAME, null);
					} catch (MqttException e) {
						System.out.println("Excepton in aux publishing in Arrowhead Processor " + n + ".");
					} catch (IOException e) {
						System.out.println("Excepton in sensor communication in Arrowhead Processor " + n + ".");
					} finally {
						if (caller != null) {
							try {
								caller.close();
							}catch (IOException e) {
								// probably expected
							}
							caller = null;
						}
					}
					mqc.publish(Constants.SENT_TOPIC_NAME, null);
					out.write(33);
				} catch (MqttException e) {
					System.out.println("Excepton in aux publishing in Arrowhead Processor " + n + ".");
				} catch (ArrowheadException a) {
					System.out.println("Excepton in arrowhead in Arrowhead Processor " + n + ".");
				}
			}
		} catch (Exception e) {
			System.out.println("Exception in Arrowhead Processor " + n + ".");
			System.out.println(e.getMessage());
		}
	}

	public void kill() {
		if(sre != null) {
			String unregUri = Utility.getUri(Constants.SERVER_IP, Constants.ARROWHEAD_SERVICE_REGISTRY_PORT,
					SR_UNREG_PATH, false, false);
			try {
				mqc.publish(Constants.SENT_TOPIC_NAME, null);
			} catch (MqttException e) {
				System.out.println("Excepton in aux publishing in Arrowhead Processor " + n + ".");
			}
			Utility.sendRequest(unregUri, "PUT", sre);
		}
		if(mqc != null) {
			try {
				mqc.close();
			} catch (MqttException e) {
				System.out.println("Problem on closing MQTT connection in Arrowhead Processor " + n + ".");
			}
			mqc = null;
		}
		this.interrupt();
	}
}
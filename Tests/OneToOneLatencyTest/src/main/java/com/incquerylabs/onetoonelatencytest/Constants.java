package com.incquerylabs.onetoonelatencytest;

public class Constants {

	public static final Integer TEST_REPETITIONS = 12;
	public static final String LOCALHOST_IP = "127.0.0.1";
	public static final String SERVER_IP = "192.168.0.141";
	
	public static final int ARROWHEAD_SERVICE_REGISTRY_PORT = 8442;
	public static final int ARROWHEAD_ORCHESTRATOR_PORT = 8440;
	public static final String ARROWHEAD_SERVICE_NAME = "comptest";
	public static final String ARROWHEAD_INTERFACE_NAME = "tcp";
	public static final int ARROWHEAD_PROVIDER_PORT = 9112;

	// DDS?
	
	public static final int MQTT_SERVER_PORT = 1883;
	public static final int MQTT_QOS = 2;
	public static final String MQTT_TOPIC_NAME = "compforward";
}

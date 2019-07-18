package com.incquerylabs.onetoonetest;

public class Constants {

	public static final Integer TEST_REPETITIONS = 12;
	public static final String LOCALHOST_IP = "127.0.0.1";
	public static final String SERVER_IP = "52.57.229.102";
	
	public static final int ARROWHEAD_SERVICE_REGISTRY_PORT = 8442;
	public static final int ARROWHEAD_ORCHESTRATOR_PORT = 8440;
	public static final String ARROWHEAD_SERVICE_NAME = "comptest";
	public static final String ARROWHEAD_INTERFACE_NAME = "tcp";
	public static final String ARROWHEAD_PROVIDER_IP = "52.57.229.102";
	public static final int ARROWHEAD_PROVIDER_PORT = 8499;

	public static final int DDS_DOMAIN_ID = 2;
	public static final int DDS_LOOP_COUNT = 0; //unlimited
	public static final String DDS_FORWARD_TOPIC_NAME = "compforward";
	public static final String DDS_BACKWARD_TOPIC_NAME = "compbackward";
	
	
	public static final int MQTT_SERVER_PORT = 1883;
	public static final int MQTT_QOS = 2;
	public static final String MQTT_FORWARD_TOPIC_NAME = "compforward";
	public static final String MQTT_BACKWARD_TOPIC_NAME = "compbackward";
}

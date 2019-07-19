package com.incquerylabs.floodtest;

public class Constants {
	
	public static final String SERVER_IP = "127.0.0.1";
	public static final String LOCALHOST_IP = "127.0.0.1";
	public static final int MAX = 50;
	public static final int MQTT_SERVER_PORT = 1883;
	public static final String SENT_TOPIC_NAME = "messageSent";
	public static final String RECEIVED_TOPIC_NAME = "messageReceived";
	public static final String CREATE_MQTTPROC_TOPIC_NAME = "crecmq";
	public static final String CREATE_DDSPROC_TOPIC_NAME = "crecdd";
	public static final String CREATE_ARRPROC_TOPIC_NAME = "crecarr";
	public static final String KILLPROC_TOPIC_NAME = "killall";
	
	public static final int ARROWHEAD_SERVICE_REGISTRY_PORT = 8442;
	public static final int ARROWHEAD_ORCHESTRATOR_PORT = 8440;
	public static final String ARROWHEAD_PROCESSOR_SERVICE_NAME = "PROCESSOR";
	public static final String ARROWHEAD_SENSOR_A_SERVICE_NAME = "SENSOR_A";
	public static final String ARROWHEAD_SENSOR_B_SERVICE_NAME = "SENSOR_B";
	public static final String ARROWHEAD_INTERFACE_NAME = "halak";
	public static final String ARROWHEAD_PRCESSOR_IP = "127.0.0.1";
	public static final String ARROWHEAD_SENSOR_IP = "127.0.0.1";
	public static final int ARROWHEAD_PROCESSOR_BASEPORT = 8500;
	public static final int ARROWHEAD_SENSOR_A_PORT = 8498;
	public static final int ARROWHEAD_SENSOR_B_PORT = 8499;
	
	public static final String MQTT_SENSOR_A_TOPIC_NAME = "MSAF";
	public static final String MQTT_SENSOR_B_TOPIC_NAME = "MSBF";
	public static final String MQTT_SENSOR_BACK_TOPIC_BASENAME = "sensbackMQTT";
	public static final String MQTT_PROCESSOR_FORWARD_TOPIC_NAME = "MSPF";
	public static final String MQTT_PROCESSOR_BACKWARD_TOPIC_NAME = "MSPB";
	
	public static final int DDS_DOMAIN_NUMBER = 2;
	public static final String DDS_PROCESSOR_FORWARD_TOPIC_NAME = "RPDDPF";
	public static final String DDS_PROCESSOR_BACKWARD_TOPIC_NAME = "DDPB";
	public static final String DDS_SENSOR_A_TOPIC_NAME = "DDAF";
	public static final String DDS_SENSOR_B_TOPIC_NAME = "DDBF";
	public static final String DDS_SENSOR_TOPIC_BASENAME = "DDSB";
}

package com.incquerylabs.arrowhead.magicdraw.dto;

import com.incquerylabs.arrowhead.magicdraw.auto.ArrowheadProfile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceDto {
	
	public String serviceDefinition;
	public SystemDto provider;
	public String serviceUri;
	public Integer version;
	public ArrowheadProfile.SecurityKindEnum secure;
	public List<String> interfaces = Collections.synchronizedList(new ArrayList<>());
	public Map<String, String> metadata = Collections.synchronizedMap(new HashMap<>());
	public String endOfValidity;
}

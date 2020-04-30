package com.incquerylabs.arrowhead.pd.dto.ahx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServiceDto {
	
	public String serviceDefinition;
	public SystemDto provider;
	public final List<String> interfaces = Collections.synchronizedList(new ArrayList<>());
}

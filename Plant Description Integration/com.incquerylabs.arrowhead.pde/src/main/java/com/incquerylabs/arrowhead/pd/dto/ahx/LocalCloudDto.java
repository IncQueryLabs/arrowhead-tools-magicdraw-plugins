package com.incquerylabs.arrowhead.pd.dto.ahx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LocalCloudDto {
	
	public final List<SystemDto> systems = Collections.synchronizedList(new ArrayList<>());
	public final List<ServiceDto> serviceRegistryEntries = Collections.synchronizedList(new ArrayList<>());
	public final List<AuthDto> authRules = Collections.synchronizedList(new ArrayList<>());
}

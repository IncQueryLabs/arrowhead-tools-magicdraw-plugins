package com.incquerylabs.arrowhead.magicdraw.dto;

import java.util.ArrayList;
import java.util.List;

public class AuthDto {
	
	public String consumer;
	public String serviceDefinition;
	public final List<String> providers = new ArrayList<>();
	public final List<String> interfaces = new ArrayList<>();
}

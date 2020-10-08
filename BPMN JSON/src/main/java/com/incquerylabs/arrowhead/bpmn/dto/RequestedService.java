package com.incquerylabs.arrowhead.bpmn.dto;

import java.util.Arrays;
import java.util.List;

public class RequestedService {

    public List<String> interfaceRequirements = Arrays.asList("HTTP-SECURE-JSON", "HTTPS-SECURE-JSON", "HTTP-INSECURE-JSON");
    public Integer versionRequirement = 1;
    public String serviceDefinitionRequirement;
}

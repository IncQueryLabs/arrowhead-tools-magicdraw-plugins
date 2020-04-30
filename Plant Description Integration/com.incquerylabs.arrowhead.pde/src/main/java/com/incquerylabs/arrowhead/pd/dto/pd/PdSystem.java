package com.incquerylabs.arrowhead.pd.dto.pd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PdSystem {
	
	public String systemName;
	public List<PdPort> ports = Collections.synchronizedList(new ArrayList<>());
}

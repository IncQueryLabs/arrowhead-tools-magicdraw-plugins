package com.incquerylabs.arrowhead.pd.dto.pd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlantDescription {
	
	public Integer id;
	public List<Integer> include;
	public String plantDescription;
	public List<PdSystem> systems = Collections.synchronizedList(new ArrayList<>());
	public List<PdConnection> connections = Collections.synchronizedList(new ArrayList<>());
}

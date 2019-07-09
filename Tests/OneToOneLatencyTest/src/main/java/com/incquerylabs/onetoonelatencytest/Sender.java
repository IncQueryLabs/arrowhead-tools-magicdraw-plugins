package com.incquerylabs.onetoonelatencytest;

import java.io.File;
import java.time.Instant;
import java.util.Map;

public interface Sender {
	
	public void send(int n,  File file);
	public void kill();
	public Map<Integer, Instant> getTimes();
}

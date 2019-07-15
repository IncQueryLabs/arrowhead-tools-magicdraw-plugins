package com.incquerylabs.raceconditiontest;

import java.time.Instant;

public interface Consumer {
	
	public void go();
	public Instant getEnd();
	public Instant getStart();
}

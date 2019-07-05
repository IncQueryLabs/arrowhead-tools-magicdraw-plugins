package com.incquerylabs.conptest;

import java.time.Instant;

public interface Receiver{
	public Instant getEnd();
	
	public void start();

	public void setEnd(Instant in);
}

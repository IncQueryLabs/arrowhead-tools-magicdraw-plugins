package com.incquerylabs.onetoonelatencytest;

import java.time.Instant;

public interface Receiver{
	
	public Instant getEnd(Integer n);	
	public Instant getMid(Integer n);	
	public void start();
	public void kill();
}
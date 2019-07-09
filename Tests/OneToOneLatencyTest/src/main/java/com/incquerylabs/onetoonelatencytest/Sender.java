package com.incquerylabs.onetoonelatencytest;

import java.io.File;

public interface Sender {
	
	public void send(int n,  File file);
	public void kill();
}

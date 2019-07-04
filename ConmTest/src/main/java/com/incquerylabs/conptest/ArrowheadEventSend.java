package com.incquerylabs.conptest;

import java.io.File;

public class ArrowheadEventSend extends Thread implements Sender {
	
	File file;
	
	public ArrowheadEventSend(File File) {
		file = File;
	}
	
	@Override
	public void run() {
		// TODO 
	}
	
	@Override
	public void send() {
		this.start();
	}
}

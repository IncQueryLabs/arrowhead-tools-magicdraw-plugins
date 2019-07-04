package com.incquerylabs.conptest.test.arrowhead;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.incquerylabs.conptest.Server;
import com.incquerylabs.conptest.arrowhead.ArrowheadDirectRec;
import com.incquerylabs.conptest.arrowhead.ArrowheadDirectSend;

public class DirectTest {
	Server server;
	
	@Before
	public void setup() {
		ArrowheadDirectRec rec = new ArrowheadDirectRec();
		File file = new File(".gitignore");
		ArrowheadDirectSend sender = new ArrowheadDirectSend(file);
		server = new Server(sender);
		rec.start();
	}
	
	@Test
	public void sendSmallFile() {
		server.go();
	}
	
}

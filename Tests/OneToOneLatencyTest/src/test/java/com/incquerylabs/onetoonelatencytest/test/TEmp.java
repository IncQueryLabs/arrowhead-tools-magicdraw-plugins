package com.incquerylabs.onetoonelatencytest.test;

import java.io.File;

import com.incquerylabs.onetoonelatencytest.arrowheaddirect.ArrowheadDirectRec;
import com.incquerylabs.onetoonelatencytest.arrowheaddirect.ArrowheadDirectSend;

public class TEmp {
	public static void main(String[] args) {
		ArrowheadDirectRec arrec = new ArrowheadDirectRec();
		arrec.start();
		ArrowheadDirectSend send = new ArrowheadDirectSend();
		File file = new File("src/test/resources/grammar.lsp");
		Test test = new Test(send, file, new File("Out/ArrowheadDirect.csv"));
		test.test(12);
		arrec.kill();
	}
}

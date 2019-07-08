package com.incquerylabs.conptest.test.arrowhead;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import com.incquerylabs.conptest.arrowheaddirect.ArrowheadDirectRec;
import com.incquerylabs.conptest.arrowheaddirect.ArrowheadDirectSend;
import com.incquerylabs.onetoonetest.Receiver;
import com.incquerylabs.onetoonetest.Sender;

public class ArrowDirectTest {

	Receiver rec = new ArrowheadDirectRec();
	File file = new File("L3.pdf");
	Sender send = new ArrowheadDirectSend(file);
	Map<Integer, Instant> map = new HashMap<Integer, Instant>();
	public static final Integer REP = 12;

	public void startup() {
		rec.start();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	public void sendFile(Integer index) {
		Instant start = Instant.now();
		send.send(index);

		map.put(index, start);
	}

	public void wrapup(String filename) {
		long l = file.length();

		FileWriter out = null;
		try {
			out = new FileWriter(new File(filename), true);
		} catch (IOException e) {
			System.out.println("Writer down!!!?");
		}

		String len = "" + l;
		String startS = "";
		String midS = "";
		String endS = "";
		String lat = "";
		String bw = "";

		long totalLat = 0;
		long totalBW = 0;

		for (Integer i = 0; i < map.size(); ++i) {
			Instant start = map.get(i);
			Instant mid = rec.getMid(i);
			Instant end = rec.getEnd(i);
			startS = start.toString();

			if (mid != null) {
				midS = mid.toString();
			} else {
				midS = "null";
				lat = "null";
				bw = "null";
			}
			if (rec.getEnd(i) != null) {
				endS = end.toString();
			} else {
				endS = "null";
				lat = "null";
				bw = "null";
			}
			if (!lat.equals("null")) {
				long latl = Duration.between(start, mid).toMillis();
				totalLat = totalLat + latl;
				lat = "" + latl;
			}
			if (!bw.equals("null")) {
				long t = Duration.between(start, end).toMillis();
				long bwl = 1000 * l / t;
				totalBW = totalBW + bwl;
				bw = "" + bwl;
			}

			try {
				String mess = len + ", " + startS + ", " + midS + ", " + endS + ", " + lat + ", " + bw + ",\n";
				out.write(mess);
				// System.out.println(mess);
			} catch (IOException e) {
				System.out.println("Writer downed!!!?");
			}
		}
		try {
			out.write(" , , , , , , " + totalLat / map.size() + ", " + totalBW / map.size() + ",\n");
			out.flush();
			System.out.println("Average latency: " + totalLat / map.size());
			System.out.println("Average bandwidth: " + totalBW / map.size());
		} catch (IOException e) {
			System.out.println("Writer downed 2!!!?");
		}
	}

	public static void main(String[] args) {
		ArrowDirectTest test = new ArrowDirectTest();
		test.startup();
		for (Integer i = 0; i < REP; ++i) {
			test.sendFile(i);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		System.out.println("Wrapping up.");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		test.wrapup("ArrowDirectOut.csv");
	}
}

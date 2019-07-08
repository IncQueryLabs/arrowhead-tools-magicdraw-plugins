package com.incquerylabs.onetoonetest.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;


import com.incquerylabs.onetoonetest.Receiver;
import com.incquerylabs.onetoonetest.Sender;
import com.incquerylabs.onetoonetest.arrowheaddirect.ArrowheadDirectRec;
import com.incquerylabs.onetoonetest.arrowheaddirect.ArrowheadDirectSend;

public class ArrowDirectTest {

	Receiver rec;
	Sender send;
	File file;
	Map<Integer, Instant> map = new HashMap<Integer, Instant>();

	public ArrowDirectTest() {
		rec = new ArrowheadDirectRec();
		send = new ArrowheadDirectSend(file);
	}
	
	public void test(Integer rep, File outFile) {
		System.out.println("Starting Test.");
		file = outFile;
		rec.start();
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		for (Integer i = 0; i < rep; ++i) {
			sendFile(i);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		System.out.println("Wrapping up.");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		wrapup(outFile);
	}

	public void sendFile(Integer index) {
		Instant start = Instant.now();
		send.send(index);

		map.put(index, start);
	}

	public void wrapup(File file) {
		long l = file.length();

		FileWriter out = null;
		try {
			out = new FileWriter(file, true);
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
		test.test(12, new File("ArrowDirect.csv"));
	}
}

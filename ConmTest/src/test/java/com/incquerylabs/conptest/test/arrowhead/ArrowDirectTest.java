package com.incquerylabs.conptest.test.arrowhead;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import com.incquerylabs.conptest.Receiver;
import com.incquerylabs.conptest.Sender;
import com.incquerylabs.conptest.arrowhead.ArrowheadDirectRec;
import com.incquerylabs.conptest.arrowhead.ArrowheadDirectSend;

public class ArrowDirectTest {

	Receiver rec = new ArrowheadDirectRec();
	File file = new File("L3.pdf");
	Sender send = new ArrowheadDirectSend(file);
	Map<Integer, Row> map = new HashMap<Integer, ArrowDirectTest.Row>();

	private void startup() {
		rec.start();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	private void sendFile(Integer index) {
		Instant start = Instant.now();
		Instant mid = send.send(index);
		
		map.put(index, new Row(start, mid));
	}

	private void wrapup(String filename) {
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
		Row row;
		
		long totalLat = 0;
		long totalBW = 0;
		
		for(Integer i = 0; i < map.size(); ++i) {
			row = map.get(i);
			startS = row.start.toString();
			
			if (row.mid != null) {
				midS = row.mid.toString();
			} else {
				midS = "null";
				lat = "null";
				bw = "null";
			}
			if (rec.getEnd(i) != null) {
				endS = rec.getEnd(i).toString();
			} else {
				endS = "null";
				lat = "null";
				bw = "null";
			}
			if (!lat.equals("null")) {
				long latl = Duration.between(row.start, row.mid).toMillis();
				totalLat = totalLat + latl;
				lat = "" + latl;
			}
			if (!bw.equals("null")) {
				long t = Duration.between(row.mid, rec.getEnd(i)).toMillis();
				long bwl = 1000 * l / t;
				totalBW = totalBW + bwl;
				bw = "" + bwl;
			}
			
			try {
				String mess = len + ", " + startS + ", " + midS + ", " + endS + ", " + lat + ", " + bw + ",\n";
				out.write(mess);
				//System.out.println(mess);
				out.flush();
			} catch (IOException e) {
				System.out.println("Writer downed!!!?");
			}
		}
		
		System.out.println("Average latency: " + totalLat/map.size());
		System.out.println("Average bandwidth: " + totalBW/map.size());
		
	}
	
	public static void main(String[] args) {
		ArrowDirectTest test = new ArrowDirectTest();
		test.startup();
		for (Integer i = 0; i < 12; ++i) {
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
	
	private static class Row{
		
		public Row(Instant start, Instant mid) {
			super();
			this.start = start;
			this.mid = mid;
		}
		Instant start;
		Instant mid;
	}
}

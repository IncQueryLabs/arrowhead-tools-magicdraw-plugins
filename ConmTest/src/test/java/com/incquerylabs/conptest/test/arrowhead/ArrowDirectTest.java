package com.incquerylabs.conptest.test.arrowhead;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.incquerylabs.conptest.Receiver;
import com.incquerylabs.conptest.Sender;
import com.incquerylabs.conptest.arrowhead.ArrowheadDirectRec;
import com.incquerylabs.conptest.arrowhead.ArrowheadDirectSend;

public class ArrowDirectTest {
	
	Receiver rec;
	Sender send;
	static Map<Integer, Row> map = new HashMap<Integer, ArrowDirectTest.Row>();
	File file;
	static BufferedWriter out;
	
	
	@BeforeAll
	public void setup() throws InterruptedException {
		rec = new ArrowheadDirectRec();
		send = new ArrowheadDirectSend(file);
		file = new File(".gitignore");		
		rec.start();
		try {
			out = new BufferedWriter(new FileWriter(new File("ArrowDirectOut.csv"), true));
		} catch (IOException e) {
			System.out.println("Writer down!!!?");
		}
		Thread.sleep(2000);
	}
	
	@ParameterizedTest
	@ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11})
	public void sendSmallFile(Integer index) {
		long length = file.length();
		Instant start = Instant.now();
		Instant mid = send.send();
		Instant end = rec.getEnd();
		map.put(index, new Row(length, start, mid, end));
		rec.setEnd(null);
	}
	
	@AfterAll
	public static void wrapup() {
		String len = "";
		String start = "";
		String mid = "";
		String end = "";
		String lat = "";
		String bw = "";
		for(Integer i = 0; i < 12; ++i) {
			Row row = map.get(i);
			len = "" + row.fileSize;
			start = row.start.toString();
			if(row.mid != null) {
				mid = row.mid.toString();
			} else {
				mid = "null";
				lat = "null";
				bw = "null";
			}
			if(row.end != null) {
				end = row.end.toString();
			} else {
				end = "null";
				lat = "null";
				bw = "null";
			}
			if(!lat.equals("null")) {
				lat = "" + Duration.between(row.mid, row.end).toMillis();
			}
			if(!bw.equals("null")) {
				bw = "" + 1000*row.fileSize/(Duration.between(row.mid, row.end).toMillis());
			}
			try {
				out.write(len + "," + start + "," + mid + "," + end + "," + lat + "," + bw + ",\n");
			} catch (IOException e) {
				System.out.println("Writer downed!!!?");
			}
			lat = "";
			bw = "";
		}
	}
	
	private static class Row{
		
		public Row(long fileSize, Instant start, Instant mid, Instant end) {
			super();
			this.fileSize = fileSize;
			this.start = start;
			this.mid = mid;
			this.end = end;
		}
		
		long fileSize;
		Instant start;
		Instant mid;
		Instant end;
	}
}

package com.incquerylabs.onetoonelatencytest.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;

import com.incquerylabs.onetoonelatencytest.Sender;

public class Test {

	Sender send;
	File inputFile;
	File outputFile;

	public Test(Sender send, File inputFile, File outputFile) {
		this.send = send;
		this.inputFile = inputFile;
		this.outputFile = outputFile;
	}

	public void test(Integer rep) {
		System.out.println("Starting Test using " + inputFile.getName() + ".");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		send.send(rep, inputFile);
		System.out.println("Wrapping up.");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		wrapup();
	}

	public void wrapup() {
		long l = inputFile.length();

		FileWriter out = null;

		try {
			out = new FileWriter(outputFile, true);
		} catch (IOException e) {
			System.out.println("Writer down!!!?");
		}

		long totalLat = 0;
		long totalBW = 0;

		Map<Integer, Instant> times = send.getTimes();

		for (Integer i = 0; i < times.size(); ++i) {
			Instant start = times.get(i);
			Instant end = times.get(i + 1);

			if (start != null && end != null) {
				String startS = start.toString();
				String endS = end.toString();

				long lat = (Duration.between(start, end).toMillis() - 2000) + 1;
				if (lat <= 0) {
					lat = 1;
				}
				totalLat = totalLat + lat;

				long bandw = 1000 * l / lat;
				totalBW = totalBW + bandw;

				try {
					String mess = l + ", " + startS + ", " + endS + ", " + lat + ", " + bandw + "\n";
					out.write(mess);
					// System.out.println(mess);
				} catch (IOException e) {
					System.out.println("Writer downed!!!?");
				}
			}
		}
		try {
			out.flush();
			System.out.println("Average latency: " + totalLat / times.size());
			System.out.println("Average bandwidth: " + totalBW / times.size());
		} catch (IOException e) {
			System.out.println("Writer downed 2!!!?");
		}
	}
}

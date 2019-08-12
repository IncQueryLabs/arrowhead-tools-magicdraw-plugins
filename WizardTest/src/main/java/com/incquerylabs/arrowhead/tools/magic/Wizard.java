package com.incquerylabs.arrowhead.tools.magic;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Wizard {

	public void compartmentalize(Path source, Path target) {
		
	}
	
	public static void main(String[] args) {
		Path source = Paths.get("model/testProj.ecore");
		Path target = Paths.get("Out");
		Wizard ted = new Wizard();
		ted.compartmentalize(source, target);
	}
}

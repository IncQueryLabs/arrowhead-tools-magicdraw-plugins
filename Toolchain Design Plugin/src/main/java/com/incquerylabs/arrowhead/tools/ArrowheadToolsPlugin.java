package com.incquerylabs.arrowhead.tools;

import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.magicdraw.core.project.ProjectEventListenerAdapter;
import com.nomagic.magicdraw.plugins.Plugin;
import com.nomagic.magicdraw.uml.ExtendedPropertyNames;
import com.nomagic.magicdraw.uml.symbols.DiagramListenerAdapter;

public class ArrowheadToolsPlugin extends Plugin {
	
	private static boolean initialized = false;
	
	@Override
	public void init() {
		Application.getInstance().getProjectsManager();
		initialized = true;
	}
	
	@Override
	public boolean isSupported() {
		return true;
	}
	
	@Override
	public boolean close() {
		return true;
	}
	
	public static boolean isInitialized() {
		return initialized;
	}
}

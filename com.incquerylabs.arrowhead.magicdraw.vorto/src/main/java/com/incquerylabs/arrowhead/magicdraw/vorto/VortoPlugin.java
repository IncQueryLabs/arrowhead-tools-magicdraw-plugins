package com.incquerylabs.arrowhead.magicdraw.vorto;

import com.nomagic.actions.NMAction;
import com.nomagic.magicdraw.actions.ActionsConfiguratorsManager;
import com.nomagic.magicdraw.plugins.Plugin;
import java.util.List;

public class VortoPlugin extends Plugin {
	
	private static boolean initialized = false;
	
	private static final String IMPORT_ACTION_GROUP = "Import From";
	
	@Override
	public void init() {
		ActionsConfiguratorsManager superManager = ActionsConfiguratorsManager.getInstance();
		superManager.addMainMenuConfigurator(manager -> {
			List<NMAction> allActions = manager.getAllActions();
			for(NMAction a : allActions) {
				if(a.getName().equals(IMPORT_ACTION_GROUP)) {
					a.addAction(new ImportVortoModel());
				}
			}
		});
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

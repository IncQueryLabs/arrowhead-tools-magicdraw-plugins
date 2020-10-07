package com.incquerylabs.arrowhead.bpmn;

import com.nomagic.actions.NMAction;
import com.nomagic.magicdraw.actions.ActionsConfiguratorsManager;
import com.nomagic.magicdraw.plugins.Plugin;

import java.util.List;

public class BpmnPlugin extends Plugin {

    private static boolean initialized = false;

    private static final String EXPORT_ACTION_GROUP = "Export To";

    @Override
    public void init() {
        ActionsConfiguratorsManager superManager = ActionsConfiguratorsManager.getInstance();
        superManager.addMainMenuConfigurator(manager -> {
            List<NMAction> allActions = manager.getAllActions();
            for(NMAction a : allActions) {
                if(a.getName().equals(EXPORT_ACTION_GROUP)) {
                    a.addAction(new ExportAction());
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
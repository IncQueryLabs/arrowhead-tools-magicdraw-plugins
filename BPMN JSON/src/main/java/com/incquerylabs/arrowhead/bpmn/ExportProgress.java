package com.incquerylabs.arrowhead.bpmn;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.incquerylabs.arrowhead.bpmn.dto.Action;
import com.incquerylabs.arrowhead.bpmn.dto.Plan;
import com.incquerylabs.arrowhead.bpmn.dto.Step;
import com.nomagic.magicdraw.cbm.profiles.BPMN2Profile;
import com.nomagic.task.ProgressStatus;
import com.nomagic.task.RunnableWithProgress;
import com.nomagic.uml2.ext.magicdraw.activities.mdbasicactivities.ActivityEdge;
import com.nomagic.uml2.ext.magicdraw.activities.mdfundamentalactivities.Activity;
import com.nomagic.uml2.ext.magicdraw.activities.mdfundamentalactivities.ActivityNode;
import com.nomagic.uml2.ext.magicdraw.activities.mdstructuredactivities.StructuredActivityNode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class ExportProgress implements RunnableWithProgress {

    private final Activity element;
    private final File chosen;

    public ExportProgress(Activity element, File chosen) {
        this.element = element;
        this.chosen = chosen;
    }

    @Override
    public void run(ProgressStatus progress) {
        progress.setMax(1);
        StructuredActivityNode first = null;
        for (ActivityNode node : element.getNode()) {
            if (node.getIncoming().size() == 0 && BPMN2Profile.isSubProcess(node)) {
                first = (StructuredActivityNode) node;
            }
        }
        if (first != null) {
            Plan plan = new Plan();
            plan.name = element.getName();
            plan.firstActionName = first.getName();
            exportAction(first, plan);
            try {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                FileWriter writer = new FileWriter(chosen);
                gson.toJson(plan, writer);
                writer.flush();
                writer.close();
                progress.increase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void exportAction(StructuredActivityNode node, Plan plan) {
        Action action = new Action();
        plan.actions.add(action);
        action.name = node.getName();

        Optional<ActivityNode> next = node.getOutgoing().stream()
                .map(ActivityEdge::getTarget)
                .filter(BPMN2Profile::isSubProcess)
                .findFirst();
        if (next.isPresent()) {
            action.nextActionName = next.get().getName();
            exportAction((StructuredActivityNode) next.get(), plan);
        }

        node.getNode().stream().filter(BPMN2Profile::isTask).forEach(task -> {
            if (task.getIncoming().size() == 0) {
                action.firstStepNames.add(task.getName());
                exportTask(task, action);
            }
        });
    }

    private void exportTask(ActivityNode task, Action action) {
        Step step = new Step();
        action.steps.add(step);
        step.name = task.getName();

        task.getOutgoing().stream().map(ActivityEdge::getTarget).filter(Objects::nonNull).forEach(target -> {
            step.nextStepNames.add(target.getName());
            exportTask(target, action);
        });
    }
}

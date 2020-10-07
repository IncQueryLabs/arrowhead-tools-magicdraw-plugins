package com.incquerylabs.arrowhead.bpmn.dto;

import java.util.ArrayList;

public class Action {

    public String name;
    public String nextActionName;
    public ArrayList<String> firstStepNames = new ArrayList<>();
    public ArrayList<Step> steps = new ArrayList<>();
}

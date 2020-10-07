package com.incquerylabs.arrowhead.bpmn.dto;

import java.util.ArrayList;

public class Step {

    public String name;
    public ArrayList<String> nextStepNames = new ArrayList<>();
    public Integer quantity = 1;
}

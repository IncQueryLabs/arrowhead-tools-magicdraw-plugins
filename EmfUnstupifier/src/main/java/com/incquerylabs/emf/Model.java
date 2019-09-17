package com.incquerylabs.emf;

import org.eclipse.emf.ecore.EObject;

import java.util.Collection;
import java.util.HashSet;


public class Model {
    Model() {
        ;
    }

    public Collection<EObject> getContents() {

        return new HashSet<>();
    }
}
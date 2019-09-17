package com.incquerylabs.emf;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.resource.ResourceSet;

public class Model {

    ResourceSet res;

    Model() {
        ;
    }

    public TreeIterator<Notifier> getContents() {
        return res.getAllContents();
    }
}
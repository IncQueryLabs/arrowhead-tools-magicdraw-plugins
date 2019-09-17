package com.incquerylabs.emf;

import org.eclipse.emf.ecore.resource.Resource;

import java.nio.file.Path;

public class MetaModel {

    Resource.Factory.Registry registry = Resource.Factory.Registry.INSTANCE;

    public MetaModel() {

    }

    public MetaModel(Path ecore, Path genmodel) {

        this.add(ecore, genmodel);
    }

    public void add(Path ecore, Path genmodel) {

    }

    public Model getModel(Path source) {
        //TODO everithing
        return new Model();
    }
}
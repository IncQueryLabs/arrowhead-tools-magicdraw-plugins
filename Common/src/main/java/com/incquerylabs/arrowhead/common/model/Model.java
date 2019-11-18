package com.incquerylabs.arrowhead.common.model;

import io.vertx.core.json.JsonObject;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Model {

    private Set<LocalCloud> localClouds = new HashSet<>();

    public Model(JsonObject base) {

    }

    public void merge(Model other) {

    }

    public JsonObject toJson() {
        return null;
    }

    public Set<LocalCloud> getLocalClouds() {
        return localClouds;
    }

    public boolean addLocalCloud(LocalCloud... clouds) {
        //TODO cascade
        return localClouds.addAll(Arrays.asList(clouds));
    }
}

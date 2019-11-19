package com.incquerylabs.arrowhead.common.model;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.HashSet;
import java.util.Set;

public class Model {

    private Set<LocalCloud> localClouds = new HashSet<>();

    public Model(JsonArray base) {
        String message = null;
        for (int i = 0; i < base.size(); ++i) {
            JsonObject cloudJson = base.getJsonObject(i);
            if (cloudJson != null) {
                try {
                    LocalCloud localCloud = new LocalCloud(cloudJson);
                } catch (IllegalArgumentException e) {
                    String under = e.getMessage();
                    message = under + "\n" + message;
                }
            } else {
                message = "Top Array MUST only contain JsonObjects describing Local Clouds!\n" + message;
            }
        }
        if (message != null) {
            throw new IllegalArgumentException(message);
        }
    }

    public void merge(Model other) {

    }

    public JsonArray toJson() {
        return null;
    }

    public Set<LocalCloud> getLocalClouds() {
        return localClouds;
    }
}

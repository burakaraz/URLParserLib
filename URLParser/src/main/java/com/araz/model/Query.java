package com.araz.model;

import java.util.HashMap;
import java.util.Map;

public class Query {

    private String name;
    private final Map<String, String> queryParameterMap;

    public Query() {
        queryParameterMap = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getQueryParameterMap() {
        return queryParameterMap;
    }

    @Override
    public String toString() {
        return name == null ? "" : name;
    }
}

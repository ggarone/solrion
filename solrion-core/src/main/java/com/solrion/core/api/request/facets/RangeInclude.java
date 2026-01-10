package com.solrion.core.api.request.facets;

public enum RangeInclude {
    LOWER("lower"), UPPER("upper"), EDGE("edge"), ALL("all");

    private final String value;

    RangeInclude(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}

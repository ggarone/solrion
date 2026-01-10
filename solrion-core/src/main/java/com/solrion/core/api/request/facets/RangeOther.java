package com.solrion.core.api.request.facets;

public enum RangeOther {
    BEFORE("before"), AFTER("after"), BETWEEN("between"), ALL("all"), NONE("none");

    private final String value;

    RangeOther(String value) {
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
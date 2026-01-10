package com.solrion.core.api.request.facets.json;

public enum JsonFacetType {
    TERMS("terms"),
    RANGE("range"),
    QUERY("queries"),
    STAT("stat"),
    FUNC("func"),
    HEATMAP("heatmap");

    private final String value;

    JsonFacetType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}

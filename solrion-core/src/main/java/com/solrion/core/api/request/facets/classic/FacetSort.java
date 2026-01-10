package com.solrion.core.api.request.facets.classic;

public enum FacetSort {
    COUNT("count"), INDEX("index");

    private final String value;

    FacetSort(String value) {
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

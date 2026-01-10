package com.solrion.core.client;

public enum ClientOperation {
    SELECT("select"),
    ADD("add"),
    DELETE("delete"),
    COMMIT("commit");

    private final String value;

    ClientOperation(String value) {
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

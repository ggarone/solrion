package com.solrion.core.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Standard response container for /select.
 */
public record DocsResult(
        @JsonProperty("numFound") Long numFound,
        @JsonProperty("request") Long start,
        @JsonProperty("maxScore") Double maxScore,
        @JsonProperty("docs") List<Object> docs
) {
    public Object firstOrNull() {
        return docs == null || docs.isEmpty() ? null : docs.get(0);
    }

    public Object lastOrNull() {
        return docs == null || docs.isEmpty() ? null : docs.get(docs.size() - 1);
    }
}

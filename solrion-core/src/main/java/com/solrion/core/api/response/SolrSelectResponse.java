package com.solrion.core.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record SolrSelectResponse(
        @JsonProperty("header") SolrResponseHeader header,
        @JsonProperty("error") SolrError error,
        @JsonProperty("response") DocsResult response,
        @JsonProperty("facet_counts") Map<String, Object> classicFacetCounts,
        @JsonProperty("facets") Map<String, Object> jsonFacets,
        @JsonProperty("grouped") Map<String, Object> grouped
) implements SolrResponse{}

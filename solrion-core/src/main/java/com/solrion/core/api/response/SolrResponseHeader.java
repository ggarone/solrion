package com.solrion.core.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record SolrResponseHeader(
        @JsonProperty("status") Integer status,
        @JsonProperty("QTime") Integer queryTime,
        @JsonProperty("params") Map<String, String> params
) {}

package com.solrion.core.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public record SolrError(
        @JsonProperty("msg") String message,
        @JsonProperty("code") Integer code,
        @JsonProperty("metadata") Map<String, Object> metadata,
        @JsonProperty("details") List<String> details
) {}
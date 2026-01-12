package com.solrion.core.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SolrUpdateResponse(
    @JsonProperty("header") SolrResponseHeader header, @JsonProperty("error") SolrError error)
    implements SolrResponse {}

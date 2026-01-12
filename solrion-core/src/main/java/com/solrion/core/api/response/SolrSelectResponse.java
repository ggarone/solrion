package com.solrion.core.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.solrion.core.api.response.facets.classic.ClassicFacetsResult;
import com.solrion.core.api.response.facets.json.JsonFacetsResult;
import com.solrion.core.api.response.grouping.GroupingResult;
import com.solrion.core.api.response.stats.classic.StatsResult;

public record SolrSelectResponse(
    @JsonProperty("responseHeader") SolrResponseHeader header,
    @JsonProperty("error") SolrError error,
    @JsonProperty("response") DocsResult result,
    @JsonProperty("facet_counts") ClassicFacetsResult classicFacets,
    @JsonProperty("facets") JsonFacetsResult jsonFacets,
    @JsonProperty("grouped") GroupingResult grouped,
    @JsonProperty("stats") StatsResult stats)
    implements SolrResponse {}

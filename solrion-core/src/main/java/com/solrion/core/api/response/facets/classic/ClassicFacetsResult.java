package com.solrion.core.api.response.facets.classic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Accessors(fluent = true)
public class ClassicFacetsResult {

  Map<String, TermsFacetResult> terms;
  Map<String, RangeFacetResult> ranges;
  Map<String, Long> queries;
  Map<String, IntervalFacetResult> intervals;
  Map<String, PivotFacetResult> pivots;
  Map<String, HeatmapFacetResult> heatmaps;

  @JsonCreator
  public ClassicFacetsResult(
      @JsonProperty("facet_fields") Map<String, TermsFacetResult> terms,
      @JsonProperty("facet_ranges") Map<String, RangeFacetResult> ranges,
      @JsonProperty("facet_queries") Map<String, Long> queries,
      @JsonProperty("facet_intervals") Map<String, IntervalFacetResult> intervals,
      @JsonProperty("facet_pivot") Map<String, PivotFacetResult> pivots,
      @JsonProperty("facet_heatmaps") Map<String, HeatmapFacetResult> heatmaps) {
    this.terms = terms;
    this.ranges = ranges;
    this.queries = queries;
    this.intervals = intervals;
    this.pivots = pivots;
    this.heatmaps = heatmaps;
  }
}

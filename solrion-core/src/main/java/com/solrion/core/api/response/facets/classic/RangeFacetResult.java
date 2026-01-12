package com.solrion.core.api.response.facets.classic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.solrion.core.internal.codec.serde.BucketMapDeserializer;
import java.util.Map;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Accessors(fluent = true)
public class RangeFacetResult {

  String start;
  String end;
  String gap;

  @JsonDeserialize(using = BucketMapDeserializer.class)
  Map<String, Long> counts;

  Long before;
  Long after;
  Long between;

  @JsonCreator
  public RangeFacetResult(
      @JsonProperty("start") String start,
      @JsonProperty("end") String end,
      @JsonProperty("gap") String gap,
      @JsonProperty("counts") Map<String, Long> counts,
      @JsonProperty("before") Long before,
      @JsonProperty("after") Long after,
      @JsonProperty("between") Long between) {
    this.start = start;
    this.end = end;
    this.gap = gap;
    this.counts = counts;
    this.before = before;
    this.after = after;
    this.between = between;
  }
}

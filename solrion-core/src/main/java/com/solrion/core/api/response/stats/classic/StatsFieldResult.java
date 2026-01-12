package com.solrion.core.api.response.stats.classic;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.Value;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Value
public class StatsFieldResult {

  // ---- common stats ----
  Long count;
  Object min;
  Object max;
  Double mean;
  Double sum;
  Double sumOfSquares;
  Double stddev;
  Long distinctValues;
  Map<String, Object> percentiles;

  // ---- forward compatibility ----
  Map<String, Object> raw;

  @JsonCreator
  public StatsFieldResult(
      @JsonProperty("count") Long count,
      @JsonProperty("min") Object min,
      @JsonProperty("max") Object max,
      @JsonProperty("mean") Double mean,
      @JsonProperty("sum") Double sum,
      @JsonProperty("sumOfSquares") Double sumOfSquares,
      @JsonProperty("stddev") Double stddev,
      @JsonProperty("distinctValues") Long distinctValues,
      @JsonProperty("percentiles") Map<String, Object> percentiles) {
    this.count = count;
    this.min = min;
    this.max = max;
    this.mean = mean;
    this.sum = sum;
    this.sumOfSquares = sumOfSquares;
    this.stddev = stddev;
    this.distinctValues = distinctValues;
    this.percentiles = percentiles == null ? Map.of() : Map.copyOf(percentiles);
    this.raw = new LinkedHashMap<>();
  }

  @JsonAnySetter
  void capture(String name, Object value) {
    raw.put(name, value);
  }
}

package com.solrion.core.api.response.facets.classic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.solrion.core.internal.codec.serde.HeatmapFacetResultDeserializer;
import java.util.List;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Accessors(fluent = true)
@JsonDeserialize(using = HeatmapFacetResultDeserializer.class)
public class HeatmapFacetResult {

  Double minX;
  Double maxX;
  Double minY;
  Double maxY;
  Integer rows;
  Integer columns;
  List<Integer> counts;

  @JsonCreator
  public HeatmapFacetResult(
      Double minX,
      Double maxX,
      Double minY,
      Double maxY,
      Integer rows,
      Integer columns,
      List<Integer> counts) {
    this.minX = minX;
    this.maxX = maxX;
    this.minY = minY;
    this.maxY = maxY;
    this.rows = rows;
    this.columns = columns;
    this.counts = counts;
  }
}

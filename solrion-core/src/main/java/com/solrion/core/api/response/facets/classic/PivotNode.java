package com.solrion.core.api.response.facets.classic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Accessors(fluent = true)
public class PivotNode {

  String field;
  Object value;
  Long count;
  List<PivotNode> children;

  @JsonCreator
  public PivotNode(
      @JsonProperty("field") String field,
      @JsonProperty("value") Object value,
      @JsonProperty("count") Long count,
      @JsonProperty("pivot") List<PivotNode> children) {
    this.field = field;
    this.value = value;
    this.count = count;
    this.children = children;
  }
}

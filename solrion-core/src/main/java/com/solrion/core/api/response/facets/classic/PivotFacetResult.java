package com.solrion.core.api.response.facets.classic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Accessors(fluent = true)
public class PivotFacetResult {

  List<PivotNode> roots;

  @JsonCreator
  public PivotFacetResult(@JsonProperty("roots") List<PivotNode> roots) {
    this.roots = roots;
  }
}

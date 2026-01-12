package com.solrion.core.api.response.facets.json;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.Value;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Value
public class JsonFacetsResult {

  @JsonProperty("count")
  Long count;

  /** Named top-level facets. Key = facet name */
  Map<String, JsonFacetResult> facets;

  @JsonCreator
  public JsonFacetsResult(@JsonProperty("count") Long count) {
    this.count = count;
    this.facets = new LinkedHashMap<>();
  }

  @JsonAnySetter
  public void addFacet(String name, JsonFacetResult facetResult) {
    facets.put(name, facetResult);
  }
}

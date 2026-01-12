package com.solrion.core.api.response.facets.json;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.Value;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Value
public class JsonFacetBucket {

  Object value;
  Long count;

  /** Nested sub-facets per bucket */
  Map<String, Object> scalars;

  Map<String, JsonFacetResult> facets;

  public JsonFacetBucket(@JsonProperty("val") Object value, @JsonProperty("count") Long count) {
    this.value = value;
    this.count = count;
    this.facets = new LinkedHashMap<>();
    this.scalars = new LinkedHashMap<>();
  }

  @JsonAnySetter
  void capture(String name, Object value) {
    if (value instanceof JsonFacetResult facet) {
      facets.put(name, facet);
    } else {
      scalars.put(name, value);
    }
  }
}

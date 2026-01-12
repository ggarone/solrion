package com.solrion.core.api.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum JsonFacetType {
  TERMS("terms"),
  RANGE("range"),
  QUERY("queries"),
  STAT("stat"),
  FUNC("func"),
  HEATMAP("heatmap");

  private final String value;

  JsonFacetType(String value) {
    this.value = value;
  }

  public String value() {
    return value;
  }

  @JsonValue
  public String jsonValue() {
    return value;
  }

  @JsonCreator
  public static JsonFacetType from(String v) {
    for (JsonFacetType t : values()) {
      if (t.value.equalsIgnoreCase(v)) {
        return t;
      }
    }
    throw new IllegalArgumentException("Unknown json facet type: " + v);
  }
}

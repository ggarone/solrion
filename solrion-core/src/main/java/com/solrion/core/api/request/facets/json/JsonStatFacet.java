package com.solrion.core.api.request.facets.json;

import com.solrion.core.api.types.JsonFacetType;
import com.solrion.core.internal.Validate;
import java.util.Map;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Accessors(fluent = true)
public class JsonStatFacet implements JsonFacet {

  String stat;
  Map<String, Object> rawOptions;

  @Builder
  public JsonStatFacet(String func, Map<String, Object> rawOptions) {
    this.stat = Validate.notBlank(func, "stat");
    this.rawOptions = rawOptions == null ? Map.of() : Map.copyOf(rawOptions);
  }

  @Override
  public JsonFacetType type() {
    return JsonFacetType.STAT;
  }

  @Override
  public Map<String, JsonFacet> facets() {
    return Map.of();
  }

  @Override
  public <R, C> R accept(JsonFacetVisitor<R, C> visitor, C ctx) {
    return visitor.visitStat(this, ctx);
  }

  public static JsonStatFacet of(String function) {
    return JsonStatFacet.builder().func(function).build();
  }
}

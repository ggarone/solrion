package com.solrion.core.api.request.facets.json;

import com.solrion.core.api.types.JsonFacetType;
import com.solrion.core.api.types.RangeInclude;
import com.solrion.core.api.types.RangeOther;
import com.solrion.core.internal.Validate;
import java.util.Map;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Accessors(fluent = true)
public class JsonRangeFacet implements JsonFacet {

  String field;
  String start;
  String end;
  String gap;
  RangeInclude include;
  RangeOther other;
  Boolean hardEnd;
  Map<String, JsonFacet> facets;
  Map<String, Object> rawOptions;

  @Builder
  public JsonRangeFacet(
      String field,
      String start,
      String end,
      String gap,
      RangeInclude include,
      RangeOther other,
      Boolean hardEnd,
      Map<String, JsonFacet> facets,
      Map<String, Object> rawOptions) {
    this.field = Validate.notBlank(field, "field");
    this.start = Validate.safeTrim(start);
    this.end = Validate.safeTrim(end);
    this.gap = Validate.safeTrim(gap);

    if (gap == null) {
      throw new IllegalArgumentException("gap is required for range facet");
    }

    this.include = include;
    this.other = other;
    this.hardEnd = hardEnd;

    this.facets = facets == null ? Map.of() : Map.copyOf(facets);
    this.rawOptions = rawOptions == null ? Map.of() : Map.copyOf(rawOptions);
  }

  @Override
  public JsonFacetType type() {
    return JsonFacetType.RANGE;
  }

  @Override
  public <R, C> R accept(JsonFacetVisitor<R, C> visitor, C ctx) {
    return visitor.visitRange(this, ctx);
  }
}

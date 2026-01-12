package com.solrion.core.api.request.facets.json;

import com.solrion.core.api.types.JsonFacetType;
import com.solrion.core.internal.Validate;
import java.util.Map;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Accessors(fluent = true)
public class JsonTermsFacet implements JsonFacet {

  String field;
  Integer limit;
  Integer offset;
  String sort;
  Integer minCount;
  Boolean missing;
  Boolean numBuckets;
  Boolean allBuckets;
  Map<String, JsonFacet> facets;
  Map<String, Object> rawOptions;

  @Builder
  public JsonTermsFacet(
      String field,
      Integer limit,
      Integer offset,
      String sort,
      Integer minCount,
      Boolean missing,
      Boolean numBuckets,
      Boolean allBuckets,
      Map<String, JsonFacet> facets,
      Map<String, Object> rawOptions) {
    this.field = Validate.notBlank(field, "field");

    if (limit != null) {
      Validate.require(limit != 0, "limit cannot be 0");
    }
    if (offset != null) {
      Validate.require(offset >= 0, "offset must be >= 0");
    }
    if (minCount != null) {
      Validate.require(minCount >= 0, "minCount must be >= 0");
    }

    this.limit = limit;
    this.offset = offset;
    this.sort = Validate.safeTrim(sort);
    this.minCount = minCount;
    this.missing = missing;
    this.numBuckets = numBuckets;
    this.allBuckets = allBuckets;

    this.facets = facets == null ? Map.of() : Map.copyOf(facets);
    this.rawOptions = rawOptions == null ? Map.of() : Map.copyOf(rawOptions);
  }

  @Override
  public JsonFacetType type() {
    return JsonFacetType.TERMS;
  }

  @Override
  public <R, C> R accept(JsonFacetVisitor<R, C> visitor, C ctx) {
    return visitor.visitTerms(this, ctx);
  }
}

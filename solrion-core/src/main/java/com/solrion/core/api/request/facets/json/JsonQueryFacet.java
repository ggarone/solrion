package com.solrion.core.api.request.facets.json;

import com.solrion.core.api.types.JsonFacetType;
import com.solrion.core.internal.Validate;
import com.solrion.core.query.Expr;
import java.util.Map;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Accessors(fluent = true)
public class JsonQueryFacet implements JsonFacet {

  Expr.BoolExpr query;
  Map<String, JsonFacet> facets;
  Map<String, Object> rawOptions;

  @Builder
  public JsonQueryFacet(
      Expr.BoolExpr query, Map<String, JsonFacet> facets, Map<String, Object> rawOptions) {
    this.query = Validate.notNull(query, "query");
    this.facets = facets == null ? Map.of() : Map.copyOf(facets);
    this.rawOptions = rawOptions == null ? Map.of() : Map.copyOf(rawOptions);
  }

  @Override
  public JsonFacetType type() {
    return JsonFacetType.QUERY;
  }

  @Override
  public <R, C> R accept(JsonFacetVisitor<R, C> visitor, C ctx) {
    return visitor.visitQuery(this, ctx);
  }
}

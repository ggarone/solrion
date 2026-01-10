package com.solrion.core.api.request.facets.json;

import com.solrion.core.internal.Validate;
import com.solrion.core.query.Expr;
import lombok.Value;
import lombok.experimental.Accessors;
import lombok.Builder;

import java.util.Map;

@Value
@Accessors(fluent = true)
public class JsonQueryFacet implements JsonFacet {

    Map<String, Expr.BoolExpr> queries;
    Map<String, JsonFacet> facets;
    Map<String, Object> rawOptions;

    @Builder
    public JsonQueryFacet(
            Map<String, Expr.BoolExpr> queries,
            Map<String, JsonFacet> facets,
            Map<String, Object> rawOptions
    ) {
        this.queries = Validate.notEmpty(queries, "queries");
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

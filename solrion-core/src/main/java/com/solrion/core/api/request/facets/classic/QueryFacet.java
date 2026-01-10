package com.solrion.core.api.request.facets.classic;

import com.solrion.core.internal.Validate;
import com.solrion.core.query.Expr;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Value
public class QueryFacet implements ClassicFacet {

    String name;
    Expr query;
    Expr.LocalParams localParams;

    @Builder
    public QueryFacet(String name, Expr query, Expr.LocalParams localParams) {
        this.query = Validate.notNull(query, "query");
        this.name = Validate.safeTrim(name);
        this.localParams = localParams;
    }

    public static QueryFacet of(String name, Expr query) {
        return QueryFacet.builder()
                .name(name)
                .query(query)
                .build();
    }

    @Override
    public <R, C> R accept(ClassicalFacetVisitor<R, C> visitor, C ctx) {
        return visitor.visitQuery(this, ctx);
    }
}


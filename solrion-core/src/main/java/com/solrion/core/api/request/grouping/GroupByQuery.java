package com.solrion.core.api.request.grouping;

import com.solrion.core.internal.Validate;
import com.solrion.core.query.Expr;

import java.util.List;

public record GroupByQuery(List<Expr.BoolExpr> queries) implements GroupingTarget {

    public GroupByQuery {
        queries = Validate.notEmpty(queries, "queries");
        queries = List.copyOf(queries);
    }
}

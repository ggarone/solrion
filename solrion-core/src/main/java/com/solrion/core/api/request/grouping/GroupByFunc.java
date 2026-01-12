package com.solrion.core.api.request.grouping;

import com.solrion.core.internal.Validate;
import com.solrion.core.query.Expr;

public record GroupByFunc(Expr func) implements GroupingTarget {

  public GroupByFunc {
    Validate.notNull(func, "func");
  }
}

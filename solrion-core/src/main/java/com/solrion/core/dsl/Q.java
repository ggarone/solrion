package com.solrion.core.dsl;

import com.solrion.core.api.types.Range;
import com.solrion.core.dsl.builder.FieldBuilder;
import com.solrion.core.query.Expr;
import com.solrion.core.query.ValueModifiers;

public final class Q {

  private Q() {}

  /* ============================================================
   * Field entry point
   * ============================================================ */

  public static FieldBuilder field(String name) {
    return new FieldBuilder(name);
  }

  /* ============================================================
   * Match-all shortcut
   * ============================================================ */

  public static Expr.BoolExpr matchAll() {
    return new Expr.Raw("*:*");
  }

  /* ============================================================
   * Low-level escape hatch (rare)
   * ============================================================ */

  public static Expr.BoolExpr raw(String raw) {
    return new Expr.Raw(raw);
  }

  /* ============================================================
   * Value helpers (delegating to existing AST)
   * ============================================================ */

  public static Expr.Term term(String value) {
    return new Expr.Term(value, ValueModifiers.empty());
  }

  public static Expr.Term phrase(String value) {
    return new Expr.Term(value, ValueModifiers.builder().phrase(true).build());
  }

  public static Expr.Term regex(String value) {
    return new Expr.Term(value, ValueModifiers.builder().regex(true).build());
  }

  public static Expr.Term wildcard(String value) {
    return new Expr.Term(value, ValueModifiers.builder().wildcard(true).build());
  }

  public static Expr.Term fuzzy(String value, int distance) {
    return new Expr.Term(value, ValueModifiers.builder().fuzzyDistance(distance).build());
  }

  public static Expr.RangeValue range(Range<String> range) {
    return new Expr.RangeValue(range);
  }

  public static Expr.RawValue rawValue(String value) {
    return new Expr.RawValue(value);
  }
}

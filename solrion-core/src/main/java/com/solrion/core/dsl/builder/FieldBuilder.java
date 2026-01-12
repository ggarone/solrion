package com.solrion.core.dsl.builder;

import com.solrion.core.api.types.Range;
import com.solrion.core.dsl.Q;
import com.solrion.core.internal.Validate;
import com.solrion.core.query.Expr;
import com.solrion.core.query.ValueModifiers;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public final class FieldBuilder {

  private final String field;

  public FieldBuilder(String field) {
    this.field = Validate.safeTrim(field);
  }

  public Expr.BoolExpr matchAll(Expr.BoolExpr... parts) {
    return matchAll(Arrays.asList(parts));
  }

  public Expr.BoolExpr matchAll(Collection<? extends Expr.BoolExpr> parts) {
    return build(combine(parts, true));
  }

  public Expr.BoolExpr matchAny(Expr.BoolExpr... parts) {
    return matchAny(Arrays.asList(parts));
  }

  public Expr.BoolExpr matchAny(Collection<? extends Expr.BoolExpr> parts) {
    return build(combine(parts, false));
  }

  private static Expr.BoolExpr combine(Collection<? extends Expr.BoolExpr> parts, boolean and) {
    if (parts == null || parts.isEmpty()) {
      return Expr.empty();
    }
    Expr.BoolExpr acc = Expr.empty();
    for (Expr.BoolExpr p : parts) {
      if (p == null || p.isEmpty()) {
        continue;
      }
      acc = acc.isEmpty() ? p : and ? new Expr.And(acc, p) : new Expr.Or(acc, p);
    }
    return acc;
  }

  /* ============================================================
   * Equality
   * ============================================================ */

  public Expr.BoolExpr eq(String value) {
    return eq(Q.term(value));
  }

  public Expr.BoolExpr eq(Expr.ValueExpr value) {
    return build(value);
  }

  /* ============================================================
   * Negation
   * ============================================================ */

  public Expr.BoolExpr neq(String value) {
    return eq(value).not();
  }

  public Expr.BoolExpr neq(Expr.ValueExpr value) {
    return eq(value).not();
  }

  /* ============================================================
   * Range semantics
   * ============================================================ */

  public <T extends Comparable<T>> Expr.BoolExpr within(Range<T> range) {
    return build(new Expr.RangeValue<>(range));
  }

  public <T extends Comparable<T>> Expr.BoolExpr within(T lo, T hi) {
    return within(Range.closed(lo, hi));
  }

  public <T extends Comparable<T>> Expr.BoolExpr between(T lo, T hi) {
    return within(lo, hi);
  }

  public Expr.BoolExpr gt(String value) {
    return within(Range.greaterThan(value));
  }

  public Expr.BoolExpr gte(String value) {
    return within(Range.atLeast(value));
  }

  public Expr.BoolExpr lt(String value) {
    return within(Range.lowerThan(value));
  }

  public Expr.BoolExpr lte(String value) {
    return within(Range.atMost(value));
  }

  /* ============================================================
   * Set membership
   * ============================================================ */

  public Expr.BoolExpr in(String... values) {
    return in(Arrays.asList(values));
  }

  public Expr.BoolExpr in(Collection<String> values) {
    if (values == null || values.isEmpty()) {
      return Expr.empty();
    }
    return in(
        matchAny(values.stream().filter(Objects::nonNull).map(Q::term).map(this::build).toList()));
  }

  public Expr.BoolExpr in(Expr.BoolExpr expr) {
    if (expr == null || expr.isEmpty()) {
      return Expr.empty();
    }
    return build(expr);
  }

  /* ============================================================
   * Text semantics
   * ============================================================ */

  public Expr.BoolExpr phrase(String value) {
    return build(Q.phrase(value));
  }

  public Expr.BoolExpr regex(String pattern) {
    return build(Q.regex(pattern));
  }

  public Expr.BoolExpr wildcard(String value) {
    return build(Q.wildcard(value));
  }

  public Expr.BoolExpr fuzzy(String value, int distance) {
    return build(Q.fuzzy(value, distance));
  }

  /* ============================================================
   * Existence semantics (NEW)
   * ============================================================ */

  public Expr.BoolExpr exists() {
    return build(Q.rawValue("*"));
  }

  public Expr.BoolExpr missing() {
    return exists().not();
  }

  /* ============================================================
   * Boosting (NEW)
   * ============================================================ */

  public Expr.BoolExpr boosted(String value, double boost) {
    return build(new Expr.Term(value, ValueModifiers.builder().boost(boost).build()));
  }

  /* ============================================================
   * Internal builder
   * ============================================================ */

  private Expr.BoolExpr build(Expr.BoolExpr expr) {
    if (Validate.isBlank(field) || expr == null || expr.isEmpty()) {
      return Expr.empty();
    }
    return new Expr.Field(field, expr, Expr.LocalParams.empty());
  }
}

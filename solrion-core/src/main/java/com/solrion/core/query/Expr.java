package com.solrion.core.query;

import com.solrion.core.types.Range;
import com.solrion.core.internal.Validate;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public sealed interface Expr
        permits Expr.BoolExpr, Expr.Empty, Expr.EmptyValue, Expr.FieldList, Expr.LocalParams, Expr.Raw, Expr.SortExpr {

    static Empty empty() {
        return new Empty();
    }

    static EmptyValue emptyValue() {
        return new EmptyValue();
    }

    <R, C> R accept(ExprVisitor<R, C> visitor, C ctx);

    // ---------------------------------------------------------------------
    // Boolean composition helpers (only on BoolExpr)
    // ---------------------------------------------------------------------

    /**
     * Empty means "no-op expression" (will be omitted by mappers/renderers).
     * This is intentionally distinct from {@code null}.
     */
    boolean isEmpty();

    sealed interface BoolExpr extends Expr permits And, Empty, Field, Group, Not, Or, Raw, ValueExpr {

        default BoolExpr and(BoolExpr other) {
            if (other == null || other.isEmpty()) return this;
            if (this.isEmpty()) return other;
            return new And(this, other);
        }

        default BoolExpr or(BoolExpr other) {
            if (other == null || other.isEmpty()) return this;
            if (this.isEmpty()) return other;
            return new Or(this, other);
        }

        default BoolExpr not() {
            if (this.isEmpty()) return this;
            return new Not(this);
        }
    }

    // ---------------------------------------------------------------------
    // Nodes
    // ---------------------------------------------------------------------

    sealed interface ValueExpr extends BoolExpr permits EmptyValue, RangeValue, RawValue, Term {
    }

    sealed interface SortExpr extends Expr permits SortField, Sort {
    }

    /**
     * Empty / no-op expression.
     */
    record Empty() implements Expr, BoolExpr {
        @Override
        public <R, C> R accept(ExprVisitor<R, C> visitor, C ctx) {
            return visitor.visitEmpty(this, ctx);
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public String toString() {
            return "<empty>";
        }
    }

    record EmptyValue() implements Expr, ValueExpr {

        @Override
        public <R, C> R accept(ExprVisitor<R, C> visitor, C ctx) {
            return visitor.visitEmptyValue(this, ctx);
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public String toString() {
            return "<empty value>";
        }
    }

    record Raw(String data) implements Expr, BoolExpr {
        public Raw {
            data = Validate.safeTrim(data);
        }

        @Override
        public <R, C> R accept(ExprVisitor<R, C> visitor, C ctx) {
            return visitor.visitRaw(this, ctx);
        }

        @Override
        public boolean isEmpty() {
            return Validate.isBlank(data);
        }
    }

    /**
     * Forces explicit grouping (parentheses) around a subexpression.
     */
    record Group(BoolExpr expr) implements BoolExpr {

        public Group(BoolExpr expr) {
            this.expr = Validate.notNull(expr, "expr");
        }

        @Override
        public <R, C> R accept(ExprVisitor<R, C> visitor, C ctx) {
            return visitor.visitGroup(this, ctx);
        }

        @Override
        public boolean isEmpty() {
            return expr == null || expr.isEmpty();
        }
    }

    record LocalParams(Map<String, String> params) implements Expr {
        public LocalParams {
            params = params == null ? Map.of() : Map.copyOf(params);
        }

        public static LocalParams empty() {
            return new LocalParams(Map.of());
        }

        @Override
        public <R, C> R accept(ExprVisitor<R, C> visitor, C ctx) {
            return visitor.visitLocalParams(this, ctx);
        }

        @Override
        public boolean isEmpty() {
            return Validate.isEmpty(params);
        }
    }

    record Field(String fieldName, BoolExpr value, LocalParams localParams) implements BoolExpr {

        public Field {
            Validate.notNull(value, "value");
            fieldName = Validate.safeTrim(fieldName);
            localParams = localParams == null ? LocalParams.empty() : localParams;
        }

        @Override
        public <R, C> R accept(ExprVisitor<R, C> visitor, C ctx) {
            return visitor.visitField(this, ctx);
        }

        @Override
        public boolean isEmpty() {
            return Validate.isBlank(fieldName) || value.isEmpty();
        }
    }

    record And(BoolExpr left, BoolExpr right) implements BoolExpr {
        public And {
            Validate.notNull(left, "left");
            Validate.notNull(right, "right");
        }

        @Override
        public <R, C> R accept(ExprVisitor<R, C> visitor, C ctx) {
            return visitor.visitAnd(this, ctx);
        }

        @Override
        public boolean isEmpty() {
            return left.isEmpty() || right.isEmpty();
        }
    }

    record Or(BoolExpr left, BoolExpr right) implements BoolExpr {
        public Or {
            Validate.notNull(left, "left");
            Validate.notNull(right, "right");
        }

        @Override
        public <R, C> R accept(ExprVisitor<R, C> visitor, C ctx) {
            return visitor.visitOr(this, ctx);
        }

        @Override
        public boolean isEmpty() {
            return left.isEmpty() || right.isEmpty();
        }
    }

    record Not(BoolExpr expr) implements BoolExpr {
        public Not {
            Validate.notNull(expr, "expr");
        }

        @Override
        public <R, C> R accept(ExprVisitor<R, C> visitor, C ctx) {
            return visitor.visitNot(this, ctx);
        }

        @Override
        public boolean isEmpty() {
            return expr.isEmpty();
        }
    }

    record Term(String term, ValueModifiers modifiers) implements ValueExpr {
        public Term {
            term = Validate.safeTrim(term);
            modifiers = modifiers == null ? ValueModifiers.empty() : modifiers;
        }

        @Override
        public <R, C> R accept(ExprVisitor<R, C> visitor, C ctx) {
            return visitor.visitTerm(this, ctx);
        }

        @Override
        public boolean isEmpty() {
            return Validate.isBlank(term);
        }
    }

    record RawValue(String raw) implements ValueExpr {
        public RawValue {
            raw = Validate.safeTrim(raw);
        }

        @Override
        public <R, C> R accept(ExprVisitor<R, C> visitor, C ctx) {
            return visitor.visitRawValue(this, ctx);
        }

        @Override
        public boolean isEmpty() {
            return Validate.isBlank(raw);
        }
    }

    record RangeValue<T extends Comparable<T>>(Range<T> range) implements ValueExpr {
        public RangeValue {
            Validate.notNull(range, "range");
        }

        @Override
        public <R, C> R accept(ExprVisitor<R, C> visitor, C ctx) {
            return visitor.visitRangeValue(this, ctx);
        }

        @Override
        public boolean isEmpty() {
            return range == null;
        }
    }

    // ---------------------------------------------------------------------
    // Sort
    // ---------------------------------------------------------------------

    record SortField(String fieldName, SortDirection direction) implements SortExpr {
        public SortField {
            fieldName = Validate.safeTrim(fieldName);
            direction = direction == null ? SortDirection.ASC : direction;
        }

        @Override
        public <R, C> R accept(ExprVisitor<R, C> visitor, C ctx) {
            return visitor.visitSortField(this, ctx);
        }

        @Override
        public boolean isEmpty() {
            return Validate.isBlank(fieldName);
        }
    }

    // ---------------------------------------------------------------------
    // Singletons
    // ---------------------------------------------------------------------

    record Sort(Collection<SortField> fields) implements SortExpr {
        public Sort {
            fields = fields == null ? List.of() : List.copyOf(fields);
        }

        @Override
        public <R, C> R accept(ExprVisitor<R, C> visitor, C ctx) {
            return visitor.visitSort(this, ctx);
        }

        @Override
        public boolean isEmpty() {
            return fields == null || fields.isEmpty() || fields.stream().allMatch(f -> f == null || f.isEmpty());
        }
    }

    record FieldList(List<String> fields, boolean includeScore, boolean includeAll) implements Expr {

        public FieldList {
            fields = fields == null ? List.of() : List.copyOf(fields);
        }

        public static FieldList all() {
            return new FieldList(List.of(), false, true);
        }

        public static FieldList of(String... fields) {
            return new FieldList(List.of(fields), false, false);
        }

        public static FieldList of(List<String> fields) {
            return new FieldList(fields, false, false);
        }

        public FieldList withScore() {
            return new FieldList(this.fields, true, this.includeAll);
        }

        @Override
        public <R, C> R accept(ExprVisitor<R, C> visitor, C ctx) {
            return visitor.visitFieldList(this, ctx);
        }

        @Override
        public boolean isEmpty() {
            return !includeAll && fields.isEmpty();
        }
    }
}

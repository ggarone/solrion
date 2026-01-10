package com.solrion.core.query;

public interface ExprVisitor<R, C> {
    R visitEmpty(Expr.Empty empty, C ctx);
    R visitRaw(Expr.Raw raw, C ctx);

    R visitLocalParams(Expr.LocalParams params, C ctx);
    R visitField(Expr.Field field, C ctx);

    R visitGroup(Expr.Group group, C ctx);
    R visitAnd(Expr.And and, C ctx);
    R visitOr(Expr.Or or, C ctx);
    R visitNot(Expr.Not not, C ctx);

    R visitEmptyValue(Expr.EmptyValue empty, C ctx);
    R visitTerm(Expr.Term term, C ctx);
    R visitRawValue(Expr.RawValue value, C ctx);
    R visitRangeValue(Expr.RangeValue<?> range, C ctx);

    R visitSortField(Expr.SortField field, C ctx);
    R visitSort(Expr.Sort sort, C ctx);

    R visitFieldList(Expr.FieldList fieldList, C ctx);
}

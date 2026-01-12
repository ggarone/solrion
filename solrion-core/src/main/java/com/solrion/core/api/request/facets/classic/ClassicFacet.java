package com.solrion.core.api.request.facets.classic;

import com.solrion.core.query.Expr;

/** Marker for classic facet types (facet.field, facet.range, ...). */
public sealed interface ClassicFacet
    permits HeatmapFacet, IntervalFacet, PivotFacet, QueryFacet, RangeFacet, TermsFacet {
  String name();

  Expr.LocalParams localParams();

  <R, C> R accept(ClassicalFacetVisitor<R, C> visitor, C ctx);
}

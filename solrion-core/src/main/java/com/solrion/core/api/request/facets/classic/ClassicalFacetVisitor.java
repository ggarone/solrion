package com.solrion.core.api.request.facets.classic;

public interface ClassicalFacetVisitor<R, C> {
    R visitTerms(TermsFacet facet, C ctx);
    R visitRange(RangeFacet<?> facet, C ctx);
    R visitQuery(QueryFacet facet, C ctx);
    R visitPivot(PivotFacet facet, C ctx);
    R visitInterval(IntervalFacet facet, C ctx);
    R visitHeatmap(HeatmapFacet facet, C ctx);
}
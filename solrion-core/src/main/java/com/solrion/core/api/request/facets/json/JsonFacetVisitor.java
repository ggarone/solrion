package com.solrion.core.api.request.facets.json;

public interface JsonFacetVisitor<R, C> {

  R visitTerms(JsonTermsFacet facet, C ctx);

  R visitRange(JsonRangeFacet facet, C ctx);

  R visitQuery(JsonQueryFacet facet, C ctx);

  R visitStat(JsonStatFacet facet, C ctx);

  R visitHeatmap(JsonHeatmapFacet facet, C ctx);
}

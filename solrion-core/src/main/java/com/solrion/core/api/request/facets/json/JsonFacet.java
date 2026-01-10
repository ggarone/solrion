package com.solrion.core.api.request.facets.json;

import java.util.Map;

public interface JsonFacet {

    JsonFacetType type();

    Map<String, JsonFacet> facets();

    Map<String, Object> rawOptions();

    <R,C> R accept(JsonFacetVisitor<R,C> visitor, C ctx);
}

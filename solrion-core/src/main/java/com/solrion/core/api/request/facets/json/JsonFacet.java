package com.solrion.core.api.request.facets.json;

import com.solrion.core.api.types.JsonFacetType;
import java.util.Map;

public interface JsonFacet {

  JsonFacetType type();

  Map<String, JsonFacet> facets();

  Map<String, Object> rawOptions();

  <R, C> R accept(JsonFacetVisitor<R, C> visitor, C ctx);
}

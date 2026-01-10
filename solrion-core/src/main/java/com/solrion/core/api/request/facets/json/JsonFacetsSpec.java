package com.solrion.core.api.request.facets.json;

import java.util.Map;

public record JsonFacetsSpec(
        boolean enabled,
        Map<String, JsonFacet> facets,
        Map<String, Object> rawOptions
) {
    public JsonFacetsSpec {
        facets = facets == null ? Map.of() : Map.copyOf(facets);
        rawOptions = rawOptions == null ? Map.of() : Map.copyOf(rawOptions);
    }

    public static JsonFacetsSpec disabled() {
        return new JsonFacetsSpec(false, Map.of(), Map.of());
    }
}

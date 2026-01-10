package com.solrion.core.api.request.facets.classic;

import java.util.List;
import java.util.Map;

public record ClassicFacetsSpec(
        boolean enabled,

        Boolean facetMissing,
        FacetSort facetSort,
        Integer facetLimit,
        Integer facetMinCount,

        List<ClassicFacet> facets,

        Map<String, String> rawParams
) {
    public ClassicFacetsSpec {
        facets = facets == null ? List.of() : List.copyOf(facets);
        rawParams = rawParams == null ? Map.of() : Map.copyOf(rawParams);
    }

    public static ClassicFacetsSpec disabled() {
        return new ClassicFacetsSpec(
                false,
                null,
                null,
                null,
                null,
                List.of(),
                Map.of()
        );
    }
}

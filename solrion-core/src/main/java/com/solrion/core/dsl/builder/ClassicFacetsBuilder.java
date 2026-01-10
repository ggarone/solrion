package com.solrion.core.dsl.builder;

import com.solrion.core.api.request.facets.classic.ClassicFacet;
import com.solrion.core.api.request.facets.classic.ClassicFacetsSpec;
import com.solrion.core.api.request.facets.classic.FacetSort;
import com.solrion.core.internal.Validate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Top-level fluent builder for classic Solr facets (facet.field, facet.range, ...).
 *
 * Facet instances are created by the caller (via Lombok builders)
 * and registered here.
 */
public final class ClassicFacetsBuilder {

    private final SelectRequestBuilder parent;

    private boolean enabled = true;
    private Boolean facetMissing;
    private FacetSort facetSort;
    private Integer facetLimit;
    private Integer facetMinCount;

    private final List<ClassicFacet> facets = new ArrayList<>();
    private final Map<String, String> rawParams = new LinkedHashMap<>();

    ClassicFacetsBuilder(SelectRequestBuilder parent) {
        this.parent = Validate.notNull(parent, "parent");
    }

    // ------------------------------------------------------------------
    // Global configuration
    // ------------------------------------------------------------------

    public ClassicFacetsBuilder enabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public ClassicFacetsBuilder missing(Boolean value) {
        this.facetMissing = value;
        return this;
    }

    public ClassicFacetsBuilder sort(FacetSort sort) {
        this.facetSort = sort;
        return this;
    }

    public ClassicFacetsBuilder limit(Integer value) {
        this.facetLimit = value;
        return this;
    }

    public ClassicFacetsBuilder minCount(Integer value) {
        this.facetMinCount = value;
        return this;
    }

    // ------------------------------------------------------------------
    // Facet registration
    // ------------------------------------------------------------------

    /**
     * Register a classic facet.
     */
    public ClassicFacetsBuilder facet(ClassicFacet facet) {
        if (facet != null) {
            facets.add(facet);
        }
        return this;
    }

    /**
     * Register multiple facets at once.
     */
    public ClassicFacetsBuilder facets(List<? extends ClassicFacet> facets) {
        if (facets == null || facets.isEmpty()) return this;

        facets.forEach(f -> {
            if (f != null) this.facets.add(f);
        });
        return this;
    }

    // ------------------------------------------------------------------
    // Raw params (escape hatch)
    // ------------------------------------------------------------------

    public ClassicFacetsBuilder param(String name, String value) {
        name = Validate.safeTrim(name);
        value = Validate.safeTrim(value);

        if (!Validate.isBlank(name) && !Validate.isBlank(value)) {
            rawParams.put(name, value);
        }
        return this;
    }

    // ------------------------------------------------------------------
    // Terminal
    // ------------------------------------------------------------------

    public SelectRequestBuilder done() {
        parent.setClassicFacets(
                new ClassicFacetsSpec(
                        enabled,
                        facetMissing,
                        facetSort,
                        facetLimit,
                        facetMinCount,
                        List.copyOf(facets),
                        Map.copyOf(rawParams)
                )
        );
        return parent;
    }
}

package com.solrion.core.dsl.builder;

import com.solrion.core.api.request.facets.json.JsonFacet;
import com.solrion.core.api.request.facets.json.JsonFacetsSpec;
import com.solrion.core.internal.Validate;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Top-level fluent builder for JSON facets (json.facet).
 *
 * <p>Facet instances are created by the caller using Lombok builders and registered here under a
 * name.
 */
public final class JsonFacetsBuilder {

  private final SelectRequestBuilder parent;

  private boolean enabled = true;
  private final Map<String, JsonFacet> facets = new LinkedHashMap<>();
  private final Map<String, Object> rawOptions = new LinkedHashMap<>();

  JsonFacetsBuilder(SelectRequestBuilder parent) {
    this.parent = Validate.notNull(parent, "parent");
  }

  // ------------------------------------------------------------------
  // Configuration
  // ------------------------------------------------------------------

  public JsonFacetsBuilder enabled(boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  /**
   * Register a JSON facet under the given name.
   *
   * @param name facet name (map key in json.facet)
   * @param facet facet definition
   */
  public JsonFacetsBuilder facet(String name, JsonFacet facet) {
    facets.put(Validate.notBlank(name, "name"), Validate.notNull(facet, "facet"));
    return this;
  }

  /** Convenience overload allowing map-style registration. */
  public JsonFacetsBuilder facets(Map<String, ? extends JsonFacet> facets) {
    if (facets == null || facets.isEmpty()) {
      return this;
    }

    facets.forEach(
        (k, v) -> {
          if (!Validate.isBlank(k) && v != null) {
            this.facets.put(k, v);
          }
        });
    return this;
  }

  /** Add raw options at the json.facet root level. */
  public JsonFacetsBuilder option(String key, Object value) {
    if (!Validate.isBlank(key) && value != null) {
      rawOptions.put(key, value);
    }
    return this;
  }

  // ------------------------------------------------------------------
  // Terminal
  // ------------------------------------------------------------------

  public SelectRequestBuilder done() {
    parent.setJsonFacets(new JsonFacetsSpec(enabled, Map.copyOf(facets), Map.copyOf(rawOptions)));
    return parent;
  }
}

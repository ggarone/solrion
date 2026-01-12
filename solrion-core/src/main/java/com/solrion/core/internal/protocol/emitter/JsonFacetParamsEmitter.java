package com.solrion.core.internal.protocol.emitter;

import com.solrion.core.api.request.facets.json.*;
import com.solrion.core.api.request.facets.json.JsonFacetVisitor;
import com.solrion.core.api.types.JsonFacetType;
import com.solrion.core.codec.SolrCodec;
import com.solrion.core.internal.Validate;
import com.solrion.core.internal.protocol.ParamBag;
import com.solrion.core.query.SolrDialectTranslator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/** Emits "json.facet" parameter from structured JsonFacet models. */
public final class JsonFacetParamsEmitter implements JsonFacetVisitor<Object, Map<String, Object>> {

  private static final Set<JsonFacetType> LEAF_FACET_TYPES =
      Set.of(JsonFacetType.STAT, JsonFacetType.QUERY);

  private final SolrDialectTranslator translator;
  private final SolrCodec codec;

  public JsonFacetParamsEmitter(SolrDialectTranslator translator, SolrCodec codec) {
    this.translator = Validate.notNull(translator, "translator");
    this.codec = Validate.notNull(codec, "codec");
  }

  public void emit(JsonFacetsSpec spec, ParamBag p) {
    if (spec == null || p == null || !spec.enabled()) {
      return;
    }
    if (spec.facets().isEmpty()) {
      return;
    }

    Map<String, Object> root = new LinkedHashMap<>();

    // ---- top-level facets ----
    spec.facets()
        .forEach(
            (name, facet) -> {
              if (Validate.isBlank(name) || facet == null) {
                return;
              }

              Map<String, Object> facetObj = new LinkedHashMap<>();
              facet.accept(this, facetObj);
              root.put(name, facetObj);
            });

    // ---- top-level raw options ----
    if (!spec.rawOptions().isEmpty()) {
      root.putAll(spec.rawOptions());
    }

    try {
      p.add("json.facet", codec.encode(root));
    } catch (Exception e) {
      throw new IllegalArgumentException("Failed serializing json.facet", e);
    }
  }

  // ------------------------------------------------------------------
  // Visitor implementations
  // ------------------------------------------------------------------

  @Override
  public Object visitTerms(JsonTermsFacet f, Map<String, Object> o) {
    o.put("type", f.type().value());
    o.put("field", f.field());

    if (f.limit() != null) {
      o.put("limit", f.limit());
    }
    if (f.offset() != null) {
      o.put("offset", f.offset());
    }
    if (f.sort() != null) {
      o.put("sort", f.sort());
    }
    if (f.minCount() != null) {
      o.put("mincount", f.minCount());
    }
    if (f.missing() != null) {
      o.put("missing", f.missing());
    }
    if (f.numBuckets() != null) {
      o.put("numBuckets", f.numBuckets());
    }
    if (f.allBuckets() != null) {
      o.put("allBuckets", f.allBuckets());
    }

    emitNestedFacets(f.facets(), o);
    emitRawOptions(f.rawOptions(), o);

    return o;
  }

  @Override
  public Object visitRange(JsonRangeFacet f, Map<String, Object> o) {
    o.put("type", f.type().value());
    o.put("field", f.field());
    o.put("start", f.start());
    o.put("end", f.end());
    o.put("gap", f.gap());

    if (f.include() != null) {
      o.put("include", f.include().value());
    }
    if (f.other() != null) {
      o.put("other", f.other().value());
    }
    if (f.hardEnd() != null) {
      o.put("hardend", f.hardEnd());
    }

    emitNestedFacets(f.facets(), o);
    emitRawOptions(f.rawOptions(), o);

    return o;
  }

  @Override
  public Object visitQuery(JsonQueryFacet f, Map<String, Object> o) {
    if (f.rawOptions().isEmpty()) {
      return translator.render(f.query());
    }

    o.put("type", f.type().value());

    String q = translator.render(f.query());
    o.put("q", q);

    emitRawOptions(f.rawOptions(), o);

    return o;
  }

  @Override
  public Object visitStat(JsonStatFacet f, Map<String, Object> o) {
    if (f.rawOptions().isEmpty()) {
      return f.stat();
    }

    o.put("type", f.type().value());
    o.put("stat", f.stat());

    emitRawOptions(f.rawOptions(), o);
    return o;
  }

  @Override
  public Object visitHeatmap(JsonHeatmapFacet f, Map<String, Object> o) {
    o.put("type", f.type().value());
    o.put("field", f.field());
    o.put("geom", f.geom());

    if (f.level() != null) {
      o.put("level", f.level());
    }
    if (f.maxCells() != null) {
      o.put("maxCells", f.maxCells());
    }

    emitNestedFacets(f.facets(), o);
    emitRawOptions(f.rawOptions(), o);

    return o;
  }

  // ------------------------------------------------------------------
  // Helpers
  // ------------------------------------------------------------------

  private void emitNestedFacets(Map<String, JsonFacet> facets, Map<String, Object> o) {
    if (facets == null || facets.isEmpty()) {
      return;
    }

    Map<String, Object> nested = new LinkedHashMap<>();
    facets.forEach(
        (name, facet) -> {
          if (Validate.isBlank(name) || facet == null) {
            return;
          }

          Map<String, Object> facetObj = canInlineFacet(facet) ? new LinkedHashMap<>() : null;
          Object out = facet.accept(this, facetObj);
          nested.put(name, out);
        });

    if (!nested.isEmpty()) {
      o.put("facet", nested);
    }
  }

  private void emitRawOptions(Map<String, Object> raw, Map<String, Object> o) {
    if (raw != null && !raw.isEmpty()) {
      o.putAll(raw);
    }
  }

  private boolean canInlineFacet(JsonFacet facet) {
    return LEAF_FACET_TYPES.contains(facet.type()) && facet.rawOptions().isEmpty();
  }
}

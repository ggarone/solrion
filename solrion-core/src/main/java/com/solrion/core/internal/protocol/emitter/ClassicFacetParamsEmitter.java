package com.solrion.core.internal.protocol.emitter;

import com.solrion.core.api.request.facets.classic.*;
import com.solrion.core.internal.Validate;
import com.solrion.core.internal.protocol.ParamBag;
import com.solrion.core.query.Expr;
import com.solrion.core.query.SolrDialectTranslator;

public final class ClassicFacetParamsEmitter implements ClassicalFacetVisitor<ParamBag, ParamBag> {

  private final SolrDialectTranslator translator;

  public ClassicFacetParamsEmitter(SolrDialectTranslator translator) {
    this.translator = Validate.notNull(translator, "translator");
  }

  public void emit(ClassicFacetsSpec spec, ParamBag p) {
    if (spec == null || !spec.enabled() || p == null) {
      return;
    }
    if (spec.facets().isEmpty()) {
      return;
    }

    p.add("facet", "true");

    if (spec.facetMissing() != null) {
      p.add("facet.missing", spec.facetMissing().toString());
    }
    if (spec.facetSort() != null) {
      p.add("facet.sort", spec.facetSort().value());
    }
    if (spec.facetLimit() != null) {
      p.add("facet.limit", spec.facetLimit().toString());
    }
    if (spec.facetMinCount() != null) {
      p.add("facet.mincount", spec.facetMinCount().toString());
    }

    for (ClassicFacet facet : spec.facets()) {
      facet.accept(this, p);
    }

    spec.rawParams().forEach(p::add);
  }

  private String applyLocalParams(String raw, String name, Expr.LocalParams lp) {
    boolean hasName = (name != null && !name.isBlank());
    if (!hasName && lp == null) {
      return raw;
    } else if (!hasName) {
      return translator.render(lp) + raw;
    }

    lp.params().put("key", name);
    return translator.render(lp) + raw;
  }

  @Override
  public ParamBag visitTerms(TermsFacet f, ParamBag p) {
    String value = applyLocalParams(f.field(), f.name(), f.localParams());
    p.add("facet.field", value);

    String base = "f." + f.field() + ".facet.";
    if (f.limit() != null) {
      p.add(base + "limit", f.limit().toString());
    }
    if (f.minCount() != null) {
      p.add(base + "mincount", f.minCount().toString());
    }
    if (f.sort() != null) {
      p.add(base + "sort", f.sort().value());
    }

    return p;
  }

  @Override
  public ParamBag visitRange(RangeFacet<?> f, ParamBag p) {
    String value = applyLocalParams(f.field(), f.name(), f.localParams());
    p.add("facet.range", value);

    String base = "f." + f.field() + ".facet.range.";
    p.add(base + "gap", f.gap());

    if (f.include() != null) {
      p.add(base + "include", f.include().value());
    }
    if (f.other() != null) {
      p.add(base + "other", f.other().value());
    }

    return p;
  }

  @Override
  public ParamBag visitQuery(QueryFacet f, ParamBag p) {
    String renderedQuery = translator.render(f.query());
    String value = applyLocalParams(renderedQuery, f.name(), f.localParams());

    p.add("facet.query", value);
    return p;
  }

  @Override
  public ParamBag visitPivot(PivotFacet f, ParamBag p) {
    String joinedFields = String.join(",", f.fields());
    String value = applyLocalParams(joinedFields, f.name(), f.localParams());

    p.add("facet.pivot", value);

    String base = "f." + joinedFields + ".facet.";
    if (f.minCount() != null) {
      p.add(base + "mincount", f.minCount().toString());
    }
    if (f.limit() != null) {
      p.add(base + "limit", f.limit().toString());
    }

    return p;
  }

  @Override
  public ParamBag visitInterval(IntervalFacet f, ParamBag p) {
    String value = applyLocalParams(f.field(), f.name(), f.localParams());

    p.add("facet.interval", value);

    String base = "f." + f.field() + ".facet.interval.";
    for (String interval : f.intervals()) {
      p.add(base + "set", interval);
    }

    if (f.minCount() != null) {
      p.add("f." + f.field() + ".facet.mincount", f.minCount().toString());
    }

    return p;
  }

  @Override
  public ParamBag visitHeatmap(HeatmapFacet f, ParamBag p) {
    String value = applyLocalParams(f.field(), f.name(), f.localParams());

    p.add("facet.heatmap", value);

    String base = "f." + f.field() + ".facet.heatmap.";
    p.add(base + "geom", f.geom());

    if (f.level() != null) {
      p.add(base + "level", f.level().toString());
    }
    if (f.maxCells() != null) {
      p.add(base + "maxCells", f.maxCells().toString());
    }

    return p;
  }
}

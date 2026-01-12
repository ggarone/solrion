package com.solrion.core.api.request.facets.json;

import com.solrion.core.api.types.JsonFacetType;
import com.solrion.core.internal.Validate;
import java.util.Map;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Accessors(fluent = true)
public class JsonHeatmapFacet implements JsonFacet {

  String field;
  String geom;
  Integer level;
  Integer maxCells;
  Map<String, JsonFacet> facets;
  Map<String, Object> rawOptions;

  @Builder
  public JsonHeatmapFacet(
      String field,
      String geom,
      Integer level,
      Integer maxCells,
      Map<String, JsonFacet> facets,
      Map<String, Object> rawOptions) {
    this.field = Validate.notBlank(field, "field");
    this.geom = Validate.notBlank(geom, "geom");

    if (level != null) {
      Validate.require(level >= 0, "level must be >= 0");
    }
    if (maxCells != null) {
      Validate.require(maxCells > 0, "maxCells must be > 0");
    }

    this.level = level;
    this.maxCells = maxCells;

    this.facets = facets == null ? Map.of() : Map.copyOf(facets);
    this.rawOptions = rawOptions == null ? Map.of() : Map.copyOf(rawOptions);
  }

  @Override
  public JsonFacetType type() {
    return JsonFacetType.HEATMAP;
  }

  @Override
  public <R, C> R accept(JsonFacetVisitor<R, C> visitor, C ctx) {
    return visitor.visitHeatmap(this, ctx);
  }
}

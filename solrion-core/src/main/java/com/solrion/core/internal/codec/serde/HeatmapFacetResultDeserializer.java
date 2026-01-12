package com.solrion.core.internal.codec.serde;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.solrion.core.api.response.facets.classic.HeatmapFacetResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class HeatmapFacetResultDeserializer extends JsonDeserializer<HeatmapFacetResult> {

  @Override
  public HeatmapFacetResult deserialize(JsonParser p, DeserializationContext ctx)
      throws IOException {

    JsonNode n = p.getCodec().readTree(p);

    return new HeatmapFacetResult(
        n.path("minX").asDouble(),
        n.path("maxX").asDouble(),
        n.path("minY").asDouble(),
        n.path("maxY").asDouble(),
        n.path("rows").asInt(),
        n.path("columns").asInt(),
        parseCounts2D(n.get("counts_ints2D")));
  }

  private List<Integer> parseCounts2D(JsonNode node) {
    if (node == null || !node.isArray() || node.isEmpty()) {
      return List.of();
    }
    List<Integer> counts = new ArrayList<>(node.size());
    for (int i = 0; i < node.size(); i++) {
      counts.add(node.get(i).asInt());
    }
    return counts;
  }
}

package com.solrion.core.api.request.stats.classic;

import java.util.List;
import java.util.Map;

public record StatsSpec(boolean enabled, List<StatsField> fields, Map<String, String> rawParams) {
  public StatsSpec {
    fields = fields == null ? List.of() : List.copyOf(fields);
    rawParams = rawParams == null ? Map.of() : Map.copyOf(rawParams);
  }

  public static StatsSpec disabled() {
    return new StatsSpec(false, List.of(), Map.of());
  }
}

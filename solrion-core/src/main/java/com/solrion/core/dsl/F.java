package com.solrion.core.dsl;

import java.util.Arrays;
import java.util.stream.Collectors;

public final class F {

  private F() {}

  public static String min(String field) {
    return "min(" + field + ")";
  }

  public static String max(String field) {
    return "max(" + field + ")";
  }

  public static String sum(String field) {
    return "sum(" + field + ")";
  }

  public static String avg(String field) {
    return "avg(" + field + ")";
  }

  public static String unique(String field) {
    return "unique(" + field + ")";
  }

  public static String percentile(String field, int... p) {
    return "percentile("
        + field
        + ","
        + Arrays.stream(p).mapToObj(String::valueOf).collect(Collectors.joining(","))
        + ")";
  }
}

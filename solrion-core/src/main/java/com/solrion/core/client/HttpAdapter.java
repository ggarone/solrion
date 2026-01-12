package com.solrion.core.client;

import com.solrion.core.internal.protocol.ParamBag;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

public final class HttpAdapter {

  private HttpAdapter() {}

  public static String toPathSegment(ClientOperation operation) {
    return switch (operation) {
      case SELECT -> "select";
      case ADD, DELETE, COMMIT -> "perBatchUpdate";
    };
  }

  public static String toQueryParams(ParamBag params) {
    if (params == null || params.isEmpty()) {
      return "";
    }

    return params.asMultiMap().entrySet().stream()
        .flatMap(
            e ->
                e.getValue().stream()
                    .map(v -> Map.entry(e.getKey(), v))
                    .map(pair -> encodePair(pair.getKey(), pair.getValue())))
        .collect(Collectors.joining("&"));
  }

  private static String encodePair(String key, String value) {
    return encode(key) + "=" + encode(value);
  }

  private static String encode(String s) {
    return URLEncoder.encode(s, StandardCharsets.UTF_8);
  }
}

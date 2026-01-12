package com.solrion.vertx.mutiny.client;

import com.solrion.core.internal.protocol.ParamBag;
import io.vertx.mutiny.core.MultiMap;

public final class ParamBagAdapter {

  private ParamBagAdapter() {}

  public static MultiMap toMultiMap(ParamBag params) {
    MultiMap out = MultiMap.caseInsensitiveMultiMap();
    for (var e : params.asMultiMap().entrySet()) {
      out.add(e.getKey(), e.getValue());
    }
    return out;
  }
}

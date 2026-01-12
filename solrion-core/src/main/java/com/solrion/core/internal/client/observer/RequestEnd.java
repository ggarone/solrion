package com.solrion.core.internal.client.observer;

import java.time.Duration;

public record RequestEnd(RequestStart request, Duration latency, Throwable error) {
  public static RequestEnd success(RequestStart request, Duration latency) {
    return new RequestEnd(request, latency, null);
  }

  public static RequestEnd failure(RequestStart request, Duration latency, Throwable cause) {
    return new RequestEnd(request, latency, cause);
  }
}

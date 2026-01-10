package com.solrion.core.client;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;

import java.time.Duration;

@Accessors(fluent = true)
@Value
@Builder
public class HttpTransportOptions {

    @Builder.Default
    Duration readTimeout = Duration.ofSeconds(30);

    @Builder.Default
    Duration connectTimeout = Duration.ofSeconds(30);

    public static HttpTransportOptions defaults() {
        return builder().build();
    }
}

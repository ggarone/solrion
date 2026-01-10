package com.solrion.vertx.mutiny.api.request;

public record BatchingOptions(
        Integer batchSize,
        Boolean ordered,
        Integer maxInFlight,
        Boolean failFast
) {
    public BatchingOptions {
        batchSize = batchSize == null ? 100 : batchSize;
        ordered = ordered == null || ordered;
        maxInFlight = maxInFlight == null ? 1 : maxInFlight;
        failFast = failFast == null || failFast;
    }

    public static BatchingOptions defaults() {
        return new BatchingOptions(null, null, null, null);
    }
}

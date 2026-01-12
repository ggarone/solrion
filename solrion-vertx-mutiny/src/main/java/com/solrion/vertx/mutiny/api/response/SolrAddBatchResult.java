package com.solrion.vertx.mutiny.api.response;

public record SolrAddBatchResult(int batchIndex, int sent, int failed) {
  public static SolrAddBatchResult success(int batchIndex, int sent) {
    return new SolrAddBatchResult(batchIndex, sent, 0);
  }

  public static SolrAddBatchResult failure(int batchIndex, int failed) {
    return new SolrAddBatchResult(batchIndex, 0, failed);
  }
}

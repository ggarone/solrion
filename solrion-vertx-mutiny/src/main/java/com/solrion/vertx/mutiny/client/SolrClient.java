package com.solrion.vertx.mutiny.client;

import com.solrion.core.api.request.SelectRequest;
import com.solrion.core.api.request.update.*;
import com.solrion.core.api.response.SolrSelectResponse;
import com.solrion.core.api.response.SolrUpdateResponse;
import com.solrion.core.api.types.SolrDocument;
import com.solrion.core.client.HttpTransportOptions;
import com.solrion.core.client.SolrClientOptions;
import com.solrion.core.internal.client.SolrClientImpl;
import com.solrion.core.internal.transport.SolrHttpTransport;
import com.solrion.vertx.mutiny.api.request.AddDocumentsBatchRequest;
import com.solrion.vertx.mutiny.api.request.BatchingOptions;
import com.solrion.vertx.mutiny.api.response.SolrAddBatchResult;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.ext.web.client.WebClient;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import lombok.Builder;

public final class SolrClient {

  private final com.solrion.core.client.SolrClient delegate;

  @Builder
  public SolrClient(
      WebClient delegate, HttpTransportOptions transportOptions, SolrClientOptions clientOptions) {
    SolrHttpTransport transport = new SolrVertxTransport(delegate, transportOptions);
    this.delegate = new SolrClientImpl(transport, clientOptions);
  }

  public Uni<SolrSelectResponse> select(SelectRequest request) {
    return deferred(() -> delegate.select(request));
  }

  public Uni<SolrUpdateResponse> add(AddDocumentsRequest request) {
    return deferred(() -> delegate.add(request));
  }

  public Uni<SolrUpdateResponse> deleteById(DeleteByIdRequest request) {
    return deferred(() -> delegate.deleteById(request));
  }

  public Uni<SolrUpdateResponse> deleteByQuery(DeleteByQueryRequest request) {
    return deferred(() -> delegate.deleteByQuery(request));
  }

  public Uni<SolrUpdateResponse> commit(CommitRequest request) {
    return deferred(() -> delegate.commit(request));
  }

  public Multi<SolrAddBatchResult> addBatched(AddDocumentsBatchRequest request) {
    BatchingOptions batching = request.batching();

    AtomicInteger batchIndex = new AtomicInteger(0);

    Multi<SolrAddBatchResult> batchMulti =
        request
            .docSource()
            .group()
            .intoLists()
            .of(batching.batchSize())
            .select()
            .where(list -> list != null && !list.isEmpty())
            .onItem()
            .transformToUni(batch -> sendAddBatch(request, batch, batchIndex.getAndIncrement()))
            .merge(batching.maxInFlight());

    if (request.atEndUpdate() == null || !request.atEndUpdate().hasCommit()) {
      return batchMulti;
    }

    return batchMulti.onCompletion().call(() -> commitAtEnd(request));
  }

  private Uni<SolrAddBatchResult> sendAddBatch(
      AddDocumentsBatchRequest request, List<SolrDocument> batch, int batchIndex) {
    Uni<SolrAddBatchResult> batchUni =
        add(new AddDocumentsRequest(
                request.collection(), request.transport(), batch, request.perBatchUpdate()))
            .map(res -> SolrAddBatchResult.success(batchIndex, batch.size()));

    if (request.batching().failFast()) {
      return batchUni;
    }

    return batchUni
        .onFailure()
        .recoverWithItem(SolrAddBatchResult.failure(batchIndex, batch.size()));
  }

  private Uni<SolrUpdateResponse> commitAtEnd(AddDocumentsBatchRequest request) {
    return commit(
        new CommitRequest(request.collection(), request.transport(), request.atEndUpdate()));
  }

  private <T> Uni<T> deferred(Supplier<CompletionStage<T>> supplier) {
    return Uni.createFrom().completionStage(supplier);
  }
}

package com.solrion.core.client;

import com.solrion.core.api.request.SelectRequest;
import com.solrion.core.api.request.update.AddDocumentsRequest;
import com.solrion.core.api.request.update.CommitRequest;
import com.solrion.core.api.request.update.DeleteByIdRequest;
import com.solrion.core.api.request.update.DeleteByQueryRequest;
import com.solrion.core.api.response.SolrSelectResponse;
import com.solrion.core.api.response.SolrUpdateResponse;

import java.util.concurrent.CompletionStage;

/**
 * High-level Solr client.
 *
 * - request building: use solr.dsl.SolrDsl
 * - param mapping: internal (protocol layer)
 * - transport: injected SolrTransport
 */
public interface SolrClient {

    CompletionStage<SolrSelectResponse> select(SelectRequest request);

    CompletionStage<SolrUpdateResponse> add(AddDocumentsRequest request);

    CompletionStage<SolrUpdateResponse> deleteById(DeleteByIdRequest request);

    CompletionStage<SolrUpdateResponse> deleteByQuery(DeleteByQueryRequest request);

    CompletionStage<SolrUpdateResponse> commit(CommitRequest request);
}

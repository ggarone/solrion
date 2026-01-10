package com.solrion.vertx.mutiny.api.request;

import io.smallrye.mutiny.Multi;
import com.solrion.core.api.request.SolrRequest;
import com.solrion.core.api.request.update.SolrDocument;
import com.solrion.core.api.request.update.UpdateOptions;
import com.solrion.core.client.HttpTransportOptions;
import com.solrion.core.internal.Validate;

public record AddDocumentsBatchRequest(
        String collection,
        HttpTransportOptions transport,
        Multi<SolrDocument> docSource,
        UpdateOptions perBatchUpdate,
        UpdateOptions atEndUpdate,
        BatchingOptions batching
) implements SolrRequest {

    public AddDocumentsBatchRequest {
        Validate.notNull(docSource, "docSource");
        transport = transport == null ? HttpTransportOptions.defaults() : transport;
        perBatchUpdate = perBatchUpdate == null ? UpdateOptions.defaults() : perBatchUpdate;
        atEndUpdate = atEndUpdate == null ? UpdateOptions.defaults() : atEndUpdate;
        batching = batching == null ? BatchingOptions.defaults() : batching;
    }
}

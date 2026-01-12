package com.solrion.core.api.request.update;

import com.solrion.core.api.request.SolrRequest;
import com.solrion.core.client.HttpTransportOptions;

public record CommitRequest(String collection, HttpTransportOptions transport, UpdateOptions update)
    implements SolrRequest {}

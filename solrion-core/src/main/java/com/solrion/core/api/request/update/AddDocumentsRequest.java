package com.solrion.core.api.request.update;

import com.solrion.core.api.request.SolrRequest;
import com.solrion.core.api.types.SolrDocument;
import com.solrion.core.client.HttpTransportOptions;
import com.solrion.core.internal.Validate;
import java.util.List;

public record AddDocumentsRequest(
    String collection,
    HttpTransportOptions transport,
    List<SolrDocument> documents,
    UpdateOptions options)
    implements SolrRequest {

  public AddDocumentsRequest {
    documents = Validate.notEmpty(documents, "docSource");
    documents = List.copyOf(documents);
    options = options == null ? UpdateOptions.defaults() : options;
  }
}

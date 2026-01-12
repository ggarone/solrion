package com.solrion.core.api.request;

import com.solrion.core.client.HttpTransportOptions;

public interface SolrRequest {
  String collection();

  HttpTransportOptions transport();
}

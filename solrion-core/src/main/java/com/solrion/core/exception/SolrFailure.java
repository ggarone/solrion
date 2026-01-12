package com.solrion.core.exception;

public interface SolrFailure {

  Type type();

  /// Indicates whether retry MAY be safe.
  /// Policy still belongs to the caller.
  boolean retryable();

  enum Type {
    TRANSPORT, // network, timeout
    HTTP_CLIENT_ERROR,
    HTTP_SERVER_ERROR,
    SOLR_ERROR, // error block in result
    CODEC,
    CLIENT_MISUSE // invalid usage
  }
}

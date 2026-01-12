package com.solrion.core.exception;

public class SolrDocumentMappingException extends SolrException {

  public SolrDocumentMappingException(Throwable cause) {
    super("Solr document mapping error occurred", cause);
  }

  @Override
  public Type type() {
    return Type.CODEC;
  }

  @Override
  public boolean retryable() {
    return false;
  }
}

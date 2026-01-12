package com.solrion.core.exception;

public final class SolrCodecException extends SolrException {

  public SolrCodecException(String message) {
    super(message);
  }

  public SolrCodecException(String message, Throwable cause) {
    super(message, cause);
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

package com.solrion.core.exception;

public final class SolrTransportException extends SolrException {
  private final int statusCode;
  private final String body;

  public SolrTransportException(int statusCode, String body) {
    super("Solr HTTP error: " + statusCode);
    this.statusCode = statusCode;
    this.body = body;
  }

  public SolrTransportException(int statusCode, String body, Throwable cause) {
    super("Solr HTTP error: " + statusCode, cause);
    this.statusCode = statusCode;
    this.body = body;
  }

  public SolrTransportException(String message, Throwable cause) {
    super("Solr Transport error: " + message, cause);
    this.statusCode = -1;
    this.body = null;
  }

  public int statusCode() {
    return statusCode;
  }

  public String body() {
    return body;
  }

  @Override
  public Type type() {
    if (statusCode < 0) {
      return Type.TRANSPORT;
    }
    if (statusCode >= 500) {
      return Type.HTTP_SERVER_ERROR;
    }
    return Type.HTTP_CLIENT_ERROR;
  }

  @Override
  public boolean retryable() {
    return !Type.HTTP_CLIENT_ERROR.equals(type());
  }
}

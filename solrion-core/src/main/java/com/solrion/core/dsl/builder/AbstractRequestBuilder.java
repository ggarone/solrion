package com.solrion.core.dsl.builder;

import com.solrion.core.client.HttpTransportOptions;

public abstract class AbstractRequestBuilder<T extends AbstractRequestBuilder<T>> {
  protected HttpTransportOptions transportOptions = HttpTransportOptions.defaults();
  protected String collection;

  @SuppressWarnings("unchecked")
  public T transport(HttpTransportOptions transportOptions) {
    this.transportOptions = transportOptions;
    return (T) this;
  }

  @SuppressWarnings("unchecked")
  public T collection(String collection) {
    this.collection = collection;
    return (T) this;
  }
}

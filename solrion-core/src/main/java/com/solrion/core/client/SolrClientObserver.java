package com.solrion.core.client;

import com.solrion.core.internal.client.observer.RequestEnd;
import com.solrion.core.internal.client.observer.RequestStart;

public interface SolrClientObserver {

  void onRequestStart(RequestStart ctx);

  void onRequestSuccess(RequestEnd ctx);

  void onRequestFailure(RequestEnd ctx);

  static SolrClientObserver noop() {
    return new SolrClientObserver() {
      @Override
      public void onRequestStart(RequestStart ctx) {}

      @Override
      public void onRequestSuccess(RequestEnd ctx) {}

      @Override
      public void onRequestFailure(RequestEnd ctx) {}
    };
  }
}

package com.solrion.logging.slf4j;

import com.solrion.core.client.HttpAdapter;
import com.solrion.core.client.SolrClientObserver;
import com.solrion.core.internal.client.observer.RequestEnd;
import com.solrion.core.internal.client.observer.RequestStart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SolrLoggingObserver implements SolrClientObserver {
  private static final Logger LOGGER = LoggerFactory.getLogger("solr.client");
  private static final String REQUEST_FORMAT =
      "{} [latency: {}ms] | Solr {} [{}] {}?{} | attributes: {}";

  @Override
  public void onRequestStart(RequestStart ctx) {}

  @Override
  public void onRequestSuccess(RequestEnd ctx) {
    if (isDebugEnabled()) {
      logRequest("SUCCESS", ctx);
    }
  }

  @Override
  public void onRequestFailure(RequestEnd ctx) {
    if (isDebugEnabled()) {
      logRequest("FAILURE", ctx);
    }
  }

  private boolean isDebugEnabled() {
    return LOGGER.isDebugEnabled();
  }

  private void logRequest(String outcome, RequestEnd ctx) {
    LOGGER.debug(
        REQUEST_FORMAT,
        outcome,
        ctx.latency().toMillis(),
        ctx.request().operation(),
        ctx.request().httpMethod(),
        ctx.request().httpPath(),
        HttpAdapter.toQueryParams(ctx.request().params()),
        ctx.request().attributes());
  }
}

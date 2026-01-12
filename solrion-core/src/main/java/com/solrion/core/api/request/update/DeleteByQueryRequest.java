package com.solrion.core.api.request.update;

import com.solrion.core.api.request.SolrRequest;
import com.solrion.core.client.HttpTransportOptions;
import com.solrion.core.internal.Validate;
import com.solrion.core.query.Expr;

/** Destructive operation: for safety, queries must NOT be empty. */
public record DeleteByQueryRequest(
    String collection, HttpTransportOptions transport, Expr.BoolExpr query, UpdateOptions update)
    implements SolrRequest {

  public DeleteByQueryRequest {
    Validate.notNull(query, "queries");
    Validate.require(!query.isEmpty(), "deleteByQuery queries cannot be empty");
    update = update == null ? UpdateOptions.defaults() : update;
  }
}

package com.solrion.core.api.response;

public interface SolrResponse {
  SolrResponseHeader header();

  SolrError error();

  default boolean hasError() {
    return error() != null;
  }

  default boolean isSuccess() {
    return header() != null && header().status() != null && header().status() == 0;
  }
}

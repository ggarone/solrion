package com.solrion.core.api.request;

public enum ResponseFormat {
  JSON("json"),
  XML("xml");

  private final String wt;

  ResponseFormat(String wt) {
    this.wt = wt;
  }

  public String wt() {
    return wt;
  }
}

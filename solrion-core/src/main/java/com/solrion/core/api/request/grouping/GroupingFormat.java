package com.solrion.core.api.request.grouping;

public enum GroupingFormat {
  GROUPED("grouped"),
  SIMPLE("simple");

  private final String value;

  GroupingFormat(String value) {
    this.value = value;
  }

  public String value() {
    return value;
  }
}

package com.solrion.core.api.response.grouping;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.solrion.core.api.response.DocsResult;
import lombok.Value;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Value
public class GroupEntry {

  Object value;
  DocsResult docsResult;

  @JsonCreator
  public GroupEntry(
      @JsonProperty("groupValue") Object value, @JsonProperty("doclist") DocsResult docsResult) {
    this.value = value;
    this.docsResult = docsResult;
  }
}

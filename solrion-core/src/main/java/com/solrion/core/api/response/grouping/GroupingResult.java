package com.solrion.core.api.response.grouping;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Map;
import lombok.Value;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Value
public class GroupingResult {

  Map<String, GroupResult> groups;

  @JsonCreator
  public GroupingResult(Map<String, GroupResult> groups) {
    this.groups = groups;
  }
}

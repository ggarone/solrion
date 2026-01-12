package com.solrion.core.api.response.facets.classic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.solrion.core.internal.codec.serde.BucketMapDeserializer;
import java.util.Map;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Accessors(fluent = true)
public class TermsFacetResult {

  Map<String, Long> buckets;

  @JsonCreator
  public TermsFacetResult(
      @JsonDeserialize(using = BucketMapDeserializer.class) Map<String, Long> buckets) {
    this.buckets = buckets;
  }
}

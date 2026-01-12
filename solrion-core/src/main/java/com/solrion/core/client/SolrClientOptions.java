package com.solrion.core.client;

import com.solrion.core.codec.SolrCodec;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Value
@Builder
public class SolrClientOptions {

  String defaultCollection;

  @Builder.Default SolrCodec codec = SolrCodec.json();

  @Builder.Default String basePath = "/solr";

  @Builder.Default boolean failOnSolrError = false;

  @Builder.Default boolean alwaysPostFormForSelect = false;

  @Builder.Default boolean selectPostFormOnThresholdExceeded = false;

  @Builder.Default int selectPostFormThresholdBytes = 2000;

  public static SolrClientOptions defaultOptions() {
    return SolrClientOptions.builder().build();
  }
}

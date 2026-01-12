package com.solrion.core.api.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.solrion.core.api.types.SolrDocument;
import com.solrion.core.internal.codec.serde.SolrDocumentDeserializer;
import java.util.List;
import java.util.Optional;
import lombok.Value;
import lombok.experimental.Accessors;

/** Standard result container for /select. */
@Accessors(fluent = true)
@Value
public class DocsResult {

  Long numFound;
  Long start;
  Double maxScore;
  List<SolrDocument> docs;

  @JsonCreator
  public DocsResult(
      @JsonProperty("numFound") Long numFound,
      @JsonProperty("start") Long start,
      @JsonProperty("maxScore") Double maxScore,
      @JsonDeserialize(contentUsing = SolrDocumentDeserializer.class) @JsonProperty("docs")
          List<SolrDocument> docs) {
    this.numFound = numFound;
    this.start = start;
    this.maxScore = maxScore;
    this.docs = docs;
  }

  public Optional<SolrDocument> first() {
    return docs == null || docs.isEmpty() ? Optional.empty() : Optional.of(docs.get(0));
  }

  public Optional<SolrDocument> last() {
    return docs == null || docs.isEmpty()
        ? Optional.empty()
        : Optional.of(docs.get(docs.size() - 1));
  }
}

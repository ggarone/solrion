package com.solrion.core.api.types;

import com.solrion.core.internal.Validate;
import java.util.Map;

/** Generic Solr document representation. */
public record SolrDocument(Map<String, Object> fields) {
  public SolrDocument {
    fields = Validate.notEmpty(fields, "fields");
    fields = Map.copyOf(fields);
  }

  public static SolrDocument of(Map<String, Object> fields) {
    return new SolrDocument(fields);
  }
}

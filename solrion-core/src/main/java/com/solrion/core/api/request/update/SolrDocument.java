package com.solrion.core.api.request.update;

import com.solrion.core.internal.Validate;

import java.util.Map;

/**
 * Generic Solr document representation.
 */
public record SolrDocument(Map<String, Object> fields) {
    public SolrDocument {
        fields = Validate.notEmpty(fields, "fields");
        fields = Map.copyOf(fields);
    }
}

package com.solrion.core.exception;

import com.solrion.core.api.response.SolrError;

public class SolrErrorException extends SolrException {

    public SolrErrorException(SolrError error) {
        super("Got solr response error set: " + error);
    }

    @Override
    public Type type() {
        return Type.SOLR_ERROR;
    }

    @Override
    public boolean retryable() {
        return false;
    }
}
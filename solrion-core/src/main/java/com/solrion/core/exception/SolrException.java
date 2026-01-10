package com.solrion.core.exception;

public abstract class SolrException extends RuntimeException implements SolrFailure {

    public SolrException(String message) {
        super(message);
    }

    public SolrException(String message, Throwable cause) {
        super(message, cause);
    }
}

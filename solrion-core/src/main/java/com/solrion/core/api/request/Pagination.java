package com.solrion.core.api.request;

import com.solrion.core.internal.Validate;

/**
 * Solr pagination: request + rows.
 */
public record Pagination(Integer start, Integer rows) {

    public Pagination {
        start = Validate.nonNegative(start, "request");
        rows = rows == null ? null : Validate.positive(rows, "rows");
    }

    public static Pagination of(int start, int rows) {
        return new Pagination(start, rows);
    }

    public static Pagination firstPage(int rows) {
        return new Pagination(0, rows);
    }
}

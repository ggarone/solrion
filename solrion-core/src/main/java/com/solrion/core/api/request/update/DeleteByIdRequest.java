package com.solrion.core.api.request.update;

import com.solrion.core.api.request.SolrRequest;
import com.solrion.core.client.HttpTransportOptions;
import com.solrion.core.internal.Validate;

import java.util.List;

public record DeleteByIdRequest(
        String collection,
        HttpTransportOptions transport,
        List<String> ids,
        UpdateOptions update
) implements SolrRequest {

    public DeleteByIdRequest {
        ids = Validate.notEmpty(ids, "ids");
        ids = List.copyOf(ids);
        update = update == null ? UpdateOptions.defaults() : update;
    }
}

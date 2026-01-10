package com.solrion.core.internal.client.observer;

import com.solrion.core.api.request.SolrRequest;
import com.solrion.core.client.ClientOperation;
import com.solrion.core.internal.protocol.ParamBag;

import java.util.HashMap;
import java.util.Map;

public record RequestStart(
        ClientOperation operation,
        String collection,
        String httpMethod,
        String httpPath,
        ParamBag params,
        SolrRequest request,
        Map<String, String> attributes
) {

    public RequestStart {
        attributes = attributes !=  null ? attributes : new HashMap<>();
    }

    public static RequestStart select(String collection, String httpMethod, String httpPath, ParamBag params, SolrRequest request) {
        return new RequestStart(ClientOperation.SELECT, collection, httpMethod, httpPath, params, request, new HashMap<>());
    }

    public static RequestStart add(String collection, String httpPath, ParamBag params, SolrRequest request) {
        return new RequestStart(ClientOperation.ADD, collection, "POST", httpPath, params, request, new HashMap<>());
    }

    public static RequestStart delete(String collection, String httpPath, ParamBag params, SolrRequest request) {
        return new RequestStart(ClientOperation.DELETE, collection, "POST", httpPath, params, request, new HashMap<>());
    }

    public static RequestStart commit(String collection, String httpPath, ParamBag params, SolrRequest request) {
        return new RequestStart(ClientOperation.COMMIT, collection, "POST", httpPath, params, request, new HashMap<>());
    }
}
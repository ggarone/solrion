package com.solrion.core.internal.client;

import com.solrion.core.api.request.SelectRequest;
import com.solrion.core.api.request.update.AddDocumentsRequest;
import com.solrion.core.api.request.update.CommitRequest;
import com.solrion.core.api.request.update.DeleteByIdRequest;
import com.solrion.core.api.request.update.DeleteByQueryRequest;
import com.solrion.core.api.response.SolrResponse;
import com.solrion.core.api.response.SolrSelectResponse;
import com.solrion.core.api.response.SolrUpdateResponse;
import com.solrion.core.client.*;
import com.solrion.core.internal.client.observer.ClientObservers;
import com.solrion.core.internal.client.observer.RequestEnd;
import com.solrion.core.internal.client.observer.RequestStart;
import com.solrion.core.client.SolrClientObserver;
import com.solrion.core.internal.transport.SolrHttpTransport;
import com.solrion.core.codec.SolrCodec;
import com.solrion.core.internal.exception.SolrCodecException;
import com.solrion.core.exception.SolrErrorException;
import com.solrion.core.internal.protocol.ParamBag;
import com.solrion.core.internal.protocol.SolrParamsMapper;
import com.solrion.core.internal.protocol.envelope.UpdateEnvelopeMapper;
import com.solrion.core.query.SolrDialectTranslator;
import com.solrion.core.internal.Validate;

import java.time.Duration;
import java.util.concurrent.CompletionStage;

public final class SolrClientImpl implements SolrClient {

    private final SolrHttpTransport transport;
    private final SolrClientObserver observer;
    private final SolrCodec codec;
    private final SolrParamsMapper paramsMapper;
    private final UpdateEnvelopeMapper envelopeMapper;
    private final SolrClientOptions clientOptions;

    public SolrClientImpl(SolrHttpTransport transport, SolrClientOptions options) {
        this.transport = Validate.notNull(transport, "transport");
        this.observer = ClientObservers.get();
        this.clientOptions = Validate.notNull(options, "perBatchUpdate");
        this.codec = Validate.notNull(options.codec(), "codec");
        this.paramsMapper = new SolrParamsMapper(SolrDialectTranslator.INSTANCE, codec);
        this.envelopeMapper = new UpdateEnvelopeMapper(SolrDialectTranslator.INSTANCE);
    }

    @Override
    public CompletionStage<SolrSelectResponse> select(SelectRequest request) {
        long startNanos = System.nanoTime();

        Validate.notNull(request, "request");
        String collection = chooseCollection(request.collection());
        String path = buildHttpPath(collection, ClientOperation.SELECT);
        ParamBag params = paramsMapper.toSelectParams(request);

        boolean usePostForm = clientOptions.alwaysPostFormForSelect()
                || (params.estimatedBytes() > clientOptions.selectPostFormThresholdBytes());

        var ctx = RequestStart.select(collection, usePostForm ? "POST" : "GET", path, params, request);
        observer.onRequestStart(ctx);

        CompletionStage<String> sent = usePostForm ?
                transport.postForm(path, params) : transport.get(path, params);

        return sent
                .thenApply(this::decodeSelect)
                .thenApply(this::failOnErrorIfNeeded)
                .whenComplete((ignore, e) -> notifyCompletion(ctx, e, startNanos));
    }

    @Override
    public CompletionStage<SolrUpdateResponse> add(AddDocumentsRequest request) {
        long startNanos = System.nanoTime();

        Validate.notNull(request, "request");
        String collection = chooseCollection(request.collection());
        String path = buildHttpPath(collection, ClientOperation.ADD);
        ParamBag params = paramsMapper.toUpdateOptions(request.options());
        Object body = envelopeMapper.toAddEnvelope(request);

        var ctx = RequestStart.add(collection, path, params, request);
        observer.onRequestStart(ctx);

        return transport.postJson(path, params, body)
                .thenApply(this::decodeUpdate)
                .thenApply(this::failOnErrorIfNeeded)
                .whenComplete((ignore, e) -> notifyCompletion(ctx, e, startNanos));
    }

    @Override
    public CompletionStage<SolrUpdateResponse> deleteById(DeleteByIdRequest request) {
        long startNanos = System.nanoTime();

        Validate.notNull(request, "request");
        String collection = chooseCollection(request.collection());
        String path = buildHttpPath(collection, ClientOperation.DELETE);
        ParamBag params = paramsMapper.toUpdateOptions(request.update());
        Object body = envelopeMapper.toDeleteByIdEnvelope(request);

        var ctx = RequestStart.delete(collection, path, params, request);
        observer.onRequestStart(ctx);

        return transport.postJson(path, params, body)
                .thenApply(this::decodeUpdate)
                .thenApply(this::failOnErrorIfNeeded)
                .whenComplete((ignore, e) -> notifyCompletion(ctx, e, startNanos));
    }

    @Override
    public CompletionStage<SolrUpdateResponse> deleteByQuery(DeleteByQueryRequest request) {
        long startNanos = System.nanoTime();

        Validate.notNull(request, "request");
        String collection = chooseCollection(request.collection());
        String path = buildHttpPath(collection, ClientOperation.DELETE);
        ParamBag params = paramsMapper.toUpdateOptions(request.update());
        Object body = envelopeMapper.toDeleteByQueryEnvelope(request);

        var ctx = RequestStart.delete(collection, path, params, request);
        observer.onRequestStart(ctx);

        return transport.postJson(path, params, body)
                .thenApply(this::decodeUpdate)
                .thenApply(this::failOnErrorIfNeeded)
                .whenComplete((ignore, e) -> notifyCompletion(ctx, e, startNanos));
    }

    @Override
    public CompletionStage<SolrUpdateResponse> commit(CommitRequest request) {
        long startNanos = System.nanoTime();

        Validate.notNull(request, "request");
        String collection = chooseCollection(request.collection());
        String path = buildHttpPath(collection, ClientOperation.COMMIT);
        ParamBag params = paramsMapper.toUpdateOptions(request.update());

        var ctx = RequestStart.commit(collection, path, params, request);
        observer.onRequestStart(ctx);

        return transport.postNoBody(path, params)
                .thenApply(this::decodeUpdate)
                .thenApply(this::failOnErrorIfNeeded)
                .whenComplete((ignore, e) -> notifyCompletion(ctx, e, startNanos));
    }

    private String chooseCollection(String requested) {
        if (!Validate.isBlank(requested)) return requested;
        String collection = clientOptions.defaultCollection();
        if (!Validate.isBlank(collection)) return collection;
        throw new IllegalArgumentException("No collection provided and no defaultCollection configured");
    }

    private String buildHttpPath(String collection, ClientOperation op) {
        return String.format("%s/%s/%s",
                clientOptions.basePath(), collection, HttpAdapter.toPathSegment(op));
    }

    private SolrUpdateResponse decodeUpdate(String body) {
        try {
            return codec.decode(body, SolrUpdateResponse.class);
        } catch (Exception e) {
            throw new SolrCodecException("Failed decoding Solr perBatchUpdate response", e.getCause());
        }
    }

    private SolrSelectResponse decodeSelect(String body) {
        try {
            return codec.decode(body, SolrSelectResponse.class);
        } catch (Exception e) {
            throw new SolrCodecException("Failed decoding Solr select response", e.getCause());
        }
    }

    private <T extends SolrResponse> T failOnErrorIfNeeded(T response) {
        if(response.hasError() && clientOptions.failOnSolrError()) {
            throw new SolrErrorException(response.error());
        }
        return response;
    }

    private void notifyCompletion(RequestStart ctx, Throwable error, long startNanos) {
        if(error == null) {
            observer.onRequestSuccess(RequestEnd.success(ctx, latency(startNanos)));
        } else {
            observer.onRequestFailure(RequestEnd.failure(ctx, latency(startNanos), CompletionStages.unwrap(error)));
        }
    }

    private Duration latency(long startNanos) {
        return Duration.ofNanos(System.nanoTime() - startNanos);
    }
}

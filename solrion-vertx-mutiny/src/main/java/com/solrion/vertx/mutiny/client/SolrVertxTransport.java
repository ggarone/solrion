package com.solrion.vertx.mutiny.client;

import com.solrion.core.client.HttpTransportOptions;
import com.solrion.core.exception.SolrTransportException;
import com.solrion.core.internal.Validate;
import com.solrion.core.internal.protocol.ParamBag;
import com.solrion.core.internal.transport.SolrHttpTransport;
import io.vertx.mutiny.core.buffer.Buffer;
import io.vertx.mutiny.ext.web.client.HttpRequest;
import io.vertx.mutiny.ext.web.client.HttpResponse;
import io.vertx.mutiny.ext.web.client.WebClient;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import java.util.concurrent.CompletionStage;

public class SolrVertxTransport implements SolrHttpTransport {

  private final WebClient delegate;
  private final HttpTransportOptions options;

  public SolrVertxTransport(WebClient delegate, HttpTransportOptions options) {
    this.delegate = Validate.notNull(delegate, "web client delegate");
    this.options = options != null ? options : HttpTransportOptions.defaults();
  }

  @Override
  public CompletionStage<String> get(String path, ParamBag params) {

    HttpRequest<Buffer> req =
        delegate
            .get(path)
            .putHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .timeout(options.readTimeout().toMillis());

    req.queryParams().addAll(ParamBagAdapter.toMultiMap(params));

    return req.send().map(this::handleTransportResponse).subscribeAsCompletionStage();
  }

  @Override
  public CompletionStage<String> postJson(String path, ParamBag params, Object jsonBody) {
    HttpRequest<Buffer> req =
        delegate
            .post(path)
            .putHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .putHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .timeout(options.readTimeout().toMillis());

    req.queryParams().addAll(ParamBagAdapter.toMultiMap(params));

    return req.sendJson(jsonBody).map(this::handleTransportResponse).subscribeAsCompletionStage();
  }

  @Override
  public CompletionStage<String> postNoBody(String path, ParamBag params) {
    HttpRequest<Buffer> req =
        delegate
            .post(path)
            .putHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .timeout(options.readTimeout().toMillis());

    req.queryParams().addAll(ParamBagAdapter.toMultiMap(params));

    return req.send().map(this::handleTransportResponse).subscribeAsCompletionStage();
  }

  @Override
  public CompletionStage<String> postForm(String path, ParamBag params) {
    HttpRequest<Buffer> req =
        delegate
            .post(path)
            .putHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .putHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED)
            .timeout(options.readTimeout().toMillis());

    return req.sendForm(ParamBagAdapter.toMultiMap(params))
        .map(this::handleTransportResponse)
        .subscribeAsCompletionStage();
  }

  private String handleTransportResponse(HttpResponse<Buffer> response) {
    if (response.statusCode() < 200 || response.statusCode() >= 300) {
      throw new SolrTransportException(response.statusCode(), response.bodyAsString());
    }
    return response.bodyAsString();
  }
}

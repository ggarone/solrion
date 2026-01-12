package com.solrion.core.internal.transport;

import com.solrion.core.internal.protocol.ParamBag;
import java.util.concurrent.CompletionStage;

/**
 * Abstraction over HTTP client.
 *
 * <p>This library avoids binding to a specific client (Quarkus REST Client, Vert.x WebClient,
 * Apache HC, ...).
 */
public interface SolrHttpTransport {

  CompletionStage<String> get(String path, ParamBag params);

  CompletionStage<String> postJson(String path, ParamBag params, Object jsonBody);

  CompletionStage<String> postNoBody(String path, ParamBag params);

  CompletionStage<String> postForm(String path, ParamBag params);
}

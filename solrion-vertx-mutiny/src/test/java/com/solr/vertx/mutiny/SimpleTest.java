package com.solr.vertx.mutiny;

import static com.solrion.core.dsl.F.*;
import static com.solrion.core.dsl.Q.*;

import com.solrion.core.api.request.SelectRequest;
import com.solrion.core.api.request.facets.json.JsonStatFacet;
import com.solrion.core.api.request.facets.json.JsonTermsFacet;
import com.solrion.core.api.request.grouping.GroupByField;
import com.solrion.core.api.response.SolrSelectResponse;
import com.solrion.core.client.HttpTransportOptions;
import com.solrion.core.client.SolrClientOptions;
import com.solrion.core.dsl.RequestBuilders;
import com.solrion.core.mapper.SolrDocumentMapper;
import com.solrion.vertx.mutiny.client.SolrClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.client.WebClient;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class SimpleTest {

  private static final SolrDocumentMapper DOCUMENT_MAPPER = SolrDocumentMapper.json();

  private record Ksor(String ksor) {}

  // in Quarkus project dep injected using Quarkus ARC (implements jakarta CDI lite)
  private static final WebClient DELEGATE =
      WebClient.create(
          Vertx.vertx(),
          new WebClientOptions().setDefaultHost("solr.alpitour.it").setDefaultPort(80));

  private static final SolrClient CLIENT =
      SolrClient.builder()
          .delegate(DELEGATE)
          .clientOptions(SolrClientOptions.builder().failOnSolrError(true).build())
          .transportOptions(
              HttpTransportOptions.builder()
                  .readTimeout(Duration.ofSeconds(30))
                  .connectTimeout(Duration.ofSeconds(10))
                  .build())
          .build();

  public static void main(String[] args) {
    LocalDateTime from = LocalDateTime.now().minus(Duration.ofDays(30));
    LocalDateTime to = LocalDateTime.now().plus(Duration.ofDays(30));

    SelectRequest complex =
        RequestBuilders.select("productMainstream_B")
            .query(field("validFrom").between(from, to))
            .jsonFacets()
            .enabled(true)
            .facet(
                "example",
                JsonTermsFacet.builder()
                    .field("ksor")
                    .limit(100)
                    .facets(Map.of("max_validFrom", JsonStatFacet.of(max("validFrom"))))
                    .build())
            .done()
            .grouping()
            .enabled(true)
            .target(GroupByField.of("ksor"))
            .done()
            .build();

    SolrSelectResponse complexRes = CLIENT.select(complex).await().indefinitely();
    List<Ksor> ksors =
        complexRes.grouped().groups().get("ksor").entries().stream()
            .map(e -> e.docsResult().docs())
            .map(docs -> DOCUMENT_MAPPER.mapAllTo(docs, Ksor.class))
            .flatMap(Collection::stream)
            .toList();

    System.out.println("ksors: " + ksors);
  }
}

package com.solr.vertx.mutiny;

import com.solrion.core.api.request.SelectRequest;
import com.solrion.core.api.request.facets.classic.TermsFacet;
import com.solrion.core.client.HttpTransportOptions;
import com.solrion.core.client.SolrClientOptions;
import com.solrion.core.dsl.Q;
import com.solrion.core.dsl.SolrRequests;
import com.solrion.vertx.mutiny.client.SolrClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.client.WebClient;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.solrion.core.dsl.Q.field;

public class SimpleTest {

    // in Quarkus project dep injected using Quarkus ARC (implements jakarta CDI lite)
    private static final WebClient delegate = WebClient.create(Vertx.vertx(), new WebClientOptions()
            .setDefaultHost("solr.alpitour.it")
            .setDefaultPort(80));

    private static final SolrClient client = SolrClient.builder()
            .delegate(delegate)
                .clientOptions(SolrClientOptions.builder()
                        .failOnSolrError(true)
                        .build())
            .transportOptions(HttpTransportOptions.builder()
                        .readTimeout(Duration.ofSeconds(30))
                        .connectTimeout(Duration.ofSeconds(10))
                        .build())
            .build();

    private static final DateTimeFormatter DT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        SelectRequest simple = SolrRequests.select("productMainstream_B")
                .query(Q.matchAll())
                .build();

        client.select(simple).await().indefinitely();

        LocalDateTime from = LocalDateTime.now().minus(Duration.ofDays(30));
        LocalDateTime to = LocalDateTime.now().plus(Duration.ofDays(30));

        SelectRequest complex = SolrRequests.select("productMainstream_B")
                .query(field("validFrom").between(from, to))
                .classicFacets()
                    .enabled(true)
                    .facet(TermsFacet.ofField("example"))
                    .limit(100)
                    .done()
                .grouping()
                    .enabled(true)
                    .done()
                .build();

        client.select(complex).await().indefinitely();
    }

    private static String dateToString(LocalDateTime date) {
        return DT_FORMATTER.format(date);
    }
}

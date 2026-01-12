# Solrion

**Solrion** is a modern, strongly-typed, asynchronous-friendly and extensible Java client and DSL for Apache Solr.

It provides:
- A fluent, expressive query DSL
- Full support for classic and JSON facets
- Structured request and response models
- Pluggable transports (HTTP, Vert.x, Mutiny)
- Observability-friendly hooks (logging, metrics, tracing)
- Zero SolrJ dependency

> Solrion is designed for **modern JVM stacks** (Java 17+, reactive runtimes, cloud-native services).

---

## ✨ Features

- ✅ Typed DSL for `select`, `update`, facets, grouping, stats
- ✅ Classic facets and JSON facets (terms, range, query, stat, heatmap)
- ✅ Clean separation between API, protocol, transport
- ✅ Jackson-based structured response mapping
- ✅ Reactive-first adapters (Vert.x / Mutiny)
- ✅ Logging via SLF4J bindings

---

## 📦 Modules

| Module | Description |
|------|-------------|
| `solrion-core` | Core API, DSL, protocol mapping, codecs |
| `solrion-logging-slf4j` | SLF4J logging integration |
| `solrion-vertx-mutiny` | Vert.x / Mutiny reactive transport |

---

## 🚀 Quick example

```java
SelectRequest req = RequestBuilders.select("products")
    .query(field("category").eq("hotel"))
    .jsonFacets()
        .enabled(true)
        .facet("by_brand",
            JsonTermsFacet.builder()
                .field("brand")
                .limit(10)
                .facets(Map.of(
                    "avg_price",
                    JsonStatFacet.avg("price")
                ))
                .build()
        )
        .done()
    .build();

package com.solrion.core.mapper;

import com.solrion.core.api.types.SolrDocument;
import java.util.List;

public interface SolrDocumentMapper {

  <T> T mapTo(SolrDocument doc, Class<T> type);

  <T> SolrDocument mapFrom(T obj);

  default <T> List<T> mapAllTo(List<SolrDocument> docs, Class<T> type) {
    if (docs == null || docs.isEmpty()) {
      return List.of();
    }
    return docs.stream().map(doc -> mapTo(doc, type)).toList();
  }

  default <T> List<SolrDocument> mapAllFrom(List<T> objs, Class<T> type) {
    if (objs == null || objs.isEmpty()) {
      return List.of();
    }
    return objs.stream().map(this::mapFrom).toList();
  }

  static SolrDocumentMapper json() {
    return JsonSolrDocumentMapper.defaults();
  }
}

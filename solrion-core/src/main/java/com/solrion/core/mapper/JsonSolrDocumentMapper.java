package com.solrion.core.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.solrion.core.api.types.SolrDocument;
import com.solrion.core.exception.SolrDocumentMappingException;
import java.util.Map;

public class JsonSolrDocumentMapper implements SolrDocumentMapper {
  private static final ObjectMapper DEFAULT_MAPPER =
      JsonMapper.builder()
          .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
          .build();

  private final ObjectMapper mapper;

  public JsonSolrDocumentMapper(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  @Override
  public <T> T mapTo(SolrDocument doc, Class<T> type) {
    try {
      return mapper.convertValue(doc.fields(), type);
    } catch (Exception e) {
      throw new SolrDocumentMappingException(e);
    }
  }

  @Override
  public <T> SolrDocument mapFrom(T obj) {
    try {
      Map<String, Object> fields =
          mapper.convertValue(obj, new TypeReference<Map<String, Object>>() {});
      return SolrDocument.of(fields);
    } catch (Exception e) {
      throw new SolrDocumentMappingException(e);
    }
  }

  public static JsonSolrDocumentMapper defaults() {
    return new JsonSolrDocumentMapper(DEFAULT_MAPPER);
  }
}

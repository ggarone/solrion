package com.solrion.core.internal.codec;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.solrion.core.codec.SolrCodec;
import com.solrion.core.exception.SolrCodecException;
import com.solrion.core.internal.Validate;
import java.util.Objects;

public final class SolrJsonCodec implements SolrCodec {

  private static final ObjectMapper DEFAULT_MAPPER =
      JsonMapper.builder()
          .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
          .build();

  private final ObjectMapper mapper;

  public SolrJsonCodec(ObjectMapper mapper) {
    this.mapper = Validate.notNull(mapper, "object mapper");
  }

  public static SolrJsonCodec defaults() {
    return new SolrJsonCodec(DEFAULT_MAPPER);
  }

  @Override
  public String encode(Object value) {
    Objects.requireNonNull(value, "data");
    try {
      return mapper.writeValueAsString(value);
    } catch (JsonProcessingException e) {
      throw new SolrCodecException("Failed to encode data as JSON", e);
    }
  }

  @Override
  public <T> T decode(Object json, Class<T> type) {
    Objects.requireNonNull(json, "json");
    Objects.requireNonNull(type, "type");
    try {
      return mapper.convertValue(json, type);
    } catch (Exception e) {
      throw new SolrCodecException("Failed to decode JSON", e);
    }
  }

  @Override
  public <T> T decode(String json, Class<T> type) {
    Objects.requireNonNull(json, "json");
    Objects.requireNonNull(type, "type");
    try {
      return mapper.readValue(json, type);
    } catch (Exception e) {
      throw new SolrCodecException("Failed to decode JSON", e);
    }
  }
}

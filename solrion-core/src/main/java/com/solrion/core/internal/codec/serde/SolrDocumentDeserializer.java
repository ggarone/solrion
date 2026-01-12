package com.solrion.core.internal.codec.serde;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.*;
import com.solrion.core.api.types.SolrDocument;
import java.io.IOException;
import java.util.Map;

public final class SolrDocumentDeserializer extends JsonDeserializer<SolrDocument> {

  @Override
  @SuppressWarnings("unchecked")
  public SolrDocument deserialize(JsonParser p, DeserializationContext ctx) throws IOException {

    ObjectCodec codec = p.getCodec();
    Map<String, Object> fields = codec.readValue(p, Map.class);
    return new SolrDocument(fields);
  }
}

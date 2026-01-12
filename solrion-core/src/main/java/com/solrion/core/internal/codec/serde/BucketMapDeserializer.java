package com.solrion.core.internal.codec.serde;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public final class BucketMapDeserializer extends JsonDeserializer<Map<String, Long>> {

  @Override
  public Map<String, Long> deserialize(JsonParser p, DeserializationContext ctx)
      throws IOException {

    JsonNode node = p.getCodec().readTree(p);
    Map<String, Long> out = new LinkedHashMap<>();

    if (node.isArray()) {
      for (int i = 0; i + 1 < node.size(); i += 2) {
        out.put(node.get(i).asText(), node.get(i + 1).asLong());
      }
    } else if (node.isObject()) {
      node.fields().forEachRemaining(e -> out.put(e.getKey(), e.getValue().asLong()));
    }

    return out;
  }
}

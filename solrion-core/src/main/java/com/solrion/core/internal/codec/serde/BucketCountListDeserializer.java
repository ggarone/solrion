package com.solrion.core.internal.codec.serde;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Solr classic facets sometimes return buckets as a flat array:
 *   ["A", 10, "B", 5, ...]
 *
 * This deserializer converts that into a list of BucketCount records.
 */
public final class BucketCountListDeserializer extends JsonDeserializer<List<BucketCountListDeserializer.BucketCount>> {

    public record BucketCount(String key, long count) {}

    @Override
    public List<BucketCount> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (p.currentToken() == null) p.nextToken();
        if (p.currentToken() != JsonToken.START_ARRAY) {
            return List.of();
        }

        List<BucketCount> out = new ArrayList<>();
        while (p.nextToken() != JsonToken.END_ARRAY) {
            String key = p.getValueAsString();
            // move to count
            if (p.nextToken() == JsonToken.END_ARRAY) break;
            long count = p.getLongValue();
            out.add(new BucketCount(key, count));
        }
        return out;
    }
}

package com.solrion.core.internal.codec.serde;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.solrion.core.types.Range;

/**
 * Deserializes map keys like "[10,20)" into {@link Range}.
 */
public final class RangeKeyDeserializer extends KeyDeserializer {
    @Override
    public Object deserializeKey(String key, DeserializationContext ctxt) {
        if (key == null) return null;
        return Range.fromString(key);
    }
}

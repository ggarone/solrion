package com.solrion.core.codec;

import com.solrion.core.internal.codec.SolrJsonCodec;

public interface SolrCodec {
    String encode(Object value);
    <T> T decode(String json, Class<T> type);
    <T> T decode(Object json, Class<T> type);

    static SolrCodec json() {
        return SolrJsonCodec.defaults();
    }
}

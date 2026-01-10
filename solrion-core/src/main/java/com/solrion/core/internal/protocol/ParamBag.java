package com.solrion.core.internal.protocol;

import com.solrion.core.internal.Validate;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Simple multi-valued params container.
 */
public final class ParamBag {

    private final Map<String, List<String>> params = new LinkedHashMap<>();

    public List<String> getAll(String key) {
        return params.get(key);
    }

    public List<String> getAllOrDefault(String key, List<String> defaultValue) {
        return params.getOrDefault(key, defaultValue);
    }

    public boolean isEmpty() {
        return params.isEmpty();
    }

    public ParamBag add(String name, String value) {
        name = Validate.safeTrim(name);
        value = value == null ? null : value.trim();
        if (Validate.isBlank(name) || Validate.isBlank(value)) return this;

        params.computeIfAbsent(name, k -> new ArrayList<>()).add(value);
        return this;
    }

    public ParamBag addAll(String name, Collection<String> values) {
        if (values == null) return this;
        for (String v : values) add(name, v);
        return this;
    }

    public ParamBag putIfAbsent(String name, String value) {
        if (contains(name)) return this;
        return add(name, value);
    }

    public boolean contains(String name) {
        name = Validate.safeTrim(name);
        return name != null && params.containsKey(name);
    }

    public Map<String, List<String>> asMultiMap() {
        return Collections.unmodifiableMap(params);
    }

    public int estimatedBytes() {
        int size = 0;
        for (var e : params.entrySet()) {
            for (String v : e.getValue()) {
                size += getBytes(e.getKey()) + 1 + getBytes(v) + 1;
            }
        }
        return size;
    }

    private int getBytes(String v) {
        return v == null ? 0 : v.getBytes(StandardCharsets.UTF_8).length;
    }

    @Override
    public String toString() {
        return "ParamBag{" +
                "params=" + params +
                '}';
    }
}

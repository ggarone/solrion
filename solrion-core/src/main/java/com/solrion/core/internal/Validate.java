package com.solrion.core.internal;

import java.util.Collection;
import java.util.Map;

/**
 * Small, explicit validation helpers used across DSL, request models and mappers.
 * Keep it boring and consistent: validate at the boundary (builders, client methods).
 */
public final class Validate {

    private Validate() {}

    public static <T> T notNull(T v, String name) {
        if (v == null) throw new IllegalArgumentException(name + " cannot be null");
        return v;
    }

    public static String notBlank(String v, String name) {
        if (v == null || v.isBlank()) throw new IllegalArgumentException(name + " cannot be blank");
        return v;
    }

    public static Integer positive(Integer v, String name) {
        if (v == null) return null;
        if (v <= 0) throw new IllegalArgumentException(name + " must be > 0");
        return v;
    }

    public static int positive(int v, String name) {
        if (v <= 0) throw new IllegalArgumentException(name + " must be > 0");
        return v;
    }

    public static Long nonNegative(Long v, String name) {
        if (v == null) return null;
        if (v < 0) throw new IllegalArgumentException(name + " must be >= 0");
        return v;
    }

    public static Integer nonNegative(Integer v, String name) {
        if (v == null) return null;
        if (v < 0) throw new IllegalArgumentException(name + " must be >= 0");
        return v;
    }

    public static <T extends Collection<?>> T notEmpty(T v, String name) {
        if (v == null || v.isEmpty()) throw new IllegalArgumentException(name + " cannot be empty");
        return v;
    }

    public static <T extends Map<?, ?>> T notEmpty(T v, String name) {
        if (v == null || v.isEmpty()) throw new IllegalArgumentException(name + " cannot be empty");
        return v;
    }

    public static <T> T require(boolean condition, String message) {
        if (!condition) throw new IllegalArgumentException(message);
        return null;
    }

    public static String safeTrim(String s) {
        return s == null ? null : s.trim();
    }

    public static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }

    public static boolean isEmpty(Collection<?> c) {
        return c == null || c.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> m) {
        return m == null || m.isEmpty();
    }

    public static <T> T orDefault(T v, T dflt) {
        return v != null ? v : dflt;
    }
}

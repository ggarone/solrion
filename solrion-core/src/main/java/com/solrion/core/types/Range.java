package com.solrion.core.types;

import com.solrion.core.internal.Validate;

import java.util.Objects;
import java.util.function.Function;

/**
 * Immutable range with optional unbounded ends.
 *
 * Canonical string form (interval notation):
 *   [a,b]  [a,b)  (a,b]  (a,b)
 *
 * Unbounded ends are rendered as:
 *   -inf for lower, inf for upper
 *
 * Parsing supports: -inf, inf, +inf, *
 */
public final class Range<T extends Comparable<? super T>> {

    private final Bound<T> lower;
    private final Bound<T> upper;

    public Range(Bound<T> lower, Bound<T> upper) {
        this.lower = normalize(lower);
        this.upper = normalize(upper);

        if (this.lower.bounded() && this.upper.bounded()) {
            T lo = this.lower.value();
            T hi = this.upper.value();
            if (lo != null && hi != null && lo.compareTo(hi) > 0) {
                throw new IllegalArgumentException("lower bound must be <= upper bound");
            }
        }
    }

    public static <T extends Comparable<? super T>> Range<T> closed(T lo, T hi) {
        Validate.notNull(lo, "lower");
        Validate.notNull(hi, "upper");
        return new Range<>(Bound.inclusive(lo), Bound.inclusive(hi));
    }

    public static <T extends Comparable<? super T>> Range<T> open(T lo, T hi) {
        Validate.notNull(lo, "lower");
        Validate.notNull(hi, "upper");
        return new Range<>(Bound.exclusive(lo), Bound.exclusive(hi));
    }

    public static <T extends Comparable<? super T>> Range<T> openClosed(T lo, T hi) {
        Validate.notNull(lo, "lower");
        Validate.notNull(hi, "upper");
        return new Range<>(Bound.exclusive(lo), Bound.inclusive(hi));
    }

    public static <T extends Comparable<? super T>> Range<T> closedOpen(T lo, T hi) {
        Validate.notNull(lo, "lower");
        Validate.notNull(hi, "upper");
        return new Range<>(Bound.inclusive(lo), Bound.exclusive(hi));
    }

    public static <T extends Comparable<? super T>> Range<T> greaterThan(T lo) {
        Validate.notNull(lo, "lower");
        return new Range<>(Bound.exclusive(lo), Bound.unbounded());
    }

    public static <T extends Comparable<? super T>> Range<T> atLeast(T lo) {
        Validate.notNull(lo, "lower");
        return new Range<>(Bound.inclusive(lo), Bound.unbounded());
    }

    public static <T extends Comparable<? super T>> Range<T> lowerThan(T hi) {
        Validate.notNull(hi, "upper");
        return new Range<>(Bound.unbounded(), Bound.exclusive(hi));
    }

    public static <T extends Comparable<? super T>> Range<T> atMost(T hi) {
        Validate.notNull(hi, "upper");
        return new Range<>(Bound.unbounded(), Bound.inclusive(hi));
    }

    public static <T extends Comparable<? super T>> Range<T> unbounded() {
        return new Range<>(Bound.<T>unbounded(), Bound.<T>unbounded());
    }

    public static Range<String> fromString(String raw) {
        return fromString(raw, Function.identity());
    }

    public static <T extends Comparable<? super T>> Range<T> fromString(String raw, Function<String, T> valueParser) {
        Validate.notBlank(raw, "range");
        Validate.notNull(valueParser, "valueParser");

        String s = raw.trim();
        if (s.length() < 5) throw new IllegalArgumentException("Invalid range: " + raw);

        char left = s.charAt(0);
        char right = s.charAt(s.length() - 1);

        boolean lowerInclusive = left == '[';
        boolean lowerExclusive = left == '(';
        boolean upperInclusive = right == ']';
        boolean upperExclusive = right == ')';

        if (!(lowerInclusive || lowerExclusive)) throw new IllegalArgumentException("Invalid lower bracket in: " + raw);
        if (!(upperInclusive || upperExclusive)) throw new IllegalArgumentException("Invalid upper bracket in: " + raw);

        String inner = s.substring(1, s.length() - 1);
        int comma = inner.indexOf(',');
        if (comma < 0) {
            throw new IllegalArgumentException("Invalid range (missing comma): " + raw);
        }

        String loToken = inner.substring(0, comma).trim();
        String hiToken = inner.substring(comma + 1).trim();

        Bound<T> lo = parseLowerBound(loToken, lowerInclusive, valueParser, raw);
        Bound<T> hi = parseUpperBound(hiToken, upperInclusive, valueParser, raw);

        return new Range<>(lo, hi);
    }

    private static boolean isTokenUnbounded(String token) {
        if (token == null) return true;
        String t = token.trim();
        if (t.isEmpty()) return true;
        return "*".equals(t)
                || "-inf".equalsIgnoreCase(t)
                || "+inf".equalsIgnoreCase(t)
                || "inf".equalsIgnoreCase(t);
    }

    private static boolean isLowerUnboundedToken(String token) {
        if (token == null) return true;
        String t = token.trim();
        if (t.isEmpty()) return true;
        return "*".equals(t) || "-inf".equalsIgnoreCase(t);
    }

    private static boolean isUpperUnboundedToken(String token) {
        if (token == null) return true;
        String t = token.trim();
        if (t.isEmpty()) return true;
        return "*".equals(t) || "inf".equalsIgnoreCase(t) || "+inf".equalsIgnoreCase(t);
    }

    private static <T extends Comparable<? super T>> Bound<T> parseLowerBound(
            String token,
            boolean inclusive,
            Function<String, T> valueParser,
            String raw
    ) {
        if (isLowerUnboundedToken(token)) return Bound.unbounded();
        if (isTokenUnbounded(token)) throw new IllegalArgumentException("Invalid lower bound token in: " + raw);

        T v;
        try {
            v = valueParser.apply(token);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed parsing lower bound '" + token + "' in: " + raw, e);
        }
        if (v == null) throw new IllegalArgumentException("Lower bound parser returned null for: " + raw);
        return inclusive ? Bound.inclusive(v) : Bound.exclusive(v);
    }

    private static <T extends Comparable<? super T>> Bound<T> parseUpperBound(
            String token,
            boolean inclusive,
            Function<String, T> valueParser,
            String raw
    ) {
        if (isUpperUnboundedToken(token)) return Bound.unbounded();
        if (isTokenUnbounded(token)) throw new IllegalArgumentException("Invalid upper bound token in: " + raw);

        T v;
        try {
            v = valueParser.apply(token);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed parsing upper bound '" + token + "' in: " + raw, e);
        }
        if (v == null) throw new IllegalArgumentException("Upper bound parser returned null for: " + raw);
        return inclusive ? Bound.inclusive(v) : Bound.exclusive(v);
    }

    private static <T> Bound<T> normalize(Bound<T> b) {
        return b == null ? Bound.unbounded() : b;
    }

    public Bound<T> lower() {
        return lower;
    }

    public Bound<T> upper() {
        return upper;
    }

    public String toCanonicalString() {
        StringBuilder sb = new StringBuilder(32);

        sb.append(lower.bounded() ? (lower.inclusive() ? '[' : '(') : '(');
        sb.append(lower.bounded() ? String.valueOf(lower.value()) : "-inf");
        sb.append(',');
        sb.append(upper.bounded() ? String.valueOf(upper.value()) : "inf");
        sb.append(upper.bounded() ? (upper.inclusive() ? ']' : ')') : ')');

        return sb.toString();
    }

    @Override
    public String toString() {
        return toCanonicalString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Range<?> other)) return false;
        return Objects.equals(lower, other.lower) && Objects.equals(upper, other.upper);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lower, upper);
    }

    public static final class Bound<T> {
        private final T value;
        private final boolean inclusive;
        private final boolean bounded;

        private Bound(T value, boolean inclusive, boolean bounded) {
            this.value = value;
            this.inclusive = inclusive;
            this.bounded = bounded;
        }

        public static <T> Bound<T> unbounded() {
            return new Bound<>(null, false, false);
        }

        public static <T> Bound<T> inclusive(T value) {
            Validate.notNull(value, "bound value");
            return new Bound<>(value, true, true);
        }

        public static <T> Bound<T> exclusive(T value) {
            Validate.notNull(value, "bound value");
            return new Bound<>(value, false, true);
        }

        public T value() {
            return value;
        }

        public boolean inclusive() {
            return inclusive;
        }

        public boolean bounded() {
            return bounded;
        }

        @Override
        public String toString() {
            if (!bounded) return "*";
            return (inclusive ? "[" : "(") + value + (inclusive ? "]" : ")");
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Bound<?> other)) return false;

            if (!this.bounded && !other.bounded) return true;
            if (this.bounded != other.bounded) return false;

            return this.inclusive == other.inclusive && Objects.equals(this.value, other.value);
        }

        @Override
        public int hashCode() {
            if (!bounded) return 0x2f3a91;
            return Objects.hash(value, inclusive, true);
        }
    }
}

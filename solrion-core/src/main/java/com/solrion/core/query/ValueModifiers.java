package com.solrion.core.query;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;

/**
 * Term-level modifiers (phrase, boost, fuzziness, etc.).
 *
 * Empty modifiers are allowed and render as no-op.
 */
@Accessors(fluent = true)
@Value
@Builder
public class ValueModifiers {
    Boolean phrase;
    Double boost;
    Integer fuzzyDistance;
    Integer proximity;
    Boolean regex;
    Boolean wildcard;

    public boolean isEmpty() {
        return phrase == null
                && boost == null
                && fuzzyDistance == null
                && proximity == null
                && regex == null
                && wildcard == null;
    }

    public static ValueModifiers empty() {
        return ValueModifiers.builder().build();
    }
}

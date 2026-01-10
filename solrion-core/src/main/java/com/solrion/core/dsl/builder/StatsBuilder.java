package com.solrion.core.dsl.builder;

import com.solrion.core.api.request.stats.classic.StatsField;
import com.solrion.core.api.request.stats.classic.StatsSpec;
import com.solrion.core.internal.Validate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Top-level fluent builder for Solr stats (stats.field).
 *
 * StatsField instances are created by the caller and registered here.
 */
public final class StatsBuilder {

    private final SelectRequestBuilder parent;

    private boolean enabled = true;
    private final List<StatsField> fields = new ArrayList<>();
    private final Map<String, String> rawParams = new LinkedHashMap<>();

    StatsBuilder(SelectRequestBuilder parent) {
        this.parent = Validate.notNull(parent, "parent");
    }

    // ------------------------------------------------------------------
    // Configuration
    // ------------------------------------------------------------------

    public StatsBuilder enabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    // ------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------

    /**
     * Register a stats field.
     */
    public StatsBuilder field(StatsField field) {
        if (field != null) {
            fields.add(field);
        }
        return this;
    }

    /**
     * Register multiple stats fields.
     */
    public StatsBuilder fields(List<? extends StatsField> fields) {
        if (fields == null || fields.isEmpty()) return this;

        fields.forEach(f -> {
            if (f != null) this.fields.add(f);
        });
        return this;
    }

    // ------------------------------------------------------------------
    // Raw params (escape hatch)
    // ------------------------------------------------------------------

    public StatsBuilder param(String name, String value) {
        name = Validate.safeTrim(name);
        value = Validate.safeTrim(value);

        if (!Validate.isBlank(name) && !Validate.isBlank(value)) {
            rawParams.put(name, value);
        }
        return this;
    }

    // ------------------------------------------------------------------
    // Terminal
    // ------------------------------------------------------------------

    public SelectRequestBuilder done() {
        if (enabled && fields.isEmpty()) {
            throw new IllegalStateException("Stats enabled but no fields specified");
        }

        parent.setStats(
                new StatsSpec(
                        enabled,
                        List.copyOf(fields),
                        Map.copyOf(rawParams)
                )
        );
        return parent;
    }
}

package com.solrion.core.internal.protocol.emitter;

import com.solrion.core.api.request.stats.classic.StatsField;
import com.solrion.core.api.request.stats.classic.StatsSpec;
import com.solrion.core.internal.protocol.ParamBag;

import java.util.StringJoiner;

/**
 * Emits classical Solr classicStats parameters.
 */
public final class ClassicStatsParamsEmitter {

    public void emit(StatsSpec spec, ParamBag p) {
        if (spec == null || p == null || !spec.enabled()) return;
        if (spec.fields().isEmpty()) return;

        // enable classicStats component
        p.add("classicStats", "true");

        // classicStats.field (repeatable)
        for (StatsField sf : spec.fields()) {
            if (sf == null) continue;
            p.add("classicStats.field", renderStatsField(sf));
        }

        // raw params last (escape hatch)
        spec.rawParams().forEach(p::add);
    }

    // ------------------------------------------------------------------
    // Rendering
    // ------------------------------------------------------------------

    private String renderStatsField(StatsField sf) {
        String field = sf.field();

        String localParams = buildLocalParams(sf);
        return localParams == null ? field : localParams + field;
    }

    private String buildLocalParams(StatsField sf) {
        boolean hasPercentiles = sf.percentiles() != null && !sf.percentiles().isEmpty();
        boolean hasDistinct = sf.calcDistinct() != null;
        boolean hasCustomLocal = sf.localParams() != null && !sf.localParams().isEmpty();

        if (!hasPercentiles && !hasDistinct && !hasCustomLocal) {
            return null;
        }

        StringJoiner joiner = new StringJoiner(" ", "{!", "}");

        // percentiles
        if (hasPercentiles) {
            StringJoiner p = new StringJoiner(",");
            sf.percentiles().forEach(v -> p.add(String.valueOf(v)));
            joiner.add("percentiles=" + p);
        }

        // distinct
        if (sf.calcDistinct() != null) {
            joiner.add("distinct=" + sf.calcDistinct());
        }

        // user-provided local params
        if (hasCustomLocal) {
            sf.localParams().params().forEach((k, v) ->
                    joiner.add(k + "=" + v)
            );
        }

        return joiner.toString();
    }
}

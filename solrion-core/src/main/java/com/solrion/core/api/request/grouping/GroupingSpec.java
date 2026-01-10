package com.solrion.core.api.request.grouping;

import com.solrion.core.internal.Validate;
import com.solrion.core.query.Expr;

import java.util.Map;

/**
 * Solr grouping / field collapsing specification.
 *
 * Exactly one grouping target must be provided.
 */
public record GroupingSpec(
        boolean enabled,
        GroupingTarget target,
        Integer limit,
        Integer offset,
        Expr.Sort groupSort,
        GroupingFormat format,
        Boolean main,
        Boolean ngroups,
        Map<String, String> rawParams
) {
    public GroupingSpec {
        if (enabled) {
            Validate.notNull(target, "target");
        }

        if (limit != null) Validate.require(limit >= 0, "limit must be >= 0");
        if (offset != null) Validate.require(offset >= 0, "offset must be >= 0");

        format = format == null ? GroupingFormat.GROUPED : format;
        rawParams = rawParams == null ? Map.of() : Map.copyOf(rawParams);
    }

    public static GroupingSpec disabled() {
        return new GroupingSpec(
                false,
                null,
                null,
                null,
                null,
                GroupingFormat.GROUPED,
                null,
                null,
                Map.of()
        );
    }
}

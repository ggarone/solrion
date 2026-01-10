package com.solrion.core.dsl.builder;

import com.solrion.core.api.request.grouping.GroupingFormat;
import com.solrion.core.api.request.grouping.GroupingSpec;
import com.solrion.core.api.request.grouping.GroupingTarget;
import com.solrion.core.internal.Validate;
import com.solrion.core.query.Expr;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Top-level fluent builder for Solr grouping (field collapsing).
 *
 * Grouping targets are created by the caller and registered here.
 */
public final class GroupingBuilder {

    private final SelectRequestBuilder parent;

    private boolean enabled = true;
    private GroupingTarget target;

    private Integer limit;
    private Integer offset;
    private Expr.Sort sort;
    private GroupingFormat format;
    private Boolean main;
    private Boolean ngroups;

    private final Map<String, String> rawParams = new LinkedHashMap<>();

    GroupingBuilder(SelectRequestBuilder parent) {
        this.parent = Validate.notNull(parent, "parent");
    }

    // ------------------------------------------------------------------
    // Configuration
    // ------------------------------------------------------------------

    public GroupingBuilder enabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public GroupingBuilder limit(Integer value) {
        this.limit = value;
        return this;
    }

    public GroupingBuilder offset(Integer value) {
        this.offset = value;
        return this;
    }

    public GroupingBuilder sort(Expr.Sort sort) {
        this.sort = sort;
        return this;
    }

    public GroupingBuilder format(GroupingFormat format) {
        this.format = format;
        return this;
    }

    public GroupingBuilder main(Boolean value) {
        this.main = value;
        return this;
    }

    public GroupingBuilder ngroups(Boolean value) {
        this.ngroups = value;
        return this;
    }

    // ------------------------------------------------------------------
    // Targets
    // ------------------------------------------------------------------

    /**
     * Register a grouping target.
     */
    public GroupingBuilder target(GroupingTarget target) {
        if (target != null) this.target = target;
        return this;
    }

    // ------------------------------------------------------------------
    // Raw params (escape hatch)
    // ------------------------------------------------------------------

    public GroupingBuilder param(String name, String value) {
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
        parent.setGrouping(
                new GroupingSpec(
                        enabled,
                        target,
                        limit,
                        offset,
                        sort,
                        format,
                        main,
                        ngroups,
                        Map.copyOf(rawParams)
                )
        );
        return parent;
    }
}

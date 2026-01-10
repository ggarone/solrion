package com.solrion.core.internal.protocol.emitter;

import com.solrion.core.api.request.grouping.*;
import com.solrion.core.internal.Validate;
import com.solrion.core.internal.protocol.ParamBag;
import com.solrion.core.query.SolrDialectTranslator;

/**
 * Emits Solr grouping / field collapsing parameters.
 *
 * Supports:
 *  - group.field
 *  - group.query
 *  - group.func
 */
public final class GroupingParamsEmitter {

    private final SolrDialectTranslator translator;

    public GroupingParamsEmitter(SolrDialectTranslator translator) {
        this.translator = Validate.notNull(translator, "translator");
    }

    public void emit(GroupingSpec spec, ParamBag p) {
        if (spec == null || p == null || !spec.enabled()) return;

        GroupingTarget target = spec.target();
        if (target == null) return;

        // enable grouping
        p.add("group", "true");

        // ---- grouping target ----
        if (target instanceof GroupByField byField) {
            byField.fields()
                    .stream()
                    .filter(f -> !Validate.isBlank(f))
                    .forEach(f -> p.add("group.field", f));
        }
        else if (target instanceof GroupByQuery byQuery) {
            byQuery.queries()
                    .forEach(q ->
                            p.add("group.query", translator.render(q))
                    );
        }
        else if (target instanceof GroupByFunc byFunc) {
            p.add("group.func", translator.render(byFunc.func()));
        }

        // ---- common options ----
        if (spec.limit() != null) {
            p.add("group.limit", String.valueOf(spec.limit()));
        }
        if (spec.offset() != null) {
            p.add("group.offset", String.valueOf(spec.offset()));
        }

        if (spec.groupSort() != null && !spec.groupSort().isEmpty()) {
            p.add("group.sort", translator.render(spec.groupSort()));
        }

        if (spec.format() != null) {
            p.add("group.format", spec.format().value());
        }
        if (spec.main() != null) {
            p.add("group.main", String.valueOf(spec.main()));
        }
        if (spec.ngroups() != null) {
            p.add("group.ngroups", String.valueOf(spec.ngroups()));
        }

        // ---- raw params last (escape hatch) ----
        spec.rawParams().forEach(p::add);
    }
}
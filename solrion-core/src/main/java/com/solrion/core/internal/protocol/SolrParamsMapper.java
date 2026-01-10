package com.solrion.core.internal.protocol;

import com.solrion.core.api.request.SelectRequest;
import com.solrion.core.api.request.update.UpdateOptions;
import com.solrion.core.codec.SolrCodec;
import com.solrion.core.internal.protocol.emitter.ClassicFacetParamsEmitter;
import com.solrion.core.internal.protocol.emitter.ClassicStatsParamsEmitter;
import com.solrion.core.internal.protocol.emitter.GroupingParamsEmitter;
import com.solrion.core.internal.protocol.emitter.JsonFacetParamsEmitter;
import com.solrion.core.query.SolrDialectTranslator;
import com.solrion.core.internal.Validate;

import java.util.List;

/**
 * Maps high-level request models to Solr queries params.
 */
public final class SolrParamsMapper {

    private final SolrDialectTranslator translator;
    private final ClassicFacetParamsEmitter classicFacets;
    private final JsonFacetParamsEmitter jsonFacets;
    private final ClassicStatsParamsEmitter classicStats;
    private final GroupingParamsEmitter grouping;

    public SolrParamsMapper(SolrDialectTranslator translator, SolrCodec codec) {
        this.translator = Validate.notNull(translator, "translator");
        this.classicFacets = new ClassicFacetParamsEmitter(translator);
        this.jsonFacets = new JsonFacetParamsEmitter(translator, codec);
        this.classicStats = new ClassicStatsParamsEmitter();
        this.grouping = new GroupingParamsEmitter(translator);
    }

    public ParamBag toSelectParams(SelectRequest req) {
        Validate.notNull(req, "req");

        ParamBag p = new ParamBag();

        // q - if empty, default to match all.
        if(req.queries() != null) {
            List<String> rendered = translator.renderAll(req.queries());
            p.addAll("q", rendered.isEmpty() ? List.of("*:*") : rendered);
        }

        // fq
        if (req.filterQueries() != null) {
            List<String> rendered = translator.renderAll(req.filterQueries());
            p.addAll("fq", rendered);
        }

        // fl
        String fl = req.fieldList() != null ? translator.render(req.fieldList()) : "*";
        p.add("fl", fl);

        // sort
        if (req.sort() != null && !req.sort().isEmpty()) {
            p.add("sort", translator.render(req.sort()));
        }

        // paging
        if (req.pagination() != null) {
            if (req.pagination().start() != null) p.add("request", String.valueOf(req.pagination().start()));
            if (req.pagination().rows() != null) p.add("rows", String.valueOf(req.pagination().rows()));
        }

        // response format (wt)
        if (req.responseFormat() != null) {
            p.add("wt", req.responseFormat().wt());
        }

        // facets
        classicFacets.emit(req.classicFacets(), p);
        jsonFacets.emit(req.jsonFacets(), p);

        // grouping
        grouping.emit(req.grouping(), p);

        // classicStats
        classicStats.emit(req.classicStats(), p);

        // raw params last
        if (req.rawParams() != null) {
            req.rawParams().forEach(p::add);
        }

        return p;
    }

    public ParamBag toUpdateOptions(UpdateOptions opt) {
        ParamBag p = new ParamBag();
        if (opt == null) return p;

        if (opt.commit() != null) p.add("commit", String.valueOf(opt.commit()));
        else if (opt.softCommit() != null) p.add("softcommit", String.valueOf(opt.softCommit()));
        else if (opt.commitWithinMs() != null) p.add("commitWithinMs", String.valueOf(opt.commitWithinMs()));

        if (opt.overwrite() != null) p.add("overwrite", String.valueOf(opt.overwrite()));
        if (opt.expungeDeletes() != null) p.add("expungeDeletes", String.valueOf(opt.expungeDeletes()));
        if (opt.waitSearcher() != null) p.add("waitSearcher", String.valueOf(opt.waitSearcher()));
        if (opt.openSearcher() != null) p.add("openSearcher", String.valueOf(opt.openSearcher()));

        return p;
    }
}

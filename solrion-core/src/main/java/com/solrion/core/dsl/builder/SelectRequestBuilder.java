package com.solrion.core.dsl.builder;

import com.solrion.core.api.request.Pagination;
import com.solrion.core.api.request.ResponseFormat;
import com.solrion.core.api.request.SelectRequest;
import com.solrion.core.api.request.facets.classic.ClassicFacetsSpec;
import com.solrion.core.api.request.facets.json.JsonFacetsSpec;
import com.solrion.core.api.request.grouping.GroupingSpec;
import com.solrion.core.api.request.stats.classic.StatsSpec;
import com.solrion.core.query.Expr;
import com.solrion.core.internal.Validate;

import java.util.*;

public final class SelectRequestBuilder extends AbstractRequestBuilder<SelectRequestBuilder> {

    private final List<Expr.BoolExpr> queries = new ArrayList<>();
    private final List<Expr.BoolExpr> filterQueries = new ArrayList<>();

    private Expr.FieldList fieldList = Expr.FieldList.all();
    private Expr.Sort sort;

    private Pagination pagination;
    private ResponseFormat responseFormat = ResponseFormat.JSON;

    private ClassicFacetsSpec classicFacets = ClassicFacetsSpec.disabled();
    private JsonFacetsSpec jsonFacets = JsonFacetsSpec.disabled();
    private GroupingSpec grouping = GroupingSpec.disabled();
    private StatsSpec stats = StatsSpec.disabled();

    private final Map<String, String> rawParams = new LinkedHashMap<>();

    public SelectRequestBuilder query(Expr.BoolExpr q) {
        if(q != null) this.queries.add(q);
        return this;
    }

    public SelectRequestBuilder query(Collection<? extends Expr.BoolExpr> qs) {
        if(qs != null) qs.forEach(this::query);
        return this;
    }

    public SelectRequestBuilder fq(Expr.BoolExpr fq) {
        if (fq != null) this.filterQueries.add(fq);
        return this;
    }

    public SelectRequestBuilder fq(Collection<? extends Expr.BoolExpr> fqs) {
        if (fqs != null) fqs.forEach(this::fq);
        return this;
    }

    public SelectRequestBuilder fl(Expr.FieldList fl) {
        this.fieldList = fl == null ? Expr.FieldList.all() : fl;
        return this;
    }

    public SelectRequestBuilder fl(String... fields) {
        this.fieldList = Expr.FieldList.of(fields);
        return this;
    }

    public SelectRequestBuilder sort(Expr.Sort sort) {
        this.sort = sort;
        return this;
    }

    public SelectRequestBuilder page(Pagination pagination) {
        this.pagination = pagination;
        return this;
    }

    public SelectRequestBuilder page(int start, int rows) {
        this.pagination = Pagination.of(start, rows);
        return this;
    }

    public SelectRequestBuilder wt(ResponseFormat format) {
        this.responseFormat = format == null ? ResponseFormat.JSON : format;
        return this;
    }

    public ClassicFacetsBuilder classicFacets() {
        return new ClassicFacetsBuilder(this);
    }

    void setClassicFacets(ClassicFacetsSpec spec) {
        this.classicFacets = spec == null ? ClassicFacetsSpec.disabled() : spec;
    }

    public JsonFacetsBuilder jsonFacets() {
        return new JsonFacetsBuilder(this);
    }

    void setJsonFacets(JsonFacetsSpec spec) {
        this.jsonFacets = spec == null ? JsonFacetsSpec.disabled() : spec;
    }

    public GroupingBuilder grouping() {
        return new GroupingBuilder(this);
    }

    void setGrouping(GroupingSpec spec) {
        this.grouping = spec == null ? GroupingSpec.disabled() : spec;
    }

    public SelectRequestBuilder param(String name, String value) {
        name = Validate.safeTrim(name);
        value = Validate.safeTrim(value);
        if (!Validate.isBlank(name) && !Validate.isBlank(value)) rawParams.put(name, value);
        return this;
    }

    public SelectRequest build() {
        return new SelectRequest(
                collection,
                transportOptions,
                List.copyOf(queries),
                List.copyOf(filterQueries),
                fieldList,
                sort,
                pagination,
                responseFormat,
                classicFacets,
                jsonFacets,
                grouping,
                stats,
                Map.copyOf(rawParams)
        );
    }

    public void setStats(StatsSpec spec) {
        this.stats = spec == null ? StatsSpec.disabled() : spec;
    }
}

package com.solrion.core.api.request;

import com.solrion.core.api.request.facets.classic.ClassicFacetsSpec;
import com.solrion.core.api.request.facets.json.JsonFacetsSpec;
import com.solrion.core.api.request.grouping.GroupingSpec;
import com.solrion.core.api.request.stats.classic.StatsSpec;
import com.solrion.core.client.HttpTransportOptions;
import com.solrion.core.query.Expr;
import com.solrion.core.internal.Validate;

import java.util.List;
import java.util.Map;

/**
 * High-level select request (Solr /select handler).
 *
 * NOTE: This is NOT a 1:1 mapping of queries params; mapping is performed in protocol layer.
 */
public record SelectRequest(
        String collection,
        HttpTransportOptions transport,
        List<Expr.BoolExpr> queries,
        List<Expr.BoolExpr> filterQueries,
        Expr.FieldList fieldList,
        Expr.Sort sort,
        Pagination pagination,
        ResponseFormat responseFormat,
        ClassicFacetsSpec classicFacets,
        JsonFacetsSpec jsonFacets,
        GroupingSpec grouping,
        StatsSpec classicStats,
        Map<String, String> rawParams
) implements SolrRequest {

    public SelectRequest {
        Validate.notNull(transport, "transport");
        queries = queries == null ? List.of() : List.copyOf(queries);
        filterQueries = filterQueries == null ? List.of() : List.copyOf(filterQueries);
        fieldList = fieldList == null ? Expr.FieldList.all() : fieldList;
        responseFormat = responseFormat == null ? ResponseFormat.JSON : responseFormat;
        classicFacets = classicFacets == null ? ClassicFacetsSpec.disabled() : classicFacets;
        jsonFacets = jsonFacets == null ? JsonFacetsSpec.disabled() : jsonFacets;
        grouping = grouping == null ? GroupingSpec.disabled() : grouping;
        classicStats = classicStats == null ? StatsSpec.disabled() : classicStats;
        rawParams = rawParams == null ? Map.of() : Map.copyOf(rawParams);
    }
}

package com.solrion.core.api.request.stats.classic;

import com.solrion.core.internal.Validate;
import com.solrion.core.query.Expr;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(fluent = true)
@Value
public class StatsField {

    String field;
    List<Integer> percentiles;
    Boolean calcDistinct;
    Expr.LocalParams localParams;

    @Builder
    public StatsField(
            String field,
            List<Integer> percentiles,
            Boolean calcDistinct,
            Expr.LocalParams localParams
    ) {
        if (percentiles != null) {
            percentiles.forEach(p ->
                    Validate.require(p > 0 && p < 100,
                            "percentile must be between 1 and 99"));
        }

        this.field = Validate.notBlank(field, "field");
        this.percentiles = percentiles == null ? List.of() : List.copyOf(percentiles);
        this.calcDistinct = calcDistinct;
        this.localParams = localParams;
    }
}


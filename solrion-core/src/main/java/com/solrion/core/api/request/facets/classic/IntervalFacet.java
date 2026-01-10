package com.solrion.core.api.request.facets.classic;

import com.solrion.core.internal.Validate;
import com.solrion.core.query.Expr;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(fluent = true)
@Value
public class IntervalFacet implements ClassicFacet {

    String name;
    String field;
    List<String> intervals;
    Integer minCount;
    Expr.LocalParams localParams;

    @Builder
    public IntervalFacet(
            String name,
            String field,
            List<String> intervals,
            Integer minCount,
            Expr.LocalParams localParams
    ) {
        this.field = Validate.notBlank(field, "field");;
        this.name = Validate.safeTrim(name);
        this.intervals = List.copyOf(Validate.notEmpty(intervals, "intervals"));
        this.minCount = minCount;
        this.localParams = localParams;
    }

    public static IntervalFacet of(
            String field,
            String... intervals
    ) {
        return IntervalFacet.builder()
                .field(field)
                .intervals(List.of(intervals))
                .build();
    }

    @Override
    public <R, C> R accept(ClassicalFacetVisitor<R, C> visitor, C ctx) {
        return visitor.visitInterval(this, ctx);
    }
}

package com.solrion.core.api.request.facets.classic;

import com.solrion.core.api.request.facets.RangeInclude;
import com.solrion.core.api.request.facets.RangeOther;
import com.solrion.core.internal.Validate;
import com.solrion.core.query.Expr;
import com.solrion.core.types.Range;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Value
public class RangeFacet<T extends Comparable<T>> implements ClassicFacet {

    String name;
    String field;
    Range<T> range;
    String gap;
    RangeInclude include;
    RangeOther other;
    Boolean hardEnd;
    Integer minCount;
    Expr.LocalParams localParams;

    @Builder
    public RangeFacet(
            String name,
            String field,
            Range<T> range,
            String gap,
            RangeInclude include,
            RangeOther other,
            Boolean hardEnd,
            Integer minCount,
            Expr.LocalParams localParams
    ) {
        this.field = Validate.notBlank(field, "field");
        this.name = Validate.safeTrim(name);
        this.range = Validate.notNull(range, "range");
        this.gap = Validate.notBlank(gap, "gap");
        this.include = include;
        this.other = other;
        this.hardEnd = hardEnd;
        this.minCount = minCount != null ? Validate.require(minCount >= 0, "minCount must be >= 0") : null;
        this.localParams = localParams;
    }

    public static <T extends Comparable<T>> RangeFacet<T> ofField(
            String field,
            Range<T> range,
            String gap
    ) {
        return RangeFacet.<T>builder()
                .field(field)
                .range(range)
                .gap(gap)
                .build();
    }

    public static <T extends Comparable<T>> RangeFacet<T> ofField(
            String name,
            String field,
            Range<T> range,
            String gap
    ) {
        return RangeFacet.<T>builder()
                .name(name)
                .field(field)
                .range(range)
                .gap(gap)
                .build();
    }

    @Override
    public <R, C> R accept(ClassicalFacetVisitor<R, C> visitor, C ctx) {
        return visitor.visitRange(this, ctx);
    }
}

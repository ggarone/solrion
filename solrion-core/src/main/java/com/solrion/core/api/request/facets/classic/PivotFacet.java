package com.solrion.core.api.request.facets.classic;

import com.solrion.core.internal.Validate;
import com.solrion.core.query.Expr;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(fluent = true)
@Value
public class PivotFacet implements ClassicFacet {

    String name;
    List<String> fields;
    Integer minCount;
    Integer limit;
    Expr.LocalParams localParams;

    @Builder
    public PivotFacet(
            String name,
            List<String> fields,
            Integer minCount,
            Integer limit,
            Expr.LocalParams localParams
    ) {
        this.fields = List.copyOf(Validate.notEmpty(fields, "fields"));
        this.name = Validate.safeTrim(name);
        this.minCount = minCount != null ? Validate.require(minCount >= 0, "minCount must be >= 0") : null;
        this.limit = limit;
        this.localParams = localParams;
    }

    public static PivotFacet of(String... fields) {
        return PivotFacet.builder()
                .fields(List.of(fields))
                .build();
    }

    @Override
    public <R, C> R accept(ClassicalFacetVisitor<R, C> visitor, C ctx) {
        return visitor.visitPivot(this, ctx);
    }
}


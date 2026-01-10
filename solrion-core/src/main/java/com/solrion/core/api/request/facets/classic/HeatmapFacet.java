package com.solrion.core.api.request.facets.classic;

import com.solrion.core.internal.Validate;
import com.solrion.core.query.Expr;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Value
public class HeatmapFacet implements ClassicFacet {

    String name;
    String field;
    String geom;
    Integer level;
    Integer maxCells;
    Expr.LocalParams localParams;

    @Builder
    public HeatmapFacet(
            String name,
            String field,
            String geom,
            Integer level,
            Integer maxCells,
            Expr.LocalParams localParams
    ) {
        this.field = Validate.notBlank(field, "field");
        this.name = Validate.safeTrim(name);
        this.geom = Validate.notBlank(geom, "geom");
        this.level = level;
        this.maxCells = maxCells;
        this.localParams = localParams;
    }

    @Override
    public <R, C> R accept(ClassicalFacetVisitor<R, C> visitor, C ctx) {
        return visitor.visitHeatmap(this, ctx);
    }
}

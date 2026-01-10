package com.solrion.core.api.request.facets.classic;

import com.solrion.core.internal.Validate;
import com.solrion.core.query.Expr;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Value
public class TermsFacet implements ClassicFacet {

    String name;
    String field;
    Integer limit;
    Integer minCount;
    String prefix;
    FacetSort sort;
    Integer offset;
    Boolean missing;
    String contains;
    Boolean containsIgnoreCase;
    Expr.LocalParams localParams;

    @Builder
    public TermsFacet(
            String name,
            String field,
            Integer limit,
            Integer minCount,
            String prefix,
            FacetSort sort,
            Integer offset,
            Boolean missing,
            String contains,
            Boolean containsIgnoreCase,
            Expr.LocalParams localParams
    ) {
        // identity
        field = Validate.notBlank(field, "field");
        name = Validate.safeTrim(name);

        // numeric constraints
        if (limit != null) Validate.require(limit != 0, "limit cannot be 0");
        if (minCount != null) Validate.require(minCount >= 0, "minCount must be >= 0");
        if (offset != null) Validate.require(offset >= 0, "offset must be >= 0");

        // string normalization
        prefix = Validate.safeTrim(prefix);
        contains = Validate.safeTrim(contains);

        // logical constraints
        if (containsIgnoreCase != null && containsIgnoreCase && contains == null) {
            throw new IllegalArgumentException(
                    "containsIgnoreCase=true requires contains to be set"
            );
        }

        // defaults
        sort = sort == null ? FacetSort.COUNT : sort;

        // assignment
        this.name = name;
        this.field = field;
        this.limit = limit;
        this.minCount = minCount;
        this.prefix = prefix;
        this.sort = sort;
        this.offset = offset;
        this.missing = missing;
        this.contains = contains;
        this.containsIgnoreCase = containsIgnoreCase;
        this.localParams = localParams;
    }

    public static TermsFacet ofField(String field) {
        return TermsFacet.builder()
                .field(field)
                .build();
    }

    public static TermsFacet ofField(String name, String field) {
        return TermsFacet.builder()
                .name(name)
                .field(field)
                .build();
    }

    @Override
    public <R, C> R accept(ClassicalFacetVisitor<R, C> visitor, C ctx) {
        return visitor.visitTerms(this, ctx);
    }
}

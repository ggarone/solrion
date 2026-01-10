package com.solrion.core.api.request.facets.json;

import com.solrion.core.internal.Validate;
import lombok.Value;
import lombok.experimental.Accessors;
import lombok.Builder;

import java.util.Map;

@Value
@Accessors(fluent = true)
public class JsonFuncFacet implements JsonFacet {

    String func;
    Map<String, JsonFacet> facets;
    Map<String, Object> rawOptions;

    @Builder
    public JsonFuncFacet(
            String func,
            Map<String, JsonFacet> facets,
            Map<String, Object> rawOptions
    ) {
        this.func = Validate.notBlank(func, "func");
        this.facets = facets == null ? Map.of() : Map.copyOf(facets);
        this.rawOptions = rawOptions == null ? Map.of() : Map.copyOf(rawOptions);
    }

    @Override
    public JsonFacetType type() {
        return JsonFacetType.FUNC;
    }

    @Override
    public <R, C> R accept(JsonFacetVisitor<R, C> visitor, C ctx) {
        return visitor.visitFunc(this, ctx);
    }
}
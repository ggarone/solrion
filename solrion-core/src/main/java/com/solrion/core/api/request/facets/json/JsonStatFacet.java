package com.solrion.core.api.request.facets.json;

import com.solrion.core.internal.Validate;
import lombok.Value;
import lombok.experimental.Accessors;
import lombok.Builder;

import java.util.Map;

@Value
@Accessors(fluent = true)
public class JsonStatFacet implements JsonFacet {

    String func;
    Map<String, Object> rawOptions;

    @Builder
    public JsonStatFacet(
            String func,
            Map<String, Object> rawOptions
    ) {
        this.func = Validate.notBlank(func, "func");
        this.rawOptions = rawOptions == null ? Map.of() : Map.copyOf(rawOptions);
    }

    @Override
    public JsonFacetType type() {
        return JsonFacetType.STAT;
    }

    @Override
    public Map<String, JsonFacet> facets() {
        return Map.of();
    }

    @Override
    public <R, C> R accept(JsonFacetVisitor<R, C> visitor, C ctx) {
        return visitor.visitStat(this, ctx);
    }
}

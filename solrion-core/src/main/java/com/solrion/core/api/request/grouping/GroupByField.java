package com.solrion.core.api.request.grouping;

import com.solrion.core.internal.Validate;

import java.util.List;

public record GroupByField(List<String> fields) implements GroupingTarget {

    public GroupByField {
        fields = Validate.notEmpty(fields, "fields");
        fields.forEach(f -> Validate.notBlank(f, "field"));
        fields = List.copyOf(fields);
    }
}

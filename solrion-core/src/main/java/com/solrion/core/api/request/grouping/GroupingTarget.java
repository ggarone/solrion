package com.solrion.core.api.request.grouping;

public sealed interface GroupingTarget permits GroupByField, GroupByQuery, GroupByFunc {}

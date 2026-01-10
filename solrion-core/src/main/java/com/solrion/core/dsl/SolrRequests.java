package com.solrion.core.dsl;

import com.solrion.core.api.request.update.*;
import com.solrion.core.dsl.builder.SelectRequestBuilder;
import com.solrion.core.dsl.builder.UpdateRequestBuilder;
import com.solrion.core.query.Expr;

import java.util.ArrayList;
import java.util.List;

/**
 * Entry point for building Solr requests in a fluent and structured way.
 */
public final class SolrRequests {

    private SolrRequests() {}

    public static SelectRequestBuilder select() {
        return new SelectRequestBuilder();
    }

    public static SelectRequestBuilder select(String collection) {
        return select().collection(collection);
    }

    public static AddDocumentRequestBuilder add() {
        return new AddDocumentRequestBuilder();
    }

    public static AddDocumentRequestBuilder add(String collection) {
        return add().collection(collection);
    }

    public static DeleteByIdRequestBuilder deleteById() {
        return new DeleteByIdRequestBuilder();
    }

    public static DeleteByIdRequestBuilder deleteById(String collection) {
        return deleteById().collection(collection);
    }

    public static DeleteByQueryRequestBuilder deleteByQuery() {
        return new DeleteByQueryRequestBuilder();
    }

    public static DeleteByQueryRequestBuilder deleteByQuery(String collection) {
        return new DeleteByQueryRequestBuilder().collection(collection);
    }

    public static CommitRequestBuilder commit() {
        return new CommitRequestBuilder();
    }

    public static CommitRequestBuilder commit(String collection) {
        return new CommitRequestBuilder().collection(collection);
    }

    // --- perBatchUpdate builders --------------------------------------------------

    public static final class AddDocumentRequestBuilder extends UpdateRequestBuilder<AddDocumentRequestBuilder> {
        private List<SolrDocument> docs = new ArrayList<>();

        public AddDocumentRequestBuilder document(SolrDocument document) {
            if(document != null) this.docs.add(document);
            return this;
        }

        public AddDocumentRequestBuilder documents(List<SolrDocument> docs) {
            if(docs != null) docs.forEach(this::document);
            return this;
        }

        public AddDocumentsRequest build() {
            return new AddDocumentsRequest(collection, transportOptions, docs, buildUpdateOptions());
        }
    }

    public static final class DeleteByIdRequestBuilder extends UpdateRequestBuilder<DeleteByIdRequestBuilder> {
        private List<String> ids = List.of();

        public DeleteByIdRequestBuilder ids(List<String> ids) {
            this.ids = ids;
            return this;
        }

        public DeleteByIdRequest build() {
            return new DeleteByIdRequest(collection, transportOptions, ids, buildUpdateOptions());
        }
    }

    public static final class DeleteByQueryRequestBuilder extends UpdateRequestBuilder<DeleteByQueryRequestBuilder> {
        private Expr.BoolExpr query = Expr.empty();

        public DeleteByQueryRequestBuilder query(Expr.BoolExpr query) {
            this.query = query;
            return this;
        }

        public DeleteByQueryRequest build() {
            return new DeleteByQueryRequest(collection, transportOptions, query, buildUpdateOptions());
        }
    }

    public static final class CommitRequestBuilder extends UpdateRequestBuilder<CommitRequestBuilder> {

        public CommitRequest build() {
            return new CommitRequest(collection, transportOptions, buildUpdateOptions());
        }
    }
}

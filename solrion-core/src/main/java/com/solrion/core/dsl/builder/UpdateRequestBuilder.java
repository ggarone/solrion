package com.solrion.core.dsl.builder;

import com.solrion.core.api.request.update.UpdateOptions;
import com.solrion.core.client.HttpTransportOptions;
import com.solrion.core.dsl.builder.AbstractRequestBuilder;

public abstract class UpdateRequestBuilder<T extends UpdateRequestBuilder<T>>
        extends AbstractRequestBuilder<UpdateRequestBuilder<T>> {
    protected Boolean commit;
    protected Boolean softCommit;
    protected Boolean overwrite;
    protected Long commitWithinMs;
    protected Boolean waitSearcher;
    protected Boolean expungeDeletes;
    protected Boolean openSearcher;

    @SuppressWarnings("unchecked")
    @Override
    public T collection(String collection) {
        this.collection = collection;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T transport(HttpTransportOptions transportOptions) {
        this.transportOptions = transportOptions;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T commit() {
        this.commit = true;
        this.commitWithinMs = null;
        this.softCommit = null;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T softCommit() {
        this.softCommit = true;
        this.commitWithinMs = null;
        this.commit = null;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T commitWithinMs(Long ms) {
        this.commitWithinMs = ms;
        this.commit =  null;
        this.softCommit = null;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T overwrite() {
        this.overwrite = true;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T waitSearcher() {
        this.waitSearcher = true;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T expungeDeletes() {
        this.expungeDeletes = true;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T openSearcher() {
        this.openSearcher = true;
        return (T) this;
    }

    public UpdateOptions buildUpdateOptions() {
        return new UpdateOptions(commit, softCommit, overwrite, commitWithinMs, waitSearcher, expungeDeletes, openSearcher);
    }
}
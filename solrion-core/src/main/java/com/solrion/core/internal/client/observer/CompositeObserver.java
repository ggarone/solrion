package com.solrion.core.internal.client.observer;

import com.solrion.core.client.SolrClientObserver;

import java.util.List;

public final class CompositeObserver implements SolrClientObserver {

    private final List<SolrClientObserver> delegates;

    public CompositeObserver(List<SolrClientObserver> delegates) {
        this.delegates = delegates;
    }

    @Override
    public void onRequestStart(RequestStart ctx) {
        for (SolrClientObserver o : delegates) {
            safe(() -> o.onRequestStart(ctx));
        }
    }

    @Override
    public void onRequestSuccess(RequestEnd ctx) {
        for (SolrClientObserver o : delegates) {
            safe(() -> o.onRequestSuccess(ctx));
        }
    }

    @Override
    public void onRequestFailure(RequestEnd ctx) {
        for (SolrClientObserver o : delegates) {
            safe(() -> o.onRequestFailure(ctx));
        }
    }

    private static void safe(Runnable r) {
        try {
            r.run();
        } catch (Throwable ignore) {
        }
    }
}


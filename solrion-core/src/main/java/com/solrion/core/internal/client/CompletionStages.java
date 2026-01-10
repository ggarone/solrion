package com.solrion.core.internal.client;

import java.util.concurrent.CompletionException;

public final class CompletionStages {

    private CompletionStages() {}

    public static Throwable unwrap(Throwable t) {
        if (t instanceof CompletionException ce && ce.getCause() != null) {
            return ce.getCause();
        }
        return t;
    }
}

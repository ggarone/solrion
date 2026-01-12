package com.solrion.core.internal.client.observer;

import com.solrion.core.client.SolrClientObserver;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public final class ClientObservers {

  private ClientObservers() {}

  private static final class Holder {
    static final SolrClientObserver INSTANCE = load();
  }

  public static SolrClientObserver get() {
    return Holder.INSTANCE;
  }

  private static SolrClientObserver load() {
    ServiceLoader<SolrClientObserver> loader = ServiceLoader.load(SolrClientObserver.class);

    List<SolrClientObserver> observers = new ArrayList<>();
    loader.iterator().forEachRemaining(observers::add);

    if (observers.isEmpty()) {
      return SolrClientObserver.noop();
    }

    return new CompositeObserver(observers);
  }
}

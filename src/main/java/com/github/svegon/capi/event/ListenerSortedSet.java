package com.github.svegon.capi.event;

import java.util.SortedSet;
import java.util.function.Function;

public class ListenerSortedSet<L> extends ListenerCollection<L, SortedSet<L>> {
    ListenerSortedSet(SortedSet<L> listeners, Function<SortedSet<L>, L> invokerFactory) {
        super(listeners, invokerFactory);
    }
}

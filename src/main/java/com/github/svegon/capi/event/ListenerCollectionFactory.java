package com.github.svegon.capi.event;

import com.github.svegon.utils.collections.ListUtil;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.*;
import java.util.function.Function;

public final class ListenerCollectionFactory {
    private ListenerCollectionFactory() {
        throw new UnsupportedOperationException();
    }

    public static <L> ListenerList<L> listenersVector(Function<List<L>, L> invokerFactory) {
        return new ListenerList<>(ListUtil.newSyncedList(), Preconditions.checkNotNull(invokerFactory));
    }

    public static <L> ListenerList<L> listenersArrayList(Function<List<L>, L> invokerFactory) {
        return new ListenerList<>(ListUtil.newExposedArrayList(),
                Preconditions.checkNotNull(invokerFactory));
    }

    public static <L> ListenerList<L> listenersLinkedList(Function<List<L>, L> invokerFactory) {
        return new ListenerList<>(Collections.synchronizedList(Lists.newLinkedList()),
                Preconditions.checkNotNull(invokerFactory));
    }

    public static <L> ListenerSet<L> listenersHashSet(Function<Set<L>, L> invokerFactory) {
        return new ListenerSet<>(Sets.newConcurrentHashSet(), Preconditions.checkNotNull(invokerFactory));
    }

    public static <L> ListenerSet<L> listenersLinkedSet(Function<Set<L>, L> invokerFactory) {
        return new ListenerSet<>(Collections.synchronizedSet(Sets.newLinkedHashSet()),
                Preconditions.checkNotNull(invokerFactory));
    }

    public static <L> ListenerSortedSet<L> listenersSortedSet(Function<SortedSet<L>, L> invokerFactory,
                                                               Comparator<? super L> comparator) {
        return new ListenerSortedSet<>(Collections.synchronizedSortedSet(Sets.newTreeSet(comparator)),
                Preconditions.checkNotNull(invokerFactory));
    }
}

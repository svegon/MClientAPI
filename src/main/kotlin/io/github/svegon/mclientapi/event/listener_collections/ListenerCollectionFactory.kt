package io.github.svegon.mclientapi.event.listener_collections

import com.google.common.collect.Lists
import com.google.common.collect.Sets
import java.util.*

object ListenerCollectionFactory {
    fun <L> listenersVector(invokerFactory: (List<L>) -> L): ListenerList<L> {
        return ListenerList(Vector(), invokerFactory)
    }

    fun <L> listenersArrayList(invokerFactory: (List<L>) -> L): ListenerList<L> {
        return ListenerList(Collections.synchronizedList(Lists.newArrayList()), invokerFactory)
    }

    fun <L> listenersLinkedList(invokerFactory: (List<L>) -> L): ListenerList<L> {
        return ListenerList(
            Collections.synchronizedList(Lists.newLinkedList()),
            invokerFactory
        )
    }

    fun <L : Any> listenersHashSet(invokerFactory: (Set<L>) -> L): ListenerSet<L> {
        return ListenerSet<L>(Sets.newConcurrentHashSet(), invokerFactory)
    }

    fun <L> listenersLinkedSet(invokerFactory: (Set<L>) -> L): ListenerSet<L> {
        return ListenerSet(
            Collections.synchronizedSet(Sets.newLinkedHashSet()),
            invokerFactory
        )
    }

    fun <L> listenersSortedSet(
        comparator: Comparator<in L>,
        invokerFactory: (SortedSet<L>) -> L
    ): ListenerSortedSet<L> {
        return ListenerSortedSet(
            Collections.synchronizedSortedSet(Sets.newTreeSet(comparator)),
            invokerFactory
        )
    }
}

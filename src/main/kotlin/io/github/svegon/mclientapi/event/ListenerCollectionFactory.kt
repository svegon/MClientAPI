package io.github.svegon.capi.event

import com.google.common.base.Preconditions
import com.google.common.collect.Lists
import com.google.common.collect.Sets
import io.github.svegon.capi.event.ListenerSet
import io.github.svegon.utils.collections.ListUtil
import java.util.*
import java.util.function.Function

class ListenerCollectionFactory private constructor() {
    init {
        throw UnsupportedOperationException()
    }

    companion object {
        fun <L> listenersVector(invokerFactory: Function<List<L>, L>?): ListenerList<L> {
            return ListenerList(ListUtil.newSyncedList(), Preconditions.checkNotNull(invokerFactory))
        }

        fun <L> listenersArrayList(invokerFactory: Function<List<L>?, L>?): ListenerList<L> {
            return ListenerList(
                ListUtil.newExposedArrayList(),
                Preconditions.checkNotNull(invokerFactory)
            )
        }

        fun <L> listenersLinkedList(invokerFactory: Function<List<L>?, L>?): ListenerList<L> {
            return ListenerList(
                Collections.synchronizedList(Lists.newLinkedList<Any?>()),
                Preconditions.checkNotNull(invokerFactory)
            )
        }

        fun <L> listenersHashSet(invokerFactory: Function<Set<L>?, L>?): ListenerSet<L> {
            return ListenerSet(Sets.newConcurrentHashSet(), Preconditions.checkNotNull(invokerFactory))
        }

        fun <L> listenersLinkedSet(invokerFactory: Function<Set<L>, L>?): ListenerSet<L> {
            return ListenerSet(
                Collections.synchronizedSet(Sets.newLinkedHashSet<Any?>()),
                Preconditions.checkNotNull(invokerFactory)
            )
        }

        fun <L> listenersSortedSet(
            invokerFactory: Function<SortedSet<L>?, L>?,
            comparator: Comparator<in L>?
        ): ListenerSortedSet<L> {
            return ListenerSortedSet(
                Collections.synchronizedSortedSet(Sets.newTreeSet(comparator)),
                Preconditions.checkNotNull(invokerFactory)
            )
        }
    }
}

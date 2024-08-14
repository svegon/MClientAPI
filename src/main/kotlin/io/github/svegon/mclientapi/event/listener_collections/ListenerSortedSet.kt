package io.github.svegon.mclientapi.event.listener_collections

import java.util.*

class ListenerSortedSet<L> internal constructor(listeners: SortedSet<L>,
                                                invokerFactory: (SortedSet<L>) -> L) :
    ListenerCollection<L, SortedSet<L>, SortedSet<L>>(listeners, invokerFactory) {
    override fun makeUnmodifiable(listeners: SortedSet<L>): SortedSet<L> {
        return Collections.unmodifiableSortedSet(listeners)
    }
}

package io.github.svegon.mclientapi.event.listener_collections

import java.util.*

class ListenerSet<L> internal constructor(listeners: MutableSet<L>, invokerFactory: (Set<L>) -> L) :
    ListenerCollection<L, MutableSet<L>, Set<L>>(listeners, invokerFactory) {
    override fun makeUnmodifiable(listeners: MutableSet<L>): Set<L> {
        return Collections.unmodifiableSet(listeners)
    }
}

package io.github.svegon.mclientapi.event.listener_collections

import java.util.Collections

class ListenerList<L> internal constructor(listeners: MutableList<L>, invokerFactory: (List<L>) -> L) :
    ListenerCollection<L, MutableList<L>, List<L>>(listeners, invokerFactory) {
    override fun makeUnmodifiable(listeners: MutableList<L>): List<L> {
        return Collections.unmodifiableList(listeners)
    }
}

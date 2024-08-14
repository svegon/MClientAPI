package io.github.svegon.mclientapi.event.listener_collections

import java.util.*

abstract class ListenerCollection<L, C : MutableCollection<L>, I : Collection<L>> internal constructor(
    private val listeners: C,
    invokerFactory: (I) -> L
) {
    private val invoker = invokerFactory(makeUnmodifiable(listeners))

    fun invoker(): L {
        return invoker
    }

    fun register(listener: L): Boolean {
        return listeners.add(listener)
    }

    fun unregister(listener: L): Boolean {
        return listeners.remove(listener)
    }

    protected abstract fun makeUnmodifiable(listeners: C): I
}

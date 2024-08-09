package io.github.svegon.mclientapi.event

abstract class ListenerCollection<L, C : MutableCollection<L>> internal constructor(
    private val listeners: C,
    invokerFactory: (C) -> L
) {
    private val invoker = invokerFactory(this.listeners)

    fun invoker(): L {
        return invoker
    }

    fun register(listener: L): Boolean {
        return listeners.add(listener)
    }

    fun unregister(listener: L): Boolean {
        return listeners.remove(listener)
    }
}

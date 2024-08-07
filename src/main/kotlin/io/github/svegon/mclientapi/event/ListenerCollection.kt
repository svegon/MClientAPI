package io.github.svegon.capi.event

import com.google.common.base.Preconditions
import java.util.function.Function

abstract class ListenerCollection<L, C : Collection<L>?> internal constructor(
    private val listeners: C,
    invokerFactory: Function<C, L>
) {
    private val invoker = invokerFactory.apply(this.listeners)

    fun invoker(): L {
        return invoker
    }

    fun register(listener: L): Boolean {
        return listeners.add(Preconditions.checkNotNull<L>(listener))
    }

    fun unregister(listener: L): Boolean {
        return listeners.remove(listener)
    }
}

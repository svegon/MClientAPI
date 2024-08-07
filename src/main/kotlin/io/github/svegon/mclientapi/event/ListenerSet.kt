package io.github.svegon.capi.event

import java.util.function.Function

class ListenerSet<L> internal constructor(listeners: Set<L>, invokerFactory: Function<Set<L>, L>) :
    ListenerCollection<L, Set<L>?>(listeners, invokerFactory)

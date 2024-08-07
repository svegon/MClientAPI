package io.github.svegon.capi.event

import java.util.function.Function

class ListenerList<L> internal constructor(listeners: List<L>, invokerFactory: Function<List<L>, L>) :
    ListenerCollection<L, List<L>?>(listeners, invokerFactory)

package io.github.svegon.capi.event

import java.util.*
import java.util.function.Function

class ListenerSortedSet<L> internal constructor(listeners: SortedSet<L>, invokerFactory: Function<SortedSet<L>, L>) :
    ListenerCollection<L, SortedSet<L>?>(listeners, invokerFactory)

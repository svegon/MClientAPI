package io.github.svegon.mclientapi.event

import java.util.*

class ListenerSortedSet<L> internal constructor(listeners: SortedSet<L>,
                                                invokerFactory: (SortedSet<L>) -> L) :
    ListenerCollection<L, SortedSet<L>>(listeners, invokerFactory)

package io.github.svegon.mclientapi.event

class ListenerSet<L> internal constructor(listeners: MutableSet<L>, invokerFactory: (MutableSet<L>) -> L) :
    ListenerCollection<L, MutableSet<L>>(listeners, invokerFactory)

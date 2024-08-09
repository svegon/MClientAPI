package io.github.svegon.mclientapi.event

class ListenerList<L> internal constructor(listeners: MutableList<L>, invokerFactory: (MutableList<L>) -> L) :
    ListenerCollection<L, MutableList<L>>(listeners, invokerFactory)

package io.github.svegon.capi.event;

import java.util.Set;
import java.util.function.Function;

public class ListenerSet<L> extends ListenerCollection<L, Set<L>> {
    ListenerSet(Set<L> listeners, Function<Set<L>, L> invokerFactory) {
        super(listeners, invokerFactory);
    }
}

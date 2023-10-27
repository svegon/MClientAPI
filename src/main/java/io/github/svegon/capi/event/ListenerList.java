package io.github.svegon.capi.event;

import java.util.List;
import java.util.function.Function;

public class ListenerList<L> extends ListenerCollection<L, List<L>> {
    ListenerList(List<L> listeners, Function<List<L>, L> invokerFactory) {
        super(listeners, invokerFactory);
    }
}

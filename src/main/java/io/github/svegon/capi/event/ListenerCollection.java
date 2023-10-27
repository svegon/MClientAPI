package io.github.svegon.capi.event;

import com.google.common.base.Preconditions;

import java.util.Collection;
import java.util.function.Function;

public abstract class ListenerCollection<L, C extends Collection<L>> {
    private final C listeners;
    private final L invoker;

    ListenerCollection(C listeners, Function<C, L> invokerFactory) {
        this.listeners = listeners;
        this.invoker = invokerFactory.apply(this.listeners);
    }

    public L invoker() {
        return invoker;
    }

    public boolean register(L listener) {
        return listeners.add(Preconditions.checkNotNull(listener));
    }

    public boolean unregister(L listener) {
        return listeners.remove(listener);
    }
}

package io.github.svegon.utils.fast.util.objects.transform;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

import java.util.Set;
import java.util.function.Predicate;

public abstract class TransformingObjectSet<T, S extends Set<T>, E> extends AbstractObjectSet<E> {
    protected final S set;

    public TransformingObjectSet(S set) {
        this.set = Preconditions.checkNotNull(set);
    }

    @Override
    public final void clear() {
        set.clear();
    }

    @Override
    public abstract ObjectIterator<E> iterator();

    @Override
    public abstract boolean contains(Object o);

    @Override
    public abstract boolean removeIf(Predicate<? super E> filter);

    @Override
    public final boolean isEmpty() {
        return set.isEmpty();
    }

    @Override
    public final int size() {
        return set.size();
    }
}

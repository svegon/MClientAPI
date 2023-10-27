package io.github.svegon.utils.fast.util.objects.transform;

import io.github.svegon.utils.fast.util.objects.AbstractObjectMultiset;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

import java.util.Collection;
import java.util.function.Predicate;

public abstract class TransformingObjectMultiset<T, C extends Collection<T>, E> extends AbstractObjectMultiset<E> {
    protected final C col;

    public TransformingObjectMultiset(C col) {
        this.col = Preconditions.checkNotNull(col);
    }

    @Override
    public final void clear() {
        col.clear();
    }

    @Override
    public abstract ObjectIterator<E> iterator();

    @Override
    public abstract boolean contains(Object o);

    @Override
    public abstract boolean removeIf(Predicate<? super E> filter);

    @Override
    public final boolean isEmpty() {
        return col.isEmpty();
    }

    @Override
    public final int size() {
        return col.size();
    }
}

package io.github.svegon.utils.fast.util.shorts.transform;

import io.github.svegon.utils.fast.util.shorts.AbstractShortMultiset;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.shorts.ShortIterator;
import it.unimi.dsi.fastutil.shorts.ShortPredicate;

import java.util.Collection;

public abstract class TransformingShortMultiset<T, C extends Collection<T>> extends AbstractShortMultiset {
    protected final C col;

    public TransformingShortMultiset(C col) {
        this.col = Preconditions.checkNotNull(col);
    }

    @Override
    public final void clear() {
        col.clear();
    }

    @Override
    public abstract ShortIterator iterator();

    @Override
    public abstract boolean contains(short value);

    @Override
    public abstract boolean removeIf(ShortPredicate filter);

    @Override
    public final boolean isEmpty() {
        return col.isEmpty();
    }

    @Override
    public final int size() {
        return col.size();
    }
}

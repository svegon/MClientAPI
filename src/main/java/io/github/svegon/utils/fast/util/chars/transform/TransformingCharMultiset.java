package io.github.svegon.utils.fast.util.chars.transform;


import io.github.svegon.utils.fast.util.chars.AbstractCharMultiset;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.chars.CharIterator;
import it.unimi.dsi.fastutil.chars.CharPredicate;

import java.util.Collection;

public abstract class TransformingCharMultiset<T, C extends Collection<T>> extends AbstractCharMultiset {
    protected final C col;

    public TransformingCharMultiset(C col) {
        this.col = Preconditions.checkNotNull(col);
    }

    @Override
    public final void clear() {
        col.clear();
    }

    @Override
    public abstract CharIterator iterator();

    @Override
    public abstract boolean contains(char value);

    @Override
    public abstract boolean removeIf(CharPredicate filter);

    @Override
    public final boolean isEmpty() {
        return col.isEmpty();
    }

    @Override
    public final int size() {
        return col.size();
    }
}

package com.github.svegon.utils.fast.util.longs.transform;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.longs.AbstractLongList;
import it.unimi.dsi.fastutil.longs.LongListIterator;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.LongPredicate;

public abstract class TransformingLongSequentialList<E, L extends List<E>> extends AbstractLongList {
    protected final L list;

    public TransformingLongSequentialList(L list) {
        this.list = Preconditions.checkNotNull(list);
    }

    @Override
    public final void clear() {
        list.clear();
    }

    @Override
    public final boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public final long getLong(int index) {
        return listIterator(index).nextLong();
    }

    @Override
    public final @NotNull LongListIterator iterator() {
        return listIterator();
    }

    @Override
    public final int size() {
        return list.size();
    }

    @Override
    public abstract LongListIterator listIterator(int index);

    @Override
    public abstract boolean removeIf(final LongPredicate filter);

    @Override
    public abstract long removeLong(int index);
}

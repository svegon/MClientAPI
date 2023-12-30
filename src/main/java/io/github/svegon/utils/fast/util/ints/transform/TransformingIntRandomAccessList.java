package io.github.svegon.utils.fast.util.ints.transform;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.AbstractIntList;
import it.unimi.dsi.fastutil.ints.IntListIterator;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.RandomAccess;
import java.util.function.IntPredicate;

public abstract class TransformingIntRandomAccessList<E, L extends List<E>> extends AbstractIntList implements RandomAccess {
    protected final L list;

    public TransformingIntRandomAccessList(L list) {
        this.list = Preconditions.checkNotNull(list);
    }

    @Override
    public final void clear() {
        list.clear();
    }

    public abstract int getInt(int index);

    @Override
    public final @NotNull IntListIterator iterator() {
        return this.listIterator();
    }

    @Override
    public final boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public abstract boolean removeIf(final IntPredicate filter);

    @Override
    public final int removeInt(int index) {
        int ret = getInt(index);
        list.remove(index);
        return ret;
    }

    @Override
    public final int size() {
        return list.size();
    }
}

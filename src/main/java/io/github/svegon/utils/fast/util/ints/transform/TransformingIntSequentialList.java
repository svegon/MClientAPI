package io.github.svegon.utils.fast.util.ints.transform;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.AbstractIntList;
import it.unimi.dsi.fastutil.ints.IntListIterator;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.IntPredicate;

public abstract class TransformingIntSequentialList<E, L extends List<E>> extends AbstractIntList {
    protected final L list;

    public TransformingIntSequentialList(L list) {
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
    public final int getInt(int index) {
        return listIterator(index).nextInt();
    }

    @Override
    public final @NotNull IntListIterator iterator() {
        return listIterator();
    }

    @Override
    public final int size() {
        return list.size();
    }

    @Override
    public abstract IntListIterator listIterator(int index);

    @Override
    public abstract boolean removeIf(final IntPredicate filter);

    @Override
    public abstract int removeInt(int index);
}

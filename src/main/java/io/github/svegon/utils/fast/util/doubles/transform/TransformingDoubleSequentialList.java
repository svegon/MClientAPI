package io.github.svegon.utils.fast.util.doubles.transform;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.doubles.AbstractDoubleList;
import it.unimi.dsi.fastutil.doubles.DoubleListIterator;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.DoublePredicate;

public abstract class TransformingDoubleSequentialList<E, L extends List<E>> extends AbstractDoubleList {
    protected final L list;

    public TransformingDoubleSequentialList(L list) {
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
    public final double getDouble(int index) {
        return listIterator(index).nextDouble();
    }

    @Override
    public final @NotNull DoubleListIterator iterator() {
        return listIterator();
    }

    @Override
    public final int size() {
        return list.size();
    }

    @Override
    public abstract DoubleListIterator listIterator(int index);

    @Override
    public abstract boolean removeIf(final DoublePredicate filter);

    @Override
    public abstract double removeDouble(int index);
}

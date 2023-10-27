package io.github.svegon.utils.fast.util.doubles.transform;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.doubles.AbstractDoubleList;
import it.unimi.dsi.fastutil.doubles.DoubleListIterator;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.RandomAccess;
import java.util.function.DoublePredicate;

public abstract class TransformingDoubleRandomAccessList<E, L extends List<E>> extends AbstractDoubleList
        implements RandomAccess {
    protected final L list;

    protected TransformingDoubleRandomAccessList(L list) {
        this.list = Preconditions.checkNotNull(list);
    }

    @Override
    public final void clear() {
        list.clear();
    }

    public abstract double getDouble(int index);

    @Override
    public final @NotNull DoubleListIterator iterator() {
        return this.listIterator();
    }

    @Override
    public final boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public abstract boolean removeIf(final DoublePredicate filter);

    @Override
    public final double removeDouble(int index) {
        double ret = getDouble(index);
        list.remove(index);
        return ret;
    }

    @Override
    public final int size() {
        return list.size();
    }
}

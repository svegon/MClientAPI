package com.github.svegon.utils.fast.util.floats.transform;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.floats.AbstractFloatList;
import it.unimi.dsi.fastutil.floats.FloatListIterator;
import it.unimi.dsi.fastutil.floats.FloatPredicate;
import it.unimi.dsi.fastutil.shorts.AbstractShortList;
import it.unimi.dsi.fastutil.shorts.ShortListIterator;
import it.unimi.dsi.fastutil.shorts.ShortPredicate;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.RandomAccess;

public abstract class TransformingFloatRandomAccessList<E, L extends List<E>> extends AbstractFloatList
        implements RandomAccess {
    protected final L list;

    protected TransformingFloatRandomAccessList(L list) {
        this.list = Preconditions.checkNotNull(list);
    }

    @Override
    public final void clear() {
        list.clear();
    }

    @Override
    public abstract float getFloat(int index);

    @Override
    public final @NotNull FloatListIterator iterator() {
        return this.listIterator();
    }

    @Override
    public final boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public abstract boolean removeIf(final FloatPredicate filter);

    @Override
    public final float removeFloat(int index) {
        float ret = getFloat(index);
        list.remove(index);
        return ret;
    }

    @Override
    public final int size() {
        return list.size();
    }
}

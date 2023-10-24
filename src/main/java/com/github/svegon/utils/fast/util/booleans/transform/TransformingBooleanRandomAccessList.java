package com.github.svegon.utils.fast.util.booleans.transform;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.booleans.AbstractBooleanList;
import it.unimi.dsi.fastutil.booleans.BooleanListIterator;
import it.unimi.dsi.fastutil.booleans.BooleanPredicate;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.RandomAccess;

public abstract class TransformingBooleanRandomAccessList<E, L extends List<E>> extends AbstractBooleanList
        implements RandomAccess {
    protected final L list;

    protected TransformingBooleanRandomAccessList(L list) {
        this.list = Preconditions.checkNotNull(list);
    }

    @Override
    public final void clear() {
        list.clear();
    }

    public abstract boolean getBoolean(int index);

    @Override
    public final @NotNull BooleanListIterator iterator() {
        return this.listIterator();
    }

    @Override
    public final boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public final boolean removeBoolean(int index) {
        boolean ret = getBoolean(index);
        list.remove(index);
        return ret;
    }

    @Override
    public final int size() {
        return list.size();
    }
}

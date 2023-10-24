package com.github.svegon.utils.fast.util.bytes.transform;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.AbstractByteList;
import it.unimi.dsi.fastutil.bytes.ByteListIterator;
import it.unimi.dsi.fastutil.bytes.BytePredicate;
import it.unimi.dsi.fastutil.doubles.AbstractDoubleList;
import it.unimi.dsi.fastutil.doubles.DoubleListIterator;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.DoublePredicate;

public abstract class TransformingByteSequentialList<E, L extends List<E>> extends AbstractByteList {
    protected final L list;

    public TransformingByteSequentialList(L list) {
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
    public final byte getByte(int index) {
        return listIterator(index).nextByte();
    }

    @Override
    public final @NotNull ByteListIterator iterator() {
        return listIterator();
    }

    @Override
    public final int size() {
        return list.size();
    }

    @Override
    public abstract ByteListIterator listIterator(int index);

    @Override
    public abstract boolean removeIf(final BytePredicate filter);

    @Override
    public abstract byte removeByte(int index);
}

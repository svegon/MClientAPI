package com.github.svegon.utils.fast.util.chars.transform;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.AbstractByteList;
import it.unimi.dsi.fastutil.bytes.ByteListIterator;
import it.unimi.dsi.fastutil.bytes.BytePredicate;
import it.unimi.dsi.fastutil.chars.AbstractCharList;
import it.unimi.dsi.fastutil.chars.CharListIterator;
import it.unimi.dsi.fastutil.chars.CharPredicate;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.RandomAccess;

public abstract class TransformingCharRandomAccessList<E, L extends List<E>> extends AbstractCharList
        implements RandomAccess {
    protected final L list;

    protected TransformingCharRandomAccessList(L list) {
        this.list = Preconditions.checkNotNull(list);
    }

    @Override
    public final void clear() {
        list.clear();
    }

    @Override
    public abstract char getChar(int index);

    @Override
    public final @NotNull CharListIterator iterator() {
        return this.listIterator();
    }

    @Override
    public final boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public abstract boolean removeIf(final CharPredicate filter);

    @Override
    public final char removeChar(int index) {
        char ret = getChar(index);
        list.remove(index);
        return ret;
    }

    @Override
    public final int size() {
        return list.size();
    }
}

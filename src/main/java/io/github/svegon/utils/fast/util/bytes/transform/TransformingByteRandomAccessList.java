package io.github.svegon.utils.fast.util.bytes.transform;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.AbstractByteList;
import it.unimi.dsi.fastutil.bytes.ByteListIterator;
import it.unimi.dsi.fastutil.bytes.BytePredicate;
import it.unimi.dsi.fastutil.doubles.AbstractDoubleList;
import it.unimi.dsi.fastutil.doubles.DoubleListIterator;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.RandomAccess;
import java.util.function.DoublePredicate;

public abstract class TransformingByteRandomAccessList<E, L extends List<E>> extends AbstractByteList
        implements RandomAccess {
    protected final L list;

    protected TransformingByteRandomAccessList(L list) {
        this.list = Preconditions.checkNotNull(list);
    }

    @Override
    public final void clear() {
        list.clear();
    }

    @Override
    public abstract byte getByte(int index);

    @Override
    public final @NotNull ByteListIterator iterator() {
        return this.listIterator();
    }

    @Override
    public final boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public abstract boolean removeIf(final BytePredicate filter);

    @Override
    public final byte removeByte(int index) {
        byte ret = getByte(index);
        list.remove(index);
        return ret;
    }

    @Override
    public final int size() {
        return list.size();
    }
}

package io.github.svegon.utils.fast.util.longs.transform;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.longs.AbstractLongList;
import it.unimi.dsi.fastutil.longs.LongListIterator;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.RandomAccess;
import java.util.function.LongPredicate;

public abstract class TransformingLongRandomAccessList<E, L extends List<E>> extends AbstractLongList implements RandomAccess {
    protected final L list;

    public TransformingLongRandomAccessList(L list) {
        this.list = Preconditions.checkNotNull(list);
    }

    @Override
    public final void clear() {
        list.clear();
    }

    public abstract long getLong(int index);

    @Override
    public final @NotNull LongListIterator iterator() {
        return this.listIterator();
    }

    @Override
    public final boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public abstract boolean removeIf(final LongPredicate filter);

    @Override
    public final long removeLong(int index) {
        long ret = getLong(index);
        list.remove(index);
        return ret;
    }

    @Override
    public final int size() {
        return list.size();
    }
}

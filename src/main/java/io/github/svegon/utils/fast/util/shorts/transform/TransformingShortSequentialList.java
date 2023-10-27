package io.github.svegon.utils.fast.util.shorts.transform;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.AbstractByteList;
import it.unimi.dsi.fastutil.bytes.ByteListIterator;
import it.unimi.dsi.fastutil.bytes.BytePredicate;
import it.unimi.dsi.fastutil.shorts.AbstractShortList;
import it.unimi.dsi.fastutil.shorts.ShortListIterator;
import it.unimi.dsi.fastutil.shorts.ShortPredicate;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class TransformingShortSequentialList<E, L extends List<E>> extends AbstractShortList {
    protected final L list;

    public TransformingShortSequentialList(L list) {
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
    public final short getShort(int index) {
        return listIterator(index).nextShort();
    }

    @Override
    public final @NotNull ShortListIterator iterator() {
        return listIterator();
    }

    @Override
    public final int size() {
        return list.size();
    }

    @Override
    public abstract ShortListIterator listIterator(int index);

    @Override
    public abstract boolean removeIf(final ShortPredicate filter);

    @Override
    public abstract short removeShort(int index);
}

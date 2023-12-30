package io.github.svegon.utils.fast.util.chars.transform;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.AbstractByteList;
import it.unimi.dsi.fastutil.bytes.ByteListIterator;
import it.unimi.dsi.fastutil.bytes.BytePredicate;
import it.unimi.dsi.fastutil.chars.AbstractCharList;
import it.unimi.dsi.fastutil.chars.CharListIterator;
import it.unimi.dsi.fastutil.chars.CharPredicate;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class TransformingCharSequentialList<E, L extends List<E>> extends AbstractCharList {
    protected final L list;

    public TransformingCharSequentialList(L list) {
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
    public final char getChar(int index) {
        return listIterator(index).nextChar();
    }

    @Override
    public final @NotNull CharListIterator iterator() {
        return listIterator();
    }

    @Override
    public final int size() {
        return list.size();
    }

    @Override
    public abstract CharListIterator listIterator(int index);

    @Override
    public abstract boolean removeIf(final CharPredicate filter);

    @Override
    public abstract char removeChar(int index);
}

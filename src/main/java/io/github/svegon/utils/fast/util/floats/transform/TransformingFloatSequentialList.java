package io.github.svegon.utils.fast.util.floats.transform;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.floats.AbstractFloatList;
import it.unimi.dsi.fastutil.floats.FloatListIterator;
import it.unimi.dsi.fastutil.floats.FloatPredicate;
import it.unimi.dsi.fastutil.shorts.AbstractShortList;
import it.unimi.dsi.fastutil.shorts.ShortListIterator;
import it.unimi.dsi.fastutil.shorts.ShortPredicate;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class TransformingFloatSequentialList<E, L extends List<E>> extends AbstractFloatList {
    protected final L list;

    public TransformingFloatSequentialList(L list) {
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
    public final float getFloat(int index) {
        return listIterator(index).nextFloat();
    }

    @Override
    public final @NotNull FloatListIterator iterator() {
        return listIterator();
    }

    @Override
    public final int size() {
        return list.size();
    }

    @Override
    public abstract FloatListIterator listIterator(int index);

    @Override
    public abstract boolean removeIf(final FloatPredicate filter);

    @Override
    public abstract float removeFloat(int index);
}

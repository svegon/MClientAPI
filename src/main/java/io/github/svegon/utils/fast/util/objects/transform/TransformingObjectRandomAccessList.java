package io.github.svegon.utils.fast.util.objects.transform;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.AbstractObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import it.unimi.dsi.fastutil.shorts.AbstractShortList;
import it.unimi.dsi.fastutil.shorts.ShortListIterator;
import it.unimi.dsi.fastutil.shorts.ShortPredicate;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.RandomAccess;
import java.util.function.Predicate;

public abstract class TransformingObjectRandomAccessList<T, L extends List<T>, E> extends AbstractObjectList<E>
        implements RandomAccess {
    protected final L list;

    protected TransformingObjectRandomAccessList(L list) {
        this.list = Preconditions.checkNotNull(list);
    }

    @Override
    public final void clear() {
        list.clear();
    }

    @Override
    public abstract E get(int index);

    @Override
    public final ObjectListIterator<E> iterator() {
        return listIterator();
    }

    @Override
    public final boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public abstract boolean removeIf(Predicate<? super E> filter);

    @Override
    public final E remove(int i) {
        E ret = get(i);
        list.remove(i);
        return ret;
    }

    @Override
    public final int size() {
        return list.size();
    }
}

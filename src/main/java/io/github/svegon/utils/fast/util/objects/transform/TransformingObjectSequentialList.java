package io.github.svegon.utils.fast.util.objects.transform;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.AbstractObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import it.unimi.dsi.fastutil.shorts.ShortListIterator;
import it.unimi.dsi.fastutil.shorts.ShortPredicate;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.ListIterator;
import java.util.function.Predicate;

public abstract class TransformingObjectSequentialList<T, L extends List<T>, E> extends AbstractObjectList<E> {
    protected final L list;

    public TransformingObjectSequentialList(L list) {
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
    public E get(int index) {
        return listIterator(index).next();
    }

    @Override
    public ObjectListIterator<E> iterator() {
        return listIterator();
    }

    @Override
    public final int size() {
        return list.size();
    }

    @Override
    public abstract ObjectListIterator<E> listIterator(int index);

    @Override
    public abstract boolean removeIf(Predicate<? super E> filter);

    @Override
    public E remove(int i) {
        ListIterator<E> itr = listIterator(i);
        E ret = itr.next();

        itr.remove();

        return ret;
    }
}

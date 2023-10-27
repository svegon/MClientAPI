package io.github.svegon.utils.fast.util.objects.transform;

import it.unimi.dsi.fastutil.objects.ObjectListIterator;

import java.util.ListIterator;

public abstract class TransformingObjectListIterator<T, I extends ListIterator<T>, E>
        extends TransformingObjectIterator<T, I, E> implements ObjectListIterator<E> {
    protected TransformingObjectListIterator(I itr) {
        super(itr);
    }

    @Override
    public final boolean hasPrevious() {
        return itr.hasPrevious();
    }

    @Override
    public final int nextIndex() {
        return itr.nextIndex();
    }

    @Override
    public final int previousIndex() {
        return itr.previousIndex();
    }

    @Override
    public void set(E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(E e) {
        throw new UnsupportedOperationException();
    }
}

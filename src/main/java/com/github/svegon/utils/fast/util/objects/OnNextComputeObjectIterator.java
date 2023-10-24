package com.github.svegon.utils.fast.util.objects;

import com.github.svegon.utils.collections.iteration.OnNextComputeIterator;
import it.unimi.dsi.fastutil.chars.CharIterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

import java.util.NoSuchElementException;

public abstract class OnNextComputeObjectIterator<E> extends OnNextComputeIterator<E> implements ObjectIterator<E> {
    private E next;

    protected abstract E computeNext();

    @Override
    public E next() {
        synchronized (lock) {
            if (hasNext()) {
                super.setState(State.UNPREPARED);
                return next;
            }

            stateError();
            throw new NoSuchElementException();
        }
    }

    @Override
    protected final State getState() {
        throw new IllegalStateException();
    }

    @Override
    protected final void setState(State state) {
        throw new IllegalStateException();
    }

    @Override
    protected final void prepareNext() {
        next = computeNext();
    }
}

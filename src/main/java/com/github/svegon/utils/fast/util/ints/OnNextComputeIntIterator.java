package com.github.svegon.utils.fast.util.ints;

import com.github.svegon.utils.collections.iteration.OnNextComputeIterator;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.longs.LongIterator;

import java.util.NoSuchElementException;

public abstract class OnNextComputeIntIterator extends OnNextComputeIterator<Integer> implements IntIterator {
    private int next;

    protected abstract int computeNext();

    @Override
    public int nextInt() {
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

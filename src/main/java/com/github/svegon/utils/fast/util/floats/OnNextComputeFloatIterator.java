package com.github.svegon.utils.fast.util.floats;

import com.github.svegon.utils.collections.iteration.OnNextComputeIterator;
import it.unimi.dsi.fastutil.floats.FloatIterator;

import java.util.NoSuchElementException;

public abstract class OnNextComputeFloatIterator extends OnNextComputeIterator<Float> implements FloatIterator {
    private float next;

    protected abstract float computeNext();

    @Override
    public float nextFloat() {
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

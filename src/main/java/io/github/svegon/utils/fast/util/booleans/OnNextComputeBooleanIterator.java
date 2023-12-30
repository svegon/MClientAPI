package io.github.svegon.utils.fast.util.booleans;

import io.github.svegon.utils.collections.iteration.OnNextComputeIterator;
import it.unimi.dsi.fastutil.booleans.BooleanIterator;

import java.util.NoSuchElementException;

public abstract class OnNextComputeBooleanIterator extends OnNextComputeIterator<Boolean> implements BooleanIterator {
    private boolean next;

    protected abstract boolean computeNext();

    @Override
    public boolean nextBoolean() {
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

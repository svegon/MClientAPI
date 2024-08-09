package io.github.svegon.utils.fast.util.longs;

import io.github.svegon.utils.collections.iteration.OnNextComputeIterator;
import it.unimi.dsi.fastutil.longs.LongIterator;

import java.util.NoSuchElementException;

public abstract class OnNextComputeLongIterator extends OnNextComputeIterator<Long> implements LongIterator {
    private long next;

    protected abstract long computeNext();

    @Override
    public long nextLong() {
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

package io.github.svegon.utils.fast.util.shorts;

import io.github.svegon.utils.collections.iteration.OnNextComputeIterator;
import it.unimi.dsi.fastutil.shorts.ShortIterator;

import java.util.NoSuchElementException;

public abstract class OnNextComputeShortIterator extends OnNextComputeIterator<Short> implements ShortIterator {
    private short next;

    protected abstract short computeNext();

    @Override
    public short nextShort() {
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

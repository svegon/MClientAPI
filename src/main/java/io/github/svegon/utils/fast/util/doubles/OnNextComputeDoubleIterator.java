package io.github.svegon.utils.fast.util.doubles;

import io.github.svegon.utils.collections.iteration.OnNextComputeIterator;
import it.unimi.dsi.fastutil.doubles.DoubleIterator;

import java.util.NoSuchElementException;

public abstract class OnNextComputeDoubleIterator extends OnNextComputeIterator<Double> implements DoubleIterator {
    private double next;

    protected abstract double computeNext();

    @Override
    public double nextDouble() {
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

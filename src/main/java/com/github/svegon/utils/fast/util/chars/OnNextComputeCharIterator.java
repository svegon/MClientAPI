package com.github.svegon.utils.fast.util.chars;

import com.github.svegon.utils.collections.iteration.OnNextComputeIterator;
import it.unimi.dsi.fastutil.chars.CharIterator;

import java.util.NoSuchElementException;

public abstract class OnNextComputeCharIterator extends OnNextComputeIterator<Character> implements CharIterator {
    private char next;

    protected abstract char computeNext();

    @Override
    public char nextChar() {
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

package com.github.svegon.utils.fast.util.bytes;

import com.github.svegon.utils.collections.iteration.OnNextComputeIterator;
import it.unimi.dsi.fastutil.bytes.ByteIterator;

import java.util.NoSuchElementException;

public abstract class OnNextComputeByteIterator extends OnNextComputeIterator<Byte> implements ByteIterator {
    private byte next;

    protected abstract byte computeNext();

    @Override
    public byte nextByte() {
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

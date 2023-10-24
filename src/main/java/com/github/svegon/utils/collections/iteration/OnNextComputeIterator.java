package com.github.svegon.utils.collections.iteration;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class OnNextComputeIterator<E> implements Iterator<E> {
    protected final Object lock;
    private State state = State.UNPREPARED;
    private RuntimeException error;

    protected OnNextComputeIterator() {
        this.lock = this;
    }

    protected OnNextComputeIterator(Object syncLock) {
        this.lock = Preconditions.checkNotNull(syncLock);
    }

    @Override
    public final boolean hasNext() {
        synchronized (lock) {
            if (state == State.UNPREPARED) {
                tryComputeNext();
            }

            return state == State.READY;
        }
    }

    protected final void finish() {
        state = State.FINISHED;
    }

    protected final void stateError() {
        if (error != null) {
            throw error;
        }
    }

    private void tryComputeNext() {
        synchronized (lock) {
            try {
                prepareNext();

                if (state == State.UNPREPARED) {
                    state = State.READY;
                }
            } catch (RuntimeException e) {
                error = e;
                state = State.ERROR;
            }
        }
    }

    protected State getState() {
        return state;
    }

    protected void setState(State state) {
        this.state = Preconditions.checkNotNull(state);
    }

    protected abstract void prepareNext();

    protected enum State {
        UNPREPARED,
        READY,
        FINISHED,
        ERROR
    }
}

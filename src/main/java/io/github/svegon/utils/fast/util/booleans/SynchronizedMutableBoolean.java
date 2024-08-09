package io.github.svegon.utils.fast.util.booleans;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.booleans.BooleanBinaryOperator;
import it.unimi.dsi.fastutil.booleans.BooleanUnaryOperator;

public final class SynchronizedMutableBoolean {
    private final Object lock;
    private volatile boolean value;

    public SynchronizedMutableBoolean(Object lock, boolean value) {
        this.lock = Preconditions.checkNotNull(lock);
        this.value = value;
    }

    public SynchronizedMutableBoolean(boolean value) {
        this.lock = this;
        this.value = value;
    }

    public SynchronizedMutableBoolean(Object lock) {
        this.lock = Preconditions.checkNotNull(lock);
    }

    public SynchronizedMutableBoolean() {
        this.lock = this;
    }

    @Override
    public String toString() {
        return String.valueOf(get());
    }

    public boolean get() {
        synchronized (lock) {
            return value;
        }
    }

    public void set(boolean value) {
        synchronized (lock) {
            this.value = value;
        }
    }

    public boolean getAndSet(boolean newValue) {
        synchronized (lock) {
            boolean ret = value;
            value = newValue;
            return ret;
        }
    }

    public boolean compareAndSet(boolean expectedValue, boolean newValue) {
        synchronized (lock) {
            if (value == expectedValue) {
                value = newValue;
                return true;
            }

            return false;
        }
    }

    public boolean getAndUpdate(BooleanUnaryOperator updateFunction) {
        synchronized (lock) {
            boolean prev = value;
            value = updateFunction.apply(prev);
            return prev;
        }
    }

    public boolean updateAndGet(BooleanUnaryOperator updateFunction) {
        synchronized (lock) {
            return value = updateFunction.apply(value);
        }
    }

    public boolean getAndAccumulate(boolean x, BooleanBinaryOperator accumulatorFunction) {
        synchronized (lock) {
            boolean prev = value;
            value = accumulatorFunction.apply(prev, x);
            return prev;
        }
    }

    public boolean accumulateAndGet(boolean x, BooleanBinaryOperator accumulatorFunction) {
        synchronized (lock) {
            return value = accumulatorFunction.apply(value, x);
        }
    }

    public boolean compareAndExchange(boolean expectedValue, boolean newValue) {
        synchronized (lock) {
            boolean witnessedValue = value;

            if (witnessedValue == expectedValue) {
                value = newValue;
            }

            return witnessedValue;
        }
    }
}

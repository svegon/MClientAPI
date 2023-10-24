package com.github.svegon.utils.fast.util.longs;

import com.google.common.base.Preconditions;

import java.util.function.LongBinaryOperator;
import java.util.function.LongUnaryOperator;

public final class SynchronizedMutableLong extends Number {
    private final Object lock;
    private volatile long value;

    public SynchronizedMutableLong(Object lock, long value) {
        this.lock = Preconditions.checkNotNull(lock);
        this.value = value;
    }

    public SynchronizedMutableLong(long value) {
        this.lock = this;
        this.value = value;
    }

    public SynchronizedMutableLong(Object lock) {
        this.lock = Preconditions.checkNotNull(lock);
    }

    public SynchronizedMutableLong() {
        this.lock = this;
    }

    @Override
    public String toString() {
        return String.valueOf(get());
    }

    @Override
    public int intValue() {
        return (int) get();
    }

    @Override
    public long longValue() {
        return get();
    }

    @Override
    public float floatValue() {
        return get();
    }

    @Override
    public double doubleValue() {
        return get();
    }

    public long get() {
        synchronized (lock) {
            return value;
        }
    }

    public void set(long value) {
        synchronized (lock) {
            this.value = value;
        }
    }

    public long getAndSet(long newValue) {
        synchronized (lock) {
            long ret = value;
            value = newValue;
            return ret;
        }
    }

    public boolean compareAndSet(long expectedValue, long newValue) {
        synchronized (lock) {
            if (value == expectedValue) {
                value = newValue;
                return true;
            }

            return false;
        }
    }

    public long getAndIncrement() {
        synchronized (lock) {
            return value++;
        }
    }

    public long getAndDecrement() {
        synchronized (lock) {
            return value--;
        }
    }

    public long getAndAdd(long delta) {
        synchronized (lock) {
            long ret = value;
            value += delta;
            return ret;
        }
    }

    public long getAndMultiply(long multiplier) {
        synchronized (lock) {
            long ret = value;
            value *= multiplier;
            return ret;
        }
    }

    public long incrementAndGet() {
        synchronized (lock) {
            return ++value;
        }
    }

    public long decrementAndGet() {
        synchronized (lock) {
            return --value;
        }
    }

    public long addAndGet(long delta) {
        synchronized (lock) {
            return value += delta;
        }
    }

    public long multiplyAndGet(long multiplier) {
        synchronized (lock) {
            return value *= multiplier;
        }
    }

    public long getAndUpdate(LongUnaryOperator updateFunction) {
        synchronized (lock) {
            long prev = value;
            value = updateFunction.applyAsLong(prev);
            return prev;
        }
    }

    public long updateAndGet(LongUnaryOperator updateFunction) {
        synchronized (lock) {
            return value = updateFunction.applyAsLong(value);
        }
    }

    public long getAndAccumulate(long x, LongBinaryOperator accumulatorFunction) {
        synchronized (lock) {
            long prev = value;
            value = accumulatorFunction.applyAsLong(prev, x);
            return prev;
        }
    }

    public long accumulateAndGet(long x, LongBinaryOperator accumulatorFunction) {
        synchronized (lock) {
            return value = accumulatorFunction.applyAsLong(value, x);
        }
    }

    public long compareAndExchange(long expectedValue, long newValue) {
        synchronized (lock) {
            long witnessedValue = value;

            if (witnessedValue == expectedValue) {
                value = newValue;
            }

            return witnessedValue;
        }
    }
}

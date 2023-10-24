package com.github.svegon.utils.fast.util.ints;

import com.google.common.base.Preconditions;

import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;

public final class SynchronizedMutableInteger extends Number {
    private final Object lock;
    private volatile int value;

    public SynchronizedMutableInteger(Object lock, int value) {
        this.lock = Preconditions.checkNotNull(lock);
        this.value = value;
    }

    public SynchronizedMutableInteger(int value) {
        this.lock = this;
        this.value = value;
    }

    public SynchronizedMutableInteger(Object lock) {
        this.lock = Preconditions.checkNotNull(lock);
    }

    public SynchronizedMutableInteger() {
        this.lock = this;
    }

    @Override
    public String toString() {
        return String.valueOf(get());
    }

    @Override
    public int intValue() {
        return get();
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

    public int get() {
        synchronized (lock) {
            return value;
        }
    }

    public void set(int value) {
        synchronized (lock) {
            this.value = value;
        }
    }

    public int getAndSet(int newValue) {
        synchronized (lock) {
            int ret = value;
            value = newValue;
            return ret;
        }
    }

    public boolean compareAndSet(int expectedValue, int newValue) {
        synchronized (lock) {
            if (value == expectedValue) {
                value = newValue;
                return true;
            }

            return false;
        }
    }

    public int getAndIncrement() {
        synchronized (lock) {
            return value++;
        }
    }

    public int getAndDecrement() {
        synchronized (lock) {
            return value--;
        }
    }

    public int getAndAdd(int delta) {
        synchronized (lock) {
            int ret = value;
            value += delta;
            return ret;
        }
    }

    public int getAndMultiply(int multiplier) {
        synchronized (lock) {
            int ret = value;
            value *= multiplier;
            return ret;
        }
    }

    public int incrementAndGet() {
        synchronized (lock) {
            return ++value;
        }
    }

    public int decrementAndGet() {
        synchronized (lock) {
            return --value;
        }
    }

    public int addAndGet(int delta) {
        synchronized (lock) {
            return value += delta;
        }
    }

    public int multiplyAndGet(int multiplier) {
        synchronized (lock) {
            return value *= multiplier;
        }
    }

    public int getAndUpdate(IntUnaryOperator updateFunction) {
        synchronized (lock) {
            int prev = value;
            value = updateFunction.applyAsInt(prev);
            return prev;
        }
    }

    public int updateAndGet(IntUnaryOperator updateFunction) {
        synchronized (lock) {
            return value = updateFunction.applyAsInt(value);
        }
    }

    public int getAndAccumulate(int x, IntBinaryOperator accumulatorFunction) {
        synchronized (lock) {
            int prev = value;
            value = accumulatorFunction.applyAsInt(prev, x);
            return prev;
        }
    }

    public int accumulateAndGet(int x, IntBinaryOperator accumulatorFunction) {
        synchronized (lock) {
            return value = accumulatorFunction.applyAsInt(value, x);
        }
    }

    public int compareAndExchange(int expectedValue, int newValue) {
        synchronized (lock) {
            int witnessedValue = value;

            if (witnessedValue == expectedValue) {
                value = newValue;
            }

            return witnessedValue;
        }
    }
}

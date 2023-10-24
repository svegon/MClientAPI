package com.github.svegon.utils.fast.util.shorts;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.shorts.ShortBinaryOperator;
import it.unimi.dsi.fastutil.shorts.ShortUnaryOperator;

public final class SynchronizedMutableShort extends Number {
    private final Object lock;
    private volatile short value;

    public SynchronizedMutableShort(Object lock, short value) {
        this.lock = Preconditions.checkNotNull(lock);
        this.value = value;
    }

    public SynchronizedMutableShort(short value) {
        this.lock = this;
        this.value = value;
    }

    public SynchronizedMutableShort(Object lock) {
        this.lock = Preconditions.checkNotNull(lock);
    }

    public SynchronizedMutableShort() {
        this.lock = this;
    }

    @Override
    public String toString() {
        return String.valueOf(get());
    }

    @Override
    public short shortValue() {
        return get();
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

    public short get() {
        synchronized (lock) {
            return value;
        }
    }

    public void set(short value) {
        synchronized (lock) {
            this.value = value;
        }
    }

    public short getAndSet(short newValue) {
        synchronized (lock) {
            short ret = value;
            value = newValue;
            return ret;
        }
    }

    public boolean compareAndSet(short expectedValue, short newValue) {
        synchronized (lock) {
            if (value == expectedValue) {
                value = newValue;
                return true;
            }

            return false;
        }
    }

    public short getAndIncrement() {
        synchronized (lock) {
            return value++;
        }
    }

    public short getAndDecrement() {
        synchronized (lock) {
            return value--;
        }
    }

    public short getAndAdd(short delta) {
        synchronized (lock) {
            short ret = value;
            value += delta;
            return ret;
        }
    }

    public short getAndMultiply(short multiplier) {
        synchronized (lock) {
            short ret = value;
            value *= multiplier;
            return ret;
        }
    }

    public short incrementAndGet() {
        synchronized (lock) {
            return ++value;
        }
    }

    public short decrementAndGet() {
        synchronized (lock) {
            return --value;
        }
    }

    public short addAndGet(short delta) {
        synchronized (lock) {
            return value += delta;
        }
    }

    public short multiplyAndGet(short multiplier) {
        synchronized (lock) {
            return value *= multiplier;
        }
    }

    public short getAndUpdate(ShortUnaryOperator updateFunction) {
        synchronized (lock) {
            short prev = value;
            value = updateFunction.apply(prev);
            return prev;
        }
    }

    public short updateAndGet(ShortUnaryOperator updateFunction) {
        synchronized (lock) {
            return value = updateFunction.apply(value);
        }
    }

    public short getAndAccumulate(short x, ShortBinaryOperator accumulatorFunction) {
        synchronized (lock) {
            short prev = value;
            value = accumulatorFunction.apply(prev, x);
            return prev;
        }
    }

    public short accumulateAndGet(short x, ShortBinaryOperator accumulatorFunction) {
        synchronized (lock) {
            return value = accumulatorFunction.apply(value, x);
        }
    }

    public short compareAndExchange(short expectedValue, short newValue) {
        synchronized (lock) {
            short witnessedValue = value;

            if (witnessedValue == expectedValue) {
                value = newValue;
            }

            return witnessedValue;
        }
    }
}

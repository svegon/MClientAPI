package com.github.svegon.utils.fast.util.floats;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.floats.FloatBinaryOperator;
import it.unimi.dsi.fastutil.floats.FloatUnaryOperator;

public final class SynchronizedMutableFloat extends Number {
    private final Object lock;
    private volatile float value;

    public SynchronizedMutableFloat(Object lock, float value) {
        this.lock = Preconditions.checkNotNull(lock);
        this.value = value;
    }

    public SynchronizedMutableFloat(float value) {
        this.lock = this;
        this.value = value;
    }

    public SynchronizedMutableFloat(Object lock) {
        this.lock = Preconditions.checkNotNull(lock);
    }

    public SynchronizedMutableFloat() {
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
        return (long) get();
    }

    @Override
    public float floatValue() {
        return get();
    }

    @Override
    public double doubleValue() {
        return get();
    }

    public float get() {
        synchronized (lock) {
            return value;
        }
    }

    public void set(float value) {
        synchronized (lock) {
            this.value = value;
        }
    }

    public float getAndSet(float newValue) {
        synchronized (lock) {
            float ret = value;
            value = newValue;
            return ret;
        }
    }

    public boolean compareAndSet(float expectedValue, float newValue) {
        synchronized (lock) {
            if (value == expectedValue) {
                value = newValue;
                return true;
            }

            return false;
        }
    }

    public float getAndIncrement() {
        synchronized (lock) {
            return value++;
        }
    }

    public float getAndDecrement() {
        synchronized (lock) {
            return value--;
        }
    }

    public float getAndAdd(float delta) {
        synchronized (lock) {
            float ret = value;
            value += delta;
            return ret;
        }
    }

    public float getAndMultiply(float multiplier) {
        synchronized (lock) {
            float ret = value;
            value *= multiplier;
            return ret;
        }
    }

    public float incrementAndGet() {
        synchronized (lock) {
            return ++value;
        }
    }

    public float decrementAndGet() {
        synchronized (lock) {
            return --value;
        }
    }

    public float addAndGet(float delta) {
        synchronized (lock) {
            return value += delta;
        }
    }

    public float multiplyAndGet(float multiplier) {
        synchronized (lock) {
            return value *= multiplier;
        }
    }

    public float getAndUpdate(FloatUnaryOperator updateFunction) {
        synchronized (lock) {
            float prev = value;
            value = updateFunction.apply(prev);
            return prev;
        }
    }

    public float updateAndGet(FloatUnaryOperator updateFunction) {
        synchronized (lock) {
            return value = updateFunction.apply(value);
        }
    }

    public float getAndAccumulate(float x, FloatBinaryOperator accumulatorFunction) {
        synchronized (lock) {
            float prev = value;
            value = accumulatorFunction.apply(prev, x);
            return prev;
        }
    }

    public float accumulateAndGet(float x, FloatBinaryOperator accumulatorFunction) {
        synchronized (lock) {
            return value = accumulatorFunction.apply(value, x);
        }
    }

    public float compareAndExchange(float expectedValue, float newValue) {
        synchronized (lock) {
            float witnessedValue = value;

            if (witnessedValue == expectedValue) {
                value = newValue;
            }

            return witnessedValue;
        }
    }
}

package io.github.svegon.utils.fast.util.doubles;

import com.google.common.base.Preconditions;

import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

public final class SynchronizedMutableDouble extends Number {
    private final Object lock;
    private volatile double value;

    public SynchronizedMutableDouble(Object lock, double value) {
        this.lock = Preconditions.checkNotNull(lock);
        this.value = value;
    }

    public SynchronizedMutableDouble(double value) {
        this.lock = this;
        this.value = value;
    }

    public SynchronizedMutableDouble(Object lock) {
        this.lock = Preconditions.checkNotNull(lock);
    }

    public SynchronizedMutableDouble() {
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
        return (float) get();
    }

    @Override
    public double doubleValue() {
        return get();
    }

    public double get() {
        synchronized (lock) {
            return value;
        }
    }

    public void set(double value) {
        synchronized (lock) {
            this.value = value;
        }
    }

    public double getAndSet(double newValue) {
        synchronized (lock) {
            double ret = value;
            value = newValue;
            return ret;
        }
    }

    public boolean compareAndSet(double expectedValue, double newValue) {
        synchronized (lock) {
            if (value == expectedValue) {
                value = newValue;
                return true;
            }

            return false;
        }
    }

    public double getAndIncrement() {
        synchronized (lock) {
            return value++;
        }
    }

    public double getAndDecrement() {
        synchronized (lock) {
            return value--;
        }
    }

    public double getAndAdd(double delta) {
        synchronized (lock) {
            double ret = value;
            value += delta;
            return ret;
        }
    }

    public double getAndMultiply(double multiplier) {
        synchronized (lock) {
            double ret = value;
            value *= multiplier;
            return ret;
        }
    }

    public double incrementAndGet() {
        synchronized (lock) {
            return ++value;
        }
    }

    public double decrementAndGet() {
        synchronized (lock) {
            return --value;
        }
    }

    public double addAndGet(double delta) {
        synchronized (lock) {
            return value += delta;
        }
    }

    public double multiplyAndGet(double multiplier) {
        synchronized (lock) {
            return value *= multiplier;
        }
    }

    public double getAndUpdate(DoubleUnaryOperator updateFunction) {
        synchronized (lock) {
            double prev = value;
            value = updateFunction.applyAsDouble(prev);
            return prev;
        }
    }

    public double updateAndGet(DoubleUnaryOperator updateFunction) {
        synchronized (lock) {
            return value = updateFunction.applyAsDouble(value);
        }
    }

    public double getAndAccumulate(double x, DoubleBinaryOperator accumulatorFunction) {
        synchronized (lock) {
            double prev = value;
            value = accumulatorFunction.applyAsDouble(prev, x);
            return prev;
        }
    }

    public double accumulateAndGet(double x, DoubleBinaryOperator accumulatorFunction) {
        synchronized (lock) {
            return value = accumulatorFunction.applyAsDouble(value, x);
        }
    }

    public double compareAndExchange(double expectedValue, double newValue) {
        synchronized (lock) {
            double witnessedValue = value;

            if (witnessedValue == expectedValue) {
                value = newValue;
            }

            return witnessedValue;
        }
    }
}

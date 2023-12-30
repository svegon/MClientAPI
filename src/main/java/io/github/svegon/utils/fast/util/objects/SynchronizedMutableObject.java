package io.github.svegon.utils.fast.util.objects;

import com.google.common.base.Preconditions;

import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

public final class SynchronizedMutableObject<T> {
    private final Object lock;
    private volatile T value;

    public SynchronizedMutableObject(Object lock, T value) {
        this.lock = Preconditions.checkNotNull(lock);
        this.value = value;
    }

    public SynchronizedMutableObject(Object lock) {
        this.lock = Preconditions.checkNotNull(lock);
    }

    public SynchronizedMutableObject() {
        this.lock = this;
    }

    @Override
    public String toString() {
        return String.valueOf(get());
    }

    public T get() {
        synchronized (lock) {
            return value;
        }
    }

    public void set(T value) {
        synchronized (lock) {
            this.value = value;
        }
    }

    public T getAndSet(T newValue) {
        synchronized (lock) {
            T ret = value;
            value = newValue;
            return ret;
        }
    }

    public boolean compareAndSet(T expectedValue, T newValue) {
        synchronized (lock) {
            if (value == expectedValue) {
                value = newValue;
                return true;
            }

            return false;
        }
    }

    public T getAndUpdate(UnaryOperator<T> updateFunction) {
        synchronized (lock) {
            T prev = value;
            value = updateFunction.apply(prev);
            return prev;
        }
    }

    public T updateAndGet(UnaryOperator<T> updateFunction) {
        synchronized (lock) {
            return value = updateFunction.apply(value);
        }
    }

    public T getAndAccumulate(T x, BinaryOperator<T> accumulatorFunction) {
        synchronized (lock) {
            T prev = value;
            value = accumulatorFunction.apply(prev, x);
            return prev;
        }
    }

    public T accumulateAndGet(T x, BinaryOperator<T> accumulatorFunction) {
        synchronized (lock) {
            return value = accumulatorFunction.apply(value, x);
        }
    }

    public T compareAndExchange(T expectedValue, T newValue) {
        synchronized (lock) {
            T witnessedValue = value;

            if (witnessedValue == expectedValue) {
                value = newValue;
            }

            return witnessedValue;
        }
    }
}

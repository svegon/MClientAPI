package io.github.svegon.utils.fast.util.chars;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.chars.CharBinaryOperator;
import it.unimi.dsi.fastutil.chars.CharUnaryOperator;

public final class SynchronizedMutableCharacter {
    private final Object lock;
    private volatile char value;

    public SynchronizedMutableCharacter(Object lock, char value) {
        this.lock = Preconditions.checkNotNull(lock);
        this.value = value;
    }

    public SynchronizedMutableCharacter(char value) {
        this.lock = this;
        this.value = value;
    }

    public SynchronizedMutableCharacter(Object lock) {
        this.lock = Preconditions.checkNotNull(lock);
    }

    public SynchronizedMutableCharacter() {
        this.lock = this;
    }

    @Override
    public String toString() {
        return String.valueOf(get());
    }

    public char get() {
        synchronized (lock) {
            return value;
        }
    }

    public void set(char value) {
        synchronized (lock) {
            this.value = value;
        }
    }

    public char getAndSet(char newValue) {
        synchronized (lock) {
            char ret = value;
            value = newValue;
            return ret;
        }
    }

    public boolean compareAndSet(char expectedValue, char newValue) {
        synchronized (lock) {
            if (value == expectedValue) {
                value = newValue;
                return true;
            }

            return false;
        }
    }

    public char getAndIncrement() {
        synchronized (lock) {
            return value++;
        }
    }

    public char getAndDecrement() {
        synchronized (lock) {
            return value--;
        }
    }

    public char getAndAdd(char delta) {
        synchronized (lock) {
            char ret = value;
            value += delta;
            return ret;
        }
    }

    public char getAndMultiply(char multiplier) {
        synchronized (lock) {
            char ret = value;
            value *= multiplier;
            return ret;
        }
    }

    public char incrementAndGet() {
        synchronized (lock) {
            return ++value;
        }
    }

    public char decrementAndGet() {
        synchronized (lock) {
            return --value;
        }
    }

    public char addAndGet(char delta) {
        synchronized (lock) {
            return value += delta;
        }
    }

    public char multiplyAndGet(char multiplier) {
        synchronized (lock) {
            return value *= multiplier;
        }
    }

    public char getAndUpdate(CharUnaryOperator updateFunction) {
        synchronized (lock) {
            char prev = value;
            value = updateFunction.apply(prev);
            return prev;
        }
    }

    public char updateAndGet(CharUnaryOperator updateFunction) {
        synchronized (lock) {
            return value = updateFunction.apply(value);
        }
    }

    public char getAndAccumulate(char x, CharBinaryOperator accumulatorFunction) {
        synchronized (lock) {
            char prev = value;
            value = accumulatorFunction.apply(prev, x);
            return prev;
        }
    }

    public char accumulateAndGet(char x, CharBinaryOperator accumulatorFunction) {
        synchronized (lock) {
            return value = accumulatorFunction.apply(value, x);
        }
    }

    public char compareAndExchange(char expectedValue, char newValue) {
        synchronized (lock) {
            char witnessedValue = value;

            if (witnessedValue == expectedValue) {
                value = newValue;
            }

            return witnessedValue;
        }
    }
}

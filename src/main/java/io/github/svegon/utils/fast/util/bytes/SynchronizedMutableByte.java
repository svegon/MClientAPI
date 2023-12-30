package io.github.svegon.utils.fast.util.bytes;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.ByteBinaryOperator;
import it.unimi.dsi.fastutil.bytes.ByteUnaryOperator;

public final class SynchronizedMutableByte extends Number {
    private final Object lock;
    private volatile byte value;

    public SynchronizedMutableByte(Object lock, byte value) {
        this.lock = Preconditions.checkNotNull(lock);
        this.value = value;
    }

    public SynchronizedMutableByte(byte value) {
        this.lock = this;
        this.value = value;
    }

    public SynchronizedMutableByte(Object lock) {
        this.lock = Preconditions.checkNotNull(lock);
    }

    public SynchronizedMutableByte() {
        this.lock = this;
    }

    @Override
    public String toString() {
        return String.valueOf(get());
    }

    @Override
    public byte byteValue() {
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

    public byte get() {
        synchronized (lock) {
            return value;
        }
    }

    public void set(byte value) {
        synchronized (lock) {
            this.value = value;
        }
    }

    public byte getAndSet(byte newValue) {
        synchronized (lock) {
            byte ret = value;
            value = newValue;
            return ret;
        }
    }

    public boolean compareAndSet(byte expectedValue, byte newValue) {
        synchronized (lock) {
            if (value == expectedValue) {
                value = newValue;
                return true;
            }

            return false;
        }
    }

    public byte getAndIncrement() {
        synchronized (lock) {
            return value++;
        }
    }

    public byte getAndDecrement() {
        synchronized (lock) {
            return value--;
        }
    }

    public byte getAndAdd(byte delta) {
        synchronized (lock) {
            byte ret = value;
            value += delta;
            return ret;
        }
    }

    public byte getAndMultiply(byte multiplier) {
        synchronized (lock) {
            byte ret = value;
            value *= multiplier;
            return ret;
        }
    }

    public byte incrementAndGet() {
        synchronized (lock) {
            return ++value;
        }
    }

    public byte decrementAndGet() {
        synchronized (lock) {
            return --value;
        }
    }

    public byte addAndGet(byte delta) {
        synchronized (lock) {
            return value += delta;
        }
    }

    public byte multiplyAndGet(byte multiplier) {
        synchronized (lock) {
            return value *= multiplier;
        }
    }

    public byte getAndUpdate(ByteUnaryOperator updateFunction) {
        synchronized (lock) {
            byte prev = value;
            value = updateFunction.apply(prev);
            return prev;
        }
    }

    public byte updateAndGet(ByteUnaryOperator updateFunction) {
        synchronized (lock) {
            return value = updateFunction.apply(value);
        }
    }

    public byte getAndAccumulate(byte x, ByteBinaryOperator accumulatorFunction) {
        synchronized (lock) {
            byte prev = value;
            value = accumulatorFunction.apply(prev, x);
            return prev;
        }
    }

    public byte accumulateAndGet(byte x, ByteBinaryOperator accumulatorFunction) {
        synchronized (lock) {
            return value = accumulatorFunction.apply(value, x);
        }
    }

    public byte compareAndExchange(byte expectedValue, byte newValue) {
        synchronized (lock) {
            byte witnessedValue = value;

            if (witnessedValue == expectedValue) {
                value = newValue;
            }

            return witnessedValue;
        }
    }
}

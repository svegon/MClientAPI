package io.github.svegon.utils.fast.util.bytes.ints;

import io.github.svegon.utils.collections.ArrayUtil;
import io.github.svegon.utils.collections.iteration.IterationUtil;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.*;
import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import oshi.annotation.concurrent.NotThreadSafe;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

@NotThreadSafe
public class Byte2IntTableMap extends AbstractByte2IntMap {
    private final int[] table = new int[256];
    private final ByteSet matchedEmptyValueKeys = new ByteArraySet();

    public Byte2IntTableMap() {

    }

    public Byte2IntTableMap(Byte2IntMap original) {
        ObjectSet<Byte2IntMap.Entry> entrySet = original.byte2IntEntrySet();
        ObjectIterator<Byte2IntMap.Entry> itr = entrySet instanceof FastEntrySet fast
                ? fast.fastIterator() : entrySet.iterator();

        while (itr.hasNext()) {
            Byte2IntMap.Entry entry = itr.next();

            put(entry.getByteKey(), entry.getIntValue());
        }
    }

    public boolean contains(byte key, int value) {
        int v = table[Byte.toUnsignedInt(key)];

        if (v != value) {
            return false;
        }

        return v != 0 || matchedEmptyValueKeys.contains(key);
    }

    @Override
    public int size() {
        int size = 0;

        for (int i = 0; i < table.length; i++) {
            if (table[i] != 0 || matchedEmptyValueKeys.contains((byte) i)) {
                size ++;
            }
        }

        return size;
    }

    @Override
    public void clear() {
        ArrayUtil.fill(table, 0);
        matchedEmptyValueKeys.clear();
    }

    @Override
    public ObjectSet<Byte2IntMap.Entry> byte2IntEntrySet() {
        return new EntrySet();
    }

    @Override
    public int getOrDefault(byte key, int defaultValue) {
        int v = table[Byte.toUnsignedInt(key)];

        if (v != 0 || matchedEmptyValueKeys.contains(key)) {
            return v;
        }

        return defaultValue;
    }

    @Override
    public int remove(byte key) {
        int index = Byte.toUnsignedInt(key);
        int v = table[index];

        if (v == 0) {
            return matchedEmptyValueKeys.remove(key) ? v : defaultReturnValue();
        }

        table[index] = 0;
        return v;
    }

    @Override
    public int putIfAbsent(byte key, final int value) {
        return computeIfAbsent(key, k -> value);
    }

    @Override
    public boolean remove(byte key, int value) {
        int index = Byte.toUnsignedInt(key);
        int v = table[index];

        if (v == value) {
            if (v == 0) {
                matchedEmptyValueKeys.remove(key);
            } else {
                table[index] = 0;
            }

            return true;
        }

        return false;
    }

    @Override
    public int replace(byte key, int value) {
        int v = table[Byte.toUnsignedInt(key)];

        if (v != 0 || matchedEmptyValueKeys.contains(key)) {
            return put(key, value);
        }

        return defaultReturnValue();
    }

    @Override
    public int computeIfAbsent(byte key, Byte2IntFunction mappingFunction) {
        Preconditions.checkNotNull(mappingFunction);

        int v = table[Byte.toUnsignedInt(key)];

        if (v != 0 || matchedEmptyValueKeys.contains(key)) {
            return v;
        }

        v = mappingFunction.get(key);

        put(key, v);

        return v;
    }

    @Override
    public int put(byte key, int value) {
        int ret = get(key);
        table[Byte.toUnsignedInt(key)] = value;

        if (value == 0) {
            matchedEmptyValueKeys.add(key);
        }

        return ret;
    }

    @Override
    public int get(byte key) {
        int v = table[Byte.toUnsignedInt(key)];

        if (v != 0 || matchedEmptyValueKeys.contains(key)) {
            return v;
        }

        return defaultReturnValue();
    }

    private final class Entry implements Byte2IntMap.Entry {
        private final byte key;

        private Entry(byte key) {
            this.key = key;
        }

        @Override
        public byte getByteKey() {
            return key;
        }

        @Override
        public int getIntValue() {
            return Byte2IntTableMap.this.get(key);
        }

        @Override
        public int setValue(int value) {
            return Byte2IntTableMap.this.put(key, value);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;

            if (!(o instanceof Map.Entry<?, ?> entry))
                return false;

            return entry instanceof Byte2IntMap.Entry e
                    ? getByteKey() == e.getByteKey() && getIntValue() == e.getIntValue()
                    : getKey().equals(entry.getKey()) && getValue().equals(entry.getValue());
        }

        @Override
        public int hashCode() {
            return Byte.hashCode(getByteKey()) ^ Integer.hashCode(getIntValue());
        }

        @Override
        public String toString() {
            return getByteKey() + "=" + getIntValue();
        }
    }

    private final class FastEntry implements Byte2IntMap.Entry {
        private byte key;
        private int value;

        @Override
        public byte getByteKey() {
            return key;
        }

        @Override
        public int getIntValue() {
            return value;
        }

        @Override
        public int setValue(int value) {
            return Byte2IntTableMap.this.put(key, value);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;

            if (!(o instanceof Map.Entry<?, ?> entry))
                return false;

            return entry instanceof Byte2IntMap.Entry e
                    ? getByteKey() == e.getByteKey() && getIntValue() == e.getIntValue()
                    : getKey().equals(entry.getKey()) && getValue().equals(entry.getValue());
        }

        @Override
        public int hashCode() {
            return Byte.hashCode(getByteKey()) ^ Integer.hashCode(getIntValue());
        }

        @Override
        public String toString() {
            return getByteKey() + "=" + getIntValue();
        }
    }

    private final class FastEntryIterator implements ObjectIterator<Byte2IntMap.Entry> {
        private final FastEntry entry = new FastEntry();
        private int nextKey;

        @Override
        public boolean hasNext() {
            return nextKey < 256;
        }

        @Override
        public Byte2IntMap.Entry next() {
            if (nextKey >= 256) {
                throw new NoSuchElementException();
            }

            entry.key = (byte) nextKey;
            entry.value = table[nextKey];

            while (nextKey < 256) {
                if (table[nextKey] != 0 || matchedEmptyValueKeys.contains((byte) nextKey)) {
                    break;
                }

                nextKey++;
            }

            return entry;
        }

        @Override
        public void remove() {
            Byte2IntTableMap.this.remove(entry.getByteKey());
        }
    }

    private final class EntrySet extends AbstractObjectSet<Byte2IntMap.Entry> implements FastEntrySet {
        @Override
        public ObjectIterator<Byte2IntMap.Entry> iterator() {
            final var it = fastIterator();
            return new ObjectIterator<>() {
                @Override
                public boolean hasNext() {
                    return it.hasNext();
                }

                @Override
                public Byte2IntMap.Entry next() {
                    final var e = it.next();
                    return new Entry(e.getByteKey());
                }

                @Override
                public void remove() {
                    it.remove();
                }
            };
        }

        @Override
        public boolean add(Byte2IntMap.Entry entry) {
            return Byte2IntTableMap.this.put(entry.getByteKey(), entry.getIntValue()) != entry.getIntValue();
        }

        @Override
        public boolean remove(Object o) {
            return o instanceof Map.Entry<?, ?> entry && (entry instanceof Byte2IntMap.Entry e
                    ? Byte2IntTableMap.this.remove(e.getByteKey(), e.getIntValue()) : entry.getKey() instanceof Byte
                    && entry.getValue() instanceof Integer && Byte2IntTableMap.this.remove((byte) entry.getKey(),
                    (int) entry.getValue()));
        }

        @Override
        public void clear() {
            Byte2IntTableMap.this.clear();
        }

        @Override
        public int size() {
            return Byte2IntTableMap.this.size();
        }

        @Override
        public boolean contains(Object o) {
            return o instanceof Map.Entry<?, ?> e && e.getKey() instanceof Byte key
                    && e.getValue() instanceof Short value && Byte2IntTableMap.this.contains(key, value);
        }

        @Override
        public ObjectIterator<Byte2IntMap.Entry> fastIterator() {
            return new FastEntryIterator();
        }

        @Override
        public void fastForEach(Consumer<? super Byte2IntMap.Entry> consumer) {
            fastIterator().forEachRemaining(consumer);
        }
    }
}

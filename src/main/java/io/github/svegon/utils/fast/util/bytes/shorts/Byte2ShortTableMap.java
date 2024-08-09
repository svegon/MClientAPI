package io.github.svegon.utils.fast.util.bytes.shorts;

import io.github.svegon.utils.collections.ArrayUtil;
import io.github.svegon.utils.collections.iteration.IterationUtil;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.*;
import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.jcip.annotations.NotThreadSafe;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

@NotThreadSafe
public class Byte2ShortTableMap extends AbstractByte2ShortMap {
    private final short[] table = new short[256];
    private final ByteSet matchedEmptyValueKeys = new ByteArraySet();

    public Byte2ShortTableMap() {

    }

    public Byte2ShortTableMap(Byte2ShortMap original) {
        ObjectSet<Byte2ShortMap.Entry> entrySet = original.byte2ShortEntrySet();
        ObjectIterator<Byte2ShortMap.Entry> itr = entrySet instanceof FastEntrySet fast
                ? fast.fastIterator() : entrySet.iterator();

        while (itr.hasNext()) {
            Byte2ShortMap.Entry entry = itr.next();

            put(entry.getByteKey(), entry.getShortValue());
        }
    }

    public boolean contains(byte key, short value) {
        short v = table[Byte.toUnsignedInt(key)];

        if (v != value) {
            return false;
        }

        return v != 0 || matchedEmptyValueKeys.contains(key);
    }

    @Override
    public int size() {
        short size = 0;

        for (short i = 0; i < table.length; i++) {
            if (table[i] != 0 || matchedEmptyValueKeys.contains((byte) i)) {
                size ++;
            }
        }

        return size;
    }

    @Override
    public void clear() {
        ArrayUtil.fill(table, (short) 0);
        matchedEmptyValueKeys.clear();
    }

    @Override
    public ObjectSet<Byte2ShortMap.Entry> byte2ShortEntrySet() {
        return new EntrySet();
    }

    @Override
    public short getOrDefault(byte key, short defaultValue) {
        short v = table[Byte.toUnsignedInt(key)];

        if (v != 0 || matchedEmptyValueKeys.contains(key)) {
            return v;
        }

        return defaultValue;
    }

    @Override
    public short remove(byte key) {
        int index = Byte.toUnsignedInt(key);
        short v = table[index];

        if (v == 0) {
            return matchedEmptyValueKeys.remove(key) ? v : defaultReturnValue();
        }

        table[index] = 0;
        return v;
    }

    @Override
    public short putIfAbsent(byte key, final short value) {
        return computeIfAbsent(key, k -> value);
    }

    @Override
    public boolean remove(byte key, short value) {
        int index = Byte.toUnsignedInt(key);
        short v = table[index];

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
    public short replace(byte key, short value) {
        short v = table[Byte.toUnsignedInt(key)];

        if (v != 0 || matchedEmptyValueKeys.contains(key)) {
            return put(key, value);
        }

        return defaultReturnValue();
    }

    @Override
    public short computeIfAbsent(byte key, Byte2ShortFunction mappingFunction) {
        Preconditions.checkNotNull(mappingFunction);

        short v = table[Byte.toUnsignedInt(key)];

        if (v != 0 || matchedEmptyValueKeys.contains(key)) {
            return v;
        }

        v = mappingFunction.get(key);

        put(key, v);

        return v;
    }

    @Override
    public short put(byte key, short value) {
        short ret = get(key);
        table[Byte.toUnsignedInt(key)] = value;

        if (value == 0) {
            matchedEmptyValueKeys.add(key);
        }

        return ret;
    }

    @Override
    public short get(byte key) {
        short v = table[Byte.toUnsignedInt(key)];

        if (v != 0 || matchedEmptyValueKeys.contains(key)) {
            return v;
        }

        return defaultReturnValue();
    }

    private final class Entry implements Byte2ShortMap.Entry {
        private final byte key;

        private Entry(byte key) {
            this.key = key;
        }

        @Override
        public byte getByteKey() {
            return key;
        }

        @Override
        public short getShortValue() {
            return Byte2ShortTableMap.this.get(key);
        }

        @Override
        public short setValue(short value) {
            return Byte2ShortTableMap.this.put(key, value);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;

            if (!(o instanceof Map.Entry<?, ?> entry))
                return false;

            return entry instanceof Byte2ShortMap.Entry e
                    ? getByteKey() == e.getByteKey() && getShortValue() == e.getShortValue()
                    : getKey().equals(entry.getKey()) && getValue().equals(entry.getValue());
        }

        @Override
        public int hashCode() {
            return Byte.hashCode(getByteKey()) ^ Short.hashCode(getShortValue());
        }

        @Override
        public String toString() {
            return getByteKey() + "=" + getShortValue();
        }
    }

    private final class FastEntry implements Byte2ShortMap.Entry {
        private byte key;
        private short value;

        @Override
        public byte getByteKey() {
            return key;
        }

        @Override
        public short getShortValue() {
            return value;
        }

        @Override
        public short setValue(short value) {
            return Byte2ShortTableMap.this.put(key, value);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;

            if (!(o instanceof Map.Entry<?, ?> entry))
                return false;

            return entry instanceof Byte2ShortMap.Entry e
                    ? getByteKey() == e.getByteKey() && getShortValue() == e.getShortValue()
                    : getKey().equals(entry.getKey()) && getValue().equals(entry.getValue());
        }

        @Override
        public int hashCode() {
            return Byte.hashCode(getByteKey()) ^ Short.hashCode(getShortValue());
        }

        @Override
        public String toString() {
            return getByteKey() + "=" + getShortValue();
        }
    }

    private final class FastEntryIterator implements ObjectIterator<Byte2ShortMap.Entry> {
        private final FastEntry entry = new FastEntry();
        private short nextKey;

        @Override
        public boolean hasNext() {
            return nextKey < 256;
        }

        @Override
        public Byte2ShortMap.Entry next() {
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
            Byte2ShortTableMap.this.remove(entry.getByteKey());
        }
    }

    private final class EntrySet extends AbstractObjectSet<Byte2ShortMap.Entry> implements FastEntrySet {
        @Override
        public ObjectIterator<Byte2ShortMap.Entry> iterator() {
            return IterationUtil.transformToObj(fastIterator(), e -> new Entry(e.getByteKey()));
        }

        @Override
        public boolean add(Byte2ShortMap.Entry entry) {
            return Byte2ShortTableMap.this.put(entry.getByteKey(), entry.getShortValue()) != entry.getShortValue();
        }

        @Override
        public boolean remove(Object o) {
            return o instanceof Map.Entry<?, ?> entry && (entry instanceof Byte2ShortMap.Entry e
                    ? Byte2ShortTableMap.this.remove(e.getByteKey(), e.getShortValue()) : entry.getKey() instanceof Byte
                    && entry.getValue() instanceof Short && Byte2ShortTableMap.this.remove((byte) entry.getKey(),
                    (short) entry.getValue()));
        }

        @Override
        public void clear() {
            Byte2ShortTableMap.this.clear();
        }

        @Override
        public int size() {
            return Byte2ShortTableMap.this.size();
        }

        @Override
        public boolean contains(Object o) {
            return o instanceof Map.Entry<?, ?> e && e.getKey() instanceof Byte key
                    && e.getValue() instanceof Short value && Byte2ShortTableMap.this.contains(key, value);
        }

        @Override
        public ObjectIterator<Byte2ShortMap.Entry> fastIterator() {
            return new FastEntryIterator();
        }

        @Override
        public void fastForEach(Consumer<? super Byte2ShortMap.Entry> consumer) {
            fastIterator().forEachRemaining(consumer);
        }
    }
}

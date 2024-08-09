package io.github.svegon.utils.fast.util.shorts.ints;

import io.github.svegon.utils.collections.iteration.IterationUtil;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.IntArrays;
import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.shorts.*;
import net.jcip.annotations.NotThreadSafe;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

@NotThreadSafe
public class Short2IntTableMap extends AbstractShort2IntMap {
    private final ShortSet matchedEmptyValueKeys = new ShortArraySet();
    private int[] table;

    public Short2IntTableMap() {
        table = IntArrays.DEFAULT_EMPTY_ARRAY;
    }

    public Short2IntTableMap(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException();
        } else if (capacity == 0) {
            table = IntArrays.EMPTY_ARRAY;
        } else {
            table = new int[capacity];
        }
    }

    public Short2IntTableMap(Short2IntMap original) {
        this(original.size());

        ObjectSet<Short2IntMap.Entry> entrySet = original.short2IntEntrySet();
        ObjectIterator<Short2IntMap.Entry> itr = entrySet instanceof FastEntrySet fast
                ? fast.fastIterator() : entrySet.iterator();

        while (itr.hasNext()) {
            Short2IntMap.Entry entry = itr.next();

            put(entry.getShortKey(), entry.getIntValue());
        }
    }

    public void ensureKeyCapacity(short key) {
        ensureKeyCapacity((char) key);
    }

    public void ensureKeyCapacity(char key) {
        table = IntArrays.ensureCapacity(table, key);
    }

    public void trimTable() {
        for (int i = table.length; i-- > 0;) {
            if (table[i] != 0 || matchedEmptyValueKeys.contains((short) i)) {
                table = IntArrays.trim(table, i + 1);
                return;
            }
        }

        table = IntArrays.EMPTY_ARRAY;
    }

    public boolean contains(short key, int value) {
        int k = Short.toUnsignedInt(key);

        if (table.length >= k) {
            return false;
        }

        int v = table[k];

        if (v != value) {
            return false;
        }

        return v != 0 || matchedEmptyValueKeys.contains(key);
    }

    @Override
    public int size() {
        int size = 0;

        for (int i = 0; i < table.length; i++) {
            if (table[i] != 0 || matchedEmptyValueKeys.contains((short) i)) {
                size ++;
            }
        }

        return size;
    }

    @Override
    public void clear() {
        table = IntArrays.EMPTY_ARRAY;
        matchedEmptyValueKeys.clear();
    }

    @Override
    public ObjectSet<Short2IntMap.Entry> short2IntEntrySet() {
        return new EntrySet();
    }

    @Override
    public int getOrDefault(short key, final int defaultValue) {
        int k = Short.toUnsignedInt(key);
        int v;

        if (k < table.length && ((v = table[k]) != 0 || matchedEmptyValueKeys.contains(key))) {
            return v;
        }

        return defaultValue;
    }

    @Override
    public int remove(short key) {
        int k = Short.toUnsignedInt(key);

        if (k >= table.length) {
            return defaultReturnValue();
        }

        int v = table[k];

        if (v == 0) {
            return matchedEmptyValueKeys.remove(key) ? v : defaultReturnValue();
        }

        table[k] = 0;
        return v;
    }

    @Override
    public int putIfAbsent(short key, final int value) {
        return computeIfAbsent(key, k -> value);
    }

    @Override
    public boolean remove(short key, int value) {
        int index = Short.toUnsignedInt(key);

        if (index >= table.length) {
            return false;
        }

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
    public int replace(short key, int value) {
        int k = Short.toUnsignedInt(key);

        if (k >= table.length) {
            return defaultReturnValue();
        }

        int v = table[k];

        if (v != 0 || matchedEmptyValueKeys.contains(key)) {
            return put(key, value);
        }

        return defaultReturnValue();
    }

    @Override
    public int computeIfAbsent(short key, Short2IntFunction mappingFunction) {
        Preconditions.checkNotNull(mappingFunction);

        int k = Short.toUnsignedInt(key);
        int v;

        if (k < table.length && ((v = table[k]) != 0 || matchedEmptyValueKeys.contains(key))) {
            return v;
        }

        v = mappingFunction.get(key);

        put(key, v);

        return v;
    }

    @Override
    public int put(short key, int value) {
        int k = Short.toUnsignedInt(key);
        int ret = get(key);
        table = IntArrays.ensureCapacity(table, k);
        table[k] = value;

        if (value == 0) {
            matchedEmptyValueKeys.add(key);
        }

        return ret;
    }

    @Override
    public int get(short key) {
        int k = Short.toUnsignedInt(key);

        if (k >= table.length) {
            return defaultReturnValue();
        }

        int v = table[k];

        if (v != 0 || matchedEmptyValueKeys.contains(key)) {
            return v;
        }

        return defaultReturnValue();
    }

    private final class Entry implements Short2IntMap.Entry {
        private final short key;

        private Entry(short key) {
            this.key = key;
        }

        @Override
        public short getShortKey() {
            return key;
        }

        @Override
        public int getIntValue() {
            return Short2IntTableMap.this.get(key);
        }

        @Override
        public int setValue(int value) {
            return Short2IntTableMap.this.put(key, value);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;

            if (!(o instanceof Map.Entry<?, ?> entry))
                return false;

            return entry instanceof Short2IntMap.Entry e
                    ? getShortKey() == e.getShortKey() && getIntValue() == e.getIntValue()
                    : getKey().equals(entry.getKey()) && getValue().equals(entry.getValue());
        }

        @Override
        public int hashCode() {
            return Short.hashCode(getShortKey()) ^ Integer.hashCode(getIntValue());
        }

        @Override
        public String toString() {
            return getShortKey() + "=" + getIntValue();
        }
    }

    private final class FastEntry implements Short2IntMap.Entry {
        private short key;
        private int value;

        @Override
        public short getShortKey() {
            return key;
        }

        @Override
        public int getIntValue() {
            return value;
        }

        @Override
        public int setValue(int value) {
            return Short2IntTableMap.this.put(key, value);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;

            if (!(o instanceof Map.Entry<?, ?> entry))
                return false;

            return entry instanceof Short2IntMap.Entry e
                    ? getShortKey() == e.getShortKey() && getIntValue() == e.getIntValue()
                    : getKey().equals(entry.getKey()) && getValue().equals(entry.getValue());
        }

        @Override
        public int hashCode() {
            return Short.hashCode(getShortKey()) ^ Integer.hashCode(getIntValue());
        }

        @Override
        public String toString() {
            return getShortKey() + "=" + getIntValue();
        }
    }

    private final class FastEntryIterator implements ObjectIterator<Short2IntMap.Entry> {
        private final FastEntry entry = new FastEntry();
        private int nextKey;

        @Override
        public boolean hasNext() {
            return nextKey < 256;
        }

        @Override
        public Short2IntMap.Entry next() {
            if (nextKey >= 256) {
                throw new NoSuchElementException();
            }

            entry.key = (short) nextKey;
            entry.value = table[nextKey];

            while (nextKey < 65536) {
                if (table[nextKey] != 0 || matchedEmptyValueKeys.contains((short) nextKey)) {
                    break;
                }

                nextKey++;
            }

            return entry;
        }

        @Override
        public void remove() {
            Short2IntTableMap.this.remove(entry.getShortKey());
        }
    }

    private final class EntrySet extends AbstractObjectSet<Short2IntMap.Entry> implements FastEntrySet {
        @Override
        public ObjectIterator<Short2IntMap.Entry> iterator() {
            return IterationUtil.transformToObj(fastIterator(), e -> new Entry(e.getShortKey()));
        }

        @Override
        public boolean add(Short2IntMap.Entry entry) {
            return Short2IntTableMap.this.put(entry.getShortKey(), entry.getIntValue()) != entry.getIntValue();
        }

        @Override
        public boolean remove(Object o) {
            return o instanceof Map.Entry<?, ?> entry && (entry instanceof Short2IntMap.Entry e
                    ? Short2IntTableMap.this.remove(e.getShortKey(), e.getIntValue()) : entry.getKey() instanceof Short
                    && entry.getValue() instanceof Integer && Short2IntTableMap.this.remove((short) entry.getKey(),
                    (int) entry.getValue()));
        }

        @Override
        public void clear() {
            Short2IntTableMap.this.clear();
        }

        @Override
        public int size() {
            return Short2IntTableMap.this.size();
        }

        @Override
        public boolean contains(Object o) {
            return o instanceof Map.Entry<?, ?> e && e.getKey() instanceof Short key
                    && e.getValue() instanceof Short value && Short2IntTableMap.this.contains(key, value);
        }

        @Override
        public ObjectIterator<Short2IntMap.Entry> fastIterator() {
            return new FastEntryIterator();
        }

        @Override
        public void fastForEach(Consumer<? super Short2IntMap.Entry> consumer) {
            fastIterator().forEachRemaining(consumer);
        }
    }
}

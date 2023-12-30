package io.github.svegon.utils.fast.util.shorts.longs;

import io.github.svegon.utils.collections.ArrayUtil;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;
import it.unimi.dsi.fastutil.objects.*;
import it.unimi.dsi.fastutil.shorts.AbstractShort2LongMap;
import it.unimi.dsi.fastutil.shorts.Short2LongFunction;
import it.unimi.dsi.fastutil.shorts.Short2LongMap;
import net.jcip.annotations.NotThreadSafe;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

@NotThreadSafe
public class Short2LongTableMap extends AbstractShort2LongMap {
    private static final Entry[] EMPTY_ENTRY_ARRAY = new Entry[0];

    private Entry[] table = EMPTY_ENTRY_ARRAY;

    public Short2LongTableMap() {

    }

    public Short2LongTableMap(short maxKey) {
        if (maxKey != 0) {
            table = new Entry[Short.toUnsignedInt(maxKey)];
        }
    }

    public Short2LongTableMap(Short2LongMap original) {
        ObjectSet<Short2LongMap.Entry> entrySet = original.short2LongEntrySet();
        ObjectIterator<Short2LongMap.Entry> itr = entrySet instanceof FastEntrySet fast
                ? fast.fastIterator() : entrySet.iterator();

        while (itr.hasNext()) {
            Short2LongMap.Entry entry = itr.next();

            put(entry.getShortKey(), entry.getLongValue());
        }
    }

    public void ensureKeyCapacity(short key) {
        ensureKeyCapacity((char) key);
    }

    public void ensureKeyCapacity(char key) {
        table = ObjectArrays.ensureCapacity(table, key);
    }

    public void trimTable() {
        for (int i = table.length; i-- > 0;) {
            if (table[i] != null) {
                table = ObjectArrays.trim(table, i + 1);
                return;
            }
        }

        table = EMPTY_ENTRY_ARRAY;
    }

    public boolean contains(short key, long value) {
        int k = Short.toUnsignedInt(key);

        if (table.length >= k) {
            return false;
        }

        Entry e = table[k];

        return e != null && e.getLongValue() == value;
    }

    private Entry getEntry(short key) {
        int i = Short.toUnsignedInt(key);

        return i >= table.length ? null : table[i];
    }

    @Override
    public int size() {
        int size = 0;

        for (Entry entry : table) {
            if (entry != null) {
                size++;
            }
        }

        return size;
    }

    @Override
    public void clear() {
        table = EMPTY_ENTRY_ARRAY;
    }

    @Override
    public ObjectSet<Short2LongMap.Entry> short2LongEntrySet() {
        return new EntrySet();
    }

    @Override
    public long getOrDefault(short key, final long defaultValue) {
        Entry e = getEntry(key);

        return e != null ? e.getLongValue() : defaultValue;
    }

    @Override
    public long remove(short key) {
        int i = Short.toUnsignedInt(key);

        if (i >= table.length) {
            return defaultReturnValue();
        }

        Entry e = table[i];
        table[i] = null;
        return e == null ? defaultReturnValue() : e.getLongValue();
    }

    @Override
    public long putIfAbsent(short key, final long value) {
        return computeIfAbsent(key, k -> value);
    }

    @Override
    public boolean remove(short key, long value) {
        int i = Short.toUnsignedInt(key);

        if (i >= table.length) {
            return false;
        }

        Entry e = table[i];

        if (e == null) {
            return false;
        }

        if (e.getLongValue() == value) {
            table[i] = null;
            return true;
        }

        return false;
    }

    @Override
    public long replace(short key, long value) {
        Entry e = getEntry(key);

        return e == null ? defaultReturnValue() : e.setValue(value);
    }

    @Override
    public long computeIfAbsent(short key, Short2LongFunction mappingFunction) {
        Preconditions.checkNotNull(mappingFunction);

        Entry e = getEntry(key);

        if (e == null) {
            long v = mappingFunction.get(key);
            put(key, v);
            return v;
        }

        return e.getLongValue();
    }

    @Override
    public long put(short key, long value) {
        int i = Short.toUnsignedInt(key);
        table = ObjectArrays.ensureCapacity(table, i);
        Entry e = table[i];

        if (e == null) {
            table[i] = e = new Entry(key);
        }

        return e.setValue(value);
    }

    @Override
    public long get(short key) {
        return getOrDefault(key, defaultReturnValue());
    }

    private final class Entry implements Short2LongMap.Entry {
        private final short key;
        private long value = defaultReturnValue();

        private Entry(short key) {
            this.key = key;
        }

        @Override
        public short getShortKey() {
            return key;
        }

        @Override
        public long getLongValue() {
            return value;
        }

        @Override
        public long setValue(long value) {
            long ret = this.value;
            this.value = value;
            return ret;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;

            if (!(o instanceof Map.Entry<?, ?> entry))
                return false;

            return entry instanceof Short2LongMap.Entry e
                    ? getShortKey() == e.getShortKey() && getLongValue() == e.getLongValue()
                    : getKey().equals(entry.getKey()) && getValue().equals(entry.getValue());
        }

        @Override
        public int hashCode() {
            return Short.hashCode(getShortKey()) ^ Long.hashCode(getLongValue());
        }

        @Override
        public String toString() {
            return getShortKey() + "=" + getLongValue();
        }
    }

    private final class EntrySet extends AbstractObjectSet<Short2LongMap.Entry> {
        @Override
        @SuppressWarnings("unchecked")
        public ObjectIterator<Short2LongMap.Entry> iterator() {
            return ObjectIterators.asObjectIterator((Iterator<Short2LongMap.Entry>) (Object)
                    Iterators.filter(ArrayUtil.asList(table).iterator(), Objects::nonNull));
        }

        @Override
        public boolean add(Short2LongMap.Entry entry) {
            return Short2LongTableMap.this.put(entry.getShortKey(), entry.getLongValue()) != entry.getLongValue();
        }

        @Override
        public boolean remove(Object o) {
            return o instanceof Map.Entry<?, ?> entry && (entry instanceof Short2LongMap.Entry e
                    ? Short2LongTableMap.this.remove(e.getShortKey(), e.getLongValue())
                    : entry.getKey() instanceof Short && entry.getValue() instanceof Long
                    && Short2LongTableMap.this.remove((short) entry.getKey(), (int) entry.getValue()));
        }

        @Override
        public void clear() {
            Short2LongTableMap.this.clear();
        }

        @Override
        public int size() {
            return Short2LongTableMap.this.size();
        }

        @Override
        public boolean contains(Object o) {
            return o instanceof Map.Entry<?, ?> e && e.getKey() instanceof Short key
                    && e.getValue() instanceof Long value && Short2LongTableMap.this.contains(key, value);
        }
    }
}

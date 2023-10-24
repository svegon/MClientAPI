package com.github.svegon.utils.fast.util.shorts.objects;

import com.github.svegon.utils.collections.ArrayUtil;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;
import it.unimi.dsi.fastutil.objects.*;
import it.unimi.dsi.fastutil.shorts.AbstractShort2ObjectMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectFunction;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import net.jcip.annotations.NotThreadSafe;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

@NotThreadSafe
public class Short2ObjectTableMap<V> extends AbstractShort2ObjectMap<V> {
    private static final Short2ObjectTableMap<?>.Entry[] EMPTY_ENTRY_ARRAY = new Short2ObjectTableMap.Entry[0];

    private Short2ObjectTableMap<?>.Entry[] table = EMPTY_ENTRY_ARRAY;

    public Short2ObjectTableMap() {

    }

    public Short2ObjectTableMap(short maxKey) {
        ensureKeyCapacity(maxKey);
    }

    public Short2ObjectTableMap(Short2ObjectMap<V> original) {
        ObjectSet<Short2ObjectMap.Entry<V>> entrySet = original.short2ObjectEntrySet();
        ObjectIterator<Short2ObjectMap.Entry<V>> itr = entrySet instanceof FastEntrySet fast
                ? fast.fastIterator() : entrySet.iterator();

        while (itr.hasNext()) {
            Short2ObjectMap.Entry<V> entry = itr.next();

            put(entry.getShortKey(), entry.getValue());
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

    @SuppressWarnings("unchecked")
    public boolean contains(short key, V value) {
        int k = Short.toUnsignedInt(key);

        if (table.length >= k) {
            return false;
        }

        Entry e = (Entry) table[k];

        return e != null && e.getValue() == value;
    }

    @SuppressWarnings("unchecked")
    private Entry getEntry(short key) {
        int i = Short.toUnsignedInt(key);

        return i >= table.length ? null : (Entry) table[i];
    }

    @SuppressWarnings("unchecked")
    @Override
    public int size() {
        int size = 0;

        for (Entry entry : (Entry[]) table) {
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
    public ObjectSet<Short2ObjectMap.Entry<V>> short2ObjectEntrySet() {
        return new EntrySet();
    }

    @Override
    public V getOrDefault(short key, final V defaultValue) {
        Entry e = getEntry(key);

        return e != null ? e.getValue() : defaultValue;
    }

    @SuppressWarnings("unchecked")
    @Override
    public V remove(short key) {
        int i = Short.toUnsignedInt(key);

        if (i >= table.length) {
            return defaultReturnValue();
        }

        Entry e = (Entry) table[i];
        table[i] = null;
        return e == null ? defaultReturnValue() : e.getValue();
    }

    @Override
    public V putIfAbsent(short key, final V value) {
        return computeIfAbsent(key, k -> value);
    }

    @Override
    public boolean remove(short key, Object value) {
        int i = Short.toUnsignedInt(key);

        if (i >= table.length) {
            return false;
        }

        @SuppressWarnings("unchecked")
        Entry e = (Entry) table[i];

        if (e == null) {
            return false;
        }

        if (e.getValue() == value) {
            table[i] = null;
            return true;
        }

        return false;
    }

    @Override
    public V replace(short key, V value) {
        Entry e = getEntry(key);

        return e == null ? defaultReturnValue() : e.setValue(value);
    }

    @Override
    public V computeIfAbsent(short key, Short2ObjectFunction<? extends V> mappingFunction) {
        Preconditions.checkNotNull(mappingFunction);

        Entry e = getEntry(key);

        if (e == null) {
            V v = mappingFunction.get(key);
            put(key, v);
            return v;
        }

        return e.getValue();
    }

    @SuppressWarnings("unchecked")
    @Override
    public V put(short key, V value) {
        int i = Short.toUnsignedInt(key);
        table = ObjectArrays.ensureCapacity(table, i);
        Entry e = (Entry) table[i];

        if (e == null) {
            table[i] = e = new Entry(key);
        }

        return e.setValue(value);
    }

    @Override
    public V get(short key) {
        return getOrDefault(key, defaultReturnValue());
    }

    private final class Entry implements Short2ObjectMap.Entry<V> {
        private final short key;
        private V value = defaultReturnValue();

        private Entry(short key) {
            this.key = key;
        }

        @Override
        public short getShortKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V ret = this.value;
            this.value = value;
            return ret;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;

            if (!(o instanceof Map.Entry<?, ?> entry))
                return false;

            return Objects.equals(getValue(), entry.getValue()) && entry instanceof Short2ObjectMap.Entry e
                    ? getShortKey() == e.getShortKey() : getKey().equals(entry.getKey());
        }

        @Override
        public int hashCode() {
            return Short.hashCode(getShortKey()) ^ Objects.hashCode(getValue());
        }

        @Override
        public String toString() {
            return getShortKey() + "=" + getValue();
        }
    }

    private final class EntrySet extends AbstractObjectSet<Short2ObjectMap.Entry<V>> {
        @Override
        @SuppressWarnings("unchecked")
        public ObjectIterator<Short2ObjectMap.Entry<V>> iterator() {
            return ObjectIterators.asObjectIterator((Iterator<Short2ObjectMap.Entry<V>>) (Object)
                    Iterators.filter(ArrayUtil.asList(table).iterator(), Objects::nonNull));
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean add(Short2ObjectMap.Entry entry) {
            return Short2ObjectTableMap.this.put(entry.getShortKey(), (V) entry.getValue()) != entry.getValue();
        }

        @Override
        public boolean remove(Object o) {
            return o instanceof Map.Entry<?, ?> entry && (entry instanceof Short2ObjectMap.Entry e
                    ? Short2ObjectTableMap.this.remove(e.getShortKey(), e.getValue())
                    : entry.getKey() instanceof Short
                    && Short2ObjectTableMap.this.remove((short) entry.getKey(), entry.getValue()));
        }

        @Override
        public void clear() {
            Short2ObjectTableMap.this.clear();
        }

        @Override
        public int size() {
            return Short2ObjectTableMap.this.size();
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean contains(Object o) {
            return o instanceof Map.Entry<?, ?> e && e.getKey() instanceof Short key
                    && Short2ObjectTableMap.this.contains(key, (V) e.getValue());
        }
    }
}

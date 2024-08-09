package io.github.svegon.utils.fast.util.ints.objects;

import io.github.svegon.utils.collections.ArrayUtil;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;
import it.unimi.dsi.fastutil.objects.*;
import it.unimi.dsi.fastutil.ints.AbstractInt2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.jcip.annotations.NotThreadSafe;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

@NotThreadSafe
public class Int2ObjectTableMap<V> extends AbstractInt2ObjectMap<V> {
    private static final Int2ObjectTableMap.Entry[] EMPTY_ENTRY_ARRAY = new Int2ObjectTableMap.Entry[0];

    private Entry[] table = EMPTY_ENTRY_ARRAY;

    public Int2ObjectTableMap() {

    }

    public Int2ObjectTableMap(int maxKey) {
        ensureKeyCapacity(maxKey);
    }

    public Int2ObjectTableMap(Int2ObjectMap<V> original) {
        ObjectSet<Int2ObjectMap.Entry<V>> entrySet = original.int2ObjectEntrySet();
        ObjectIterator<Int2ObjectMap.Entry<V>> itr = entrySet instanceof FastEntrySet fast
                ? fast.fastIterator() : entrySet.iterator();

        while (itr.hasNext()) {
            Int2ObjectMap.Entry<V> entry = itr.next();

            put(entry.getIntKey(), entry.getValue());
        }
    }

    public void ensureKeyCapacity(int key) {
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

    public boolean contains(int key, V value) {
        if (table.length >= key) {
            return false;
        }

        Entry e = table[key];

        return e != null && e.getValue() == value;
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
    public ObjectSet<Int2ObjectMap.Entry<V>> int2ObjectEntrySet() {
        return new EntrySet();
    }

    @Override
    public V getOrDefault(int key, final V defaultValue) {
        Entry e = getEntry(key);

        return e != null ? e.getValue() : defaultValue;
    }

    @Override
    public V remove(int key) {
        if (key < 0 || key >= table.length) {
            return defaultReturnValue();
        }

        Entry e = table[key];
        table[key] = null;
        return e == null ? defaultReturnValue() : e.getValue();
    }

    @Override
    public V putIfAbsent(int key, final V value) {
        return computeIfAbsent(key, k -> value);
    }

    @Override
    public boolean remove(int key, Object value) {
        if (key < 0 || key >= table.length) {
            return false;
        }

        Entry e = table[key];

        if (e == null) {
            return false;
        }

        if (e.getValue() == value) {
            table[key] = null;
            return true;
        }

        return false;
    }

    @Override
    public V replace(int key, V value) {
        Entry e = getEntry(key);

        return e == null ? defaultReturnValue() : e.setValue(value);
    }

    @Override
    public V computeIfAbsent(int key, Int2ObjectFunction<? extends V> mappingFunction) {
        Preconditions.checkNotNull(mappingFunction);

        Entry e = getEntry(key);

        if (e == null) {
            V v = mappingFunction.get(key);
            put(key, v);
            return v;
        }

        return e.getValue();
    }

    @Override
    public V put(int key, V value) {
        table = ObjectArrays.ensureCapacity(table, key);
        Entry e = table[key];

        if (e == null) {
            table[key] = e = new Entry(key);
        }

        return e.setValue(value);
    }

    @Override
    public V get(int key) {
        return getOrDefault(key, defaultReturnValue());
    }

    public Entry getEntry(int key) {
        return 0 < key && key < table.length ? table[key] : null;
    }

    public final class Entry implements Int2ObjectMap.Entry<V> {
        private final int key;
        private V value = defaultReturnValue();

        private Entry(int key) {
            this.key = key;
        }

        @Override
        public int getIntKey() {
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

            return Objects.equals(getValue(), entry.getValue()) && entry instanceof Int2ObjectMap.Entry e
                    ? getIntKey() == e.getIntKey() : getKey().equals(entry.getKey());
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(getIntKey()) ^ Objects.hashCode(getValue());
        }

        @Override
        public String toString() {
            return getIntKey() + "=" + getValue();
        }
    }

    private final class EntrySet extends AbstractObjectSet<Int2ObjectMap.Entry<V>> {
        @Override
        @SuppressWarnings("unchecked")
        public ObjectIterator<Int2ObjectMap.Entry<V>> iterator() {
            return ObjectIterators.asObjectIterator((Iterator<Int2ObjectMap.Entry<V>>) (Object)
                    Iterators.filter(ArrayUtil.asList(table).iterator(), Objects::nonNull));
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean add(Int2ObjectMap.Entry entry) {
            return Int2ObjectTableMap.this.put(entry.getIntKey(), (V) entry.getValue()) != entry.getValue();
        }

        @Override
        public boolean remove(Object o) {
            return o instanceof Map.Entry<?, ?> entry && (entry instanceof Int2ObjectMap.Entry e
                    ? Int2ObjectTableMap.this.remove(e.getIntKey(), e.getValue())
                    : entry.getKey() instanceof Integer
                    && Int2ObjectTableMap.this.remove((int) entry.getKey(), entry.getValue()));
        }

        @Override
        public void clear() {
            Int2ObjectTableMap.this.clear();
        }

        @Override
        public int size() {
            return Int2ObjectTableMap.this.size();
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean contains(Object o) {
            return o instanceof Map.Entry<?, ?> e && e.getKey() instanceof Integer key
                    && Int2ObjectTableMap.this.contains(key, (V) e.getValue());
        }
    }
}

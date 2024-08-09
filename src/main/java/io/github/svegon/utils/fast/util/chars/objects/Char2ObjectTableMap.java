package io.github.svegon.utils.fast.util.chars.objects;

import io.github.svegon.utils.collections.iteration.IterationUtil;
import it.unimi.dsi.fastutil.chars.*;
import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectArrays;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import oshi.annotation.concurrent.NotThreadSafe;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.IntFunction;

@NotThreadSafe
public class Char2ObjectTableMap<V> extends AbstractChar2ObjectMap<V> {
    private static final Object NULL_MASK = new Object();

    private final ObjectSet<Char2ObjectMap.Entry<V>> entrySet =
            (ObjectSet<Char2ObjectMap.Entry<V>>) (Object) new EntrySet();
    private Object[] table;

    public Char2ObjectTableMap() {
        table = ObjectArrays.DEFAULT_EMPTY_ARRAY;
    }

    public Char2ObjectTableMap(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("negative capacity: " + capacity);
        } else if (capacity == 0) {
            table = ObjectArrays.DEFAULT_EMPTY_ARRAY;
        } else {
            table = new Object[capacity];
        }
    }

    public void trimToSize() {
        int i = table.length;

        while (i-- > 0 && table[i] == null);

        if (i != table.length) {
            table = Arrays.copyOf(table, i);
        }
    }

    public void ensureCapacity(char capacity) {
        if (table.length < capacity) {
            table = Arrays.copyOf(table, capacity);
        }
    }

    @SuppressWarnings("unchecked")
    private V getMasked(char key) {
        return key < table.length ? (V) table[key] : null;
    }

    @Override
    public int size() {
        return char2ObjectEntrySet().size();
    }

    @Override
    public boolean containsKey(char k) {
        return getMasked(k) != null;
    }

    @Override
    public void clear() {
        table = ObjectArrays.EMPTY_ARRAY;
    }

    @Override
    public ObjectSet<Char2ObjectMap.Entry<V>> char2ObjectEntrySet() {
        return entrySet;
    }

    @Override
    public V getOrDefault(char key, V defaultValue) {
        V v = getMasked(key);

        return v == null ? defaultValue : unmaskNull(key);
    }

    @Override
    public V remove(char key) {
        V ret = get(key);

        table[key] = null;

        return ret;
    }

    @Override
    public V putIfAbsent(char key, V value) {
        V v = getMasked(key);

        if (v == null) {
            put(key, value);
            return value;
        }

        return unmaskNull(v);
    }

    @Override
    public boolean remove(char key, Object value) {
        V v = getMasked(key);

        if (v == null) {
            return false;
        }

        v = unmaskNull(v);

        if (Objects.equals(v, value)) {
            remove(key);
            return true;
        }

        return false;
    }

    @Override
    public boolean replace(char key, V oldValue, V newValue) {
        V v = getMasked(key);

        if (v == null) {
            return false;
        }

        v = unmaskNull(v);

        if (Objects.equals(v, oldValue)) {
            put(key, newValue);
            return true;
        }

        return false;
    }

    @Override
    public V computeIfAbsent(char key, IntFunction<? extends V> mappingFunction) {
        V v = getMasked(key);

        if (v == null) {
            V value = mappingFunction.apply(key);
            put(key, value);
            return value;
        }

        return unmaskNull(v);
    }

    @Override
    public V computeIfAbsent(char key, Char2ObjectFunction<? extends V> mappingFunction) {
        V v = getMasked(key);

        if (v == null) {
            V value = mappingFunction.get(key);
            put(key, value);
            return value;
        }

        return unmaskNull(v);
    }

    @Override
    public V computeIfPresent(char key, BiFunction<? super Character, ? super V, ? extends V> remappingFunction) {
        V v = getMasked(key);

        if (v != null) {
            V value = remappingFunction.apply(key, unmaskNull(v));
            put(key, value);
            return value;
        }

        return defaultReturnValue();
    }

    @Override
    public V compute(char key, BiFunction<? super Character, ? super V, ? extends V> remappingFunction) {
        V v = getMasked(key);
        V value = remappingFunction.apply(key, unmaskNull(v));

        put(key, value);
        return value;
    }

    @Override
    public V merge(char key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        V v = getMasked(key);
        V newValue = v == null ? value : remappingFunction.apply(unmaskNull(v), value);

        if (newValue == null) {
            remove(key);
        } else {
            put(key, newValue);
        }

        return newValue;
    }

    @Override
    public V put(char key, V value) {
        V v = getMasked(key);

        if (v == null) {
            ensureCapacity(key);
            table[key] = value;

            return defaultReturnValue();
        }

        table[key] = value;

        return unmaskNull(v);
    }

    @Override
    public V get(char key) {
        V v = getMasked(key);

        return v == null ? defaultReturnValue() : unmaskNull(v);
    }

    private static Object maskNull(Object o) {
        return o == null ? NULL_MASK : o;
    }

    @SuppressWarnings("unchecked")
    private static <V> V unmaskNull(Object o) {
        return o == NULL_MASK ? null : (V) o;
    }

    private final class KeySet extends AbstractCharSet {
        @Override
        public CharIterator iterator() {
            return IterationUtil.filter(CharIterators.fromTo((char) 0, (char) table.length),
                    (c) -> c < table.length && table[c] != null);
        }

        @Override
        public int size() {
            return (int) CharIterables.size(this);
        }

        @Override
        public boolean contains(char k) {
            return containsKey(k);
        }
    }

    private final class EntrySet extends AbstractObjectSet<Entry> {
        @Override
        public ObjectIterator<Entry> iterator() {
            return IterationUtil.mapToObj(keySet().iterator(), Entry::new);
        }

        @Override
        public int size() {
            return keySet().size();
        }

        @Override
        public boolean contains(Object o) {
            return o instanceof Map.Entry e && e.getKey() instanceof Character c
                    && Objects.equals(get((char) c), e.getValue());
        }

        @Override
        public boolean add(Entry entry) {
            V v = entry.getValue();
            return put(entry.getCharKey(), v) != v;
        }
    }

    private final class Entry implements Char2ObjectMap.Entry<V> {
        private final char key;

        private Entry(char key) {
            this.key = key;
        }

        @Override
        public char getCharKey() {
            return key;
        }

        @Override
        public V getValue() {
            return get(key);
        }

        @Override
        public V setValue(V value) {
            return put(key, value);
        }
    }
}

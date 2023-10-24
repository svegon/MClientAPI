package com.github.svegon.utils.hash;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;
import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.Size64;
import it.unimi.dsi.fastutil.objects.*;
import net.jcip.annotations.NotThreadSafe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.github.svegon.utils.collections.ArrayUtil;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Stream;

/**
 * a different implementaton of accessing items via their hash
 * It is unsynchronized and is likely to break if accessed from multiple threads.
 *
 * @param <K>
 * @param <V>
 * @param <E>
 */
@NotThreadSafe
public abstract class AbstractForkTable<K, V, E extends Map.Entry<K, V>> extends AbstractMap<K, E>
        implements Size64, Hash {
    public static final int BRANCH_FACTOR_LIMIT = 5;
    public static final int DEFAULT_BRANCH_FACTOR = 4;
    public static final Key2EntryEntry<?, ?, ?>[] EMPTY_ENTRY_ENTRY_ARRAY = new Key2EntryEntry<?, ?, ?>[0];

    private final ObjectSet<E> values = initValues();
    private final ObjectSet<Entry<K, E>> entrySet = initEntrySet();
    private final int branchFactor;
    protected final Object[] tree;
    protected final int depth;
    protected final int depthMinusOne;
    protected final int branchCoveredBits;
    protected final int branching;
    protected final int branchIndexMask;
    protected long size;

    AbstractForkTable(int branchFactor) {
        Preconditions.checkArgument(0 <= branchFactor && branchFactor < BRANCH_FACTOR_LIMIT);

        this.branchFactor = branchFactor;
        this.branchCoveredBits = 1 << branchFactor;
        this.branching = 1 << branchCoveredBits;
        this.branchIndexMask = branching - 1;
        this.depth = 1 << (BRANCH_FACTOR_LIMIT - branchFactor);
        this.depthMinusOne = depth - 1;
        this.tree = new Object[branching];
    }

    @Override
    public final boolean remove(Object key, Object value) {
        E e = AbstractForkTable.this.get(key);

        if (e != null && e.equals(value)) {
            AbstractForkTable.this.remove(key);
            return true;
        }

        return false;
    }

    @Deprecated
    @Override
    public final int size() {
        return Size64.super.size();
    }

    /**
     * The size may be up to 2 ** 32
     *
     * @return the size as longs
     */
    @Override
    public long size64() {
        return size;
    }

    @Override
    public final boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public abstract E get(final @Nullable Object key);

    @Override
    public final E put(@Nullable K key, final @NotNull E value) {
        return value.getKey() == key ? put(value) : null;
    }

    @Override
    public abstract E remove(final @Nullable Object key);

    @Override
    public final void clear() {
        ArrayUtil.clear(tree);
    }

    @NotNull
    @Override
    public ObjectSet<E> values() {
        return values;
    }

    @Override
    public ObjectSet<Entry<K, E>> entrySet() {
        return entrySet;
    }

    @Override
    public final E getOrDefault(Object key, E defaultValue) {
        E value = get(key);

        return value != null ? value : defaultValue;
    }

    @Override
    public final void replaceAll(final @NotNull BiFunction<? super K, ? super E, ? extends E> function) {
        Preconditions.checkNotNull(function);

        for (E entry : values()) {
            entry.setValue(function.apply(entry.getKey(), entry).getValue());
        }
    }

    @Nullable
    @Override
    public final E putIfAbsent(final @Nullable K key, final @NotNull E value) {
        return computeIfAbsent(key, (k) -> value);
    }

    @Override
    public final boolean replace(K key, E oldValue, E newValue) {
        E curValue = get(key);

        if (!Objects.equals(curValue, oldValue)) {
            return false;
        }

        put(key, newValue);
        return true;
    }

    @Nullable
    @Override
    public final E replace(K key, E value) {
        E curValue = null;

        if (containsKey(key)) {
            curValue = put(key, value);
        }

        return curValue;
    }

    protected abstract Key2EntryEntry<K, V, E> newK2EEntry(E entry);

    protected ObjectSet<E> initValues() {
        return new AbstractObjectSet<E>() {
            @SuppressWarnings("unchecked")
            public ObjectIterator<E> iterator() {
                Stream<Object> stream = Arrays.stream(tree);

                for (int i = 0; i < depthMinusOne; i++) {
                    stream = stream.flatMap((o) -> o != null ? Arrays.stream((Object[]) o) : null);
                }

                return ObjectIterators.asObjectIterator(Iterators.concat(stream.filter(Objects::nonNull)
                        .map((o) -> ((List<E>) o).listIterator()).iterator()));
            }

            public int size() {
                return AbstractForkTable.this.size();
            }

            public boolean isEmpty() {
                return AbstractForkTable.this.isEmpty();
            }

            @Override
            public boolean add(E e) {
                return !Objects.equals(e, AbstractForkTable.this.put(e.getKey(), e));
            }

            @Override
            public boolean remove(Object o) {
                return o instanceof Entry entry
                        && AbstractForkTable.this.remove(entry.getKey(), entry.getValue());
            }

            public void clear() {
                AbstractForkTable.this.clear();
            }

            public boolean contains(Object v) {
                return AbstractForkTable.this.containsValue(v);
            }
        };
    }

    protected ObjectSet<Entry<K, E>> initEntrySet() {
        return new AbstractObjectSet<>() {
            @Override
            public int size() {
                return AbstractForkTable.this.size();
            }

            @Override
            public boolean isEmpty() {
                return AbstractForkTable.this.isEmpty();
            }

            @Override
            public boolean contains(Object o) {
                if (!(o instanceof Entry<?, ?> entry)) {
                    return false;
                }

                return Objects.equals(AbstractForkTable.this.get(entry), entry.getValue());
            }

            @Override
            public ObjectIterator<Entry<K, E>> iterator() {
                return ObjectIterators.asObjectIterator(Iterators.transform(values().iterator(), e -> newK2EEntry(e)));
            }

            @Override
            public boolean remove(Object o) {
                return o instanceof Entry<?, ?> entry
                        && AbstractForkTable.this.remove(entry.getKey(), entry.getValue());
            }

            @Override
            public void clear() {
                AbstractForkTable.this.clear();
            }
        };
    }

    public final int branchFactor() {
        return branchFactor;
    }

    @SuppressWarnings("unchecked")
    public final void rehash() {
        Key2EntryEntry<K, V, E>[] entries = (Key2EntryEntry<K, V, E>[]) entrySet().toArray(EMPTY_ENTRY_ENTRY_ARRAY);

        clear();

        for (Key2EntryEntry<K, V, E> entry : entries) {
            put((E) entry);
        }
    }

    public abstract E put(E entry);

    public static abstract class Key2EntryEntry<K, V, E extends Entry<K, V>>
            implements Object2ObjectMap.Entry<K, E> {
        protected final E entry;

        public Key2EntryEntry(E entry) {
            this.entry = entry;
        }

        @Override
        public final E getValue() {
            return entry;
        }

        /**
         * Doesn't technically return the old value since the value
         * doesn't change and is only modified.
         *
         * @param value
         * @return
         */
        @Override
        public final E setValue(E value) {
            if (keysEqual(value)) {
                entry.setValue(value.getValue());
            }

            return entry;
        }

        @Override
        @SuppressWarnings("unchecked")
        public final boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof Entry<?, ?> entry)) {
                return false;
            }

            try {
                return keysEqual((Entry<K, ?>) entry) && Objects.equals(getValue(), entry.getValue());
            } catch (ClassCastException e) {
                return false;
            }
        }

        @Override
        public final int hashCode() {
            return keyHashCode() ^ Objects.hashCode(getValue());
        }

        @Override
        public final String toString() {
            return getValue().toString();
        }

        protected abstract boolean keysEqual(Entry<K, ?> value);

        protected abstract int keyHashCode();
    }
}

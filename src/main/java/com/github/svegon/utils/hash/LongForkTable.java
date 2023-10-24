package com.github.svegon.utils.hash;

import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.longs.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class LongForkTable<V, E extends LongForkTable.Entry<V, E>>
        extends AbstractForkTable<Long, V, E> implements Long2ObjectMap<E> {
    private final LongSet keySet = initKeySet();
    private final ObjectSet<Long2ObjectMap.Entry<E>> entrySet = initLongEntrySet();
    private final LongHash.Strategy strategy;

    public LongForkTable(int branchFactor, @Nullable LongHash.Strategy strategy) {
        super(branchFactor);
        this.strategy = strategy != null ? strategy : HashUtil.defaultLongStrategy();
    }

    public LongForkTable(@Nullable LongHash.Strategy strategy) {
        this(DEFAULT_BRANCH_FACTOR, strategy);
    }

    public LongForkTable(int branchFactor) {
        this(branchFactor, null);
    }

    public LongForkTable() {
        this(null);
    }

    @Deprecated
    @Override
    public E get(Object key) {
        return key instanceof Long ? get((long) key) : null;
    }

    @Deprecated
    @Override
    public E remove(Object key) {
        return key instanceof Long ? remove((long) key) : defaultReturnValue();
    }

    @Override
    protected Long2EntryEntry<V, E> newK2EEntry(E entry) {
        return new Long2EntryEntry<V, E>(this, entry);
    }

    @Override
    @SuppressWarnings("unchecked")
    public E get(long key) {
        Object e = tree;
        int hash = strategy.hashCode(key);

        for (int i = 0; i < depth && e != null; i++) {
            e = ((Object[]) e)[hash & branchIndexMask];
            hash >>>= branchCoveredBits;
        }

        if (e != null) {
            for (E entry : (List<E>) e) {
                if (strategy.equals(key, entry.getLongKey())) {
                    return entry;
                }
            }
        }

        return null;
    }

    @Override
    public E put(long key, final @NotNull E value) {
        return value.getLongKey() == key ? put(value) : null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E remove(long key) {
        Object[] e = tree;
        int hash = strategy.hashCode(key);
        int h = hash;

        for (int i = 0; i < depth - 1; i++) {
            int temp = h & branchIndexMask;

            if (e[temp] == null) {
                e[temp] = new Object[branching];
            }

            e = (Object[]) e[temp];
            h >>>= branchCoveredBits;
        }

        int temp = h & branchIndexMask;

        if (e[temp] == null) {
            e[temp] = new Vector<>();
        }

        ListIterator<E> itr = ((List<E>) e[temp]).listIterator();

        while (itr.hasNext()) {
            E entry = itr.next();

            if (entry.hash == hash && strategy.equals(key, entry.getLongKey())) {
                itr.remove();
                size--;

                return entry;
            }
        }

        return null;
    }

    @Override
    public E getOrDefault(long key, final @Nullable E defaultValue) {
        E value = get(key);

        return value != null ? value : defaultValue;
    }

    @Override
    public E putIfAbsent(long key, final E value) {
        return computeIfAbsent(key, (k) -> value);
    }

    @Override
    public boolean replace(long key, E oldValue, E newValue) {
        E curValue = get(key);

        if (!Objects.equals(curValue, oldValue)) {
            return false;
        }

        put(key, newValue);
        return true;
    }

    @Override
    public E replace(long key, E value) {
        E curValue = null;

        if (containsKey(key)) {
            curValue = put(key, value);
        }

        return curValue;
    }

    @Override
    public void defaultReturnValue(E rv) {
        if (rv != defaultReturnValue()) {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public E defaultReturnValue() {
        return null;
    }

    @NotNull
    @Override
    public LongSet keySet() {
        return keySet;
    }

    @Override
    public ObjectSet<Long2ObjectMap.Entry<E>> long2ObjectEntrySet() {
        return entrySet;
    }

    @Override
    public boolean containsKey(long key) {
        return get(key) != null;
    }

    @SuppressWarnings("unchecked")
    public E put(final @NotNull E entry) {
        long key = entry.getLongKey();
        int hash = strategy.hashCode(key);
        Object[] e = tree;
        int h = hash;

        for (int i = 0; i < depthMinusOne; i++) {
            int temp = h & branchIndexMask;

            if (((Object[]) e)[temp] == null) {
                ((Object[]) e)[temp] = new Object[branching];
            }

            e = (Object[]) ((Object[]) e)[temp];
            h >>>= branchCoveredBits;
        }

        int temp = h & branchIndexMask;

        if (((Object[]) e)[temp] == null) {
            ((Object[]) e)[temp] = new Vector<>();
        }

        List<E> list = (List<E>) ((Object[]) e)[temp];
        ListIterator<E> itr = list.listIterator();

        while (itr.hasNext()) {
            E entry0 = itr.next();

            if (entry0.hash == hash && strategy.equals(key, entry0.getLongKey())) {
                itr.set(entry);
                return entry0;
            }
        }

        list.add(entry);
        size++;

        return null;
    }

    public LongHash.Strategy getStrategy() {
        return strategy;
    }

    private LongSet initKeySet() {
        return new KeySet<>(this);
    }

    private ObjectSet<Long2ObjectMap.Entry<E>> initLongEntrySet() {
        return new EntrySet<>(this);
    }

    public static abstract class Entry<V, E extends Entry<V, E>> implements Long2ObjectMap.Entry<V> {
        final LongForkTable<V, E> table;
        final long key;
        final int hash;

        protected Entry(LongForkTable<V, E> table, int hash, long key) {
            this.table = table;
            this.key = key;
            this.hash = hash;
        }

        @Override
        public final long getLongKey() {
            return key;
        }

        @Deprecated
        @Override
        public final Long getKey() {
            return Long2ObjectMap.Entry.super.getKey();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof Long2ObjectMap.Entry entry)) {
                return false;
            }

            return table.strategy.equals(getKey(), entry.getLongKey()) && Objects.equals(getValue(), entry.getValue());
        }

        @Override
        public int hashCode() {
            return hash ^ Objects.hashCode(getValue());
        }
    }

    private static final class Long2EntryEntry<V, E extends Entry<V, E>>
            extends Key2EntryEntry<Long, V, E> implements Long2ObjectMap.Entry<E> {
        private final LongForkTable<V, E> ref;

        public Long2EntryEntry(LongForkTable<V, E> ref, E entry) {
            super(entry);
            this.ref = ref;
        }

        @Override
        public long getLongKey() {
            return entry.getLongKey();
        }

        @Override
        protected boolean keysEqual(Map.Entry<Long, ?> value) {
            return ref.strategy.equals(value instanceof Long2ObjectMap.Entry<?>
                    ? ((Long2ObjectMap.Entry<?>) value).getLongKey() : value.getKey(), getLongKey());
        }

        @Override
        protected int keyHashCode() {
            return entry.hash;
        }
    }

    private static final class KeySet<V, E extends Entry<V, E>> extends AbstractLongSet {
        private final LongForkTable<V, E> ref;

        private KeySet(LongForkTable<V, E> ref) {
            this.ref = ref;
        }

        public @NotNull LongIterator iterator() {
            return new LongIterator() {
                private Iterator<Long2ObjectMap.Entry<E>> i = ref.long2ObjectEntrySet().iterator();

                public boolean hasNext() {
                    return i.hasNext();
                }

                @Override
                public long nextLong() {
                    return i.next().getLongKey();
                }

                public void remove() {
                    i.remove();
                }
            };
        }

        public int size() {
            return ref.size();
        }

        public boolean isEmpty() {
            return ref.isEmpty();
        }

        public void clear() {
            ref.clear();
        }

        public boolean contains(Object k) {
            return ref.containsKey(k);
        }
    }

    private static final class EntrySet<V, E extends Entry<V, E>> extends AbstractObjectSet<Long2ObjectMap.Entry<E>> {
        private final LongForkTable<V, E> ref;

        private EntrySet(LongForkTable<V, E> ref) {
            this.ref = ref;
        }

        @Override
        public int size() {
            return ref.size();
        }

        @Override
        public boolean isEmpty() {
            return ref.isEmpty();
        }

        @Override
        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry<?, ?> entry)) {
                return false;
            }

            return Objects.equals(ref.get(entry.getKey()), entry.getValue());
        }

        @Override
        public ObjectIterator<Long2ObjectMap.Entry<E>> iterator() {
            return new ObjectIterator<>() {
                final int[] nestedArrayIndexes = new int[ref.depth];
                Long2EntryEntry<V, E> prev;
                Long2EntryEntry<V, E> next;

                @Override
                public boolean hasNext() {
                    if (next == null) {
                        computeNext();
                    }

                    return next != null;
                }

                @Override
                public synchronized Long2EntryEntry<V, E> next() {
                    if (next == null) {
                        computeNext();

                        if (next == null) {
                            throw new NoSuchElementException();
                        }
                    }

                    prev = next;
                    next = null;
                    return prev;
                }

                @Override
                public void remove() {
                    Long2EntryEntry<V, E> p;

                    if ((p = prev) == null) {
                        throw new IllegalStateException();
                    }

                    ref.remove(p.getKey());
                }

                @SuppressWarnings("unchecked")
                private synchronized void computeNext() {
                    if (next != null) {
                        return;
                    }

                    nestedArrayIndexes[nestedArrayIndexes.length - 1]++;
                    Object e = ref.tree;

                    for (int i = 0; i < nestedArrayIndexes.length; i++) {
                        while (nestedArrayIndexes[i] < ref.branching) {
                            if (((Object[]) e)[nestedArrayIndexes[i]] != null) {
                                e = ((Object[]) e)[nestedArrayIndexes[i]];
                                break;
                            } else {
                                nestedArrayIndexes[i]++;
                            }
                        }

                        if (nestedArrayIndexes[i] == ref.branching) {
                            if (i == 0) {
                                return;
                            }

                            nestedArrayIndexes[i--] = 0;
                            nestedArrayIndexes[i]++;
                        }
                    }

                    next = ref.newK2EEntry((E) e);
                }
            };
        }

        @Override
        public boolean remove(Object o) {
            if (!(o instanceof Map.Entry<?, ?> entry)) {
                return false;
            }

            return ref.remove(entry.getKey(), entry.getValue());
        }

        @Override
        public void clear() {
            ref.clear();
        }
    }
}

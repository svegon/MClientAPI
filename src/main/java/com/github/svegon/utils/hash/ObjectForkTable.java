package com.github.svegon.utils.hash;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import org.jetbrains.annotations.NotNull;
import com.github.svegon.utils.collections.ListUtil;

import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;

public final class ObjectForkTable<K, V, E extends ObjectForkTable.Entry<K, V, E>>
        extends AbstractForkTable<K, V, E> implements Object2ObjectMap<K, E> {
    private final ObjectSet<K> keySet = new KeySet<>(this);
    private final Hash.Strategy<? super K> strategy;

    public ObjectForkTable(int branchFactor, Hash.Strategy<? super K> strategy) {
        super(branchFactor);
        this.strategy = strategy != null ? strategy : HashUtil.defaultStrategy();
    }

    public ObjectForkTable(Hash.Strategy<? super K> strategy) {
        this(DEFAULT_BRANCH_FACTOR, strategy);
    }

    public ObjectForkTable(int branchFactor) {
        this(branchFactor, null);
    }

    public ObjectForkTable() {
        this(null);
    }

    @SuppressWarnings("unchecked")
    public List<E> getAll(K key) {
        Object e = tree;
        int hash = strategy.hashCode(key);

        for (int i = 0; i < depth && e != null; i++) {
            e = ((Object[]) e)[hash & branchIndexMask];
            hash >>>= branchCoveredBits;
        }

        if (e != null) {
            return (List<E>) e;
        }

        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E get(Object key) {
        Object e = tree;
        K k = (K) key;
        int hash;

        try {
            hash = strategy.hashCode(k);
        } catch (ClassCastException notKeyClass) {
            return null;
        }

        int h = hash;

        for (int i = 0; i < depth && e != null; i++) {
            e = ((Object[]) e)[h & branchIndexMask];
            h >>>= branchCoveredBits;
        }

        if (e != null) {
            for (E entry : (List<E>) e) {
                if (entry.hash == hash && strategy.equals(k, entry.getKey())) {
                    return entry;
                }
            }
        }

        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E remove(Object key) {
        Object[] e = tree;
        K k;
        int hash;

        try {
            k = (K) key;
            hash = strategy.hashCode(k);
        } catch (ClassCastException invalidClass) {
            return defaultReturnValue();
        }

        int h = hash;

        for (int i = 0; i < depth - 1; i++) {
            int temp = h & branchIndexMask;

            if (e[temp] == null) {
                return defaultReturnValue();
            }

            e = (Object[]) e[temp];
            h >>>= branchCoveredBits;
        }

        if (e[h] == null) {
            return defaultReturnValue();
        }

        ListIterator<E> itr = ((List<E>) e[h]).listIterator();

        while (itr.hasNext()) {
            E entry = itr.next();

            if (entry.hash == hash && strategy.equals(k, entry.getKey())) {
                itr.remove();
                size--;

                return entry;
            }
        }

        return defaultReturnValue();
    }

    @Override
    protected Object2EntryEntry<K, V, E> newK2EEntry(E entry) {
        return new Object2EntryEntry<>(this, entry);
    }

    @Override
    public void defaultReturnValue(E rv) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E defaultReturnValue() {
        return null;
    }

    @Override
    public ObjectSet<K> keySet() {
        return keySet;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ObjectSet<Object2ObjectMap.Entry<K, E>> object2ObjectEntrySet() {
        return (ObjectSet<Object2ObjectMap.Entry<K, E>>) (Object) entrySet();
    }

    @Override
    public E put(final @NotNull E entry) {
        assert entry.table == this;

        K key = entry.getKey();
        Object[] e = tree;
        int hash = strategy.hashCode(key);
        int h = hash;

        for (int i = 0; i != depthMinusOne; i++) {
            int index = h & branchIndexMask;

            if (e[index] == null) {
                e[index] = new Object[branching];
            }

            e = (Object[]) e[index];
            h >>>= branchCoveredBits;
        }

        if (e[h] == null) {
            e[h] = ListUtil.newSyncedList();
        }

        List<E> list = (List<E>) e[h];
        ListIterator<E> itr = list.listIterator();

        while (itr.hasNext()) {
            E entry0 = itr.next();

            if (hash == entry0.hash && strategy.equals(key, entry0.getKey())) {
                itr.set(entry);
                return entry0;
            }
        }

        list.add(entry);
        size++;

        return null;
    }

    public Hash.Strategy<? super K> getStrategy() {
        return strategy;
    }

    public static abstract class Entry<K, V, E extends Entry<K, V, E>>
            implements Object2ObjectMap.Entry<K, V> {
        final ObjectForkTable<K, V, E> table;
        private final K key;
        protected final int hash;

        public Entry(ObjectForkTable<K, V, E> table, K key) {
            this.table = table;
            this.key = key;
            this.hash = table.strategy.hashCode(key);
        }

        @Override
        public final K getKey() {
            return key;
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof Map.Entry entry)) {
                return false;
            }

            try {
                return table.strategy.equals(getKey(), (K) entry.getKey())
                        && Objects.equals(getValue(), entry.getValue());
            } catch (ClassCastException e) {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return hash ^ Objects.hashCode(getValue());
        }

        public final ObjectForkTable<K, V, E> getTable() {
            return table;
        }

        @Override
        public String toString() {
            return getKey() + "=" + getValue();
        }
    }

    public static final class Object2EntryEntry<K, V, E extends Entry<K, V, E>>
            extends Key2EntryEntry<K, V, E> implements Object2ObjectMap.Entry<K, E> {
        private final ObjectForkTable<K, V, E> ref;

        public Object2EntryEntry(ObjectForkTable<K, V, E> ref, E entry) {
            super(entry);
            this.ref = ref;
        }

        @Override
        protected boolean keysEqual(Map.Entry<K, ?> value) {
            return ref.strategy.equals(getKey(), value.getKey());
        }

        @Override
        protected int keyHashCode() {
            return entry.hash;
        }

        @Override
        public K getKey() {
            return entry.getKey();
        }
    }

    private static final class KeySet<K, V, E extends Entry<K, V, E>>
            extends AbstractObjectSet<K> {
        private final ObjectForkTable<K, V, E> ref;

        private KeySet(ObjectForkTable<K, V, E> ref) {
            this.ref = ref;
        }

        public ObjectIterator<K> iterator() {
            return new ObjectIterator<>() {
                private ObjectIterator<E> i = ref.values().iterator();

                public boolean hasNext() {
                    return i.hasNext();
                }

                public K next() {
                    return i.next().getKey();
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
}

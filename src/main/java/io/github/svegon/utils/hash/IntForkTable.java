package io.github.svegon.utils.hash;

import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.ints.*;
import net.jcip.annotations.NotThreadSafe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@NotThreadSafe
public final class IntForkTable<V, E extends IntForkTable.Entry<V, E>>
        extends AbstractForkTable<Integer, V, E> implements Int2ObjectMap<E> {
    private final IntSet keySet = initKeySet();
    private final ObjectSet<Int2ObjectMap.Entry<E>> entrySet = initIntEntrySet();
    private final IntHash.Strategy strategy;

    public IntForkTable(int branchFactor, @Nullable IntHash.Strategy strategy) {
        super(branchFactor);
        this.strategy = strategy != null ? strategy : HashUtil.defaultIntStrategy();
    }

    public IntForkTable(@Nullable IntHash.Strategy strategy) {
        this(DEFAULT_BRANCH_FACTOR, strategy);
    }

    public IntForkTable(int branchFactor) {
        this(branchFactor, null);
    }

    public IntForkTable() {
        this(null);
    }

    @Deprecated
    @Override
    public E get(Object key) {
        return key instanceof Integer ? get((int) key) : null;
    }

    @Deprecated
    @Override
    public E remove(Object key) {
        return key instanceof Integer ? remove((int) key) : defaultReturnValue();
    }

    @Override
    protected Int2EntryEntry<V, E> newK2EEntry(E entry) {
        return new Int2EntryEntry<V, E>(this, entry);
    }

    @Override
    @SuppressWarnings("unchecked")
    public E get(int key) {
        Object e = tree;
        int hash = strategy.hashCode(key);

        for (int i = 0; i < depth && e != null; i++) {
            e = ((Object[]) e)[hash & branchIndexMask];
            hash >>>= branchCoveredBits;
        }

        if (e != null) {
            for (E entry : (List<E>) e) {
                if (entry.hash == hash && strategy.equals(key, entry.getIntKey())) {
                    return entry;
                }
            }
        }

        return null;
    }

    @Override
    public E put(int key, final @NotNull E value) {
        return value.getIntKey() == key ? put(value) : null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E remove(int key) {
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

            if (entry.hash == hash && strategy.equals(key, entry.getIntKey())) {
                itr.remove();
                size--;

                return entry;
            }
        }

        return null;
    }

    @Override
    public E getOrDefault(int key, final @Nullable E defaultValue) {
        E value = get(key);

        return value != null ? value : defaultValue;
    }

    @Override
    public E putIfAbsent(int key, final E value) {
        return computeIfAbsent(key, (k) -> value);
    }

    @Override
    public boolean replace(int key, E oldValue, E newValue) {
        E curValue = get(key);

        if (!Objects.equals(curValue, oldValue)) {
            return false;
        }

        put(key, newValue);
        return true;
    }

    @Override
    public E replace(int key, E value) {
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
    public IntSet keySet() {
        return keySet;
    }

    @Override
    public ObjectSet<Int2ObjectMap.Entry<E>> int2ObjectEntrySet() {
        return entrySet;
    }

    @Override
    public boolean containsKey(int key) {
        return get(key) != null;
    }

    @SuppressWarnings("unchecked")
    public E put(final @NotNull E entry) {
        int key = entry.getIntKey();
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

            if (entry0.hash == hash && strategy.equals(key, entry0.getIntKey())) {
                itr.set(entry);
                return entry0;
            }
        }

        list.add(entry);
        size++;

        return null;
    }

    public IntHash.Strategy getStrategy() {
        return strategy;
    }

    private IntSet initKeySet() {
        return new KeySet<>(this);
    }

    private ObjectSet<Int2ObjectMap.Entry<E>> initIntEntrySet() {
        return new EntrySet<>(this);
    }

    public static abstract class Entry<V, E extends Entry<V, E>> implements Int2ObjectMap.Entry<V> {
        protected final IntForkTable<V, E> table;
        private final int key;
        protected final int hash;

        protected Entry(IntForkTable<V, E> table, int key) {
            this.table = table;
            this.key = key;
            this.hash = table.strategy.hashCode(key);
        }

        @Override
        public final int getIntKey() {
            return key;
        }

        @Deprecated
        @Override
        public final Integer getKey() {
            return Int2ObjectMap.Entry.super.getKey();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof Map.Entry entry)) {
                return false;
            }

            return entry.getKey() instanceof Integer && table.strategy.equals(getIntKey(), (int) entry.getKey())
                    && Objects.equals(getValue(), entry.getValue());
        }

        @Override
        public int hashCode() {
            return hash ^ Objects.hashCode(getValue());
        }
    }

    private static final class Int2EntryEntry<V, E extends Entry<V, E>>
            extends Key2EntryEntry<Integer, V, E> implements Int2ObjectMap.Entry<E> {
        private final IntForkTable<V, E> ref;

        public Int2EntryEntry(IntForkTable<V, E> ref, E entry) {
            super(entry);
            this.ref = ref;
        }

        @Override
        public int getIntKey() {
            return entry.getIntKey();
        }

        @Override
        protected boolean keysEqual(Map.Entry<Integer, ?> value) {
            return ref.strategy.equals(value instanceof Int2ObjectMap.Entry<?>
                    ? ((Int2ObjectMap.Entry<?>) value).getIntKey() : value.getKey(), getIntKey());
        }

        @Override
        protected int keyHashCode() {
            return entry.hash;
        }
    }

    private static final class KeySet<V, E extends Entry<V, E>> extends AbstractIntSet {
        private final IntForkTable<V, E> ref;

        private KeySet(IntForkTable<V, E> ref) {
            this.ref = ref;
        }

        public @NotNull IntIterator iterator() {
            return new IntIterator() {
                private Iterator<E> i = ref.values().iterator();

                public boolean hasNext() {
                    return i.hasNext();
                }

                @Override
                public int nextInt() {
                    return i.next().getIntKey();
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

    private static final class EntrySet<V, E extends Entry<V, E>> extends AbstractObjectSet<Int2ObjectMap.Entry<E>> {
        private final IntForkTable<V, E> ref;

        private EntrySet(IntForkTable<V, E> ref) {
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
        public ObjectIterator<Int2ObjectMap.Entry<E>> iterator() {
            return new ObjectIterator<>() {
                final int[] nestedArrayIndexes = new int[ref.depth];
                Int2EntryEntry<V, E> prev;
                Int2EntryEntry<V, E> next;

                @Override
                public boolean hasNext() {
                    if (next == null) {
                        computeNext();
                    }

                    return next != null;
                }

                @Override
                public synchronized Int2EntryEntry<V, E> next() {
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
                    Int2EntryEntry<V, E> p;

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

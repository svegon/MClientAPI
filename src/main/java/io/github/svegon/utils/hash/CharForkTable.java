package io.github.svegon.utils.hash;

import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.shorts.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class CharForkTable<V, E extends CharForkTable.Entry<V, E>>
        extends AbstractForkTable<Short, V, E> implements Short2ObjectMap<E> {
    private final ShortSet keySet = initKeySet();
    private final ObjectSet<Short2ObjectMap.Entry<E>> entrySet = initShortEntrySet();
    private final ShortHash.Strategy strategy;

    public CharForkTable(int branchFactor, @Nullable ShortHash.Strategy strategy) {
        super(branchFactor);
        this.strategy = strategy != null ? strategy : HashUtil.defaultShortStrategy();
    }

    public CharForkTable(@Nullable ShortHash.Strategy strategy) {
        this(DEFAULT_BRANCH_FACTOR, strategy);
    }

    public CharForkTable(int branchFactor) {
        this(branchFactor, null);
    }

    public CharForkTable() {
        this(null);
    }

    @Deprecated
    @Override
    public E get(Object key) {
        return key instanceof Short ? get((short) key) : null;
    }

    @Deprecated
    @Override
    public E remove(Object key) {
        return key instanceof Short ? remove((short) key) : defaultReturnValue();
    }

    @Override
    protected Short2EntryEntry<V, E> newK2EEntry(E entry) {
        return new Short2EntryEntry<>(this, entry);
    }

    @Override
    @SuppressWarnings("unchecked")
    public E get(short key) {
        Object e = tree;
        int hash = strategy.hashCode(key);

        for (int i = 0; i < depth && e != null; i++) {
            e = ((Object[]) e)[hash & branchIndexMask];
            hash >>>= branchCoveredBits;
        }

        if (e != null) {
            for (E entry : (List<E>) e) {
                if (strategy.equals(key, entry.getShortKey())) {
                    return entry;
                }
            }
        }

        return null;
    }

    @Override
    public E put(short key, final @NotNull E value) {
        return value.getShortKey() == key ? put(value) : null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E remove(short key) {
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

            if (entry.hash == hash && strategy.equals(key, entry.getShortKey())) {
                itr.remove();
                size--;

                return entry;
            }
        }

        return null;
    }

    @Override
    public E getOrDefault(short key, final @Nullable E defaultValue) {
        E value = get(key);

        return value != null ? value : defaultValue;
    }

    @Override
    public E putIfAbsent(short key, final E value) {
        return computeIfAbsent(key, (k) -> value);
    }

    @Override
    public boolean replace(short key, E oldValue, E newValue) {
        E curValue = get(key);

        if (!Objects.equals(curValue, oldValue)) {
            return false;
        }

        put(key, newValue);
        return true;
    }

    @Override
    public E replace(short key, E value) {
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
    public ShortSet keySet() {
        return keySet;
    }

    @Override
    public ObjectSet<Short2ObjectMap.Entry<E>> short2ObjectEntrySet() {
        return entrySet;
    }

    @Override
    public boolean containsKey(short key) {
        return get(key) != null;
    }

    @SuppressWarnings("unchecked")
    public E put(final @NotNull E entry) {
        short key = entry.getShortKey();
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

            if (entry0.hash == hash && strategy.equals(key, entry0.getShortKey())) {
                itr.set(entry);
                return entry0;
            }
        }

        list.add(entry);
        size++;

        return null;
    }

    public ShortHash.Strategy getStrategy() {
        return strategy;
    }

    private ShortSet initKeySet() {
        return new KeySet<>(this);
    }

    private ObjectSet<Short2ObjectMap.Entry<E>> initShortEntrySet() {
        return new EntrySet<>(this);
    }

    public static abstract class Entry<V, E extends Entry<V, E>> implements Short2ObjectMap.Entry<V> {
        final CharForkTable<V, E> table;
        final short key;
        final int hash;

        protected Entry(CharForkTable<V, E> table, int hash, short key) {
            this.table = table;
            this.key = key;
            this.hash = hash;
        }

        @Override
        public final short getShortKey() {
            return key;
        }

        @Deprecated
        @Override
        public final Short getKey() {
            return Short2ObjectMap.Entry.super.getKey();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof Short2ObjectMap.Entry entry)) {
                return false;
            }

            return table.strategy.equals(getKey(), entry.getShortKey()) && Objects.equals(getValue(), entry.getValue());
        }

        @Override
        public int hashCode() {
            return hash ^ Objects.hashCode(getValue());
        }
    }

    private static final class Short2EntryEntry<V, E extends Entry<V, E>>
            extends Key2EntryEntry<Short, V, E> implements Short2ObjectMap.Entry<E> {
        private final CharForkTable<V, E> ref;

        public Short2EntryEntry(CharForkTable<V, E> ref, E entry) {
            super(entry);
            this.ref = ref;
        }

        @Override
        public short getShortKey() {
            return entry.getShortKey();
        }

        @Override
        protected boolean keysEqual(Map.Entry<Short, ?> value) {
            return ref.strategy.equals(value instanceof Short2ObjectMap.Entry<?>
                    ? ((Short2ObjectMap.Entry<?>) value).getShortKey() : value.getKey(), getShortKey());
        }

        @Override
        protected int keyHashCode() {
            return entry.hash;
        }
    }

    private static final class KeySet<V, E extends Entry<V, E>> extends AbstractShortSet {
        private final CharForkTable<V, E> ref;

        private KeySet(CharForkTable<V, E> ref) {
            this.ref = ref;
        }

        public @NotNull ShortIterator iterator() {
            return new ShortIterator() {
                private Iterator<Short2ObjectMap.Entry<E>> i = ref.short2ObjectEntrySet().iterator();

                public boolean hasNext() {
                    return i.hasNext();
                }

                @Override
                public short nextShort() {
                    return i.next().getShortKey();
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

    private static final class EntrySet<V, E extends Entry<V, E>> extends AbstractObjectSet<Short2ObjectMap.Entry<E>> {
        private final CharForkTable<V, E> ref;

        private EntrySet(CharForkTable<V, E> ref) {
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
        public ObjectIterator<Short2ObjectMap.Entry<E>> iterator() {
            return new ObjectIterator<>() {
                final int[] nestedArrayIndexes = new int[ref.depth];
                Short2EntryEntry<V, E> prev;
                Short2EntryEntry<V, E> next;

                @Override
                public boolean hasNext() {
                    if (next == null) {
                        computeNext();
                    }

                    return next != null;
                }

                @Override
                public synchronized Short2EntryEntry<V, E> next() {
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
                    Short2EntryEntry<V, E> p;

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

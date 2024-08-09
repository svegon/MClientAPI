package io.github.svegon.utils.hash;

import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.floats.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class FloatForkTable<V, E extends FloatForkTable.Entry<V, E>>
        extends AbstractForkTable<Float, V, E> implements Float2ObjectMap<E> {
    private final FloatSet keySet = initKeySet();
    private final ObjectSet<Float2ObjectMap.Entry<E>> entrySet = initFloatEntrySet();
    private final FloatHash.Strategy strategy;

    public FloatForkTable(int branchFactor, @Nullable FloatHash.Strategy strategy) {
        super(branchFactor);
        this.strategy = strategy != null ? strategy : HashUtil.defaultFloatStrategy();
    }

    public FloatForkTable(@Nullable FloatHash.Strategy strategy) {
        this(DEFAULT_BRANCH_FACTOR, strategy);
    }

    public FloatForkTable(int branchFactor) {
        this(branchFactor, null);
    }

    public FloatForkTable() {
        this(null);
    }

    @Deprecated
    @Override
    public E get(Object key) {
        return key instanceof Float ? get((float) key) : null;
    }

    @Deprecated
    @Override
    public E remove(Object key) {
        return key instanceof Float ? remove((float) key) : defaultReturnValue();
    }

    @Override
    protected Float2EntryEntry<V, E> newK2EEntry(E entry) {
        return new Float2EntryEntry<V, E>(this, entry);
    }

    @Override
    @SuppressWarnings("unchecked")
    public E get(float key) {
        Object e = tree;
        int hash = strategy.hashCode(key);

        for (int i = 0; i < depth && e != null; i++) {
            e = ((Object[]) e)[hash & branchIndexMask];
            hash >>>= branchCoveredBits;
        }

        if (e != null) {
            for (E entry : (List<E>) e) {
                if (strategy.equals(key, entry.getFloatKey())) {
                    return entry;
                }
            }
        }

        return null;
    }

    @Override
    public E put(float key, final @NotNull E value) {
        return value.getFloatKey() == key ? put(value) : null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E remove(float key) {
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

            if (entry.hash == hash && strategy.equals(key, entry.getFloatKey())) {
                itr.remove();
                size--;

                return entry;
            }
        }

        return null;
    }

    @Override
    public E getOrDefault(float key, final @Nullable E defaultValue) {
        E value = get(key);

        return value != null ? value : defaultValue;
    }

    @Override
    public E putIfAbsent(float key, final E value) {
        return computeIfAbsent(key, (k) -> value);
    }

    @Override
    public boolean replace(float key, E oldValue, E newValue) {
        E curValue = get(key);

        if (!Objects.equals(curValue, oldValue)) {
            return false;
        }

        put(key, newValue);
        return true;
    }

    @Override
    public E replace(float key, E value) {
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
    public FloatSet keySet() {
        return keySet;
    }

    @Override
    public ObjectSet<Float2ObjectMap.Entry<E>> float2ObjectEntrySet() {
        return entrySet;
    }

    @Override
    public boolean containsKey(float key) {
        return get(key) != null;
    }

    @SuppressWarnings("unchecked")
    public E put(final @NotNull E entry) {
        float key = entry.getFloatKey();
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

            if (entry0.hash == hash && strategy.equals(key, entry0.getFloatKey())) {
                itr.set(entry);
                return entry0;
            }
        }

        list.add(entry);
        size++;

        return null;
    }

    public FloatHash.Strategy getStrategy() {
        return strategy;
    }

    private FloatSet initKeySet() {
        return new KeySet<>(this);
    }

    private ObjectSet<Float2ObjectMap.Entry<E>> initFloatEntrySet() {
        return new EntrySet<>(this);
    }

    public static abstract class Entry<V, E extends Entry<V, E>> implements Float2ObjectMap.Entry<V> {
        final FloatForkTable<V, E> table;
        final float key;
        final int hash;

        protected Entry(FloatForkTable<V, E> table, int hash, float key) {
            this.table = table;
            this.key = key;
            this.hash = hash;
        }

        @Override
        public final float getFloatKey() {
            return key;
        }

        @Deprecated
        @Override
        public final Float getKey() {
            return Float2ObjectMap.Entry.super.getKey();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof Float2ObjectMap.Entry entry)) {
                return false;
            }

            return table.strategy.equals(getKey(), entry.getFloatKey()) && Objects.equals(getValue(), entry.getValue());
        }

        @Override
        public int hashCode() {
            return hash ^ Objects.hashCode(getValue());
        }
    }

    private static final class Float2EntryEntry<V, E extends Entry<V, E>>
            extends Key2EntryEntry<Float, V, E> implements Float2ObjectMap.Entry<E> {
        private final FloatForkTable<V, E> ref;

        public Float2EntryEntry(FloatForkTable<V, E> ref, E entry) {
            super(entry);
            this.ref = ref;
        }

        @Override
        public float getFloatKey() {
            return entry.getFloatKey();
        }

        @Override
        protected boolean keysEqual(Map.Entry<Float, ?> value) {
            return ref.strategy.equals(value instanceof Float2ObjectMap.Entry<?>
                    ? ((Float2ObjectMap.Entry<?>) value).getFloatKey() : value.getKey(), getFloatKey());
        }

        @Override
        protected int keyHashCode() {
            return entry.hash;
        }
    }

    private static final class KeySet<V, E extends Entry<V, E>> extends AbstractFloatSet {
        private final FloatForkTable<V, E> ref;

        private KeySet(FloatForkTable<V, E> ref) {
            this.ref = ref;
        }

        public @NotNull FloatIterator iterator() {
            return new FloatIterator() {
                private Iterator<Float2ObjectMap.Entry<E>> i = ref.float2ObjectEntrySet().iterator();

                public boolean hasNext() {
                    return i.hasNext();
                }

                @Override
                public float nextFloat() {
                    return i.next().getFloatKey();
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

    private static final class EntrySet<V, E extends Entry<V, E>> extends AbstractObjectSet<Float2ObjectMap.Entry<E>> {
        private final FloatForkTable<V, E> ref;

        private EntrySet(FloatForkTable<V, E> ref) {
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
        public ObjectIterator<Float2ObjectMap.Entry<E>> iterator() {
            return new ObjectIterator<>() {
                final int[] nestedArrayIndexes = new int[ref.depth];
                Float2EntryEntry<V, E> prev;
                Float2EntryEntry<V, E> next;

                @Override
                public boolean hasNext() {
                    if (next == null) {
                        computeNext();
                    }

                    return next != null;
                }

                @Override
                public synchronized Float2EntryEntry<V, E> next() {
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
                    Float2EntryEntry<V, E> p;

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

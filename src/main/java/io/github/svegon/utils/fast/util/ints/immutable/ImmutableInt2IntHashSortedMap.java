package io.github.svegon.utils.fast.util.ints.immutable;

import io.github.svegon.utils.ComparingUtil;
import io.github.svegon.utils.collections.MultisetUtil;
import io.github.svegon.utils.collections.iteration.IterationUtil;
import io.github.svegon.utils.fast.util.ints.AbstractIntMultiset;
import io.github.svegon.utils.fast.util.ints.IntMultiset;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.*;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntIterator;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Immutable
public final class ImmutableInt2IntHashSortedMap extends ImmutableInt2IntSortedMap implements Hash {
    final ImmutableInt2IntHashMap.Entry[][] tree;
    private final int size;
    private final EntrySet entrySet = new EntrySet();
    private final KeySet keySet;
    private final Values values = new Values();

    @SuppressWarnings({"unchecked"})
    ImmutableInt2IntHashSortedMap(final int hashCode, final int @NotNull [] keys,
                                  final @NotNull ImmutableInt2IntHashSortedMap model) {
        super(hashCode);
        tree = new ImmutableInt2IntHashMap.Entry[HashCommon.arraySize(model.size(), VERY_FAST_LOAD_FACTOR)][];
        size = model.size();
        keySet = new KeySet(keys, model.comparator(), model.strategy());

        final List<Entry>[] intermediateTree = new List[tree.length];
        final int expectedSize = 5 + model.size() / tree.length + model.size() / tree.length / 10;
        final ObjectBidirectionalIterator<ImmutableInt2IntHashMap.Entry> it =
                (ObjectBidirectionalIterator) model.int2IntEntrySet().iterator();

        Arrays.setAll(intermediateTree, (j) -> Lists.newArrayListWithCapacity(expectedSize));

        while (it.hasNext()) {
            ImmutableInt2IntHashMap.Entry entry = it.next();

            intermediateTree[(entry.hash & 0x7fffffff) % intermediateTree.length].add(entry);
        }

        Arrays.setAll(tree, (j) -> intermediateTree[j].toArray(ImmutableInt2IntHashMap.MODEL_ARRAY));
    }

    ImmutableInt2IntHashSortedMap(final @NotNull Int2IntOpenCustomHashMap model,
                                  final @Nullable IntComparator comparator) {
        super(model.hashCode());
        tree = new ImmutableInt2IntHashMap.Entry[HashCommon.arraySize(model.size(), VERY_FAST_LOAD_FACTOR)][];
        size = model.size();

        final int[] keys = new int[model.size()];
        final List<Entry>[] intermediateTree = new List[tree.length];
        final int expectedSize = 5 + model.size() / tree.length + model.size() / tree.length / 10;
        final ObjectIterator<Entry> it = model.int2IntEntrySet().fastIterator();
        int i = 0;

        Arrays.setAll(intermediateTree, (j) -> Lists.newArrayListWithCapacity(expectedSize));

        while (it.hasNext()) {
            Entry e = it.next();
            ImmutableInt2IntHashMap.Entry entry = new ImmutableInt2IntHashMap.Entry(model.strategy(),
                    e.getIntKey(), e.getIntValue());

            intermediateTree[(entry.hash & 0x7fffffff) % intermediateTree.length].add(entry);
            keys[i++] = e.getIntKey();
        }

        Arrays.setAll(tree, (j) -> intermediateTree[j].toArray(ImmutableInt2IntHashMap.MODEL_ARRAY));
        IntArrays.unstableSort(keys, comparator);

        keySet = new KeySet(keys, comparator, model.strategy());
    }

    public IntHash.Strategy strategy() {
        return keySet.strategy;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public ObjectSortedSet<Entry> int2IntEntrySet() {
        return entrySet;
    }

    @Override
    public @NotNull ImmutableIntSortedSet keySet() {
        return keySet;
    }

    @Override
    public IntMultiset values() {
        return values;
    }

    @Override
    public ImmutableInt2IntSortedMap subMap(int fromKey, int toKey) {
        if (compare(fromKey, toKey) > 0) {
            throw new IllegalArgumentException();
        }

        int from = 0;
        int to = keySet.elements.length;

        while (compare(keySet.elements[from], fromKey) < 0) {
            from++;

            if (from >= keySet.elements.length) {
                return of(comparator());
            }
        }

        while (compare(keySet.elements[--to], toKey) > 0) {
            if (to <= 0) {
                return of(comparator());
            }
        }

        if (from > ++to) {
            throw new IllegalArgumentException();
        }

        if (from == to) {
            return of(comparator());
        }

        if (from == 0 && to == keySet.elements.length) {
            return this;
        }

        int h = 0;
        int[] elements = Arrays.copyOfRange(keySet.elements, from, to);

        for (int i : elements) {
            int v = getEntry(i).getIntValue();

            h += strategy().hashCode(i) ^ Integer.hashCode(v);
        }

        return new ImmutableInt2IntHashSortedMap(h, elements, this);
    }

    @Override
    public ImmutableInt2IntSortedMap headMap(int toKey) {
        int to = keySet.elements.length;

        while (compare(keySet.elements[--to], toKey) > 0) {
            if (to <= 0) {
                return of(comparator());
            }
        }

        if (++to == keySet.elements.length) {
            return this;
        }

        int h = 0;
        int[] elements = Arrays.copyOf(keySet.elements, to);

        for (int i : elements) {
            int v = getEntry(i).getIntValue();

            h += strategy().hashCode(i) ^ Integer.hashCode(v);
        }

        return new ImmutableInt2IntHashSortedMap(h, elements, this);
    }

    @Override
    public ImmutableInt2IntSortedMap tailMap(int fromKey) {
        int from = 0;

        while (compare(keySet.elements[from], fromKey) < 0) {
            from++;

            if (from >= keySet.elements.length) {
                return of(comparator());
            }
        }

        int h = 0;
        int[] elements = Arrays.copyOfRange(keySet.elements, from, keySet.elements.length);

        for (int k : elements) {
            int v = getEntry(k).getIntValue();

            h += strategy().hashCode(k) ^ Integer.hashCode(v);
        }

        return new ImmutableInt2IntHashSortedMap(h, elements, this);
    }

    @Override
    public int firstIntKey() {
        return keySet.firstInt();
    }

    @Override
    public int lastIntKey() {
        return keySet.lastInt();
    }

    @Override
    public IntComparator comparator() {
        return keySet.comparator();
    }

    @Override
    public boolean containsKey(int key) {
        return getEntry(key) != null;
    }

    @Override
    public boolean containsValue(int value) {
        return values().contains(value);
    }

    @Override
    public int getOrDefault(int key, int defaultValue) {
        Entry entry = getEntry(key);
        return entry != null ? entry.getIntValue() : defaultValue;
    }

    public Entry getEntry(int key) {
        int hash = strategy().hashCode(key);
        ImmutableInt2IntHashMap.Entry[] branch = tree[(hash & 0x7fffffff) % tree.length];

        for (ImmutableInt2IntHashMap.Entry e : branch) {
            if (e.hash == hash && strategy().equals(e.getIntKey(), key)) {
                return e;
            }
        }

        return null;
    }

    private final class EntrySet extends AbstractObjectSortedSet<Entry> implements Hash {
        @Override
        public int hashCode() {
            return ImmutableInt2IntHashSortedMap.this.hashCode();
        }

        @Override
        public ObjectBidirectionalIterator<Entry> iterator(Entry fromElement) {
            if (fromElement == null) {
                return iterator();
            }

            ObjectBidirectionalIterator<Entry> it = iterator();
            int key = fromElement.getIntKey();

            while (it.hasNext()) {
                if (compare(key, it.next().getIntKey()) < 0) {
                    it.previous();
                    break;
                }
            }

            return it;
        }

        @Override
        public @NotNull ObjectBidirectionalIterator<Entry> iterator() {
            return IterationUtil.mapToObj(keySet.iterator(), ImmutableInt2IntHashSortedMap.this::getEntry);
        }

        @Override
        public @NotNull Comparator<? super Entry> comparator() {
            return nullsFirst(ComparingUtil.comparingInt(Entry::getIntKey,
                    ImmutableInt2IntHashSortedMap.this.comparator()));
        }

        @Override
        @SuppressWarnings("unchecked")
        public ObjectSortedSet<Entry> subSet(Entry fromElement, Entry toElement) {
            if (fromElement == null) {
                return headSet(toElement);
            }

            if (toElement == null) {
                return ObjectSortedSets.EMPTY_SET;
            }

            return subMap(fromElement.getIntKey(), toElement.getIntKey()).int2IntEntrySet();
        }

        @Override
        @SuppressWarnings("unchecked")
        public ObjectSortedSet<Entry> headSet(Entry toElement) {
            if (toElement == null) {
                return ObjectSortedSets.EMPTY_SET;
            }

            if (compare(lastIntKey(), toElement.getIntKey()) < 0) {
                return this;
            }

            return headMap(toElement.getIntKey()).int2IntEntrySet();
        }

        @Override
        public ObjectSortedSet<Entry> tailSet(Entry fromElement) {
            if (fromElement == null || compare(firstIntKey(), fromElement.getIntKey()) >= 0) {
                return this;
            }

            return tailMap(fromElement.getIntKey()).int2IntEntrySet();
        }

        @Override
        public Entry first() {
            return null;
        }

        @Override
        public Entry last() {
            return null;
        }

        @Override
        public int size() {
            return ImmutableInt2IntHashSortedMap.this.size();
        }
    }

    @Immutable
    private final class KeySet extends AbstractImmutableIntHashSortedSet implements Hash {
        @SuppressWarnings("unchecked")
        KeySet(int[] keys, IntComparator comparator, IntHash.Strategy strategy) {
            super(((ObjectSet<ImmutableInt2IntHashMap.Entry>) (Object) int2IntEntrySet()).parallelStream()
                    .mapToInt(e -> e.hash).sum(), keys, comparator, strategy);
        }

        @Override
        public ImmutableIntSortedSet toSortedSet(@Nullable IntComparator comparator) {
            return copyOf(this, comparator);
        }

        @Override
        public boolean contains(int key) {
            return containsKey(key);
        }
    }

    @Immutable
    private final class Values extends AbstractIntMultiset {
        private ObjectSet<IntMultiset.Entry> intEntrySet;

        @Override
        public @NotNull IntIterator iterator() {
            return IterationUtil.transformToInt(int2IntEntrySet().iterator(), Int2IntMap.Entry::getIntValue);
        }

        @Override
        public int size() {
            return ImmutableInt2IntHashSortedMap.this.size();
        }

        @Override
        public ObjectSet<IntMultiset.Entry> intEntrySet() {
            if (intEntrySet == null) {
                Int2IntOpenHashMap entries = new Int2IntOpenHashMap(size());

                for (int s : this) {
                    entries.addTo(s, 1);
                }

                final ObjectOpenHashSet<IntMultiset.Entry> entrySet = new ObjectOpenHashSet<>();
                entries.int2IntEntrySet().fastForEach(e -> entrySet.add(MultisetUtil.immutableEntry(
                        e.getIntKey(), e.getIntValue())));
                intEntrySet = ObjectSets.unmodifiable(entrySet);
            }

            return intEntrySet;
        }
    }
}

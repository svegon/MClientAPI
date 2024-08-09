package io.github.svegon.utils.fast.util.ints.immutable;

import io.github.svegon.utils.ComparingUtil;
import io.github.svegon.utils.collections.MultisetUtil;
import io.github.svegon.utils.collections.iteration.IterationUtil;
import io.github.svegon.utils.fast.util.shorts.AbstractShortMultiset;
import io.github.svegon.utils.fast.util.shorts.ShortMultiset;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.*;
import it.unimi.dsi.fastutil.shorts.Short2IntOpenHashMap;
import it.unimi.dsi.fastutil.shorts.ShortIterator;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Immutable
public final class ImmutableInt2ShortHashSortedMap extends ImmutableInt2ShortSortedMap implements Hash {
    final ImmutableInt2ShortHashMap.Entry[][] tree;
    private final int size;
    private final EntrySet entrySet = new EntrySet();
    private final KeySet keySet;
    private final Values values = new Values();

    @SuppressWarnings({"unchecked"})
    ImmutableInt2ShortHashSortedMap(final int hashCode, final int @NotNull [] keys,
                                         final @NotNull ImmutableInt2ShortHashSortedMap model) {
        super(hashCode);
        tree = new ImmutableInt2ShortHashMap.Entry[HashCommon.arraySize(model.size(), VERY_FAST_LOAD_FACTOR)][];
        size = model.size();
        keySet = new KeySet(keys, model.comparator(), model.strategy());

        final List<Entry>[] intermediateTree = new List[tree.length];
        final int expectedSize = 5 + model.size() / tree.length + model.size() / tree.length / 10;
        final ObjectBidirectionalIterator<ImmutableInt2ShortHashMap.Entry> it =
                (ObjectBidirectionalIterator) model.int2ShortEntrySet().iterator();

        Arrays.setAll(intermediateTree, (j) -> Lists.newArrayListWithCapacity(expectedSize));

        while (it.hasNext()) {
            ImmutableInt2ShortHashMap.Entry entry = it.next();

            intermediateTree[(entry.hash & 0x7fffffff) % intermediateTree.length].add(entry);
        }

        Arrays.setAll(tree, (j) -> intermediateTree[j].toArray(ImmutableInt2ShortHashMap.MODEL_ARRAY));
    }

    ImmutableInt2ShortHashSortedMap(final @NotNull Int2ShortOpenCustomHashMap model,
                                    final @Nullable IntComparator comparator) {
        super(model.hashCode());
        tree = new ImmutableInt2ShortHashMap.Entry[HashCommon.arraySize(model.size(), VERY_FAST_LOAD_FACTOR)][];
        size = model.size();

        final int[] keys = new int[model.size()];
        final List<Entry>[] intermediateTree = new List[tree.length];
        final int expectedSize = 5 + model.size() / tree.length + model.size() / tree.length / 10;
        final ObjectIterator<Entry> it = model.int2ShortEntrySet().fastIterator();
        int i = 0;

        Arrays.setAll(intermediateTree, (j) -> Lists.newArrayListWithCapacity(expectedSize));

        while (it.hasNext()) {
            Entry e = it.next();
            ImmutableInt2ShortHashMap.Entry entry = new ImmutableInt2ShortHashMap.Entry(model.strategy(),
                    e.getIntKey(), e.getShortValue());

            intermediateTree[(entry.hash & 0x7fffffff) % intermediateTree.length].add(entry);
            keys[i++] = e.getIntKey();
        }

        Arrays.setAll(tree, (j) -> intermediateTree[j].toArray(ImmutableInt2ShortHashMap.MODEL_ARRAY));
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
    public ObjectSortedSet<Entry> int2ShortEntrySet() {
        return entrySet;
    }

    @Override
    public @NotNull ImmutableIntSortedSet keySet() {
        return keySet;
    }

    @Override
    public ShortMultiset values() {
        return values;
    }

    @Override
    public ImmutableInt2ShortSortedMap subMap(int fromKey, int toKey) {
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
            short v = getEntry(i).getShortValue();

            h += strategy().hashCode(i) ^ Short.hashCode(v);
        }

        return new ImmutableInt2ShortHashSortedMap(h, elements, this);
    }

    @Override
    public ImmutableInt2ShortSortedMap headMap(int toKey) {
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
            short v = getEntry(i).getShortValue();

            h += strategy().hashCode(i) ^ Short.hashCode(v);
        }

        return new ImmutableInt2ShortHashSortedMap(h, elements, this);
    }

    @Override
    public ImmutableInt2ShortSortedMap tailMap(int fromKey) {
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
            short v = getEntry(k).getShortValue();

            h += strategy().hashCode(k) ^ Short.hashCode(v);
        }

        return new ImmutableInt2ShortHashSortedMap(h, elements, this);
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
    public boolean containsValue(short value) {
        return values().contains(value);
    }

    @Override
    public short getOrDefault(int key, short defaultValue) {
        Entry entry = getEntry(key);
        return entry != null ? entry.getShortValue() : defaultValue;
    }

    public Entry getEntry(int key) {
        int hash = strategy().hashCode(key);
        ImmutableInt2ShortHashMap.Entry[] branch = tree[(hash & 0x7fffffff) % tree.length];

        for (ImmutableInt2ShortHashMap.Entry e : branch) {
            if (e.hash == hash && strategy().equals(e.getIntKey(), key)) {
                return e;
            }
        }

        return null;
    }

    private final class EntrySet extends AbstractObjectSortedSet<Entry> implements Hash {
        @Override
        public int hashCode() {
            return ImmutableInt2ShortHashSortedMap.this.hashCode();
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
            return IterationUtil.mapToObj(keySet.iterator(), ImmutableInt2ShortHashSortedMap.this::getEntry);
        }

        @Override
        public @NotNull Comparator<? super Entry> comparator() {
            return Comparator.nullsFirst(ComparingUtil.comparingInt(Entry::getIntKey,
                    ImmutableInt2ShortHashSortedMap.this.comparator()));
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

            return subMap(fromElement.getIntKey(), toElement.getIntKey()).int2ShortEntrySet();
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

            return headMap(toElement.getIntKey()).int2ShortEntrySet();
        }

        @Override
        public ObjectSortedSet<Entry> tailSet(Entry fromElement) {
            if (fromElement == null || compare(firstIntKey(), fromElement.getIntKey()) >= 0) {
                return this;
            }

            return tailMap(fromElement.getIntKey()).int2ShortEntrySet();
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
            return ImmutableInt2ShortHashSortedMap.this.size();
        }
    }

    @Immutable
    private final class KeySet extends AbstractImmutableIntHashSortedSet implements Hash {
        @SuppressWarnings("unchecked")
        KeySet(int[] keys, IntComparator comparator, IntHash.Strategy strategy) {
            super(((ObjectSet<ImmutableInt2ShortHashMap.Entry>) (Object) int2ShortEntrySet()).parallelStream()
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
    private final class Values extends AbstractShortMultiset {
        private ObjectSet<ShortMultiset.Entry> shortEntrySet;

        @Override
        public @NotNull ShortIterator iterator() {
            return IterationUtil.transformToShort(int2ShortEntrySet().iterator(), Int2ShortMap.Entry::getShortValue);
        }

        @Override
        public int size() {
            return ImmutableInt2ShortHashSortedMap.this.size();
        }

        @Override
        public ObjectSet<ShortMultiset.Entry> shortEntrySet() {
            if (shortEntrySet == null) {
                Short2IntOpenHashMap entries = new Short2IntOpenHashMap(size());

                for (short s : this) {
                    entries.addTo(s, 1);
                }

                final ObjectOpenHashSet<ShortMultiset.Entry> entrySet = new ObjectOpenHashSet<>();
                entries.short2IntEntrySet().fastForEach(e -> entrySet.add(MultisetUtil.immutableEntry(
                        e.getShortKey(), e.getIntValue())));
                shortEntrySet = ObjectSets.unmodifiable(entrySet);
            }

            return shortEntrySet;
        }
    }
}

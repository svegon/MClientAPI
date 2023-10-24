package com.github.svegon.utils.fast.util.ints.immutable;

import com.github.svegon.utils.collections.MultisetUtil;
import com.github.svegon.utils.collections.SetType;
import com.github.svegon.utils.collections.iteration.IterationUtil;
import com.github.svegon.utils.fast.util.shorts.AbstractShortMultiset;
import com.github.svegon.utils.fast.util.shorts.ShortMultiset;
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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Immutable
public final class ImmutableInt2ShortHashMap extends ImmutableInt2ShortMap implements Hash {
    public static final Entry[] MODEL_ARRAY = {};

    final Entry[][] tree;
    private final IntHash.Strategy strategy;
    private final int size;
    private final EntrySet entrySet = new EntrySet();
    private KeySet keySet;
    private final Values values = new Values();

    ImmutableInt2ShortHashMap(final @NotNull Int2ShortOpenCustomHashMap model) {
        super(model.hashCode());
        tree = new Entry[HashCommon.arraySize(model.size(), VERY_FAST_LOAD_FACTOR)][];
        strategy = model.strategy();
        size = model.size();

        final List<Entry>[] intermediateTree = new List[tree.length];
        final int expectedSize = 5 + model.size() / tree.length + model.size() / tree.length / 10;

        Arrays.setAll(intermediateTree, (i) -> Lists.newArrayListWithCapacity(expectedSize));

        model.int2ShortEntrySet().fastForEach(e -> {
            Entry entry = new Entry(strategy, e.getIntKey(), e.getShortValue());

            intermediateTree[(entry.hash & 0x7fffffff) % intermediateTree.length].add(entry);
        });

        Arrays.setAll(tree, (i) -> intermediateTree[i].toArray(MODEL_ARRAY));
    }

    public IntHash.Strategy strategy() {
        return strategy;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public ObjectSet<Int2ShortMap.Entry> int2ShortEntrySet() {
        return entrySet;
    }

    @Override
    public @NotNull ImmutableIntSet keySet() {
        if (keySet == null) {
            keySet = new KeySet();
        }

        return keySet;
    }

    @Override
    public ShortMultiset values() {
        return values;
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
        int hash = strategy.hashCode(key);
        Entry[] branch = tree[(hash & 0x7fffffff) % tree.length];

        for (Entry e : branch) {
            if (e.hash == hash && strategy.equals(e.getIntKey(), key)) {
                return e;
            }
        }

        return null;
    }

    static final class Entry implements Int2ShortMap.Entry, Hash {
        private final IntHash.Strategy strategy;
        private final int key;
        private final short value;
        final int hash;

        Entry(IntHash.Strategy strategy, int key, short value) {
            this.strategy = strategy;
            this.key = key;
            this.value = value;
            this.hash = strategy.hashCode(key);
        }

        @Override
        public int getIntKey() {
            return key;
        }

        @Override
        public short getShortValue() {
            return value;
        }

        @Override
        public short setValue(short value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;

            if (!(o instanceof Map.Entry entry))
                return false;

            return entry instanceof Int2ShortMap.Entry e ? strategy.equals(e.getIntKey(), getIntKey())
                    && getShortValue() == e.getShortValue() : entry.getKey() instanceof Integer
                    && strategy.equals((int) entry.getKey(), getIntKey()) && getValue().equals(entry.getValue());
        }

        @Override
        public int hashCode() {
            return hash ^ Short.hashCode(getShortValue());
        }

        @Override
        public String toString() {
            return getIntKey() + "=" + getShortValue();
        }
    }

    private final class EntrySet extends AbstractObjectSet<Int2ShortMap.Entry> implements Hash {
        @Override
        public int hashCode() {
            return ImmutableInt2ShortHashMap.this.hashCode();
        }

        @Override
        public ObjectIterator<Int2ShortMap.Entry> iterator() {
            return IterationUtil.flatMapToObj(Arrays.asList(tree).listIterator(), ObjectIterators::wrap);
        }

        @Override
        public int size() {
            return ImmutableInt2ShortHashMap.this.size();
        }
    }

    @Immutable
    private final class KeySet extends ImmutableIntSet implements Hash {
        @SuppressWarnings("unchecked")
        KeySet() {
            super(((ObjectSet<Entry>) (Object) int2ShortEntrySet()).parallelStream()
                    .mapToInt(e -> e.hash).sum());
        }

        @Override
        public ImmutableIntSortedSet toSortedSet(final @Nullable IntComparator comparator) {
            return ImmutableIntSortedSet.copyOf(this, comparator);
        }

        @Override
        public SetType getType() {
            return SetType.HASH;
        }

        @Override
        public int size() {
            return ImmutableInt2ShortHashMap.this.size();
        }

        @Override
        public IntIterator iterator() {
            return IterationUtil.mapToInt(int2ShortEntrySet().iterator(), Int2ShortMap.Entry::getIntKey);
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
            return ImmutableInt2ShortHashMap.this.size();
        }

        @Override
        public ObjectSet<ShortMultiset.Entry> shortEntrySet() {
            if (shortEntrySet == null) {
                Short2IntOpenHashMap entries = new Short2IntOpenHashMap(size());

                for (short s : this) {
                    entries.addTo(s, 1);
                }

                final ObjectSet<ShortMultiset.Entry> entrySet = new ObjectOpenHashSet<>();
                entries.short2IntEntrySet().fastForEach(e -> entrySet.add(MultisetUtil.immutableEntry(
                        e.getShortKey(), e.getIntValue())));
                shortEntrySet = ObjectSets.unmodifiable(entrySet);
            }

            return shortEntrySet;
        }
    }
}

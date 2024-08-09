package io.github.svegon.utils.fast.util.ints.immutable;

import io.github.svegon.utils.collections.MultisetUtil;
import io.github.svegon.utils.collections.SetType;
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
import java.util.List;
import java.util.Map;

@Immutable
public final class ImmutableInt2IntHashMap extends ImmutableInt2IntMap implements Hash {
    public static final Entry[] MODEL_ARRAY = {};

    final Entry[][] tree;
    private final IntHash.Strategy strategy;
    private final int size;
    private final EntrySet entrySet = new EntrySet();
    private KeySet keySet;
    private final Values values = new Values();

    ImmutableInt2IntHashMap(final @NotNull Int2IntOpenCustomHashMap model) {
        super(model.hashCode());
        tree = new Entry[HashCommon.arraySize(model.size(), VERY_FAST_LOAD_FACTOR)][];
        strategy = model.strategy();
        size = model.size();

        final List<Entry>[] intermediateTree = new List[tree.length];
        final int expectedSize = 5 + model.size() / tree.length + model.size() / tree.length / 10;

        Arrays.setAll(intermediateTree, (i) -> Lists.newArrayListWithCapacity(expectedSize));

        model.int2IntEntrySet().fastForEach(e -> {
            Entry entry = new Entry(strategy, e.getIntKey(), e.getIntValue());

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
    public ObjectSet<Int2IntMap.Entry> int2IntEntrySet() {
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
    public IntMultiset values() {
        return values;
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
        int hash = strategy.hashCode(key);
        Entry[] branch = tree[(hash & 0x7fffffff) % tree.length];

        for (Entry e : branch) {
            if (e.hash == hash && strategy.equals(e.getIntKey(), key)) {
                return e;
            }
        }

        return null;
    }

    static final class Entry implements Int2IntMap.Entry, Hash {
        private final IntHash.Strategy strategy;
        private final int key;
        private final int value;
        final int hash;

        Entry(IntHash.Strategy strategy, int key, int value) {
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
        public int getIntValue() {
            return value;
        }

        @Override
        public int setValue(int value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;

            if (!(o instanceof Map.Entry entry))
                return false;

            return entry instanceof Int2IntMap.Entry e ? strategy.equals(e.getIntKey(), getIntKey())
                    && getIntValue() == e.getIntValue() : entry.getKey() instanceof Integer
                    && strategy.equals((int) entry.getKey(), getIntKey()) && getValue().equals(entry.getValue());
        }

        @Override
        public int hashCode() {
            return hash ^ Integer.hashCode(getIntValue());
        }

        @Override
        public String toString() {
            return getIntKey() + "=" + getIntValue();
        }
    }

    private final class EntrySet extends AbstractObjectSet<Int2IntMap.Entry> implements Hash {
        @Override
        public int hashCode() {
            return ImmutableInt2IntHashMap.this.hashCode();
        }

        @Override
        public ObjectIterator<Int2IntMap.Entry> iterator() {
            return IterationUtil.flatMapToObj(Arrays.asList(tree).listIterator(), ObjectIterators::wrap);
        }

        @Override
        public int size() {
            return ImmutableInt2IntHashMap.this.size();
        }
    }

    @Immutable
    private final class KeySet extends ImmutableIntSet implements Hash {
        @SuppressWarnings("unchecked")
        KeySet() {
            super(((ObjectSet<Entry>) (Object) int2IntEntrySet()).parallelStream()
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
            return ImmutableInt2IntHashMap.this.size();
        }

        @Override
        public IntIterator iterator() {
            return IterationUtil.mapToInt(int2IntEntrySet().iterator(), Int2IntMap.Entry::getIntKey);
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
            return ImmutableInt2IntHashMap.this.size();
        }

        @Override
        public ObjectSet<IntMultiset.Entry> intEntrySet() {
            if (intEntrySet == null) {
                Int2IntOpenHashMap entries = new Int2IntOpenHashMap(size());

                for (int s : this) {
                    entries.addTo(s, 1);
                }

                final ObjectSet<IntMultiset.Entry> entrySet = new ObjectOpenHashSet<>();
                entries.int2IntEntrySet().fastForEach(e -> entrySet.add(MultisetUtil.immutableEntry(
                        e.getIntKey(), e.getIntValue())));
                intEntrySet = ObjectSets.unmodifiable(entrySet);
            }

            return intEntrySet;
        }
    }
}

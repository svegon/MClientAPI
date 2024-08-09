package io.github.svegon.utils.fast.util.ints.immutable;

import io.github.svegon.utils.collections.MultisetUtil;
import io.github.svegon.utils.collections.SetType;
import io.github.svegon.utils.collections.iteration.IterationUtil;
import io.github.svegon.utils.fast.util.ints.AbstractIntMultiset;
import io.github.svegon.utils.fast.util.ints.IntMultiset;
import io.github.svegon.utils.hash.IntForkTable;
import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.Size64;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

@Immutable
public class ImmutableInt2IntForkTableMap extends ImmutableInt2IntMap implements Size64 {
    private final IntForkTable<Integer, Entry> table;
    private final KeySet keySet = new KeySet();
    private final ValuesMultiset values = new ValuesMultiset();

    ImmutableInt2IntForkTableMap(final @NotNull Int2IntOpenCustomHashMap map) {
        super(map.hashCode());
        this.table = new IntForkTable<>(map.strategy());

        for (Int2IntMap.Entry e : map.int2IntEntrySet()) {
            table.put(new Entry(table, e.getIntKey(), e.getIntValue()));
        }
    }

    @Override
    public long size64() {
        return table.size64();
    }

    @Override
    @SuppressWarnings("deprecation")
    public final int size() {
        return Size64.super.size();
    }

    @Override
    @SuppressWarnings("unchecked")
    public ObjectSet<Int2IntMap.Entry> int2IntEntrySet() {
        return (ObjectSet) table.values();
    }

    @Override
    public ImmutableIntSet keySet() {
        return keySet;
    }

    @Override
    public IntMultiset values() {
        return values;
    }

    @Override
    public boolean containsKey(int key) {
        return table.containsKey(key);
    }

    @Override
    public boolean containsValue(int value) {
        return values().contains(value);
    }

    @Override
    public int getOrDefault(int key, int defaultValue) {
        Entry e = getEntry(key);
        return e == null ? defaultValue : e.getIntValue();
    }

    public Entry getEntry(int key) {
        return table.get(key);
    }

    static final class Entry extends IntForkTable.Entry<Integer, Entry> implements Int2IntMap.Entry {
        private final int value;

        public Entry(IntForkTable<Integer, Entry> table, int key, int value) {
            super(table, key);
            this.value = value;
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
            if (this == o) {
                return true;
            }

            if (!(o instanceof Map.Entry<?, ?> entry)) {
                return false;
            }

            return entry instanceof Int2IntMap.Entry e ? table.getStrategy().equals(getIntKey(), e.getIntKey())
                    && getIntValue() == e.getIntValue() : entry.getKey() instanceof Integer
                    && table.getStrategy().equals(getIntKey(), (int) entry.getKey()) && Objects.equals(getValue(),
                    entry.getValue());
        }

        @Override
        public int hashCode() {
            return hash ^ Integer.hashCode(getIntValue());
        }
    }

    @Immutable
    private final class KeySet extends ImmutableIntSet implements Hash {
        KeySet() {
            super(table.keySet().hashCode());
        }

        @Override
        public ImmutableIntSortedSet toSortedSet(final @Nullable IntComparator comparator) {
            return ImmutableIntSortedSet.copyOf(this, comparator, SetType.memoryPriority());
        }

        @Override
        public SetType getType() {
            return SetType.HASH;
        }

        @Override
        public int size() {
            return ImmutableInt2IntForkTableMap.this.size();
        }

        @Override
        public @NotNull IntIterator iterator() {
            return table.keySet().iterator();
        }

        @Override
        public boolean contains(int key) {
            return ImmutableInt2IntForkTableMap.this.containsKey(key);
        }
    }

    @Immutable
    final class ValuesMultiset extends AbstractIntMultiset implements Size64 {
        private ObjectSet<IntMultiset.Entry> intEntrySet;

        @Override
        public @NotNull IntIterator iterator() {
            return IterationUtil.mapToInt(keySet.iterator(), (k) -> table.get(k).getIntValue());
        }

        @Deprecated
        @Override
        @SuppressWarnings("deprecation")
        public int size() {
            return Size64.super.size();
        }

        @Override
        public long size64() {
            return ImmutableInt2IntForkTableMap.this.size64();
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
                intEntrySet = entrySet;
            }

            return intEntrySet;
        }
    }
}

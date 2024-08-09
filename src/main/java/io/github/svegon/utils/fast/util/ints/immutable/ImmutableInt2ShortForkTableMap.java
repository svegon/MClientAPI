package io.github.svegon.utils.fast.util.ints.immutable;

import io.github.svegon.utils.collections.MultisetUtil;
import io.github.svegon.utils.collections.SetType;
import io.github.svegon.utils.collections.iteration.IterationUtil;
import io.github.svegon.utils.fast.util.shorts.AbstractShortMultiset;
import io.github.svegon.utils.fast.util.shorts.ShortMultiset;
import io.github.svegon.utils.hash.IntForkTable;
import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.Size64;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.shorts.Short2IntOpenHashMap;
import it.unimi.dsi.fastutil.shorts.ShortIterator;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

@Immutable
public class ImmutableInt2ShortForkTableMap extends ImmutableInt2ShortMap implements Size64 {
    private final IntForkTable<Short, Entry> table;
    private final KeySet keySet = new KeySet();
    private final ValuesMultiset values = new ValuesMultiset();

    ImmutableInt2ShortForkTableMap(final @NotNull Int2ShortOpenCustomHashMap map) {
        super(map.hashCode());
        this.table = new IntForkTable<>(map.strategy());

        for (Int2ShortMap.Entry e : map.int2ShortEntrySet()) {
            table.put(new Entry(table, e.getIntKey(), e.getShortValue()));
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
    public ObjectSet<Int2ShortMap.Entry> int2ShortEntrySet() {
        return (ObjectSet) table.values();
    }

    @Override
    public ImmutableIntSet keySet() {
        return keySet;
    }

    @Override
    public ShortMultiset values() {
        return values;
    }

    @Override
    public boolean containsKey(int key) {
        return table.containsKey(key);
    }

    @Override
    public boolean containsValue(short value) {
        return values().contains(value);
    }

    @Override
    public short getOrDefault(int key, short defaultValue) {
        Entry e = getEntry(key);
        return e == null ? defaultValue : e.getShortValue();
    }

    public Entry getEntry(int key) {
        return table.get(key);
    }

    static final class Entry extends IntForkTable.Entry<Short, Entry> implements Int2ShortMap.Entry {
        private final short value;

        public Entry(IntForkTable<Short, Entry> table, int key, short value) {
            super(table, key);
            this.value = value;
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
            if (this == o) {
                return true;
            }

            if (!(o instanceof Map.Entry<?, ?> entry)) {
                return false;
            }

            return entry instanceof Int2ShortMap.Entry e ? table.getStrategy().equals(getIntKey(), e.getIntKey())
                    && getShortValue() == e.getShortValue() : entry.getKey() instanceof Integer
                    && table.getStrategy().equals(getIntKey(), (int) entry.getKey()) && Objects.equals(getValue(),
                    entry.getValue());
        }

        @Override
        public int hashCode() {
            return hash ^ Short.hashCode(getShortValue());
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
            return ImmutableInt2ShortForkTableMap.this.size();
        }

        @Override
        public @NotNull IntIterator iterator() {
            return table.keySet().iterator();
        }

        @Override
        public boolean contains(int key) {
            return ImmutableInt2ShortForkTableMap.this.containsKey(key);
        }
    }

    @Immutable
    final class ValuesMultiset extends AbstractShortMultiset implements Size64 {
        private ObjectSet<ShortMultiset.Entry> shortEntrySet;

        @Override
        public @NotNull ShortIterator iterator() {
            return IterationUtil.mapToShort(keySet.iterator(), (k) -> table.get(k).getShortValue());
        }

        @Deprecated
        @Override
        @SuppressWarnings("deprecation")
        public int size() {
            return Size64.super.size();
        }

        @Override
        public long size64() {
            return ImmutableInt2ShortForkTableMap.this.size64();
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
                shortEntrySet = entrySet;
            }

            return shortEntrySet;
        }
    }
}

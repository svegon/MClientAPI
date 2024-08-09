package io.github.svegon.utils.fast.util.ints.immutable;

import io.github.svegon.utils.collections.MultisetUtil;
import io.github.svegon.utils.fast.util.ints.IntMultiset;
import it.unimi.dsi.fastutil.ints.IntComparator;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import it.unimi.dsi.fastutil.objects.ObjectSortedSets;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;

@Immutable
public class ImmutableInt2IntEmptySortedMap extends ImmutableInt2IntSortedMap {
    public static final ImmutableInt2IntEmptySortedMap DEFAULT_SORTED =
            new ImmutableInt2IntEmptySortedMap(Integer::compare) {
                public IntComparator comparator() {
                    return null;
                }

                @Override
                public ImmutableIntSortedSet keySet() {
                    return EmptyImmutableIntSortedSet.DEFAULT;
                }
            };

    private final @NotNull IntComparator comparator;

    ImmutableInt2IntEmptySortedMap(final @NotNull IntComparator comparator) {
        super(0);
        this.comparator = comparator;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public IntMultiset values() {
        return MultisetUtil.emptyIntMultiset();
    }

    @Override
    public int get(int key) {
        return defaultReturnValue();
    }

    @Override
    public boolean containsKey(int key) {
        return false;
    }

    @Override
    public boolean containsValue(int value) {
        return false;
    }

    @Override
    public int getOrDefault(int key, int defaultValue) {
        return defaultValue;
    }

    @Override
    public ObjectSortedSet<Entry> int2IntEntrySet() {
        return (ObjectSortedSet<Entry>) ObjectSortedSets.<Entry>emptySet();
    }

    @Override
    public ImmutableIntSortedSet keySet() {
        return ImmutableIntSortedSet.of(comparator());
    }

    @Override
    public ImmutableInt2IntSortedMap subMap(int fromKey, int toKey) {
        if (comparator.compare(fromKey, toKey) > 0) {
            throw new IllegalArgumentException("fromKey " + fromKey + " is larger than toKey " + toKey);
        }

        return this;
    }

    @Override
    public ImmutableInt2IntSortedMap headMap(int toKey) {
        return this;
    }

    @Override
    public ImmutableInt2IntSortedMap tailMap(int fromKey) {
        return this;
    }

    @Override
    public int firstIntKey() {
        throw new NoSuchElementException();
    }

    @Override
    public int lastIntKey() {
        throw new NoSuchElementException();
    }

    @Override
    public IntComparator comparator() {
        return comparator;
    }
}

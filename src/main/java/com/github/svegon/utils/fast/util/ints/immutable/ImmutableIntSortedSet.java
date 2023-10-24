package com.github.svegon.utils.fast.util.ints.immutable;

import com.github.svegon.utils.ComparingUtil;
import com.github.svegon.utils.collections.ArrayUtil;
import com.github.svegon.utils.collections.SetType;
import com.github.svegon.utils.hash.HashUtil;
import it.unimi.dsi.fastutil.ints.*;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Immutable
public abstract class ImmutableIntSortedSet extends ImmutableIntSet implements IntSortedSet {
    private static final SetType[] SET_TYPES = SetType.values();

    ImmutableIntSortedSet(int hashCode) {
        super(hashCode);
    }

    @Override
    public ImmutableIntSortedSet toSortedSet(@Nullable IntComparator comparator) {
        return comparator == comparator() ? this : of(new IntOpenHashSet(this), comparator,
                ComparingUtil.prioritize(getType()));
    }

    @Override
    public abstract IntBidirectionalIterator iterator(int fromElement);

    @Override
    public abstract IntBidirectionalIterator iterator();

    @Override
    public IntSortedSet headSet(int toElement) {
        return subSet(firstInt(), toElement);
    }

    public static ImmutableIntSortedSet of(final @Nullable IntComparator comparator) {
        return comparator == null ? EmptyImmutableIntSortedSet.DEFAULT : new EmptyImmutableIntSortedSet(comparator);
    }

    public static ImmutableIntSingletonSortedSet of(final @Nullable IntComparator comparator, final int value) {
        return comparator == null ? new ImmutableIntSingletonDefaultSortedSet(value)
                : new ImmutableIntSingletonSortedSet(comparator, value);
    }

    public static ImmutableIntSortedSet of(final @Nullable IntComparator comparator,
                                           final int @NotNull ... values) {
        return copyOf(ArrayUtil.asList(values), comparator);
    }

    public static ImmutableIntSortedSet of(final @Nullable IntComparator comparator,
                                           final Comparator<? super SetType> priority, final int @NotNull ... values) {
        return copyOf(ArrayUtil.asList(values), comparator, priority);
    }

    public static ImmutableIntSortedSet copyOf(final @NotNull IntIterable iterable,
                                               final @Nullable IntComparator comparator) {
        return copyOf(iterable, comparator, SetType.memoryPriority());
    }

    public static ImmutableIntSortedSet copyOf(final @NotNull IntIterable iterable,
                                                final @Nullable IntComparator comparator,
                                               Comparator<? super SetType> priority) {
        if (iterable instanceof ImmutableIntCollection c) {
            return c.toSortedSet(comparator);
        }

        return iterable instanceof IntCollection c ? of(new IntOpenHashSet(c), comparator, priority)
                : copyOf(iterable.iterator(), comparator, priority);
    }

    public static ImmutableIntSortedSet copyOf(final @NotNull IntIterator itr,
                                               final @Nullable IntComparator comparator) {
        return copyOf(itr, comparator, SetType.memoryPriority());
    }

    public static ImmutableIntSortedSet copyOf(final @NotNull IntIterator itr,
                                               final @Nullable IntComparator comparator,
                                               final Comparator<? super SetType> priority) {
        return of(new IntOpenHashSet(itr), comparator, priority);
    }

    public static ImmutableIntSortedSet copyOf(final @NotNull IntSortedSet set) {
        return set instanceof ImmutableIntSortedSet s ? s : of(new IntOpenHashSet(set), set.comparator(),
                SetType.memoryPriority());
    }

    public static ImmutableIntSortedSet copyOf(final @NotNull IntSortedSet set,
                                               final Comparator<? super SetType> priority) {
        if (set instanceof ImmutableIntSortedSet s && Arrays.stream(SET_TYPES).max(priority).get() == s.getType()) {
            return s;
        }

        return of(new IntOpenHashSet(set), set.comparator(), priority);
    }

    public static ImmutableIntSortedSet copyOf(final @NotNull IntIterator itr, final @NotNull IntHash.Strategy strategy,
                                               final @Nullable IntComparator comparator) {
        return copyOf(new IntOpenCustomHashSet(itr, strategy), comparator);
    }

    public static ImmutableIntSortedSet copyOf(final @NotNull IntIterable iterable,
                                               final @NotNull IntHash.Strategy strategy,
                                               final @Nullable IntComparator comparator) {
        return iterable instanceof IntCollection c ? copyOf(new IntOpenCustomHashSet(c, strategy), comparator)
                : copyOf(iterable.iterator(), strategy, comparator);
    }

    public static ImmutableIntSortedSet copyOf(final @NotNull IntHash.Strategy strategy,
                                               final @Nullable IntComparator comparator,
                                               final int @NotNull ... elements) {
        return of(comparator, new IntOpenCustomHashSet(elements, strategy));
    }

    public static ImmutableIntSortedSet copyOf(final @NotNull IntOpenCustomHashSet set,
                                               final @Nullable IntComparator comparator) {
        return copyOf(set, set.strategy(), comparator);
    }

    public static ImmutableIntSortedSet copyOf(final @NotNull IntLinkedOpenCustomHashSet set,
                                               final @Nullable IntComparator comparator) {
        return copyOf(set, set.strategy(), comparator);
    }

    public static ImmutableIntSortedSet copyOf(final @NotNull IntLinkedOpenCustomHashSet set) {
        return copyOf(set, set.comparator());
    }

    public static IntInterval interval(int from, int to) {
        return new IntInterval(Math.min(from, to), Math.max(from, to));
    }

    static ImmutableIntSortedSet of(int[] elements, @Nullable IntComparator comparator, ImmutableIntSortedSet original) {
        if (elements.length == 0) {
            return of(comparator);
        }

        if (elements.length == 1) {
            return of(comparator, elements[0]);
        }

        int min = Arrays.stream(elements).min().getAsInt();
        int max = Arrays.stream(elements).max().getAsInt();
        int length = max - min + 1;

        if (comparator == null) {
            if (length == elements.length) {
                return new IntInterval(min, max);
            }

            return switch (original.getType()) {
                case HASH -> new ImmutableIntCustomHashDefaultSortedSet(elements,
                        ((AbstractImmutableIntHashSortedSet) original).strategy);
                case COMPRESSED_TABLE -> new ImmutableIntCompressedTableDefaultSortedSet(elements, length, min);
                case TABLE -> new ImmutableIntTableDefaultSortedSet(elements, length, min);
            };
        }

        return switch (original.getType()) {
            case HASH -> new ImmutableIntCustomHashSortedSet(elements,
                    ((AbstractImmutableIntHashSortedSet) original).strategy, comparator);
            case COMPRESSED_TABLE -> new ImmutableIntCompressedTableSortedSet(elements, length, min, comparator);
            case TABLE -> new ImmutableIntTableSortedSet(elements, length, min, comparator);
        };
    }

    static ImmutableIntSortedSet of(IntOpenHashSet set, @Nullable IntComparator comparator,
                                    final @NotNull Comparator<? super SetType> priority) {
        if (set.size() == 0) {
            return of(comparator);
        }

        if (set.size() == 1) {
            return of(comparator, set.iterator().nextInt());
        }

        final int min = set.intStream().min().getAsInt();
        final int max = set.intStream().max().getAsInt();
        final int length = max - min + 1;
        final double fragmentation = (double) length / set.size();

        if (comparator == null && fragmentation == 1) {
            return new IntInterval(min, max);
        }

        switch (getType(fragmentation, priority)) {
            case HASH -> {
                return comparator == null
                        ? new ImmutableIntCustomHashDefaultSortedSet(set.toIntArray(), HashUtil.defaultIntStrategy())
                        : new ImmutableIntCustomHashSortedSet(set.toIntArray(), HashUtil.defaultIntStrategy(),
                        comparator);
            }
            case COMPRESSED_TABLE -> {
                int[] elements = set.toIntArray();

                IntArrays.unstableSort(elements, comparator);

                return comparator == null ? new ImmutableIntCompressedTableDefaultSortedSet(elements, length, min)
                        : new ImmutableIntCompressedTableSortedSet(elements, length, min, comparator);
            }
            case TABLE -> {
                int[] elements = set.toIntArray();

                IntArrays.unstableSort(elements, comparator);

                return comparator == null ? new ImmutableIntTableDefaultSortedSet(elements, length, min)
                        : new ImmutableIntTableSortedSet(elements, length, min, comparator);
            }
        }

        throw new IllegalStateException();
    }

    static ImmutableIntSortedSet of(final @Nullable IntComparator comparator,
                                           final @NotNull IntOpenCustomHashSet values) {
        return comparator == null ? new ImmutableIntCustomHashDefaultSortedSet(values.toIntArray(), values.strategy())
                : new ImmutableIntCustomHashSortedSet(values.toIntArray(), values.strategy(), comparator);
    }

    private static SetType getType(double fragmentation, Comparator<? super SetType> priority) {
        for (SetType type : SET_TYPES) {
            if (fragmentation <= type.memoryEffectiveFragmentation()) {
                return type;
            }
        }

        return Arrays.stream(SET_TYPES).max(priority).get();
    }

    static {
        ArrayUtil.parallelReverse(SET_TYPES);
    }
}

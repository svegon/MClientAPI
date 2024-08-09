package io.github.svegon.utils.fast.util.bytes.immutable;

import io.github.svegon.utils.collections.ArrayUtil;
import io.github.svegon.utils.collections.stream.StreamUtil;
import it.unimi.dsi.fastutil.bytes.*;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Immutable
public abstract class ImmutableByteSortedSet extends ImmutableByteSet implements ByteSortedSet {
    ImmutableByteSortedSet(int hashCode) {
        super(hashCode);
    }

    @Override
    public ImmutableByteList toList() {
        return new RegularImmutableByteList(toByteArray());
    }

    @Override
    public final ImmutableByteSortedSet toSortedSet(@Nullable ByteComparator comparator) {
        return comparator == comparator() ? this : copyOf(iterator(), comparator);
    }

    @Override
    public ByteBidirectionalIterator iterator() {
        return iterator(firstByte());
    }

    @Override
    public ByteSortedSet headSet(byte toElement) {
        return subSet(firstByte(), toElement);
    }

    public static ImmutableByteSortedSet of(final @Nullable ByteComparator comparator) {
        return comparator == null ? EmptyImmutableByteSortedSet.DEFAULT : new EmptyImmutableByteSortedSet(comparator);
    }

    public static ImmutableByteSingletonSortedSet of(final @Nullable ByteComparator comparator, final byte value) {
        return comparator == null ? new ImmutableByteSingletonDefaultSortedSet(value)
                : new ImmutableByteSingletonSortedSet(comparator, value);
    }

    public static ImmutableByteSortedSet of(final @Nullable ByteComparator comparator,
                                            final byte @NotNull ... values) {
        return copyOf(ArrayUtil.asList(values), comparator);
    }

    public static ImmutableByteSortedSet copyOf(final @NotNull ByteIterable iterable,
                                                final @Nullable ByteComparator comparator) {
        if (iterable instanceof ImmutableByteCollection c) {
            return c.toSortedSet(comparator);
        }

        return iterable instanceof ByteCollection c ? of(new ByteOpenHashSet(c), comparator)
                : copyOf(iterable.iterator(), comparator);
    }

    public static ImmutableByteSortedSet copyOf(final @NotNull ByteIterator itr,
                                                final @Nullable ByteComparator comparator) {
        return of(new ByteOpenHashSet(itr), comparator);
    }

    public static ImmutableByteSortedSet copyOf(final @NotNull ByteSortedSet set) {
        return set instanceof ImmutableByteSortedSet s ? s : of(new ByteOpenHashSet(set), set.comparator());
    }

    static ImmutableByteSortedSet of(ByteOpenHashSet set, @Nullable ByteComparator comparator) {
        if (set.size() == 0) {
            return comparator == null ? EmptyImmutableByteSortedSet.DEFAULT
                    : new EmptyImmutableByteSortedSet(comparator);
        }

        if (set.size() == 1) {
            return of(comparator, set.iterator().nextByte());
        }

        if (comparator == null) {
            byte min = StreamUtil.byteStream(set.spliterator(), true).min().get();
            byte max = StreamUtil.byteStream(set.spliterator(), true).max().get();
            ByteInterval interval = new ByteInterval(min, max);

            if (interval.equals(set)) {
                return interval;
            }
        }

        byte[] a = set.toByteArray();

        ByteArrays.unstableSort(a, comparator);

        return comparator == null ? new ImmutableByteTableDefaultSortedSet(a)
                : new ImmutableByteTableSortedSet(a, comparator);
    }
}

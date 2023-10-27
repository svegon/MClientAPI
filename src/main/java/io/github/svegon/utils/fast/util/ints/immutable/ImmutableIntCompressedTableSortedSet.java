package io.github.svegon.utils.fast.util.ints.immutable;

import io.github.svegon.utils.ConditionUtil;
import io.github.svegon.utils.collections.SetType;
import it.unimi.dsi.fastutil.ints.IntBidirectionalIterator;
import it.unimi.dsi.fastutil.ints.IntComparator;
import it.unimi.dsi.fastutil.ints.IntSortedSet;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

@Immutable
public class ImmutableIntCompressedTableSortedSet extends ImmutableIntSortedSet {
    final int[] elements;
    final byte[] table;
    final int offset;
    private final @Nullable IntComparator comparator;

    ImmutableIntCompressedTableSortedSet(int[] elements, byte[] table, int offset, @Nullable IntComparator comparator) {
        super(hash(elements, table, offset));
        this.elements = elements;
        this.table = table;
        this.offset = offset;
        this.comparator = comparator;
    }

    ImmutableIntCompressedTableSortedSet(int[] elements, int tableLength, int offset, @Nullable IntComparator comparator) {
        this(elements, new byte[tableLength], offset, comparator);
    }

    @Override
    public SetType getType() {
        return SetType.COMPRESSED_TABLE;
    }

    @Override
    public ImmutableIntList toList() {
        return new RegularImmutableIntList(elements);
    }

    @Override
    public int size() {
        return elements.length;
    }

    @Override
    public boolean contains(int e) {
        e -= offset;
        int i = e >>> 3;

        return i < table.length && ConditionUtil.hasFlag(table[i], e & 7);
    }

    @Override
    public IntBidirectionalIterator iterator(int fromElement) {
        int i = 0;

        while (i < elements.length && compare(elements[i], fromElement) < 0) {
            i++;
        }

        return new ImmutableIntTableSortedSet.Itr(elements, i);
    }

    @Override
    public @NotNull IntBidirectionalIterator iterator() {
        return new ImmutableIntTableSortedSet.Itr(elements, 0);
    }

    @Override
    public IntSortedSet subSet(int fromElement, int toElement) {
        if (compare(fromElement, toElement) > 0) {
            throw new IllegalArgumentException();
        }

        int from = 0;
        int to = elements.length;

        while (compare(elements[from], fromElement) < 0) {
            from++;

            if (from >= elements.length) {
                return of(comparator());
            }
        }

        while (compare(elements[--to], toElement) > 0) {
            if (to <= 0) {
                return of(comparator());
            }
        }

        if (from > ++to) {
            throw new IllegalArgumentException();
        }

        if (from == 0 && to == elements.length) {
            return this;
        }

        return of(Arrays.copyOfRange(elements, from, to), comparator(), this);
    }

    @Override
    public IntSortedSet tailSet(int fromElement) {
        int from = 0;

        while (compare(elements[from], fromElement) < 0) {
            from++;

            if (from >= elements.length) {
                return of(comparator());
            }
        }

        return of(Arrays.copyOfRange(elements, from, elements.length), comparator(), this);
    }

    @Override
    public IntComparator comparator() {
        return comparator;
    }

    @Override
    public int firstInt() {
        return elements[0];
    }

    @Override
    public int lastInt() {
        return elements[elements.length - 1];
    }

    protected int compare(int a, int b) {
        return comparator().compare(a, b);
    }

    private static int hash(int[] elements, byte[] table, int offset) {
        int h = 0;

        for (int i : elements) {
            h += Integer.hashCode(i);
            i -= offset;
            int index = i >>> 3;
            int subIndex = i & 7;
            table[index] |= 1 << subIndex;
        }

        return h;
    }
}

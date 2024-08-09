package io.github.svegon.utils.fast.util.ints.immutable;

import net.jcip.annotations.Immutable;

@Immutable
public final class ImmutableIntCompressedTableDefaultSortedSet extends ImmutableIntCompressedTableSortedSet {
    ImmutableIntCompressedTableDefaultSortedSet(int[] elements, byte[] table, int offset) {
        super(elements, table, offset, null);
    }

    ImmutableIntCompressedTableDefaultSortedSet(int[] elements, int tableLength, int offset) {
        this(elements, new byte[tableLength], offset);
    }

    @Override
    protected int compare(int a, int b) {
        return Integer.compare(a, b);
    }
}

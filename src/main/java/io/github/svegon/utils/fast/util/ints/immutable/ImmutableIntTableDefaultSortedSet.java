package io.github.svegon.utils.fast.util.ints.immutable;

public final class ImmutableIntTableDefaultSortedSet extends ImmutableIntTableSortedSet {
    ImmutableIntTableDefaultSortedSet(int[] elements, int tableLength, int offset) {
        super(elements, tableLength, offset, null);
    }

    @Override
    protected int compare(int a, int b) {
        return Integer.compare(a, b);
    }
}

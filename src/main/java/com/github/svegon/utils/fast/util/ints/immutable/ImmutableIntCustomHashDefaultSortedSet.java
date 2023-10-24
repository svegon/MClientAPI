package com.github.svegon.utils.fast.util.ints.immutable;

import it.unimi.dsi.fastutil.ints.IntHash;

public final class ImmutableIntCustomHashDefaultSortedSet extends ImmutableIntCustomHashSortedSet {
    ImmutableIntCustomHashDefaultSortedSet(int[][] tree, int[] elements, IntHash.Strategy strategy) {
        super(tree, elements, strategy, null);
    }

    ImmutableIntCustomHashDefaultSortedSet(int[] elements, IntHash.Strategy strategy) {
        super(elements, strategy, null);
    }

    @Override
    protected int compare(int a, int b) {
        return Integer.compare(a, b);
    }
}

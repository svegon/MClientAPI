package io.github.svegon.utils.fast.util.ints.immutable;

import net.jcip.annotations.Immutable;

@Immutable
public final class ImmutableIntSingletonDefaultSortedSet extends ImmutableIntSingletonSortedSet {
    ImmutableIntSingletonDefaultSortedSet(final int element) {
        super(null, element);
    }

    @Override
    protected int compare(int a, int b) {
        return Integer.compare(a, b);
    }
}

package io.github.svegon.utils.fast.util.ints.immutable;

import it.unimi.dsi.fastutil.ints.*;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractImmutableIntTableSortedSet extends AbstractImmutableIntSortedSet {
    final int offset;

    AbstractImmutableIntTableSortedSet(int hashCode, int[] elements, int offset, @Nullable IntComparator comparator) {
        super(hashCode, elements, comparator);
        this.offset = offset;
    }
}

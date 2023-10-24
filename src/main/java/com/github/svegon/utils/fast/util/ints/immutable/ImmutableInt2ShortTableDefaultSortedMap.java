package com.github.svegon.utils.fast.util.ints.immutable;

import it.unimi.dsi.fastutil.ints.Int2ShortOpenCustomHashMap;

public class ImmutableInt2ShortTableDefaultSortedMap extends ImmutableInt2ShortTableSortedMap {
    ImmutableInt2ShortTableDefaultSortedMap(int hashCode, short[] table, AbstractImmutableIntTableSortedSet keySet) {
        super(hashCode, table, keySet);
    }

    ImmutableInt2ShortTableDefaultSortedMap(int hashCode, AbstractImmutableIntTableSortedSet keySet) {
        super(hashCode, keySet);
    }

    ImmutableInt2ShortTableDefaultSortedMap(Int2ShortOpenCustomHashMap values,
                                            AbstractImmutableIntTableSortedSet prototype) {
        super(null, values, prototype);
    }

    @Override
    protected ImmutableInt2ShortTableSortedMap rangeCopy(int hashCode, short[] table,
                                                         AbstractImmutableIntTableSortedSet keySet) {
        return new ImmutableInt2ShortTableDefaultSortedMap(hashCode, table, keySet);
    }
}

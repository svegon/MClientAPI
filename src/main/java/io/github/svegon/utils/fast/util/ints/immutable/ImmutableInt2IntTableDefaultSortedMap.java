package io.github.svegon.utils.fast.util.ints.immutable;

import it.unimi.dsi.fastutil.ints.Int2IntOpenCustomHashMap;

public class ImmutableInt2IntTableDefaultSortedMap extends ImmutableInt2IntTableSortedMap {
    ImmutableInt2IntTableDefaultSortedMap(int hashCode, int[] table, AbstractImmutableIntTableSortedSet keySet) {
        super(hashCode, table, keySet);
    }

    ImmutableInt2IntTableDefaultSortedMap(int hashCode, AbstractImmutableIntTableSortedSet keySet) {
        super(hashCode, keySet);
    }

    ImmutableInt2IntTableDefaultSortedMap(Int2IntOpenCustomHashMap values,
                                          AbstractImmutableIntTableSortedSet prototype) {
        super(null, values, prototype);
    }

    @Override
    protected ImmutableInt2IntTableSortedMap rangeCopy(int hashCode, int[] table,
                                                         AbstractImmutableIntTableSortedSet keySet) {
        return new ImmutableInt2IntTableDefaultSortedMap(hashCode, table, keySet);
    }
}

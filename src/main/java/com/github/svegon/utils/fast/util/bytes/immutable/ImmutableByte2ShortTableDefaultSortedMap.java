package com.github.svegon.utils.fast.util.bytes.immutable;

import com.github.svegon.utils.fast.util.bytes.shorts.Byte2ShortTableMap;
import net.jcip.annotations.Immutable;

@Immutable
public class ImmutableByte2ShortTableDefaultSortedMap extends ImmutableByte2ShortTableSortedMap {
    ImmutableByte2ShortTableDefaultSortedMap(int hashCode, short[] table, ImmutableByteTableSortedSet keySet) {
        super(hashCode, table, keySet);
    }

    ImmutableByte2ShortTableDefaultSortedMap(Byte2ShortTableMap values) {
        super(null, values);
    }

    @Override
    protected int compare(byte a, byte b) {
        return Byte.compare(a, b);
    }

    @Override
    protected ImmutableByte2ShortTableSortedMap rangeCopy(int hashCode, short[] table,
                                                          ImmutableByteTableSortedSet keySet) {
        return new ImmutableByte2ShortTableDefaultSortedMap(hashCode, table, keySet);
    }
}

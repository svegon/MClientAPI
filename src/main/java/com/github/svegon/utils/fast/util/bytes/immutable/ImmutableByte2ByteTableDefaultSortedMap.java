package com.github.svegon.utils.fast.util.bytes.immutable;

import it.unimi.dsi.fastutil.bytes.Byte2ByteOpenHashMap;
import net.jcip.annotations.Immutable;

@Immutable
public final class ImmutableByte2ByteTableDefaultSortedMap extends ImmutableByte2ByteTableSortedMap {
    ImmutableByte2ByteTableDefaultSortedMap(int hashCode, byte[] table, ImmutableByteTableSortedSet keySet) {
        super(hashCode, table, keySet);
    }

    ImmutableByte2ByteTableDefaultSortedMap(Byte2ByteOpenHashMap values) {
        super(null, values);
    }

    @Override
    protected int compare(byte a, byte b) {
        return Byte.compare(a, b);
    }

    @Override
    protected ImmutableByte2ByteTableSortedMap rangeCopy(int hashCode, byte[] table,
                                                         ImmutableByteTableSortedSet keySet) {
        return new ImmutableByte2ByteTableDefaultSortedMap(hashCode, table, keySet);
    }
}

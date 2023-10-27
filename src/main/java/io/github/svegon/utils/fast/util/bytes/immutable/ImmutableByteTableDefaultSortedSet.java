package io.github.svegon.utils.fast.util.bytes.immutable;

import it.unimi.dsi.fastutil.bytes.ByteComparator;

public final class ImmutableByteTableDefaultSortedSet extends ImmutableByteTableSortedSet {
    ImmutableByteTableDefaultSortedSet(byte[] elements) {
        super(elements, null);
    }

    @Override
    protected int compare(byte a, byte b) {
        return Byte.compare(a, b);
    }

    @Override
    protected ImmutableByteTableSortedSet of(byte[] elements, ByteComparator comparator) {
        return new ImmutableByteTableDefaultSortedSet(elements);
    }
}

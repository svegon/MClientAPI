package com.github.svegon.utils.fast.util.bytes.immutable;

import net.jcip.annotations.Immutable;

@Immutable
public final class ImmutableByteSingletonDefaultSortedSet extends ImmutableByteSingletonSortedSet {
    ImmutableByteSingletonDefaultSortedSet(final byte element) {
        super(null, element);
    }

    @Override
    protected int compare(byte a, byte b) {
        return Byte.compare(a, b);
    }
}

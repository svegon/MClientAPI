package io.github.svegon.utils.fast.util.bytes.immutable;

import it.unimi.dsi.fastutil.bytes.ByteArrays;
import net.jcip.annotations.Immutable;

import java.util.Arrays;
import java.util.RandomAccess;

@Immutable
public final class RegularImmutableByteList extends ImmutableByteList implements RandomAccess {
    public static final ImmutableByteList EMPTY = new RegularImmutableByteList(ByteArrays.EMPTY_ARRAY);

    final byte[] values;

    RegularImmutableByteList(byte[] values) {
        super(Arrays.hashCode(values));
        this.values = values;
    }

    @Override
    public byte getByte(int index) {
        return values[index];
    }

    @Override
    public int size() {
        return values.length;
    }

    @Override
    public void getElements(int from, byte[] a, int offset, int length) {
        System.arraycopy(values, from, a, offset, length);
    }
}

package io.github.svegon.utils.fast.util.ints.immutable;

import it.unimi.dsi.fastutil.ints.IntArrays;
import net.jcip.annotations.Immutable;

import java.util.Arrays;
import java.util.RandomAccess;

@Immutable
public final class RegularImmutableIntList extends ImmutableIntList implements RandomAccess {
    public static final ImmutableIntList EMPTY = new RegularImmutableIntList(IntArrays.EMPTY_ARRAY);

    final int[] values;

    RegularImmutableIntList(int[] values) {
        super(Arrays.hashCode(values));
        this.values = values;
    }

    @Override
    public int getInt(int index) {
        return values[index];
    }

    @Override
    public int size() {
        return values.length;
    }

    @Override
    public void getElements(int from, int[] a, int offset, int length) {
        System.arraycopy(values, from, a, offset, length);
    }
}

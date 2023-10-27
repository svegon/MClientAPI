package io.github.svegon.utils.fast.util.ints.immutable;

import io.github.svegon.utils.collections.SetType;
import it.unimi.dsi.fastutil.ints.IntComparator;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.Nullable;

@Immutable
public class ImmutableIntTableSortedSet extends AbstractImmutableIntTableSortedSet {
    final boolean[] table;

    ImmutableIntTableSortedSet(int[] elements, boolean[] table, int offset, @Nullable IntComparator comparator) {
        super(hash(elements, table, offset), elements, offset, comparator);
        this.table = table;
    }

    ImmutableIntTableSortedSet(int[] elements, int tableLength, int offset, @Nullable IntComparator comparator) {
        this(elements, new boolean[tableLength], offset, comparator);
    }

    @Override
    public SetType getType() {
        return SetType.TABLE;
    }

    @Override
    public boolean contains(int key) {
        key -= offset;

        return key < table.length && table[key];
    }

    private static int hash(int[] elements, boolean[] table, int offset) {
        int h = 0;

        for (int i : elements) {
            h += Integer.hashCode(i);
            table[i - offset] = true;
        }

        return h;
    }
}

package io.github.svegon.utils.fast.util.shorts.booleans;

import io.github.svegon.utils.collections.iteration.IterationUtil;
import io.github.svegon.utils.fast.util.ints.immutable.ImmutableIntList;
import io.github.svegon.utils.fast.util.shorts.ImprovedShortCollection;
import it.unimi.dsi.fastutil.booleans.BooleanArrays;
import it.unimi.dsi.fastutil.shorts.AbstractShortSet;
import it.unimi.dsi.fastutil.shorts.ShortIterable;
import it.unimi.dsi.fastutil.shorts.ShortIterator;
import net.jcip.annotations.NotThreadSafe;
import org.jetbrains.annotations.NotNull;

@NotThreadSafe
public class ShortTableSet extends AbstractShortSet implements ImprovedShortCollection {
    private boolean[] table;

    public ShortTableSet() {
        table = BooleanArrays.DEFAULT_EMPTY_ARRAY;
    }

    public ShortTableSet(short expectedMaxKey) {
        table = expectedMaxKey == 0 ? BooleanArrays.EMPTY_ARRAY : new boolean[Short.toUnsignedInt(expectedMaxKey)];
    }

    public ShortTableSet(final @NotNull ShortIterable elements) {
        this(elements.iterator());
    }

    public ShortTableSet(final @NotNull ShortIterator elements) {
        this((short) 16);

        while (elements.hasNext()) {
            add(elements.nextShort());
        }
    }

    @Override
    public ShortIterator iterator() {
        return IterationUtil.mapToShort(IterationUtil.filter(ImmutableIntList.range(0, table.length).iterator(),
                (i) -> table[i]), (i) -> (short) i);
    }

    @Override
    public int size() {
        int size = 0;

        for (boolean bl : table) {
            if (bl) {
                size++;
            }
        }

        return size;
    }

    @Override
    public boolean add(short k) {
        int i = Short.toUnsignedInt(k);
        table = BooleanArrays.ensureCapacity(table, i + 1);
        return table[i] != (table[i] = true);
    }

    @Override
    public boolean contains(short k) {
        int i = Short.toUnsignedInt(k);

        return i < table.length && table[i];
    }

    @Override
    public boolean remove(short k) {
        int i = Short.toUnsignedInt(k);

        if (i >= table.length) {
            return false;
        }

        return table[i] != (table[i] = false);
    }
}

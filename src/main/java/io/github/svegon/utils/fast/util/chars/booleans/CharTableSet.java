package io.github.svegon.utils.fast.util.chars.booleans;

import io.github.svegon.utils.collections.iteration.IterationUtil;
import it.unimi.dsi.fastutil.booleans.BooleanArrays;
import it.unimi.dsi.fastutil.chars.AbstractCharSet;
import it.unimi.dsi.fastutil.chars.CharIterable;
import it.unimi.dsi.fastutil.chars.CharIterator;
import it.unimi.dsi.fastutil.chars.CharIterators;
import org.jetbrains.annotations.NotNull;
import oshi.annotation.concurrent.NotThreadSafe;

import java.util.stream.IntStream;

@NotThreadSafe
public class CharTableSet extends AbstractCharSet {
    private boolean[] table;

    public CharTableSet() {
        table = BooleanArrays.DEFAULT_EMPTY_ARRAY;
    }

    public CharTableSet(char expectedMaxKey) {
        table = expectedMaxKey == 0 ? BooleanArrays.EMPTY_ARRAY : new boolean[expectedMaxKey];
    }

    public CharTableSet(final @NotNull CharIterable elements) {
        this(elements.iterator());
    }

    public CharTableSet(final @NotNull CharIterator elements) {
        this((char) 16);

        while (elements.hasNext()) {
            add(elements.nextChar());
        }
    }

    @Override
    public CharIterator iterator() {
        return IterationUtil.filter(CharIterators.asCharIterator(IntStream.range(0, table.length).iterator()),
                (i) -> table[i]);
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
    public boolean add(char k) {
        table = BooleanArrays.ensureCapacity(table, k + 1);
        return table[k] != (table[k] = true);
    }

    @Override
    public boolean contains(char k) {
        return k < table.length && table[k];
    }

    @Override
    public boolean remove(char k) {
        if (k >= table.length) {
            return false;
        }

        return table[k] != (table[k] = false);
    }
}

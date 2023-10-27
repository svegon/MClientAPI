package io.github.svegon.utils.fast.util.chars.booleans;

import io.github.svegon.utils.ConditionUtil;
import io.github.svegon.utils.collections.ArrayUtil;
import io.github.svegon.utils.fast.util.chars.ImprovedCharCollection;
import io.github.svegon.utils.fast.util.chars.OnNextComputeCharIterator;
import io.github.svegon.utils.math.MathUtil;
import it.unimi.dsi.fastutil.bytes.ByteArrays;
import it.unimi.dsi.fastutil.bytes.ByteListIterator;
import it.unimi.dsi.fastutil.chars.AbstractCharSet;
import it.unimi.dsi.fastutil.chars.CharCollection;
import it.unimi.dsi.fastutil.chars.CharIterable;
import it.unimi.dsi.fastutil.chars.CharIterator;
import net.jcip.annotations.NotThreadSafe;
import org.jetbrains.annotations.NotNull;

@NotThreadSafe
public class CharCompressedTableSet extends AbstractCharSet implements ImprovedCharCollection {
    private byte[] table;

    public CharCompressedTableSet() {
        table = ByteArrays.DEFAULT_EMPTY_ARRAY;
    }

    public CharCompressedTableSet(char expectedMaxKey) {
        table = expectedMaxKey == 0 ? ByteArrays.EMPTY_ARRAY : new byte[(expectedMaxKey >>> 3)
                + (expectedMaxKey & 7) != 0 ? 1 : 0];
    }

    public CharCompressedTableSet(final @NotNull CharIterable elements) {
        this(elements.iterator());
    }

    public CharCompressedTableSet(final @NotNull CharIterator elements) {
        this((char) 16);

        while (elements.hasNext()) {
            add(elements.nextChar());
        }
    }

    @Override
    public CharIterator iterator() {
        return new OnNextComputeCharIterator() {
            final ByteListIterator it = ArrayUtil.asList(table).iterator();

            @Override
            protected char computeNext() {
                byte b;

                while (it.hasNext()) {
                    if ((b = it.nextByte()) != 0) {
                        return (char) ((it.nextIndex() << 3) | MathUtil.highestOneBit(b));
                    }
                }

                finish();
                return 0;
            }
        };
    }

    @Override
    public int size() {
        return ArrayUtil.stream(table).mapToInt(Integer::bitCount).sum();
    }

    @Override
    public boolean add(char k) {
        int index = k >>> 3;
        int mod = k & 7;
        table = ByteArrays.ensureCapacity(table, index + 1);
        return table[k] != (table[index] |= 1 << mod);
    }

    @Override
    public boolean contains(char k) {
        int index = k >>> 3;

        if (index >= table.length) {
            return false;
        }

        return ConditionUtil.hasFlag(table[index], k & 7);
    }

    @Override
    public boolean remove(char k) {
        int index = k >>> 3;

        if (index >= table.length) {
            return false;
        }

        return table[index] != (table[index] &= ~(1 << (k & 7)));
    }

    @Override
    public boolean removeAll(final @NotNull CharCollection c) {
        boolean modified = false;

        for (char s : c) {
            modified |= remove(s);
        }

        return modified;
    }

    @Override
    public boolean retainAll(final @NotNull CharCollection c) {
        return removeIf((e) -> !c.contains(e));
    }
}

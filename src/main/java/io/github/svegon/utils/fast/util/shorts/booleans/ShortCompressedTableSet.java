package io.github.svegon.utils.fast.util.shorts.booleans;

import io.github.svegon.utils.ConditionUtil;
import io.github.svegon.utils.collections.ArrayUtil;
import io.github.svegon.utils.fast.util.shorts.ImprovedShortCollection;
import io.github.svegon.utils.fast.util.shorts.OnNextComputeShortIterator;
import io.github.svegon.utils.math.MathUtil;
import it.unimi.dsi.fastutil.bytes.ByteArrays;
import it.unimi.dsi.fastutil.bytes.ByteListIterator;
import it.unimi.dsi.fastutil.shorts.AbstractShortSet;
import it.unimi.dsi.fastutil.shorts.ShortCollection;
import it.unimi.dsi.fastutil.shorts.ShortIterable;
import it.unimi.dsi.fastutil.shorts.ShortIterator;
import net.jcip.annotations.NotThreadSafe;
import org.jetbrains.annotations.NotNull;

@NotThreadSafe
public class ShortCompressedTableSet extends AbstractShortSet implements ImprovedShortCollection {
    private byte[] table;

    public ShortCompressedTableSet() {
        table = ByteArrays.DEFAULT_EMPTY_ARRAY;
    }

    public ShortCompressedTableSet(short expectedMaxKey) {
        table = expectedMaxKey == 0 ? ByteArrays.EMPTY_ARRAY : new byte[(expectedMaxKey >>> 3)
                + (expectedMaxKey & 7) != 0 ? 1 : 0];
    }

    public ShortCompressedTableSet(final @NotNull ShortIterable elements) {
        this(elements.iterator());
    }

    public ShortCompressedTableSet(final @NotNull ShortIterator elements) {
        this((short) 16);

        while (elements.hasNext()) {
            add(elements.nextShort());
        }
    }

    @Override
    public ShortIterator iterator() {
        return new OnNextComputeShortIterator() {
            final ByteListIterator it = ArrayUtil.asList(table).iterator();

            @Override
            protected short computeNext() {
                byte b;

                while (it.hasNext()) {
                    if ((b = it.nextByte()) != 0) {
                        return (short) ((it.nextIndex() << 3) | MathUtil.highestOneBit(b));
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
    public boolean add(short k) {
        int i = Short.toUnsignedInt(k);
        int index = i >>> 3;
        int mod = i & 7;
        table = ByteArrays.ensureCapacity(table, index + 1);
        return table[i] != (table[index] |= 1 << mod);
    }

    @Override
    public boolean contains(short k) {
        int i = Short.toUnsignedInt(k);
        int index = i >>> 3;

        if (index >= table.length) {
            return false;
        }

        return ConditionUtil.hasFlag(table[index], i & 7);
    }

    @Override
    public boolean remove(short k) {
        int i = Short.toUnsignedInt(k);
        int index = i >>> 3;

        if (index >= table.length) {
            return false;
        }

        return table[index] != (table[index] &= ~(1 << (i & 7)));
    }

    @Override
    public boolean removeAll(final @NotNull ShortCollection c) {
        boolean modified = false;

        for (short s : c) {
            modified |= remove(s);
        }

        return modified;
    }

    @Override
    public boolean retainAll(final @NotNull ShortCollection c) {
        return removeIf((e) -> !c.contains(e));
    }
}

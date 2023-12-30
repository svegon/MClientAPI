package io.github.svegon.utils.fast.util.bytes.immutable;

import it.unimi.dsi.fastutil.bytes.ByteBidirectionalIterator;
import it.unimi.dsi.fastutil.bytes.ByteComparator;
import it.unimi.dsi.fastutil.bytes.ByteConsumer;
import it.unimi.dsi.fastutil.bytes.ByteSortedSet;
import net.jcip.annotations.Immutable;

import java.util.NoSuchElementException;

@Immutable
public final class ByteInterval extends ImmutableByteSortedSet {
    private final byte from;
    private final byte to;
    private final int size;

    ByteInterval(byte from, byte to) {
        super(hash(from, to));
        this.from = from;
        this.to = to;
        this.size = to - from + 1;
    }

    @Override
    public void forEach(ByteConsumer action) {
        for (byte b = from; b <= to; b++) {
            action.accept(b);
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean contains(byte key) {
        return from <= key && key <= to;
    }

    @Override
    public ByteBidirectionalIterator iterator(byte fromElement) {
        return new ByteBidirectionalIterator() {
            byte current = fromElement;

            @Override
            public byte previousByte() {
                if (current <= fromElement) {
                    throw new NoSuchElementException();
                }

                return --current;
            }

            @Override
            public boolean hasPrevious() {
                return current > fromElement;
            }

            @Override
            public byte nextByte() {
                if (current > to) {
                    throw new NoSuchElementException();
                }

                return current++;
            }

            @Override
            public boolean hasNext() {
                return current <= to;
            }
        };
    }

    @Override
    public ByteSortedSet subSet(byte fromElement, byte toElement) {
        return new ByteInterval(fromElement, (byte) (toElement - 1));
    }

    @Override
    public ByteSortedSet tailSet(byte fromElement) {
        return new ByteInterval(fromElement, to);
    }

    @Override
    public ByteComparator comparator() {
        return null;
    }

    @Override
    public byte firstByte() {
        return from;
    }

    @Override
    public byte lastByte() {
        return to;
    }

    public static int hash(byte from, byte to) {
        return ((to - 1) * to - from * (from - 1)) >>> 1;
    }
}

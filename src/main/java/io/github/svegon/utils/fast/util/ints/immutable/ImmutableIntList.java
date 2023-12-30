package io.github.svegon.utils.fast.util.ints.immutable;

import io.github.svegon.utils.collections.SetType;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.*;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static it.unimi.dsi.fastutil.Size64.sizeOf;

@Immutable
public abstract class ImmutableIntList extends ImmutableIntCollection implements IntList {
    ImmutableIntList(int hashCode) {
        super(hashCode);
    }

    @Override
    public final ImmutableIntList toList() {
        return this;
    }

    @Override
    public ImmutableIntSortedSet toSortedSet(@Nullable IntComparator comparator) {
        return ImmutableIntSortedSet.of(new IntOpenHashSet(this), comparator, SetType.memoryPriority());
    }

    @Override
    @SuppressWarnings("unchecked")
    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof List<?> l)) {
            return false;
        }

        if (size() != l.size()) {
            return false;
        }

        try {
            return compareTo((List<Integer>) l) == 0;
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public final @NotNull IntListIterator iterator() {
        return listIterator();
    }

    @Override
    public boolean contains(int key) {
        return indexOf(key) >= 0;
    }

    @Override
    public int[] toArray(int @Nullable [] a) {
        if (a == null || a.length < size()) {
            a = size() == 0 ? IntArrays.DEFAULT_EMPTY_ARRAY : new int[size()];
        }

        getElements(0, a, 0, size());
        return a;
    }

    @Override
    public IntSpliterator spliterator() {
        if (this instanceof RandomAccess) {
            return new IndexBasedImmutableSpliterator(this);
        } else {
            return IntSpliterators.asSpliterator(iterator(), sizeOf(this),
                    IntSpliterators.LIST_SPLITERATOR_CHARACTERISTICS | Spliterator.IMMUTABLE);
        }
    }

    @Override
    public final void addElements(int index, int[] a, int offset, int length) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean addAll(int index, @NotNull Collection<? extends Integer> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final @NotNull IntListIterator listIterator() {
        return listIterator(0);
    }

    @Override
    public IntListIterator listIterator(int index) {
        return new RandomAccessImmutableListIterator(Preconditions.checkPositionIndex(index, size()));
    }

    @Override
    public ImmutableIntList subList(int from, int to) {
        Preconditions.checkPositionIndexes(from, to, size());
        return this instanceof RandomAccess ? new RandomAccessSubList(this, from, to)
                : new SubList(this, from, to);
    }

    @Override
    public final void size(int size) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void getElements(int from, int[] a, int offset, int length) {
        Preconditions.checkPositionIndexes(from, from + length, size());
        IntArrays.ensureOffsetLength(a, offset, length);

        IntListIterator it = listIterator(from);

        while (it.hasNext()) {
            a[it.nextIndex()] = it.nextInt();
        }
    }

    @Override
    public final void removeElements(int from, int to) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final void addElements(int index, int[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final void add(int index, int key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean addAll(int index, IntCollection c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final int set(int index, int k) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(int k) {
        IntListIterator it = listIterator();

        while (it.hasNext()) {
            if (it.nextInt() == k) {
                return it.previousIndex();
            }
        }

        return -1;
    }

    @Override
    public int lastIndexOf(int k) {
        IntListIterator it = listIterator(size() - 1);

        while (it.hasPrevious()) {
            if (it.previousInt() == k) {
                return it.nextIndex();
            }
        }

        return -1;
    }

    @Override
    public final int removeInt(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final void sort(IntComparator comparator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final void unstableSort(IntComparator comparator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int compareTo(@NotNull List<? extends Integer> o) {
        int c = size() - o.size();

        if (c != 0) {
            return c;
        }

        IntListIterator itr = listIterator();
        ListIterator<? extends Integer> oItr = o.listIterator();

        while (itr.hasNext()) {
            if (!oItr.hasNext()) {
                return 1;
            }

            c = Integer.compare(itr.nextInt(), oItr.next());

            if (c != 0) {
                return c;
            }
        }

        return oItr.hasNext() ? -1 : 0;
    }

    public static IntRange range(int from, int to, int increment) {
        if (increment == 0) {
            throw new IllegalArgumentException("The list can never reach the end value");
        }

        return increment < 0 ? new IntRange(Math.min(from, to), Math.max(from, to), increment)
                : new IntRange(Math.max(from, to), Math.min(from, to), increment);
    }

    public static IntRange range(int from, int to) {
        return range(from, to, 1);
    }

    public static IntRange range(int to) {
        return range(0, to);
    }

    public static ImmutableIntList of(int @NotNull ... values) {
        return values.length == 0 ? RegularImmutableIntList.EMPTY : new RegularImmutableIntList(values.clone());
    }

    public static ImmutableIntList of(IntIterator it) {
        return copyOf(new IntArrayList(it));
    }

    public static ImmutableIntList copyOf(IntIterable iterable) {
        if (iterable instanceof ImmutableIntCollection c) {
            return c.toList();
        }

        if (iterable.getClass() == IntArrayList.class) {
            int[] array = ((IntArrayList) iterable).toIntArray();
            return array.length == 0 ? RegularImmutableIntList.EMPTY : new RegularImmutableIntList(array);
        }

        return iterable instanceof IntCollection ? of(((IntCollection) iterable).toIntArray())
                : of(iterable.iterator());
    }

    protected class RandomAccessImmutableListIterator implements IntListIterator {
        protected int index;

        public RandomAccessImmutableListIterator(int index) {
            this.index = index;
        }

        @Override
        public int nextInt() {
            return ImmutableIntList.this.getInt(index++);
        }

        @Override
        public boolean hasNext() {
            return index < ImmutableIntList.this.size();
        }

        @Override
        public int previousInt() {
            return ImmutableIntList.this.getInt(--index);
        }

        @Override
        public boolean hasPrevious() {
            return index >= 0;
        }

        @Override
        public int nextIndex() {
            return index;
        }

        @Override
        public int previousIndex() {
            return index - 1;
        }
    }

    protected static class IndexBasedImmutableSpliterator implements IntSpliterator {
        protected final ImmutableIntList list;
        protected final int start;
        protected final int fence;
        protected int index;

        public IndexBasedImmutableSpliterator(ImmutableIntList list, int index, int fence) {
            this.list = list;
            this.start = index;
            this.fence = fence;
            this.index = index;
        }

        public IndexBasedImmutableSpliterator(ImmutableIntList list) {
            this(list, 0, list.size());
        }

        @Override
        public long skip(long n) {
            Preconditions.checkArgument(n >= 0, "Argument must be nonnegative: %f", n);

            int i = index;
            index = (int) Math.min(index + n, fence);
            return index - i;
        }

        @Override
        public IntSpliterator trySplit() {
            return new IndexBasedImmutableSpliterator(list, start, index);
        }

        @Override
        public long estimateSize() {
            return fence - index;
        }

        @Override
        public long getExactSizeIfKnown() {
            return estimateSize();
        }

        @Override
        public int characteristics() {
            return IntSpliterators.LIST_SPLITERATOR_CHARACTERISTICS | Spliterator.IMMUTABLE;
        }

        @Override
        public boolean tryAdvance(java.util.function.IntConsumer action) {
            if (index >= fence) {
                return false;
            }

            action.accept(list.getInt(index++));
            return true;
        }
    }

    protected static class SubList extends ImmutableIntList {
        protected final ImmutableIntList list;
        protected final int from;
        protected final int size;

        public SubList(ImmutableIntList list, int from, int to) {
            super(hash(list, from, to));
            this.list = list;
            this.from = from;
            this.size = to - from;
        }

        @Override
        public int getInt(int index) {
            return list.getInt(index);
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public IntListIterator listIterator(int index) {
            return list.listIterator(from + index);
        }

        @Override
        public ImmutableIntList subList(int from, int to) {
            Preconditions.checkPositionIndexes(from, to, size());
            return new SubList(list, this.from + from, this.from + to);
        }

        private static int hash(ImmutableIntList list, int from, int to) {
            IntListIterator it = list.listIterator(from);
            int h = 1;

            while (to-- != from) {
                h = 31 * h + Integer.hashCode(it.nextInt());
            }

            return h;
        }
    }

    protected static class RandomAccessSubList extends SubList implements RandomAccess {
        public RandomAccessSubList(ImmutableIntList list, int from, int to) {
            super(list, from, to);
        }

        @Override
        public IntSpliterator spliterator() {
            return new IndexBasedImmutableSpliterator(list, from, from + size);
        }
    }
}

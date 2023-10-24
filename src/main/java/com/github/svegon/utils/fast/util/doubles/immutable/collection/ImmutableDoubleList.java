package com.github.svegon.utils.fast.util.doubles.immutable.collection;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.doubles.*;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.DoubleConsumer;

import static it.unimi.dsi.fastutil.Size64.sizeOf;

@Immutable
public abstract class ImmutableDoubleList extends ImmutableDoubleCollection implements DoubleList {
    ImmutableDoubleList(int hashCode) {
        super(hashCode);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        try {
            return compareTo((List) obj) == 0;
        } catch (ClassCastException ignored) {
            return false;
        }
    }

    @Override
    public final @NotNull DoubleListIterator iterator() {
        return listIterator();
    }

    @Override
    public boolean contains(double key) {
        return indexOf(key) >= 0;
    }

    @Override
    public double[] toArray(double[] a) {
        if (a.length < size()) {
            a = new double[size()];
        }

        getElements(0, a, 0, size());
        return a;
    }

    @Override
    public DoubleSpliterator spliterator() {
        if (this instanceof RandomAccess) {
            return new IndexBasedImmutableSpliterator(this);
        } else {
            return DoubleSpliterators.asSpliterator(iterator(), sizeOf(this),
                    DoubleSpliterators.LIST_SPLITERATOR_CHARACTERISTICS | Spliterator.IMMUTABLE);
        }
    }

    @Override
    public final void addElements(int index, double[] a, int offset, int length) {
        if (length != 0) {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public final boolean addAll(int index, @NotNull Collection<? extends Double> c) {
        Preconditions.checkPositionIndex(index, size());

        if (c.isEmpty()) {
            return false;
        }

        throw new UnsupportedOperationException();
    }

    @Override
    public final @NotNull DoubleListIterator listIterator() {
        return listIterator(0);
    }

    @Override
    public DoubleListIterator listIterator(int index) {
        return new RandomAccessImmutableListIterator(Preconditions.checkPositionIndex(index, size()));
    }

    @Override
    public ImmutableDoubleList subList(int from, int to) {
        Preconditions.checkPositionIndexes(from, to, size());
        return this instanceof RandomAccess ? new RandomAccessSubList(this, from, to)
                : new SubList(this, from, to);
    }

    @Override
    public final void size(int size) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void getElements(int from, double[] a, int offset, int length) {
        Preconditions.checkPositionIndexes(from, from + length, size());
        DoubleArrays.ensureOffsetLength(a, offset, length);

        DoubleListIterator it = listIterator(from);

        while (it.hasNext()) {
            a[it.nextIndex()] = it.nextDouble();
        }
    }

    @Override
    public final void removeElements(int from, int to) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final void addElements(int index, double[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final void add(int index, double key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean addAll(int index, DoubleCollection c) {
        Preconditions.checkPositionIndex(index, size());

        if (c.isEmpty()) {
            return false;
        }

        throw new UnsupportedOperationException();
    }

    @Override
    public final double set(int index, double k) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(double k) {
        DoubleListIterator it = listIterator();

        while (it.hasNext()) {
            if (it.nextDouble() == k) {
                return it.previousIndex();
            }
        }

        return -1;
    }

    @Override
    public int lastIndexOf(double k) {
        DoubleListIterator it = listIterator(size() - 1);

        while (it.hasPrevious()) {
            if (it.previousDouble() == k) {
                return it.nextIndex();
            }
        }

        return -1;
    }

    @Override
    public final double removeDouble(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final void sort(DoubleComparator comparator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final void unstableSort(DoubleComparator comparator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int compareTo(@NotNull List<? extends Double> o) {
        int c = size() - o.size();

        if (c != 0) {
            return c;
        }

        DoubleListIterator itr = listIterator();
        ListIterator<? extends Double> oItr = o.listIterator();

        while (itr.hasNext()) {
            if (!oItr.hasNext()) {
                return 1;
            }

            c = Double.compare(itr.nextDouble(), oItr.next());

            if (c != 0) {
                return c;
            }
        }

        return oItr.hasNext() ? -1 : 0;
    }

    public static ImmutableDoubleList of(double... values) {
        return values.length == 0 ? RegularImmutableDoubleList.EMPTY : new RegularImmutableDoubleList(values.clone());
    }

    public static ImmutableDoubleList of(DoubleIterator it) {
        return copyOf(new DoubleArrayList(it));
    }

    public static ImmutableDoubleList copyOf(DoubleIterable iterable) {
        if (iterable.getClass() == DoubleArrayList.class) {
            double[] array = ((DoubleArrayList) iterable).toDoubleArray();
            return array.length == 0 ? RegularImmutableDoubleList.EMPTY : new RegularImmutableDoubleList(array);
        }

        return iterable instanceof DoubleCollection ? of(((DoubleCollection) iterable).toDoubleArray())
                : of(iterable.iterator());
    }

    protected class RandomAccessImmutableListIterator implements DoubleListIterator {
        protected int index;

        public RandomAccessImmutableListIterator(int index) {
            this.index = index;
        }

        @Override
        public double nextDouble() {
            return ImmutableDoubleList.this.getDouble(index++);
        }

        @Override
        public boolean hasNext() {
            return index < ImmutableDoubleList.this.size();
        }

        @Override
        public double previousDouble() {
            return ImmutableDoubleList.this.getDouble(--index);
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

    public static class IndexBasedImmutableSpliterator implements DoubleSpliterator {
        private final DoubleList list;
        private final int start;
        private final int fence;
        private int index;

        public IndexBasedImmutableSpliterator(DoubleList list, int index, int fence) {
            this.list = list;
            this.start = index;
            this.fence = fence;
            this.index = index;
        }

        public IndexBasedImmutableSpliterator(DoubleList list) {
            this(list, 0, list.size());
        }

        @Override
        public long skip(long n) {
            int i = index;
            index = (int) Math.min(index + n, fence);

            if (index < 0) {
                index = fence;
            }

            return index - i;
        }

        @Override
        public DoubleSpliterator trySplit() {
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
            return DoubleSpliterators.LIST_SPLITERATOR_CHARACTERISTICS | Spliterator.IMMUTABLE;
        }

        @Override
        public boolean tryAdvance(DoubleConsumer action) {
            if (index >= fence) {
                return false;
            }

            action.accept(list.getDouble(index++));
            return true;
        }
    }

    private static int hash(DoubleList list, int from, int to) {
        int h = 1;
        DoubleListIterator it = list.listIterator(from);

        while (it.nextIndex() != to) {
            h = 31 * h + Double.hashCode(it.nextDouble());
        }

        return h;
    }

    protected static class SubList extends ImmutableDoubleList {
        protected final ImmutableDoubleList list;
        protected final int from;
        protected final int size;

        public SubList(ImmutableDoubleList list, int from, int to) {
            super(hash(list, from, to));
            this.list = list;
            this.from = from;
            this.size = to - from;
        }

        @Override
        public double getDouble(int index) {
            return list.getDouble(index);
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public ImmutableDoubleList subList(int from, int to) {
            Preconditions.checkPositionIndexes(from, to, size());
            return new SubList(list, this.from + from, this.from + to);
        }
    }

    protected static class RandomAccessSubList extends SubList implements RandomAccess {
        public RandomAccessSubList(ImmutableDoubleList list, int from, int to) {
            super(list, from, to);
        }

        @Override
        public DoubleSpliterator spliterator() {
            return new IndexBasedImmutableSpliterator(list, from, from + size);
        }
    }
}

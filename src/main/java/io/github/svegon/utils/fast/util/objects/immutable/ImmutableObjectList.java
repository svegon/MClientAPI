package io.github.svegon.utils.fast.util.objects.immutable;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.*;
import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static it.unimi.dsi.fastutil.Size64.sizeOf;

@Immutable
public abstract class ImmutableObjectList<E> extends ImmutableObjectCollection<E> implements ObjectList<E> {
    ImmutableObjectList(int hashCode) {
        super(hashCode);
    }

    @Override
    public final ImmutableObjectList<E> toList() {
        return this;
    }

    @Override
    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof List<?> list)) {
            return false;
        }

        if (size() != list.size()) {
            return false;
        }

        ListIterator<E> it = listIterator();
        ListIterator<?> itr = list.listIterator();

        while (it.hasNext()) {
            try {
                if (!Objects.equals(it.next(), itr.next())) {
                    return false;
                }
            } catch (NoSuchElementException e) {
                throw new ConcurrentModificationException(e);
            }
        }

        if (itr.hasNext()) {
            throw new ConcurrentModificationException();
        }

        return true;
    }

    @Override
    public final @NotNull ObjectListIterator<E> iterator() {
        return listIterator();
    }

    @Override
    public boolean contains(Object key) {
        return indexOf(key) >= 0;
    }

    @Override
    public int indexOf(final Object k) {
        final ObjectListIterator<E> i = listIterator();

        if (k == null) {
            while (i.hasNext()) {
                E e = i.next();

                if (e == null) {
                    return i.previousIndex();
                }
            }
        } else {
            while (i.hasNext()) {
                E e = i.next();

                if (k.equals(e)) {
                    return i.previousIndex();
                }
            }
        }

        return -1;
    }

    @Override
    public int lastIndexOf(final Object k) {
        ObjectListIterator<E> i = listIterator(size());

        if (k == null) {
            while (i.hasPrevious()) {
                E e = i.previous();

                if (e == null) {
                    return i.nextIndex();
                }
            }
        } else {
            while (i.hasPrevious()) {
                E e = i.previous();

                if (k.equals(e)) {
                    return i.nextIndex();
                }
            }
        }

        return -1;
    }

    @Override
    public ObjectSpliterator<E> spliterator() {
        if (this instanceof RandomAccess) {
            return new IndexBasedImmutableSpliterator<>(this);
        } else {
            return ObjectSpliterators.asSpliterator(iterator(), sizeOf(this),
                    ObjectSpliterators.LIST_SPLITERATOR_CHARACTERISTICS | Spliterator.IMMUTABLE);
        }
    }

    @Override
    public final void addElements(int index, E[] a, int offset, int length) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final @NotNull ObjectListIterator<E> listIterator() {
        return listIterator(0);
    }

    @Override
    public ObjectListIterator<E> listIterator(int index) {
        return new RandomAccessImmutableListIterator(Preconditions.checkPositionIndex(index, size()));
    }

    @Override
    public ImmutableObjectList<E> subList(int from, int to) {
        Preconditions.checkPositionIndexes(from, to, size());
        return this instanceof RandomAccess ? new RandomAccessSubList<>(this, from, to)
                : new SubList<>(this, from, to);
    }

    @Override
    public final void size(int size) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void getElements(int from, Object[] a, int offset, int length) {
        Preconditions.checkPositionIndexes(from, from + length, size());
        ObjectArrays.ensureOffsetLength(a, offset, length);

        ListIterator<E> it = listIterator(from);

        while (it.hasNext()) {
            a[it.nextIndex()] = it.next();
        }
    }

    @Override
    public final void removeElements(int from, int to) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final void addElements(int index, E[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final void add(int index, E key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean addAll(int index, Collection c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final E set(int index, E k) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final E remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final void sort(Comparator comparator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final void unstableSort(Comparator comparator) {
        throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("unchecked")
    public int compareTo(@NotNull List<? extends E> l) {
        if (l == this)
            return 0;

        if (l instanceof ObjectList) {
            final ObjectListIterator<E> i1 = listIterator(), i2 = ((ObjectList<E>) l).listIterator();
            int r;
            E e1, e2;
            while (i1.hasNext() && i2.hasNext()) {
                e1 = i1.next();
                e2 = i2.next();
                if ((r = (((Comparable<E>) (e1)).compareTo(e2))) != 0)
                    return r;
            }
            return i2.hasNext() ? -1 : (i1.hasNext() ? 1 : 0);
        }
        ListIterator<? extends E> i1 = listIterator(), i2 = l.listIterator();
        int r;
        while (i1.hasNext() && i2.hasNext()) {
            if ((r = ((Comparable<? super E>) i1.next()).compareTo(i2.next())) != 0)
                return r;
        }
        return i2.hasNext() ? -1 : (i1.hasNext() ? 1 : 0);
    }

    @SuppressWarnings("unchecked")
    public static <E> ImmutableObjectList<E> of(E @NotNull ... values) {
        return values.length == 0 ? ImmutableObjectArrayList.EMPTY : new ImmutableObjectArrayList<>(values.clone());
    }

    public static <E> ImmutableObjectList<E> of(Iterator<E> it) {
        return copyOf(Lists.newArrayList(it));
    }

    @SuppressWarnings("unchecked")
    public static <E> ImmutableObjectList<E> copyOf(Iterable<E> iterable) {
        if (iterable instanceof ImmutableObjectCollection<E> c) {
            return c.toList();
        }

        if (iterable.getClass() == ArrayList.class) {
            Object[] array = ((ArrayList<E>) iterable).toArray();
            return array.length == 0 ? ImmutableObjectArrayList.EMPTY : new ImmutableObjectArrayList<>(array);
        }

        return iterable instanceof Collection ? of((E[]) ((Collection<E>) iterable).toArray())
                : of(iterable.iterator());
    }

    @Override
    public final <T> @NotNull T @NotNull [] toArray(T @NotNull [] a) {
        if (a.length < size()) {
            a = Arrays.copyOf(a, size());
        }

        getElements(0, a, 0, size());

        return a;
    }

    public static <E> int hash(List<E> list, int from, int to) {
        ListIterator<E> it = list.listIterator(from);
        int h = 0;

        while (to-- != from) {
            h = 31 * h + Objects.hashCode(it.next());
        }

        return h;
    }

    @ThreadSafe
    protected class RandomAccessImmutableListIterator implements ObjectListIterator<E> {
        protected final AtomicInteger index = new AtomicInteger();

        public RandomAccessImmutableListIterator(int index) {
            this.index.set(index);
        }

        @Override
        public E next() {
            return ImmutableObjectList.this.get(index.getAndIncrement());
        }

        @Override
        public boolean hasNext() {
            return index.get() < ImmutableObjectList.this.size();
        }

        @Override
        public E previous() {
            return ImmutableObjectList.this.get(index.decrementAndGet());
        }

        @Override
        public boolean hasPrevious() {
            return index.get() >= 0;
        }

        @Override
        public int nextIndex() {
            return index.get();
        }

        @Override
        public int previousIndex() {
            return index.get() - 1;
        }
    }

    @ThreadSafe
    protected static class IndexBasedImmutableSpliterator<E> implements ObjectSpliterator<E> {
        private final ImmutableObjectList<E> list;
        private final int start;
        private final int fence;
        private int index;

        public IndexBasedImmutableSpliterator(ImmutableObjectList<E> list, int index, int fence) {
            this.list = list;
            this.start = index;
            this.fence = fence;
            this.index = index;
        }

        public IndexBasedImmutableSpliterator(ImmutableObjectList<E> list) {
            this(list, 0, list.size());
        }

        @Override
        public long skip(long n) {
            Preconditions.checkArgument(n >= 0, "Argument must be nonnegative: %f", n);

            synchronized (this) {
                int i = index;
                index = (int) Math.min(index + n, fence);
                return index - i;
            }
        }

        @Override
        public ObjectSpliterator<E> trySplit() {
            return new IndexBasedImmutableSpliterator<>(list, start, index);
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
            return ObjectSpliterators.LIST_SPLITERATOR_CHARACTERISTICS | Spliterator.IMMUTABLE;
        }

        @Override
        public boolean tryAdvance(Consumer<? super E> action) {
            E e;

            synchronized (this) {
                if (index >= fence) {
                    return false;
                }

                e = list.get(index++);
            }

            action.accept(e);
            return true;
        }
    }

    protected static class SubList<E> extends ImmutableObjectList<E> {
        protected final ImmutableObjectList<E> list;
        protected final int from;
        protected final int size;

        public SubList(ImmutableObjectList<E> list, int from, int to) {
            super(hash(list, from, to));
            this.list = list;
            this.from = from;
            this.size = to - from;
        }

        @Override
        public E get(int index) {
            return list.get(index);
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public ObjectListIterator<E> listIterator(int index) {
            return list.listIterator(from + index);
        }

        @Override
        public ImmutableObjectList<E> subList(int from, int to) {
            Preconditions.checkPositionIndexes(from, to, size());
            return new SubList<>(list, this.from + from, this.from + to);
        }
    }

    protected static class RandomAccessSubList<E> extends SubList<E> implements RandomAccess {
        public RandomAccessSubList(ImmutableObjectList<E> list, int from, int to) {
            super(list, from, to);
        }

        @Override
        public ObjectSpliterator<E> spliterator() {
            return new IndexBasedImmutableSpliterator<>(list, from, from + size);
        }
    }
}

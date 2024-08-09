package io.github.svegon.utils.collections;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.ObjectArrays;
import net.jcip.annotations.NotThreadSafe;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Predicate;

/**
 * a version of {@code java.util.ArrayList} which passes a reference of its array instead of copying where possible
 * It's unsynchronized which is likely to be problematic if used by multiple threads. You can fix that by enclosing
 * the exposed array list with {@code Collections.synchronizedList}.
 * @param <E>
 */
@NotThreadSafe
public class ExposedArrayList<E> extends AbstractList<E> implements RandomAccess, Cloneable {
    private Object[] a;

    public ExposedArrayList(Object[] array) {
        setArray(array);
    }

    public ExposedArrayList() {
        a = ObjectArrays.DEFAULT_EMPTY_ARRAY;
    }

    public ExposedArrayList(Collection<?> c) {
        this(c.toArray());
    }

    @Override
    public boolean add(E e) {
        a = ArrayUtil.concat(a, e);
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E get(int index) {
        return (E) a[index];
    }

    @Override
    public int size() {
        return a.length;
    }

    @Override
    public boolean contains(Object o) {
        return Arrays.asList(a).contains(o);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return addAll(size(), c);
    }

    @Override
    public boolean removeAll(final Collection<?> c) {
        return removeIf(c::contains);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean removeIf(final Predicate<? super E> filter) {
        return !Arrays.equals(a, a = ArrayUtil.filter(a, (o) -> !filter.test((E) o)));
    }

    @Override
    public boolean retainAll(final @NotNull Collection<?> c) {
        return removeIf((e) -> !c.contains(e));
    }

    @Override
    public E set(int index, E element) {
        E e = get(index);
        a[index] = element;
        return e;
    }

    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size()) {
            throw new IndexOutOfBoundsException(index);
        }

        Object[] data = a;
        data = Arrays.copyOf(data, data.length + 1);
        System.arraycopy(data, index, data, index + 1, data.length - index - 1);
        data[index] = element;
        a = data;
    }

    @Override
    public E remove(int index) {
        E e = get(index);
        removeRange(index, index + 1);
        return e;
    }

    @Override
    public boolean remove(Object o) {
        Object[] data = a;
        int i = Arrays.asList(data).indexOf(o);

        if (i < 0) {
            return false;
        }

        a = ArrayUtil.pop(data, i);
        return true;
    }

    @Override
    public void clear() {
        a = ObjectArrays.EMPTY_ARRAY;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (c.isEmpty()) {
            return false;
        }

        a = Arrays.copyOf(a, a.length + c.size());
        System.arraycopy(a, index, a, index + c.size(), a.length - index - c.size());

        for (E element : c) {
            a[index++] = element;
        }

        return true;
    }

    @Override
    public Iterator<E> iterator() {
        return listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new ListItr(index);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof List<?> list)) {
            return false;
        }

        if (list.getClass() == ExposedArrayList.class && getClass() == ExposedArrayList.class) {
            return Arrays.equals(a, ((ExposedArrayList<?>) list).a);
        }

        if (size() != list.size()) {
            return false;
        }

        ListIterator<E> e1 = listIterator();
        ListIterator<?> e2 = ((List<?>) o).listIterator();

        while (e1.hasNext() && e2.hasNext()) {
            E o1 = e1.next();
            Object o2 = e2.next();

            if (!(Objects.equals(o1, o2)))
                return false;
        }

        return !(e1.hasNext() || e2.hasNext());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(a);
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        Object[] data = a;
        System.arraycopy(data, toIndex, data, fromIndex, toIndex - fromIndex);
        a = Arrays.copyOf(data, data.length + fromIndex - toIndex);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ExposedArrayList<E> clone() throws CloneNotSupportedException {
        ExposedArrayList<E> clone = (ExposedArrayList<E>) super.clone();
        clone.a = a.clone();

        return clone;
    }

    @NotNull
    @Override
    public Object @NotNull [] toArray() {
        return a;
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public <T> T @NotNull [] toArray(@NotNull T @NotNull [] a) {
        Object[] data = this.a;

        if (a.length < data.length) {
            if (a.getClass() == Object[].class) {
                return (T[]) toArray();
            }

            return (T[]) Arrays.copyOf(data, data.length, a.getClass());
        }

        System.arraycopy(data, 0, a, 0, data.length);
        return a;
    }

    public final Object[] getArray() {
        return a;
    }

    public final void setArray(Object[] array) {
        a = Preconditions.checkNotNull(array);
    }

    public final class ListItr implements ListIterator<E> {
        private Object[] array = a;
        private int cursor;

        public ListItr(int cursor) {
            this.cursor = cursor;
        }

        @Override
        public boolean hasNext() {
            return cursor < array.length;
        }

        @Override
        @SuppressWarnings("unchecked")
        public E next() {
            try {
                return (E) array[cursor++];
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new NoSuchElementException();
            }
        }

        @Override
        public boolean hasPrevious() {
            return cursor > 0;
        }

        @Override
        @SuppressWarnings("unchecked")
        public E previous() {
            try {
                return (E) array[--cursor];
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new NoSuchElementException();
            }
        }

        @Override
        public int nextIndex() {
            return cursor;
        }

        @Override
        public int previousIndex() {
            return cursor - 1;
        }

        @Override
        public void remove() {
            if (cursor <= 0) {
                throw new IllegalStateException();
            }

            ExposedArrayList.this.remove(cursor - 1);
            array = ExposedArrayList.this.a;
        }

        @Override
        public void set(E e) {
            ExposedArrayList.this.set(cursor - 1, e);
            array = a;
        }

        @Override
        public void add(E e) {
            ExposedArrayList.this.add(cursor - 1, e);
            array = ExposedArrayList.this.a;
        }
    }
}

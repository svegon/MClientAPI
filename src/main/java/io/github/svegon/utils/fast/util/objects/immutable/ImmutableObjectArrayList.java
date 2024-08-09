package io.github.svegon.utils.fast.util.objects.immutable;

import it.unimi.dsi.fastutil.objects.ObjectArrays;

import java.util.Arrays;

public class ImmutableObjectArrayList<E> extends ImmutableObjectList<E> {
    public static final ImmutableObjectArrayList EMPTY = new ImmutableObjectArrayList(ObjectArrays.EMPTY_ARRAY);

    private final Object[] elements;

    ImmutableObjectArrayList(Object[] elements) {
        super(Arrays.hashCode(elements));
        this.elements = elements;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E get(int index) {
        return (E) elements[index];
    }

    @Override
    public int size() {
        return elements.length;
    }

    @Override
    public void getElements(int from, Object[] a, int offset, int length) {
        System.arraycopy(elements, from, a, offset, length);
    }
}

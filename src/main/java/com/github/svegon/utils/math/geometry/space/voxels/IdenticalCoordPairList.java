package com.github.svegon.utils.math.geometry.space.voxels;

import com.github.svegon.utils.interfaces.function.IntIntIntTriPredicate;
import it.unimi.dsi.fastutil.doubles.*;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.function.DoubleConsumer;

public final class IdenticalCoordPairList extends AbstractDoubleList implements CoordPairList {
    private final DoubleList coordList;

    public IdenticalCoordPairList(final @NotNull DoubleList coordList) {
        this.coordList = coordList;
    }

    @Override
    public boolean forEachIndexPair(@NotNull IntIntIntTriPredicate predicate) {
        int size = coordList.size() - 1;

        for (int i = 0; i < size; ++i) {
            if (predicate.test(i, i, i)) {
                continue;
            }

            return false;
        }
        return true;
    }

    @Override
    public int size() {
        return coordList.size();
    }

    @Override
    public boolean isEmpty() {
        return coordList.isEmpty();
    }

    @Override
    public DoubleListIterator iterator() {
        return coordList.iterator();
    }

    @Override
    public DoubleSpliterator spliterator() {
        return coordList.spliterator();
    }

    @Override
    public void forEach(DoubleConsumer action) {
        coordList.forEach(action);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return coordList.containsAll(c);
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends Double> c) {
        return coordList.addAll(index, c);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return coordList.removeAll(c);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return coordList.retainAll(c);
    }

    @Override
    public void clear() {
        coordList.clear();
    }

    @Override
    public DoubleListIterator listIterator(int index) {
        return coordList.listIterator(index);
    }

    @Override
    public IdenticalCoordPairList subList(int from, int to) {
        return new IdenticalCoordPairList(coordList.subList(from, to));
    }

    @Override
    public void size(int size) {
        coordList.size(size);
    }

    @Override
    public void getElements(int from, double[] a, int offset, int length) {
        coordList.getElements(from, a, offset, length);
    }

    @Override
    public void removeElements(int from, int to) {
        coordList.removeElements(from, to);
    }

    @Override
    public void addElements(int index, double[] a, int offset, int length) {
        coordList.addElements(index, a, offset, length);
    }

    @Override
    public void setElements(int index, double[] a, int offset, int length) {
        coordList.setElements(index, a, offset, length);
    }

    @Override
    public boolean add(double key) {
        return coordList.add(key);
    }

    @Override
    public boolean contains(double key) {
        return coordList.contains(key);
    }

    @Override
    public boolean rem(double key) {
        return coordList.rem(key);
    }

    @Override
    public double[] toDoubleArray() {
        return coordList.toDoubleArray();
    }

    @Override
    public double[] toArray(double[] a) {
        return coordList.toArray(a);
    }

    @Override
    public boolean addAll(DoubleCollection c) {
        return coordList.addAll(c);
    }

    @Override
    public boolean containsAll(DoubleCollection c) {
        return coordList.containsAll(c);
    }

    @Override
    public boolean removeAll(DoubleCollection c) {
        return coordList.removeAll(c);
    }

    @Override
    public boolean retainAll(DoubleCollection c) {
        return coordList.retainAll(c);
    }

    @Override
    public void add(int index, double key) {
        coordList.add(index, key);
    }

    @Override
    public boolean addAll(int index, DoubleCollection c) {
        return coordList.addAll(index, c);
    }

    @Override
    public double set(int index, double k) {
        return coordList.set(index, k);
    }

    @Override
    public double getDouble(int index) {
        return coordList.getDouble(index);
    }

    @Override
    public int indexOf(double k) {
        return coordList.indexOf(k);
    }

    @Override
    public int lastIndexOf(double k) {
        return coordList.lastIndexOf(k);
    }

    @Override
    public double removeDouble(int index) {
        return coordList.removeDouble(index);
    }

    @Override
    public boolean addAll(int index, DoubleList l) {
        return coordList.addAll(index, l);
    }

    @Override
    public void sort(DoubleComparator comparator) {
        coordList.sort(comparator);
    }

    @Override
    public void unstableSort(DoubleComparator comparator) {
        coordList.unstableSort(comparator);
    }

    @Override
    public int compareTo(@NotNull List<? extends Double> o) {
        return coordList.compareTo(o);
    }
}

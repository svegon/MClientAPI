package com.github.svegon.utils.math.geometry.space.voxels;

import com.github.svegon.utils.interfaces.function.IntIntIntTriPredicate;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.doubles.AbstractDoubleList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import org.jetbrains.annotations.NotNull;

public class DisjointCoordPairList extends AbstractDoubleList implements CoordPairList {
    private final DoubleList first;
    private final DoubleList second;
    private final double[] combinedElements;
    private final boolean invert;

    private DisjointCoordPairList(DoubleList first, DoubleList second, double[] combinedElements, boolean invert) {
        this.first = first;
        this.second = second;
        this.combinedElements = combinedElements;
        this.invert = invert;
    }

    public DisjointCoordPairList(DoubleList first, DoubleList second, boolean invert) {
        this(invert ? second : first, invert ? first : second, new double[first.size() + second.size()], invert);

        this.first.getElements(0, combinedElements, 0, this.first.size());
        this.second.getElements(0, combinedElements, this.first.size(), this.second.size());
    }

    @Override
    public boolean forEachIndexPair(final @NotNull IntIntIntTriPredicate predicate) {
        Preconditions.checkNotNull(predicate);

        IntIntIntTriPredicate effective = invert ? (i, j, k) -> predicate.test(j, i, k) : predicate;
        int finalIndex = first.size() - 1;

        for (int i = 0; i < first.size(); i++) {
            if (!effective.test(i, -1, i)) {
                return false;
            }
        }

        for (int i = 0; i < second.size(); i++) {
            if (!effective.test(finalIndex, i, first.size() + i)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public double getDouble(int index) {
        return combinedElements[index];
    }

    @Override
    public double set(int index, double k) {
        double ret = getDouble(index);
        combinedElements[index] = k;
        return ret;
    }

    @Override
    public int size() {
        return combinedElements.length;
    }
}

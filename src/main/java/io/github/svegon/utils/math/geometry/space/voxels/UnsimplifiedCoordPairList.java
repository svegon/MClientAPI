package io.github.svegon.utils.math.geometry.space.voxels;

import io.github.svegon.utils.interfaces.function.IntIntIntTriPredicate;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.doubles.AbstractDoubleList;
import it.unimi.dsi.fastutil.doubles.DoubleList;

public final class UnsimplifiedCoordPairList extends AbstractDoubleList implements CoordPairList {
    private final double[] valueIndices;
    private final int[] minValues;
    private final int[] maxValues;
    private final int size;

    public UnsimplifiedCoordPairList(DoubleList first, DoubleList second, boolean includeFirst, boolean includeSecond) {
        double d = Double.NaN;
        int firstSize = first.size();
        int secondSize = second.size();
        int total = firstSize + secondSize;
        this.valueIndices = new double[total];
        this.minValues = new int[total];
        this.maxValues = new int[total];
        int l = 0;
        int m = 0;
        int n = 0;

        while (true) {
            boolean bl3 = m >= firstSize;
            boolean bl4 = n >= secondSize;

            if (bl3 && bl4) {
                break;
            }

            boolean bl5 = !bl3 && (bl4 || first.getDouble(m) < second.getDouble(n) + 1.0E-7);

            if (bl5) {
                ++m;

                if (!includeFirst && (n == 0 || bl4)) {
                    continue;
                }
            } else {
                ++n;

                if (!includeSecond && (m == 0 || bl3)) {
                    continue;
                }
            }

            int o = m - 1;
            int p = n - 1;
            double e = bl5 ? first.getDouble(o) : second.getDouble(p);

            if (!(d >= e - 1.0E-7)) {
                this.minValues[l] = o;
                this.maxValues[l] = p;
                this.valueIndices[l] = e;
                ++l;
                d = e;
                continue;
            }

            this.minValues[l - 1] = o;
            this.maxValues[l - 1] = p;
        }

        this.size = Math.max(1, l);
    }

    @Override
    public double getDouble(int index) {
        Preconditions.checkElementIndex(index, size);
        return valueIndices[index];
    }

    @Override
    public int size() {
        return size;
    }

    public boolean forEachIndexPair(IntIntIntTriPredicate predicate) {
        Preconditions.checkNotNull(predicate);

        int i = size - 1;

        for (int j = 0; j < i; i++) {
            if (!predicate.test(minValues[j], maxValues[j], j)) {
                return false;
            }
        }

        return true;
    }
}

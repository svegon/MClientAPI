package io.github.svegon.utils.fast.util.doubles.doubles;

import it.unimi.dsi.fastutil.doubles.AbstractDoubleList;
import net.jcip.annotations.Immutable;

@Immutable
public final class FractionalDoubleList extends AbstractDoubleList {
    private final double sectionCount;
    private final int size;

    private FractionalDoubleList(double sectionCount, int size) {
        this.sectionCount = sectionCount;
        this.size = size;
    }

    public FractionalDoubleList(int sectionCount) {
        this(sectionCount, sectionCount + 1);
    }

    @Override
    public double getDouble(int index) {
        return index / sectionCount;
    }

    @Override
    public int size() {
        return size;
    }
}

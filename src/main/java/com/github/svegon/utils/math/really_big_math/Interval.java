package com.github.svegon.utils.math.really_big_math;

import com.github.svegon.utils.ConditionUtil;
import com.github.svegon.utils.fast.util.objects.OnNextComputeObjectIterator;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.jcip.annotations.Immutable;

import java.util.Iterator;

@Immutable
public final class Interval extends AbstractObjectSet<Number> {
    private static final byte MIN_INCLUDED_FLAG = 0x01;
    private static final byte MAX_INCLUDED_FLAG = 0x02;

    private final InfiniFloat min;
    private final InfiniFloat max;
    private final byte flags;

    Interval(InfiniFloat min, InfiniFloat max, byte flags) {
        this.min = min;
        this.max = max;
        this.flags = flags;
    }

    public InfiniFloat min() {
        return min;
    }

    public InfiniFloat max() {
        return max;
    }

    public boolean minIncluded() {
        return ConditionUtil.hasAllFlags(flags, MIN_INCLUDED_FLAG);
    }

    public boolean maxIncluded() {
        return ConditionUtil.hasAllFlags(flags, MAX_INCLUDED_FLAG);
    }

    public Iterator<InfiniFloat> iterator(InfiniFloat spacing) {
        return new Itr(Preconditions.checkNotNull(spacing).abs(), minIncluded() ? min : (InfiniFloat) min.add(spacing),
                max(), maxIncluded());
    }

    @Override
    public ObjectIterator<Number> iterator() {
        return (ObjectIterator<Number>) (Object) iterator(InfiniFloat.DIV_PRECISION);
    }

    @Override
    public int size() {
        if (min().equals(max())) {
            return ConditionUtil.hasAllFlags(flags, MIN_INCLUDED_FLAG | MAX_INCLUDED_FLAG) ? 1 : 0;
        }

        return Integer.MAX_VALUE;
    }

    @Override
    public boolean contains(Object o) {
        if (!(o instanceof Number n)) {
            return false;
        }

        int c = min().compareTo(n);

        if (minIncluded() ? c > 0 : c >= 0) {
            return false;
        }

        c = max().compareTo(n);

        return maxIncluded() ? c >= 0 : c > 0;
    }

    @Override
    public String toString() {
        return (minIncluded() ? "<" : "(") + min() + ", " + max() + (maxIncluded() ? ">" : ")");
    }

    public static Interval interval(InfiniFloat min, InfiniFloat max, boolean minIncluded, boolean maxIncluded) {
        if (min.compareTo(max) > 0) {
            InfiniFloat var = max;
            max = min;
            min = var;
        }

        byte flags = 0;

        if (minIncluded) {
            flags |= MIN_INCLUDED_FLAG;
        }

        if (maxIncluded) {
            flags |= MAX_INCLUDED_FLAG;
        }

        return new Interval(min, max, flags);
    }

    private static final class Itr extends OnNextComputeObjectIterator<InfiniFloat> {
        private final InfiniFloat spacing;
        private final InfiniFloat max;
        private final boolean maxIncluded;
        private InfiniFloat next;

        Itr(InfiniFloat spacing, InfiniFloat min, InfiniFloat max, boolean maxIncluded) {
            this.spacing = spacing;
            this.max = max;
            this.maxIncluded = maxIncluded;
            this.next = min;
        }

        @Override
        protected InfiniFloat computeNext() {
            next = next.add(spacing);
            int c = next.compareTo(max);

            if (maxIncluded ? c > 0 : c >= 0) {
                finish();
            }

            return next;
        }
    }
}

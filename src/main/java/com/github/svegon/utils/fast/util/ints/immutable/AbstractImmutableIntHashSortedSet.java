package com.github.svegon.utils.fast.util.ints.immutable;

import com.github.svegon.utils.collections.SetType;
import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.ints.IntComparator;
import it.unimi.dsi.fastutil.ints.IntHash;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractImmutableIntHashSortedSet extends AbstractImmutableIntSortedSet implements Hash {
    final IntHash.Strategy strategy;

    AbstractImmutableIntHashSortedSet(int hashCode, int[] elements, @Nullable IntComparator comparator,
                                      IntHash.Strategy strategy) {
        super(hashCode, elements, comparator);
        this.strategy = strategy;
    }

    AbstractImmutableIntHashSortedSet(int[] elements, @Nullable IntComparator comparator,
                                      IntHash.Strategy strategy) {
        this(hash(elements, strategy), elements, comparator, strategy);
    }

    @Override
    public final SetType getType() {
        return SetType.HASH;
    }

    static int hash(int[] elements, IntHash.Strategy strategy) {
        int h = 0;

        for (int i : elements) {
            h += strategy.hashCode(i);
        }

        return h;
    }
}

package io.github.svegon.utils.fast.util.shorts.transform.ints;

import io.github.svegon.utils.collections.SetUtil;
import io.github.svegon.utils.collections.iteration.IterationUtil;
import io.github.svegon.utils.fast.util.shorts.transform.TransformingShortSortedSet;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.Int2ShortFunction;
import it.unimi.dsi.fastutil.ints.IntSortedSet;
import it.unimi.dsi.fastutil.shorts.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class I2STransformingSortedSet extends TransformingShortSortedSet<Integer, IntSortedSet> {
    private final Int2ShortFunction forwardingTransformer;
    private final Short2IntFunction backingTransformer;

    public I2STransformingSortedSet(final IntSortedSet set,
                                    final Int2ShortFunction forwardingTransformer,
                                    final Short2IntFunction backingTransformer) {
        super(set);
        this.forwardingTransformer = Preconditions.checkNotNull(forwardingTransformer);
        this.backingTransformer = Preconditions.checkNotNull(backingTransformer);
    }

    @Override
    public ShortBidirectionalIterator iterator(short fromshortlement) {
        return IterationUtil.mapToShort(set.iterator(backingTransformer.get(fromshortlement)),
                forwardingTransformer);
    }

    @Override
    public ShortBidirectionalIterator iterator() {
        return IterationUtil.mapToShort(set.iterator(), forwardingTransformer);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(short o) {
        return set.contains(backingTransformer.get( o));
    }

    @Override
    public boolean removeIf(ShortPredicate filter) {
        Preconditions.checkNotNull(filter);
        return set.removeIf(s -> filter.test(forwardingTransformer.get(s)));
    }

    @Override
    public @Nullable ShortComparator comparator() {
        return set.comparator() != null ? (o1, o2) -> set.comparator().compare(backingTransformer.get(o1),
                backingTransformer.get(o2)) : (o1, o2) -> Integer.compare(backingTransformer.get(o1), backingTransformer.get(o2));
    }

    @Override
    public @NotNull ShortSortedSet subSet(short fromshortlement, short toshortlement) {
        return SetUtil.mapToShort(set.subSet(backingTransformer.get(fromshortlement),
                backingTransformer.get(toshortlement)), forwardingTransformer, backingTransformer);
    }

    @Override
    public @NotNull ShortSortedSet headSet(short toshortlement) {
        return SetUtil.mapToShort(set.headSet(backingTransformer.get(toshortlement)), forwardingTransformer,
                backingTransformer);
    }

    @Override
    public @NotNull ShortSortedSet tailSet(short fromshortlement) {
        return SetUtil.mapToShort(set.tailSet(backingTransformer.get(fromshortlement)), forwardingTransformer,
                backingTransformer);
    }

    @Override
    public short lastShort() {
        return forwardingTransformer.get(set.lastInt());
    }
}

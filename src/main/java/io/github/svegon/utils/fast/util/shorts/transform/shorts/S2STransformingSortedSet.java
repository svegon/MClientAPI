package io.github.svegon.utils.fast.util.shorts.transform.shorts;

import io.github.svegon.utils.collections.SetUtil;
import io.github.svegon.utils.collections.iteration.IterationUtil;
import io.github.svegon.utils.fast.util.shorts.transform.TransformingShortSortedSet;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.shorts.ShortSortedSet;
import it.unimi.dsi.fastutil.shorts.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class S2STransformingSortedSet extends TransformingShortSortedSet<Short, ShortSortedSet> {
    private final ShortUnaryOperator forwardingTransformer;
    private final ShortUnaryOperator backingTransformer;

    public S2STransformingSortedSet(final ShortSortedSet set,
                                    final ShortUnaryOperator forwardingTransformer,
                                    final ShortUnaryOperator backingTransformer) {
        super(set);
        this.forwardingTransformer = Preconditions.checkNotNull(forwardingTransformer);
        this.backingTransformer = Preconditions.checkNotNull(backingTransformer);
    }

    @Override
    public ShortBidirectionalIterator iterator(short fromshortlement) {
        return IterationUtil.mapToShort(set.iterator(backingTransformer.apply(fromshortlement)),
                forwardingTransformer);
    }

    @Override
    public ShortBidirectionalIterator iterator() {
        return IterationUtil.mapToShort(set.iterator(), forwardingTransformer);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(short o) {
        return set.contains(backingTransformer.apply( o));
    }

    @Override
    public boolean removeIf(ShortPredicate filter) {
        Preconditions.checkNotNull(filter);
        return set.removeIf(s -> filter.test(forwardingTransformer.apply(s)));
    }

    @Override
    public @Nullable ShortComparator comparator() {
        return set.comparator() != null ? (o1, o2) -> set.comparator().compare(backingTransformer.apply(o1),
                backingTransformer.apply(o2)) : (o1, o2) -> Short.compare(backingTransformer.apply(o1), backingTransformer.apply(o2));
    }

    @Override
    public @NotNull ShortSortedSet subSet(short fromshortlement, short toshortlement) {
        return SetUtil.mapToShort(set.subSet(backingTransformer.apply(fromshortlement),
                backingTransformer.apply(toshortlement)), forwardingTransformer, backingTransformer);
    }

    @Override
    public @NotNull ShortSortedSet headSet(short toshortlement) {
        return SetUtil.mapToShort(set.headSet(backingTransformer.apply(toshortlement)), forwardingTransformer,
                backingTransformer);
    }

    @Override
    public @NotNull ShortSortedSet tailSet(short fromshortlement) {
        return SetUtil.mapToShort(set.tailSet(backingTransformer.apply(fromshortlement)), forwardingTransformer,
                backingTransformer);
    }

    @Override
    public short lastShort() {
        return forwardingTransformer.apply(set.lastShort());
    }
}

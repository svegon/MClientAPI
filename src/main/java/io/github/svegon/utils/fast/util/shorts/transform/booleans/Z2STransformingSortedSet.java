package io.github.svegon.utils.fast.util.shorts.transform.booleans;

import io.github.svegon.utils.collections.SetUtil;
import io.github.svegon.utils.collections.iteration.IterationUtil;
import io.github.svegon.utils.fast.util.booleans.BooleanSortedSet;
import io.github.svegon.utils.fast.util.shorts.transform.TransformingShortSortedSet;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.booleans.Boolean2ShortFunction;
import it.unimi.dsi.fastutil.shorts.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Z2STransformingSortedSet extends TransformingShortSortedSet<Boolean, BooleanSortedSet> {
    private final Boolean2ShortFunction forwardingTransformer;
    private final ShortPredicate backingTransformer;

    public Z2STransformingSortedSet(final BooleanSortedSet set,
                                    final Boolean2ShortFunction forwardingTransformer,
                                    final ShortPredicate backingTransformer) {
        super(set);
        this.forwardingTransformer = Preconditions.checkNotNull(forwardingTransformer);
        this.backingTransformer = Preconditions.checkNotNull(backingTransformer);
    }

    @Override
    public ShortBidirectionalIterator iterator(short fromshortlement) {
        return IterationUtil.mapToShort(set.iterator(backingTransformer.test(fromshortlement)),
                forwardingTransformer);
    }

    @Override
    public ShortBidirectionalIterator iterator() {
        return IterationUtil.mapToShort(set.iterator(), forwardingTransformer);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(short o) {
        return set.contains(backingTransformer.test( o));
    }

    @Override
    public boolean removeIf(ShortPredicate filter) {
        Preconditions.checkNotNull(filter);
        return set.removeIf(s -> filter.test(forwardingTransformer.get(s)));
    }

    @Override
    public @Nullable ShortComparator comparator() {
        return set.comparator() != null ? (o1, o2) -> set.comparator().compare(backingTransformer.test(o1),
                backingTransformer.test(o2)) : (o1, o2) -> Boolean.compare(backingTransformer.test(o1),
                backingTransformer.test(o2));
    }

    @Override
    public @NotNull ShortSortedSet subSet(short fromshortlement, short toshortlement) {
        return SetUtil.mapToShort(set.subSet(backingTransformer.test(fromshortlement),
                backingTransformer.test(toshortlement)), forwardingTransformer, backingTransformer);
    }

    @Override
    public @NotNull ShortSortedSet headSet(short toshortlement) {
        return SetUtil.mapToShort(set.headSet(backingTransformer.test(toshortlement)), forwardingTransformer,
                backingTransformer);
    }

    @Override
    public @NotNull ShortSortedSet tailSet(short fromshortlement) {
        return SetUtil.mapToShort(set.tailSet(backingTransformer.test(fromshortlement)), forwardingTransformer,
                backingTransformer);
    }

    @Override
    public short lastShort() {
        return forwardingTransformer.get(set.lastBoolean());
    }
}

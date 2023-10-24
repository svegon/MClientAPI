package com.github.svegon.utils.fast.util.chars.transform.shorts;

import com.github.svegon.utils.collections.SetUtil;
import com.github.svegon.utils.collections.iteration.IterationUtil;
import com.github.svegon.utils.fast.util.chars.transform.TransformingCharSortedSet;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.shorts.Short2CharFunction;
import it.unimi.dsi.fastutil.shorts.ShortSortedSet;
import it.unimi.dsi.fastutil.chars.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class S2CTransformingSortedSet extends TransformingCharSortedSet<Short, ShortSortedSet> {
    private final Short2CharFunction forwardingTransformer;
    private final Char2ShortFunction backingTransformer;

    public S2CTransformingSortedSet(final ShortSortedSet set,
                                    final Short2CharFunction forwardingTransformer,
                                    final Char2ShortFunction backingTransformer) {
        super(set);
        this.forwardingTransformer = Preconditions.checkNotNull(forwardingTransformer);
        this.backingTransformer = Preconditions.checkNotNull(backingTransformer);
    }

    @Override
    public CharBidirectionalIterator iterator(char fromcharlement) {
        return IterationUtil.mapToChar(set.iterator(backingTransformer.get(fromcharlement)),
                forwardingTransformer);
    }

    @Override
    public CharBidirectionalIterator iterator() {
        return IterationUtil.mapToChar(set.iterator(), forwardingTransformer);
    }

    @Override
    public boolean contains(char o) {
        return set.contains(backingTransformer.get( o));
    }

    @Override
    public boolean removeIf(CharPredicate filter) {
        Preconditions.checkNotNull(filter);
        return set.removeIf(s -> filter.test(forwardingTransformer.get(s)));
    }

    @Override
    public @Nullable CharComparator comparator() {
        return set.comparator() != null ? (o1, o2) -> set.comparator().compare(backingTransformer.get(o1),
                backingTransformer.get(o2))
                : (o1, o2) -> Short.compare(backingTransformer.get(o1), backingTransformer.get(o2));
    }

    @Override
    public @NotNull CharSortedSet subSet(char fromcharlement, char tocharlement) {
        return SetUtil.mapToChar(set.subSet(backingTransformer.get(fromcharlement),
                backingTransformer.get(tocharlement)), forwardingTransformer, backingTransformer);
    }

    @Override
    public @NotNull CharSortedSet headSet(char tocharlement) {
        return SetUtil.mapToChar(set.headSet(backingTransformer.get(tocharlement)), forwardingTransformer,
                backingTransformer);
    }

    @Override
    public @NotNull CharSortedSet tailSet(char fromcharlement) {
        return SetUtil.mapToChar(set.tailSet(backingTransformer.get(fromcharlement)), forwardingTransformer,
                backingTransformer);
    }

    @Override
    public char lastChar() {
        return forwardingTransformer.get(set.lastShort());
    }
}

package com.github.svegon.utils.fast.util.chars.transform.ints;

import com.github.svegon.utils.collections.SetUtil;
import com.github.svegon.utils.collections.iteration.IterationUtil;
import com.github.svegon.utils.fast.util.chars.transform.TransformingCharSortedSet;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.chars.*;
import it.unimi.dsi.fastutil.ints.Int2CharFunction;
import it.unimi.dsi.fastutil.ints.IntSortedSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class I2CTransformingSortedSet extends TransformingCharSortedSet<Integer, IntSortedSet> {
    private final Int2CharFunction forwardingTransformer;
    private final Char2IntFunction backingTransformer;

    public I2CTransformingSortedSet(final IntSortedSet set,
                                    final Int2CharFunction forwardingTransformer,
                                    final Char2IntFunction backingTransformer) {
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
                : (o1, o2) -> Integer.compare(backingTransformer.get(o1), backingTransformer.get(o2));
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
        return forwardingTransformer.get(set.lastInt());
    }
}

package io.github.svegon.utils.fast.util.chars.transform.booleans;

import io.github.svegon.utils.collections.SetUtil;
import io.github.svegon.utils.collections.iteration.IterationUtil;
import io.github.svegon.utils.fast.util.booleans.BooleanSortedSet;
import io.github.svegon.utils.fast.util.chars.transform.TransformingCharSortedSet;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.booleans.Boolean2CharFunction;
import it.unimi.dsi.fastutil.chars.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Z2CTransformingSortedSet extends TransformingCharSortedSet<Boolean, BooleanSortedSet> {
    private final Boolean2CharFunction forwardingTransformer;
    private final CharPredicate backingTransformer;

    public Z2CTransformingSortedSet(final BooleanSortedSet set,
                                    final Boolean2CharFunction forwardingTransformer,
                                    final CharPredicate backingTransformer) {
        super(set);
        this.forwardingTransformer = Preconditions.checkNotNull(forwardingTransformer);
        this.backingTransformer = Preconditions.checkNotNull(backingTransformer);
    }

    @Override
    public CharBidirectionalIterator iterator(char fromcharlement) {
        return IterationUtil.mapToChar(set.iterator(backingTransformer.test(fromcharlement)),
                forwardingTransformer);
    }

    @Override
    public CharBidirectionalIterator iterator() {
        return IterationUtil.mapToChar(set.iterator(), forwardingTransformer);
    }

    @Override
    public boolean contains(char o) {
        return set.contains(backingTransformer.test( o));
    }

    @Override
    public boolean removeIf(CharPredicate filter) {
        Preconditions.checkNotNull(filter);
        return set.removeIf(s -> filter.test(forwardingTransformer.get(s)));
    }

    @Override
    public @Nullable CharComparator comparator() {
        return set.comparator() != null ? (o1, o2) -> set.comparator().compare(backingTransformer.test(o1),
                backingTransformer.test(o2)) : (o1, o2) -> Boolean.compare(backingTransformer.test(o1), backingTransformer.test(o2));
    }

    @Override
    public @NotNull CharSortedSet subSet(char fromcharlement, char tocharlement) {
        return SetUtil.mapToChar(set.subSet(backingTransformer.test(fromcharlement),
                backingTransformer.test(tocharlement)), forwardingTransformer, backingTransformer);
    }

    @Override
    public @NotNull CharSortedSet headSet(char tocharlement) {
        return SetUtil.mapToChar(set.headSet(backingTransformer.test(tocharlement)), forwardingTransformer,
                backingTransformer);
    }

    @Override
    public @NotNull CharSortedSet tailSet(char fromcharlement) {
        return SetUtil.mapToChar(set.tailSet(backingTransformer.test(fromcharlement)), forwardingTransformer,
                backingTransformer);
    }

    @Override
    public char lastChar() {
        return forwardingTransformer.get(set.lastBoolean());
    }
}

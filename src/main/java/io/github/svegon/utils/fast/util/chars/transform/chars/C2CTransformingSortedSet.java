package io.github.svegon.utils.fast.util.chars.transform.chars;

import io.github.svegon.utils.collections.SetUtil;
import io.github.svegon.utils.collections.iteration.IterationUtil;
import io.github.svegon.utils.fast.util.chars.transform.TransformingCharSortedSet;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.chars.CharUnaryOperator;
import it.unimi.dsi.fastutil.chars.CharSortedSet;
import it.unimi.dsi.fastutil.chars.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class C2CTransformingSortedSet extends TransformingCharSortedSet<Character, CharSortedSet> {
    private final CharUnaryOperator forwardingTransformer;
    private final CharUnaryOperator backingTransformer;

    public C2CTransformingSortedSet(final CharSortedSet set,
                                    final CharUnaryOperator forwardingTransformer,
                                    final CharUnaryOperator backingTransformer) {
        super(set);
        this.forwardingTransformer = Preconditions.checkNotNull(forwardingTransformer);
        this.backingTransformer = Preconditions.checkNotNull(backingTransformer);
    }

    @Override
    public CharBidirectionalIterator iterator(char fromcharlement) {
        return IterationUtil.mapToChar(set.iterator(backingTransformer.apply(fromcharlement)),
                forwardingTransformer);
    }

    @Override
    public CharBidirectionalIterator iterator() {
        return IterationUtil.mapToChar(set.iterator(), forwardingTransformer);
    }

    @Override
    public boolean contains(char o) {
        return set.contains(backingTransformer.apply( o));
    }

    @Override
    public boolean removeIf(CharPredicate filter) {
        Preconditions.checkNotNull(filter);
        return set.removeIf(s -> filter.test(forwardingTransformer.apply(s)));
    }

    @Override
    public @Nullable CharComparator comparator() {
        return set.comparator() != null ? (o1, o2) -> set.comparator().compare(backingTransformer.apply(o1),
                backingTransformer.apply(o2))
                : (o1, o2) -> Character.compare(backingTransformer.apply(o1), backingTransformer.apply(o2));
    }

    @Override
    public @NotNull CharSortedSet subSet(char fromcharlement, char tocharlement) {
        return SetUtil.mapToChar(set.subSet(backingTransformer.apply(fromcharlement),
                backingTransformer.apply(tocharlement)), forwardingTransformer, backingTransformer);
    }

    @Override
    public @NotNull CharSortedSet headSet(char tocharlement) {
        return SetUtil.mapToChar(set.headSet(backingTransformer.apply(tocharlement)), forwardingTransformer,
                backingTransformer);
    }

    @Override
    public @NotNull CharSortedSet tailSet(char fromcharlement) {
        return SetUtil.mapToChar(set.tailSet(backingTransformer.apply(fromcharlement)), forwardingTransformer,
                backingTransformer);
    }

    @Override
    public char lastChar() {
        return forwardingTransformer.apply(set.lastChar());
    }
}

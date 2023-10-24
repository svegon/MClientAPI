package com.github.svegon.utils.fast.util.chars.transform;

import it.unimi.dsi.fastutil.chars.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.SortedSet;

public abstract class TransformingCharSortedSet<E, S extends SortedSet<E>> extends TransformingCharSet<E, S>
        implements CharSortedSet {
    protected TransformingCharSortedSet(S set) {
        super(set);
    }

    @Override
    public abstract CharBidirectionalIterator iterator(char fromElement);

    @Override
    public abstract CharBidirectionalIterator iterator();

    @Override
    public CharSpliterator spliterator() {
        return CharSortedSet.super.spliterator();
    }

    @Override
    public abstract boolean contains(char o);

    @Override
    public abstract boolean removeIf(CharPredicate filter);

    @Nullable
    @Override
    public abstract CharComparator comparator();

    @NotNull
    @Override
    public abstract CharSortedSet subSet(char fromElement, char toElement);

    @NotNull
    @Override
    public abstract CharSortedSet headSet(char toElement);

    @NotNull
    @Override
    public abstract CharSortedSet tailSet(char fromElement);

    @Override
    public final char firstChar() {
        return iterator().nextChar();
    }

    @Override
    public abstract char lastChar();
}

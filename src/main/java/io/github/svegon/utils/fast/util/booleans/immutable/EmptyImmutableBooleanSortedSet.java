package io.github.svegon.utils.fast.util.booleans.immutable;

import io.github.svegon.utils.fast.util.booleans.BooleanSortedSet;
import it.unimi.dsi.fastutil.booleans.BooleanBidirectionalIterator;
import it.unimi.dsi.fastutil.booleans.BooleanComparator;
import it.unimi.dsi.fastutil.booleans.BooleanIterators;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;

public final class EmptyImmutableBooleanSortedSet extends ImmutableBooleanSortedSet {
    EmptyImmutableBooleanSortedSet(@Nullable BooleanComparator comparator) {
        super(comparator);
    }

    @Override
    public ImmutableBooleanList asList() {
        return RegularImmutableBooleanList.EMPTY;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean contains(boolean key) {
        return false;
    }

    @Override
    public BooleanBidirectionalIterator iterator(boolean fromElement) {
        return iterator();
    }

    @Override
    public BooleanBidirectionalIterator iterator() {
        return BooleanIterators.EMPTY_ITERATOR;
    }

    @Override
    public BooleanSortedSet subSet(boolean fromElement, boolean toElement) {
        return this;
    }

    @Override
    public BooleanSortedSet headSet(boolean toElement) {
        return this;
    }

    @Override
    public BooleanSortedSet tailSet(boolean fromElement) {
        return this;
    }

    @Override
    public boolean firstBoolean() {
        throw new NoSuchElementException();
    }

    @Override
    public boolean lastBoolean() {
        throw new NoSuchElementException();
    }

    @Override
    public int size() {
        return 0;
    }
}

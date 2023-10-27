package io.github.svegon.utils.fast.util.booleans.immutable;

import io.github.svegon.utils.fast.util.booleans.BooleanSortedSet;
import it.unimi.dsi.fastutil.booleans.BooleanBidirectionalIterator;
import it.unimi.dsi.fastutil.booleans.BooleanComparator;
import it.unimi.dsi.fastutil.booleans.BooleanIterators;
import org.jetbrains.annotations.NotNull;

public final class FullImmutableBooleanSortedSet extends ImmutableBooleanSortedSet {
    public static final FullImmutableBooleanSortedSet DEFAULT_SORTING = new FullImmutableBooleanSortedSet();

    private final BooleanComparator actualComparator;

    private FullImmutableBooleanSortedSet() {
        super(null);
        this.actualComparator = Boolean::compare;
    }

    FullImmutableBooleanSortedSet(@NotNull BooleanComparator comparator) {
        super(comparator);
        this.actualComparator = comparator;
    }

    @Override
    public int hashCode() {
        return 2468;
    }

    @Override
    public boolean contains(boolean key) {
        return true;
    }

    @Override
    public boolean lastBoolean() {
        return actualComparator.compare(false, true) <= 0;
    }

    @Override
    public int size() {
        return 2;
    }

    @Override
    public BooleanBidirectionalIterator iterator(boolean fromElement) {
        boolean first = firstBoolean();
        return first == fromElement ? BooleanIterators.wrap(new boolean[]{first, !first})
                : BooleanIterators.singleton(!first);
    }

    @Override
    public BooleanBidirectionalIterator iterator() {
        boolean first = firstBoolean();
        return BooleanIterators.wrap(new boolean[]{first, !first});
    }

    @Override
    public BooleanSortedSet subSet(boolean fromElement, boolean toElement) {
        return actualComparator.compare(fromElement, toElement) < 0 ? of(comparator(),
                fromElement) : empty(comparator());
    }

    @Override
    public BooleanSortedSet headSet(boolean toElement) {
        return subSet(firstBoolean(), toElement);
    }

    @Override
    public BooleanSortedSet tailSet(boolean fromElement) {
        return subSet(fromElement, lastBoolean());
    }

    @Override
    public boolean firstBoolean() {
        return actualComparator.compare(false, true) > 0;
    }
}

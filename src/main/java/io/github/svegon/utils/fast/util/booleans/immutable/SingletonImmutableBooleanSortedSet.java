package io.github.svegon.utils.fast.util.booleans.immutable;

import it.unimi.dsi.fastutil.booleans.BooleanBidirectionalIterator;
import it.unimi.dsi.fastutil.booleans.BooleanComparator;
import it.unimi.dsi.fastutil.booleans.BooleanIterators;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;
import io.github.svegon.utils.fast.util.booleans.BooleanSortedSet;

@Immutable
public final class SingletonImmutableBooleanSortedSet extends ImmutableBooleanSortedSet {
    public static final SingletonImmutableBooleanSortedSet FALSE = new SingletonImmutableBooleanSortedSet(false);
    public static final SingletonImmutableBooleanSortedSet TRUE = new SingletonImmutableBooleanSortedSet(true);

    private final BooleanComparator actualComparator;
    private final boolean value;

    private SingletonImmutableBooleanSortedSet(boolean value) {
        super(null);
        this.actualComparator = Boolean::compare;
        this.value = value;
    }

    SingletonImmutableBooleanSortedSet(@NotNull BooleanComparator comparator, boolean value) {
        super(comparator);
        this.actualComparator = comparator;
        this.value = value;
    }

    @Override
    public int hashCode() {
        return Boolean.hashCode(value);
    }

    @Override
    public boolean contains(boolean key) {
        return key == value;
    }

    @Override
    public BooleanBidirectionalIterator iterator(boolean fromElement) {
        return fromElement == value ? iterator() : BooleanIterators.EMPTY_ITERATOR;
    }

    @Override
    public @NotNull BooleanBidirectionalIterator iterator() {
        return BooleanIterators.singleton(value);
    }

    @Override
    public BooleanSortedSet subSet(boolean fromElement, boolean toElement) {
        return actualComparator.compare(fromElement, toElement) == 0
                ? empty(comparator()) : this;
    }

    @Override
    public BooleanSortedSet headSet(boolean toElement) {
        return actualComparator.compare(value, toElement) < 0 ? this : empty(comparator());
    }

    @Override
    public BooleanSortedSet tailSet(boolean fromElement) {
        return actualComparator.compare(value, fromElement) < 0 ? empty(comparator()) : this;
    }

    @Override
    public boolean firstBoolean() {
        return value;
    }

    @Override
    public boolean lastBoolean() {
        return value;
    }

    @Override
    public int size() {
        return 1;
    }
}

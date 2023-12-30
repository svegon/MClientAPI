package io.github.svegon.utils.fast.util.booleans.immutable;

import io.github.svegon.utils.collections.ArrayUtil;
import it.unimi.dsi.fastutil.booleans.*;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import io.github.svegon.utils.fast.util.booleans.BooleanSortedSet;

import java.util.Spliterator;

import static it.unimi.dsi.fastutil.Size64.sizeOf;

@Immutable
public abstract class ImmutableBooleanSortedSet extends ImmutableBooleanSet implements BooleanSortedSet {
    private final BooleanComparator comparator;

    ImmutableBooleanSortedSet(final @Nullable BooleanComparator comparator) {
        this.comparator = comparator;
    }

    @Override
    public final BooleanSpliterator spliterator() {
        return BooleanSpliterators.asSpliteratorFromSorted(iterator(), sizeOf(this),
                BooleanSpliterators.SORTED_SET_SPLITERATOR_CHARACTERISTICS | Spliterator.IMMUTABLE,
                comparator());
    }

    @Override
    public final ImmutableBooleanSortedSet asSortedSet(@Nullable BooleanComparator comparator) {
        return comparator == comparator() ? this : copyOf(this, comparator);
    }

    @Override
    public abstract boolean contains(boolean key);

    @Override
    public final BooleanComparator comparator() {
        return comparator;
    }

    @Override
    public abstract boolean lastBoolean();

    @Override
    public abstract int size();

    public static ImmutableBooleanSortedSet empty(final @Nullable BooleanComparator comparator) {
        return new EmptyImmutableBooleanSortedSet(comparator);
    }

    public static ImmutableBooleanSortedSet of(final boolean element, final @Nullable BooleanComparator comparator) {
        if (comparator == null) {
            return element ? SingletonImmutableBooleanSortedSet.TRUE : SingletonImmutableBooleanSortedSet.FALSE;
        }

        return new SingletonImmutableBooleanSortedSet(comparator, element);
    }

    public static ImmutableBooleanSortedSet both(final @Nullable BooleanComparator comparator) {
        return comparator != null ? new FullImmutableBooleanSortedSet(comparator)
                : FullImmutableBooleanSortedSet.DEFAULT_SORTING;
    }

    public static ImmutableBooleanSortedSet of(final @Nullable BooleanComparator comparator,
                                               final boolean @NotNull ... values) {
        return copyOf(comparator, ArrayUtil.asList(values).iterator());
    }

    public static ImmutableBooleanSortedSet copyOf(final @NotNull BooleanIterable iterable,
                                                   final @Nullable BooleanComparator comparator) {
        if (iterable instanceof BooleanCollection c) {
            if (c.contains(false)) {
                return c.contains(true) ? both(comparator) : of(comparator, false);
            } else {
                return c.contains(true) ? of(comparator, true) : empty(comparator);
            }
        }

        return copyOf(comparator, iterable.iterator());
    }

    public static ImmutableBooleanSortedSet copyOf(final @Nullable BooleanComparator comparator,
                                                   final @NotNull BooleanIterator itr) {
        boolean true_present = false;
        boolean false_present = false;

        while (itr.hasNext() && !(true_present && false_present)) {
            if (itr.nextBoolean()) {
                true_present = true;
            } else {
                false_present = true;
            }
        }

        if (false_present) {
            return true_present ? both(comparator) : of(comparator, false);
        } else {
            return true_present ? of(comparator, true) : empty(comparator);
        }
    }

    public static ImmutableBooleanSortedSet copyOf(final @NotNull BooleanSortedSet set) {
        if (set instanceof ImmutableBooleanSortedSet s) {
            return s;
        }

        return copyOf(set.comparator(), set.iterator());
    }
}

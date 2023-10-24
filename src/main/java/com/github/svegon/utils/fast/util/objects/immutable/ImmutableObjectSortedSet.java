package com.github.svegon.utils.fast.util.objects.immutable;

import com.github.svegon.utils.ComparingUtil;
import com.github.svegon.utils.hash.HashUtil;
import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Immutable
public abstract class ImmutableObjectSortedSet<E> extends ImmutableObjectSet<E> implements ObjectSortedSet<E>,
        Comparator<E> {
    ImmutableObjectSortedSet(int hashCode) {
        super(hashCode);
    }

    @Override
    public ImmutableObjectSortedSet<E> toSortedSet(@Nullable Comparator<? super E> comparator) {
        return comparator == comparator() ? this : copyOf(comparator, iterator());
    }

    @Override
    public abstract ObjectBidirectionalIterator<E> iterator(@Nullable E fromElement);

    @Override
    public abstract ObjectBidirectionalIterator<E> iterator();

    @Override
    public abstract ImmutableObjectSortedSet<E> subSet(@Nullable E fromElement, @Nullable E toElement);

    @Override
    public abstract ImmutableObjectSortedSet<E> headSet(@Nullable E toElement);

    @Override
    public abstract ImmutableObjectSortedSet<E> tailSet(@Nullable E fromElement);

    @Nullable
    @Override
    public abstract Comparator<? super E> comparator();

    @Override
    public int compare(E o1, E o2) {
        return ComparingUtil.compare(comparator(), o1, o2);
    }

    public static <E> ImmutableObjectSortedSet<E> of(final @Nullable Comparator<? super E> comparator) {
        return comparator == null ? EmptyImmutableObjectSortedSet.DEFAULT : new EmptyImmutableObjectSortedSet<>(comparator);
    }

    @SuppressWarnings("unchecked")
    public static <E> ImmutableNullSingletonSortedSet<E> of(final @Nullable Comparator<? super E> comparator,
                                                            final E value) {
        if (value == null) {
            return comparator == null ? ImmutableNullSingletonSortedSet.DEFAULT
                    : new ImmutableNullSingletonSortedSet(comparator);
        } else {
            return new ImmutableObjectSingletonSortedSet<>(value, comparator);
        }
    }

    public static <E> ImmutableObjectSortedSet<E> of(final @Nullable Comparator<? super E> comparator,
                                              final E @NotNull ... values) {
        return copyOf(comparator, Arrays.asList(values));
    }

    public static <E> ImmutableObjectSortedSet<E> copyOf(final @Nullable Comparator<? super E> comparator,
                                                         final @NotNull Iterable<E> iterable) {
        return copyOf(HashUtil.defaultStrategy(), comparator, iterable);
    }

    public static <E> ImmutableObjectSortedSet<E> copyOf(final @Nullable Comparator<? super E> comparator,
                                                         final @NotNull Iterator<E> iterator) {
        return copyOf(HashUtil.defaultStrategy(), comparator, iterator);
    }

    public static <E> ImmutableObjectSortedSet<E> of(final @Nullable Hash.Strategy<? super E> strategy,
                                                     final @Nullable Comparator<? super E> comparator,
                                                     final E @NotNull ... values) {
        return copyOf(strategy, comparator, Arrays.asList(values));
    }

    public static <E> ImmutableObjectSortedSet<E> copyOf(final @Nullable Hash.Strategy<? super E> strategy,
                                                         final @Nullable Comparator<? super E> comparator,
                                                         final @NotNull Iterable<E> iterable) {
        return iterable instanceof Collection<E> c ? of(new ObjectOpenCustomHashSet<>(c,
                strategy == null ? HashUtil.defaultStrategy() : strategy), comparator)
                : copyOf(strategy, comparator, iterable.iterator());
    }

    public static <E> ImmutableObjectSortedSet<E> copyOf(@Nullable Hash.Strategy<? super E> strategy,
                                                         final @Nullable Comparator<? super E> comparator,
                                                         final @NotNull Iterator<E> iterator) {
        if (strategy == null) {
            strategy = HashUtil.defaultStrategy();
        }

        return of(new ObjectOpenCustomHashSet<>(iterator, strategy), comparator);
    }

    static <E> ImmutableObjectSortedSet<E> of(final @NotNull ObjectOpenCustomHashSet<E> unmodifiableSet,
                                              final @Nullable Comparator<? super E> comparator) {
        return switch (unmodifiableSet.size()) {
            case 0 -> of(comparator);
            case 1 -> of(comparator, unmodifiableSet.iterator().next());
            default -> new ImmutableObjectHashSortedSet<>(unmodifiableSet, comparator);
        };
    }
}

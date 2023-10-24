package com.github.svegon.utils.fast.util.objects.immutable;

import com.github.svegon.utils.hash.HashUtil;
import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Immutable
public abstract class ImmutableObjectSet<E> extends ImmutableObjectCollection<E> implements ObjectSet<E> {
    ImmutableObjectSet(int hashCode) {
        super(hashCode);
    }

    @Override
    public final ImmutableObjectSet<E> toSet() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Set<?> s)) {
            return false;
        }

        if (size() != s.size()) {
            return false;
        }

        return containsAll(s);
    }

    @Override
    public abstract boolean contains(@Nullable Object element);

    public static <E> ImmutableObjectSet<E> of() {
        return ImmutableObjectSortedSet.of((Comparator<? super E>) null);
    }

    public static <E> ImmutableObjectSet<E> of(final @NotNull E value) {
        return ImmutableObjectSortedSet.of(null, value);
    }

    public static <E> ImmutableObjectSet<E> of(final E @NotNull ... values) {
        return copyOf(Arrays.asList(values));
    }

    public static <E> ImmutableObjectSet<E> copyOf(final @NotNull Iterable<E> iterable) {
        return iterable instanceof Collection<E> c ? of(new ObjectOpenCustomHashSet<>(c, HashUtil.defaultStrategy()))
                : copyOf(iterable.iterator());
    }

    public static <E> ImmutableObjectSet<E> copyOf(final @NotNull Iterator<E> iterator) {
        return of(new ObjectOpenCustomHashSet<>(iterator, HashUtil.defaultStrategy()));
    }

    public static <E> ImmutableObjectSet<E> of(final @Nullable Hash.Strategy<? super E> strategy,
                                               final E @NotNull ... values) {
        return copyOf(strategy, Arrays.asList(values));
    }

    public static <E> ImmutableObjectSet<E> copyOf(final @Nullable Hash.Strategy<? super E> strategy,
                                                   final @NotNull Iterable<E> iterable) {
        return iterable instanceof Collection<E> c ? of(new ObjectOpenCustomHashSet<>(c,
                strategy == null ? HashUtil.defaultStrategy() : strategy)) : copyOf(strategy, iterable.iterator());
    }

    public static <E> ImmutableObjectSet<E> copyOf(@Nullable Hash.Strategy<? super E> strategy,
                                                   final @NotNull Iterator<E> iterator) {
        if (strategy == null) {
            strategy = HashUtil.defaultStrategy();
        }

        return of(new ObjectOpenCustomHashSet<>(iterator, strategy));
    }

    static <E> ImmutableObjectSet<E> of(ObjectOpenCustomHashSet<E> hashSet) {
        switch (hashSet.size()) {
            case 0 -> {
                return of();
            }
            case 1 -> {
                return of(hashSet.iterator().next());
            }
            default -> {
                return new ImmutableObjectHashSet<>(hashSet);
            }
        }
    }
}

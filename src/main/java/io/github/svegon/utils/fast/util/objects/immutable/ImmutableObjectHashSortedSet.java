package io.github.svegon.utils.fast.util.objects.immutable;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ObjectIterators;
import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

public final class ImmutableObjectHashSortedSet<E> extends ImmutableObjectSortedSet<E> implements Hash {
    private final Comparator<? super E> comparator;
    private final Strategy<? super E> strategy;
    final E[][] tree;
    final E[] elements;

    @SuppressWarnings("unchecked")
    ImmutableObjectHashSortedSet(ObjectOpenCustomHashSet<E> model, Comparator<? super E> cmp) {
        super(model.hashCode());
        comparator = cmp;
        strategy = model.strategy();
        tree = (E[][]) new Object[HashCommon.arraySize(size(), VERY_FAST_LOAD_FACTOR)][];
        elements = (E[]) model.toArray();

        ImmutableObjectHashSet.fillTree(tree, model, strategy);
        Arrays.sort(elements, comparator);
    }

    @SuppressWarnings("unchecked")
    ImmutableObjectHashSortedSet(Strategy<? super E> strategy, Comparator<? super E> comparator, E[] elements) {
        super(hash(elements, strategy));
        this.strategy = strategy;
        this.comparator = comparator;
        this.elements = elements;
        this.tree = (E[][]) new Object[HashCommon.arraySize(size(), VERY_FAST_LOAD_FACTOR)][];

        ImmutableObjectHashSet.fillTree(tree, Arrays.asList(elements), strategy);
    }

    @Override
    public ImmutableObjectSortedSet<E> toSortedSet(@Nullable Comparator<? super E> comparator) {
        return Objects.equals(comparator(), comparator) ? this
                : ImmutableObjectSortedSet.copyOf(strategy(), comparator, iterator());
    }

    @Override
    public int size() {
        return elements.length;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(@Nullable Object element) {
        E[] branch = tree[(Objects.hashCode(element) & 0x7fffffff) % tree.length];

        for (E e : branch) {
            try {
                if (strategy().equals((E) element, e)) {
                    return true;
                }
            } catch (ClassCastException ignore) {

            }
        }

        return false;
    }

    @Override
    public ObjectBidirectionalIterator<E> iterator(@Nullable E fromElement) {
        ObjectBidirectionalIterator<E> it = iterator();

        while (it.hasNext()) {
            if (compare(it.next(), fromElement) >= 0) {
                it.previous();
                break;
            }
        }

        return it;
    }

    @Override
    public @NotNull ObjectBidirectionalIterator<E> iterator() {
        return ObjectIterators.wrap(elements);
    }

    @Override
    public ImmutableObjectSortedSet<E> subSet(@Nullable E fromElement, @Nullable E toElement) {
        if (compare(fromElement, toElement) > 0) {
            throw new IllegalArgumentException("fromElement " + fromElement + " > toElement" + toElement);
        }

        int from = 0;
        int to = elements.length;

        while (compare(elements[from], fromElement) < 0) {
            from++;

            if (from >= elements.length) {
                return ImmutableObjectSortedSet.of(comparator());
            }
        }

        while (compare(elements[--to], toElement) > 0) {
            if (to <= 0) {
                return ImmutableObjectSortedSet.of(comparator());
            }
        }

        if (from > ++to) {
            throw new IllegalArgumentException();
        }

        if (to <= from) {
            return ImmutableObjectSortedSet.of(comparator());
        }

        if (from == 0 && to == elements.length) {
            return this;
        }

        return new ImmutableObjectHashSortedSet<>(strategy(), comparator(), Arrays.copyOfRange(elements, from, to));
    }

    @Override
    public ImmutableObjectSortedSet<E> headSet(@Nullable E toElement) {
        int from = 0;
        int to = elements.length;

        while (compare(elements[--to], toElement) > 0) {
            if (to <= 0) {
                return ImmutableObjectSortedSet.of(comparator());
            }
        }

        if (++to == elements.length) {
            return this;
        }

        return new ImmutableObjectHashSortedSet<>(strategy(), comparator(), Arrays.copyOfRange(elements, from, to));
    }

    @Override
    public ImmutableObjectSortedSet<E> tailSet(@Nullable E fromElement) {
        int from = 0;

        while (compare(elements[from], fromElement) < 0) {
            from++;

            if (from >= elements.length) {
                return ImmutableObjectSortedSet.of(comparator());
            }
        }

        if (from == 0) {
            return this;
        }

        return new ImmutableObjectHashSortedSet<>(strategy(), comparator(),
                Arrays.copyOfRange(elements, from, elements.length));
    }

    @Override
    public E first() {
        return elements[0];
    }

    @Override
    public E last() {
        return elements[elements.length - 1];
    }

    @Override
    public @Nullable Comparator<? super E> comparator() {
        return comparator;
    }

    public Strategy<? super E> strategy() {
        return strategy;
    }

    private static <E> int hash(E[] elements, Strategy<? super E> strategy) {
        int h = 0;

        for (E e : elements) {
            h += strategy.hashCode(e);
        }

        return h;
    }
}

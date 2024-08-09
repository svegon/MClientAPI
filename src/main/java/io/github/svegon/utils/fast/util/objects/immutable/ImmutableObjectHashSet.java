package io.github.svegon.utils.fast.util.objects.immutable;

import io.github.svegon.utils.collections.iteration.IterationUtil;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectIterators;
import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public final class ImmutableObjectHashSet<E> extends ImmutableObjectSet<E> implements Hash {
    private final Strategy<? super E> strategy;
    private final int size;
    final Object[][] tree;

    ImmutableObjectHashSet(ObjectOpenCustomHashSet<E> model) {
        super(model.hashCode());
        strategy = model.strategy();
        size = model.size();
        tree = new Object[HashCommon.arraySize(size(), VERY_FAST_LOAD_FACTOR)][];

        fillTree(tree, model, strategy);
    }

    @Override
    public ImmutableObjectSortedSet<E> toSortedSet(@Nullable Comparator<? super E> comparator) {
        return ImmutableObjectSortedSet.copyOf(strategy(), comparator, iterator());
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(@Nullable Object element) {
        Object[] branch = tree[(Objects.hashCode(element) & 0x7fffffff) % tree.length];

        for (Object e : branch) {
            try {
                if (strategy().equals((E) element, (E) e)) {
                    return true;
                }
            } catch (ClassCastException ignore) {

            }
        }

        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ObjectIterator<E> iterator() {
        return (ObjectIterator<E>) IterationUtil.flatMapToObj(ObjectIterators.wrap(tree),
                (Function<Object[], @Nullable ObjectIterator<Object>>) ObjectIterators::wrap);
    }

    public Strategy<? super E> strategy() {
        return strategy;
    }

    public static <E> void fillTree(final @NotNull Object[][] tree, final @NotNull Iterable<E> elements,
                                    final @NotNull Hash.Strategy<? super E> strategy) {
        final List<E>[] intermediateTree = new List[tree.length];

        Arrays.setAll(intermediateTree, (i) -> Lists.newLinkedList());

        for (E e : elements) {
            intermediateTree[(strategy.hashCode(e) & 0x7fffffff) % intermediateTree.length].add(e);
        }

        Arrays.setAll(tree, (i) -> intermediateTree[i].toArray());
    }
}

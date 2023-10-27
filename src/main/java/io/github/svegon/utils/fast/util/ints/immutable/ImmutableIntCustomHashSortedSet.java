package io.github.svegon.utils.fast.util.ints.immutable;

import io.github.svegon.utils.collections.ArrayUtil;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.ints.*;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class ImmutableIntCustomHashSortedSet extends AbstractImmutableIntHashSortedSet {
    final int[][] tree;

    ImmutableIntCustomHashSortedSet(int[][] tree, int[] elements, IntHash.Strategy strategy,
                                    @Nullable IntComparator comparator) {
        super(hash(strategy, elements), elements, comparator, strategy);
        this.tree = tree;
    }

    ImmutableIntCustomHashSortedSet(int[] elements, IntHash.Strategy strategy,
                                    @Nullable IntComparator comparator) {
        this(elements.length < ArrayUtil.PARALLEL_REVERSE_NO_FORK ? makeTree(elements, strategy)
                : makeTreeFast(elements, strategy), elements, strategy, comparator);

        IntArrays.unstableSort(elements, comparator);
    }

    @Override
    public ImmutableIntCustomHashSortedSet toSortedSet(@Nullable IntComparator comparator) {
        return comparator == comparator() ? this : new ImmutableIntCustomHashSortedSet(elements, strategy, comparator);
    }

    @Override
    public boolean contains(int key) {
        int hash = strategy.hashCode(key);
        int[] branch = tree[(hash & 0x7fffffff) % tree.length];

        for (int j : branch) {
            if (strategy.equals(key, j)) {
                return true;
            }
        }

        return false;
    }

    private static int[][] makeTree(int[] elements, IntHash.Strategy strategy) {
        int[][] tree = new int[HashCommon.arraySize(elements.length, VERY_FAST_LOAD_FACTOR)][0];

        for (int i : elements) {
            int index = (strategy.hashCode(i) & 0x7fffffff) % tree.length;
            int[] branch = tree[index] = Arrays.copyOf(tree[index], tree[index].length + 1);
            branch[branch.length - 1] = i;
        }

        return tree;
    }

    private static int[][] makeTreeFast(int[] elements, IntHash.Strategy strategy) {
        final int[][] tree = new int[HashCommon.arraySize(elements.length, VERY_FAST_LOAD_FACTOR)][];
        final IntList[] unitializedTree = new IntList[tree.length];
        final int initialLength = elements.length / unitializedTree.length + 1;

        Arrays.setAll(unitializedTree, (i) -> new IntArrayList(initialLength));

        for (int i : elements) {
            int index = (strategy.hashCode(i) & 0x7fffffff) % unitializedTree.length;
            unitializedTree[index].add(i);
        }

        Arrays.setAll(tree, (i) -> unitializedTree[i].toIntArray());

        return tree;
    }

    private static int hash(IntHash.Strategy strategy, int[] elements) {
        int h = 0;

        for (int i : elements) {
            h += strategy.hashCode(i);
        }

        return h;
    }
}

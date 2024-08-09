package io.github.svegon.utils.math.geometry.space.voxels;

import io.github.svegon.utils.interfaces.function.IntIntIntTriPredicate;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import org.jetbrains.annotations.NotNull;

import java.util.RandomAccess;

/**
 * a support class for voxel shape operations
 */
public interface CoordPairList extends DoubleList, RandomAccess {
    boolean forEachIndexPair(final @NotNull IntIntIntTriPredicate predicate);
}

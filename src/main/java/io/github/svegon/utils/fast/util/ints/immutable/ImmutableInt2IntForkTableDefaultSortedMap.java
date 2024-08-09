package io.github.svegon.utils.fast.util.ints.immutable;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.IntHash;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Immutable
public final class ImmutableInt2IntForkTableDefaultSortedMap extends ImmutableInt2IntForkTableSortedMap {
    ImmutableInt2IntForkTableDefaultSortedMap(int hashCode, int @NotNull [] keys,
                                              @NotNull ImmutableInt2IntForkTableSortedMap original) {
        super(hashCode, keys, original);
    }

    ImmutableInt2IntForkTableDefaultSortedMap(@NotNull Int2IntMap map, final @Nullable IntHash.Strategy strategy) {
        super(map, strategy, null);
    }

    @Override
    public int compare(int k1, int k2) {
        return Integer.compare(k1, k2);
    }

    @Override
    protected ImmutableInt2IntForkTableSortedMap rangeCopy(int hashCode, int[] keys,
                                                             ImmutableInt2IntForkTableSortedMap original) {
        return new ImmutableInt2IntForkTableDefaultSortedMap(hashCode, keys, original);
    }
}

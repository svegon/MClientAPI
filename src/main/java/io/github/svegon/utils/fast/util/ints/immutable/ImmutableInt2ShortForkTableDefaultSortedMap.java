package io.github.svegon.utils.fast.util.ints.immutable;

import it.unimi.dsi.fastutil.ints.Int2ShortMap;
import it.unimi.dsi.fastutil.ints.IntHash;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Immutable
public final class ImmutableInt2ShortForkTableDefaultSortedMap extends ImmutableInt2ShortForkTableSortedMap {
    ImmutableInt2ShortForkTableDefaultSortedMap(int hashCode, int @NotNull [] keys,
                                                @NotNull ImmutableInt2ShortForkTableSortedMap original) {
        super(hashCode, keys, original);
    }

    ImmutableInt2ShortForkTableDefaultSortedMap(@NotNull Int2ShortMap map, final @Nullable IntHash.Strategy strategy) {
        super(map, strategy, null);
    }

    @Override
    public int compare(int k1, int k2) {
        return Integer.compare(k1, k2);
    }

    @Override
    protected ImmutableInt2ShortForkTableSortedMap rangeCopy(int hashCode, int[] keys,
                                                             ImmutableInt2ShortForkTableSortedMap original) {
        return new ImmutableInt2ShortForkTableDefaultSortedMap(hashCode, keys, original);
    }
}

package io.github.svegon.capi.mixininterface;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatHandler;

public interface IStatHandler {
    Object2IntMap<Stat<?>> getStatMap();

    void copyFrom(StatHandler statHandler);
}

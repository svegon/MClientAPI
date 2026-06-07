package io.github.svegon.mclientapi.mixin;

import io.github.svegon.mclientapi.mixininterface.IStatsCounter;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatsCounter;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(StatsCounter.class)
public abstract class StatsCounterMixin implements IStatsCounter {
    @Shadow protected @Final Object2IntMap<Stat<?>> stats;

    @Override
    public Object2IntMap<Stat<?>> getStatMap() {
        return stats;
    }

    @Override
    public void copyFrom(@NotNull StatsCounter statHandler) {
        stats.clear();
        stats.putAll(((IStatsCounter) statHandler).getStatMap());
    }
}

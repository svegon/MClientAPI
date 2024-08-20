package io.github.svegon.mclientapi.mixin;

import io.github.svegon.mclientapi.mixininterface.MClientAPIStatHandler;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(StatHandler.class)
public abstract class StatHandlerMixin implements MClientAPIStatHandler {
    @Shadow
    @Final
    protected Object2IntMap<Stat<?>> statMap;

    @Override
    public Object2IntMap<Stat<?>> getStatMap() {
        return statMap;
    }

    @Override
    public void copyFrom(StatHandler statHandler) {
        statMap.clear();
        statMap.putAll(((MClientAPIStatHandler) statHandler).getStatMap());
    }
}

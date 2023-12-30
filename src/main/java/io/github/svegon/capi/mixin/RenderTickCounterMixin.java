package io.github.svegon.capi.mixin;

import io.github.svegon.capi.mixininterface.IRenderTickCounter;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RenderTickCounter.class)
public abstract class RenderTickCounterMixin implements IRenderTickCounter {
    @Shadow
    private long prevTimeMillis;
    @Shadow
    @Final
    @Mutable
    private float tickTime;

    @Override
    public long getPrevTimeMillis() {
        return prevTimeMillis;
    }

    @Override
    public void setPrevTimeMillis(long prevTimeMillis) {
        this.prevTimeMillis = prevTimeMillis;
    }

    @Override
    public float getTickTime() {
        return tickTime;
    }

    @Override
    public void setTickTime(float tickTime) {
        this.tickTime = tickTime;
    }
}

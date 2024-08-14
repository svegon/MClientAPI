package io.github.svegon.mclientapi.client.mixin;

import io.github.svegon.mclientapi.client.mixinterface.IRenderTickCounter;
import it.unimi.dsi.fastutil.floats.FloatUnaryOperator;
import net.minecraft.client.render.RenderTickCounter;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RenderTickCounter.class)
public interface RenderTickCounterMixin {
    @Mixin(RenderTickCounter.Dynamic.class)
    abstract class Dynamic implements RenderTickCounter, IRenderTickCounter {
        @Shadow
        private float tickDeltaBeforePause;
        @Shadow
        private long prevTimeMillis;
        @Shadow
        private long timeMillis;
        @Mutable
        @Shadow
        private @Final float tickTime;
        @Mutable
        @Shadow
        private @Final FloatUnaryOperator targetMillisPerTick;
        @Shadow
        private boolean paused;
        @Shadow
        private boolean tickFrozen;

        @Override
        public float getTickDeltaBeforePause() {
            return tickDeltaBeforePause;
        }

        @Override
        public void setTickDeltaBeforePause(float v) {
            tickDeltaBeforePause = v;
        }

        @Override
        public long getTimeMillis() {
            return timeMillis;
        }

        @Override
        public void setTimeMillis(long l) {
            timeMillis = l;
        }

        @NotNull
        @Override
        public FloatUnaryOperator getTargetMillisPerTick() {
            return targetMillisPerTick;
        }

        @Override
        public void setTargetMillisPerTick(@NotNull FloatUnaryOperator floatUnaryOperator) {
            targetMillisPerTick = floatUnaryOperator;
        }

        @Override
        public boolean getPaused() {
            return paused;
        }

        @Override
        public void setPaused(boolean b) {
            paused = b;
        }

        @Override
        public boolean getTickFrozen() {
            return tickFrozen;
        }

        @Override
        public void setTickFrozen(boolean b) {
            tickFrozen = b;
        }

        @Override
        public long getPrevTimeMillis() {
            return prevTimeMillis;
        }

        @Override
        public void setPrevTimeMillis(long l) {
            prevTimeMillis = l;
        }

        @Override
        public float getTickTime() {
            return tickTime;
        }

        @Override
        public void setTickTime(float v) {
            tickTime = v;
        }

        @Override
        public float getTps() {
            return 1000 / tickTime;
        }

        @Override
        public void setTps(float value) {
            tickTime = 1000 / value;
        }
    }
}

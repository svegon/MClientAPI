package io.github.svegon.mclientapi.client.mixin.input;

import io.github.svegon.mclientapi.client.mixinterface.ITickTimer;
import it.unimi.dsi.fastutil.floats.FloatUnaryOperator;
import net.minecraft.client.DeltaTracker;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(DeltaTracker.class)
public interface DeltaTrackerMixin {
    @Mixin(DeltaTracker.Timer.class)
    abstract class Timer implements DeltaTracker, ITickTimer {
        @Shadow private long lastMs;
        @Shadow private long lastUiMs;
        @Mutable @Shadow private @Final float msPerTick;
        @Mutable @Shadow private @Final FloatUnaryOperator targetMsptProvider;
        @Shadow private boolean paused;
        @Shadow private boolean frozen;

        @Override
        public @NotNull FloatUnaryOperator getMClientAPI$targetMillisPerTick() {
            return targetMsptProvider;
        }

        @Override
        public void setMClientAPI$targetMillisPerTick(@NotNull FloatUnaryOperator floatUnaryOperator) {
            this.targetMsptProvider = floatUnaryOperator;
        }

        @Override
        public boolean getMClientAPI$paused() {
            return paused;
        }

        @Override
        public long getMClientAPI$prevGameTime() {
            return lastMs;
        }

        @Override
        public void setMClientAPI$prevGameTime(long l) {
            this.lastMs = l;
        }

        @Override
        public long getMClientAPI$prevRealTime() {
            return lastUiMs;
        }

        @Override
        public float getMClientAPI$tickTime() {
            return msPerTick;
        }

        @Override
        public void setMClientAPI$tickTime(float v) {
            msPerTick = v;
        }

        @Override
        public void setMClientAPI$prevRealTime(long l) {
            this.lastUiMs = l;
        }

        @Override
        public boolean getMClientAPI$frozen() {
            return frozen;
        }
    }
}

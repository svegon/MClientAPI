package io.github.svegon.mclientapi.client.mixin.rendering;

import io.github.svegon.mclientapi.client.event.render.block.IsFluidSideCoveredCallback;
import io.github.svegon.mclientapi.client.event.render.block.SameFluidCheckCallback;
import net.minecraft.client.renderer.block.FluidRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FluidRenderer.class)
public abstract class FluidRendererMixin {
    @Inject(method = "isNeighborSameFluid", at = @At("HEAD"), cancellable = true)
    private static void onIsSameFluid(FluidState a, FluidState b, CallbackInfoReturnable<Boolean> callback) {
        SameFluidCheckCallback.Companion.getEVENT().invoker().isSameFluid(a, b, callback);
    }

    @Inject(method = "isFaceOccludedByState", at = @At("HEAD"), cancellable = true)
    private static void onIsSideCovered(final Direction direction, final float height, final BlockState state,
                                        CallbackInfoReturnable<Boolean> callback) {
        IsFluidSideCoveredCallback.Companion.getEVENT().invoker().isFluidSideCovered(direction,
                height, state, callback);
    }
}

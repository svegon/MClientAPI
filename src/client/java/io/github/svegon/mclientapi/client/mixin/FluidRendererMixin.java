package io.github.svegon.mclientapi.client.mixin;

import io.github.svegon.mclientapi.client.event.render.IsFluidSideCoveredCallback;
import io.github.svegon.mclientapi.client.event.render.SameFluidCheckCallback;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.block.FluidRenderer;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FluidRenderer.class)
public abstract class FluidRendererMixin {
    @Inject(method = "isSameFluid", at = @At("HEAD"), cancellable = true)
    private static void onIsSameFluid(FluidState a, FluidState b, CallbackInfoReturnable<Boolean> callback) {
        SameFluidCheckCallback.EVENT.invoker().isSameFluid(a, b, callback);
    }

    @Inject(method = "isSideCovered(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/Direction;" +
            "FLnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Z", at = @At("HEAD"),
            cancellable = true)
    private static void onIsSideCovered(BlockView world, Direction direction, float height, BlockPos pos,
                                        BlockState state, CallbackInfoReturnable<Boolean> callback) {
        IsFluidSideCoveredCallback.EVENT.invoker().isFluidSideCovered(world, direction, height, pos, state, callback);
    }
}

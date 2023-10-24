package com.github.svegon.capi.mixin;

import com.github.svegon.capi.event.render.ShouldDrawSideCallback;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "me.jellysquid.mods.sodium.client.render.occlusion.BlockOcclusionCache", remap = false)
public abstract class SodiumBlockOcclusionCacheMixin {
	@Inject(method = "shouldDrawSide", at = @At("HEAD"), remap = false, cancellable = true)
	public void shouldDrawSide(BlockState state, BlockView view, BlockPos pos, Direction facing,
							   CallbackInfoReturnable<Boolean> callback) {
		ShouldDrawSideCallback.EVENT.invoker().shouldDrawSide(state, view, pos, facing, pos.offset(facing), callback);
	}
}

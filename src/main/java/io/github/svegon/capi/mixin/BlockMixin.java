package io.github.svegon.capi.mixin;

import io.github.svegon.capi.event.render.ShouldDrawSideCallback;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemConvertible;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class BlockMixin extends AbstractBlock implements ItemConvertible {
    private BlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "shouldDrawSide", at = @At("HEAD"), cancellable = true)
    private static void onShouldDrawSide(BlockState state, BlockView world, BlockPos pos, Direction side,
                                         BlockPos otherPos, CallbackInfoReturnable<Boolean> callback) {
        ShouldDrawSideCallback.EVENT.invoker().shouldDrawSide(state, world, pos, side, otherPos, callback);
    }
}

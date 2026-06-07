package io.github.svegon.mclientapi.client.mixin.block;

import io.github.svegon.mclientapi.client.event.render.block.ShouldDrawSideCallback;
import net.fabricmc.fabric.api.block.v1.FabricBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class BlockMixin extends BlockBehaviour implements ItemLike, FabricBlock {
    @Inject(method = "shouldRenderFace", at = @At("HEAD"), cancellable = true)
    private static void onShouldDrawSide(BlockState state, BlockState neighborState, Direction direction,
                                         CallbackInfoReturnable<Boolean> cir) {
        ShouldDrawSideCallback.Companion.getEVENT().invoker().shouldDrawSide(state, neighborState, direction, cir);
    }

    private BlockMixin(Properties properties) {
        super(properties);
    }
}

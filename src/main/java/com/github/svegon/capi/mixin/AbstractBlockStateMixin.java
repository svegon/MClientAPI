package com.github.svegon.capi.mixin;

import com.github.svegon.capi.event.block.BlockStateShapeEvents;
import com.github.svegon.capi.event.render.BlockRenderTypeCallback;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockStateMixin {
    @Shadow protected abstract BlockState asBlockState();

    @Inject(method = "getRenderType", at = @At("RETURN"), cancellable = true)
    private void onGetRenderType(CallbackInfoReturnable<BlockRenderType> callback) {
        BlockRenderTypeCallback.EVENT.invoker().getBlockRenderType(asBlockState(), callback);
    }

    @Inject(method = "getOutlineShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;" +
            "Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;", at = @At("RETURN"),
            cancellable = true)
    private void onGetOultineShape(BlockView world, BlockPos pos, ShapeContext context,
                                   CallbackInfoReturnable<VoxelShape> cir) {
        BlockStateShapeEvents.OUTLINE_SHAPE.invoker().getOutlineShape(asBlockState(), world, pos, context, cir);
    }

    @Inject(method = "getCollisionShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;" +
            "Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;", at = @At("RETURN"),
            cancellable = true)
    private void onGetCollisionShape(BlockView world, BlockPos pos, ShapeContext context,
                                     CallbackInfoReturnable<VoxelShape> cir) {
        BlockStateShapeEvents.COLLISION_SHAPE.invoker().getCollisionShape(asBlockState(), world, pos, context, cir);
    }

    @Inject(method = "getCameraCollisionShape", at = @At("RETURN"), cancellable = true)
    private void onGetCameraCollisionShape(BlockView world, BlockPos pos, ShapeContext context,
                                     CallbackInfoReturnable<VoxelShape> cir) {
        BlockStateShapeEvents.CAMERA_COLLISION_SHAPE.invoker().getCameraCollisionShape(asBlockState(),
                world, pos, context, cir);
    }

    @Inject(method = "getRaycastShape", at = @At("RETURN"), cancellable = true)
    private void onGetRaycastShape(BlockView world, BlockPos pos, CallbackInfoReturnable<VoxelShape> cir) {
        BlockStateShapeEvents.RAYCAST_SHAPE.invoker().getRaycastShape(asBlockState(), world, pos, cir);
    }
}

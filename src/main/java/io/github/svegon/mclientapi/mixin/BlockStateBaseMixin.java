package io.github.svegon.mclientapi.mixin;

import io.github.svegon.mclientapi.event.block.BlockStateShapeEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.TypedInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockStateBaseMixin extends StateHolder<@NotNull Block, @NotNull BlockState>
        implements TypedInstance<@NotNull Block> {
    @Shadow protected abstract BlockState asState();

    @Inject(method = "getFaceOcclusionShape", at = @At("RETURN"), cancellable = true)
    private void onGetFaceOcclusionShape(final Direction direction, CallbackInfoReturnable<RenderShape> callback) {
        BlockStateShapeEvents.INSTANCE.getFACE_OCCLUSION_SHAPE().invoker().getFaceOcclusionShape(asState(),
                direction, callback);
    }

    @Inject(method = "getOcclusionShape", at = @At("RETURN"), cancellable = true)
    private void onGetOcclusionShape(CallbackInfoReturnable<RenderShape> callback) {
        BlockStateShapeEvents.INSTANCE.getOCCLUSION_SHAPE().invoker().getOcclusionShape(asState(), callback);
    }

    @Inject(method = "getRenderShape", at = @At("RETURN"), cancellable = true)
    private void onGetRenderType(CallbackInfoReturnable<RenderShape> callback) {
        BlockStateShapeEvents.INSTANCE.getRENDER_SHAPE().invoker().getRenderShape(asState(), callback);
    }

    @Inject(method = "getShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;" +
            "Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;",
            at = @At("RETURN"), cancellable = true)
    private void onGetOutlineShape(BlockGetter level, BlockPos pos, CollisionContext context,
                                   CallbackInfoReturnable<VoxelShape> cir) {
        BlockStateShapeEvents.INSTANCE.getOUTLINE_SHAPE().invoker().getOutlineShape(asState(),
                level, pos, context, cir);
    }

    @Inject(method = "getCollisionShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;" +
            "Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;",
            at = @At("RETURN"), cancellable = true)
    private void onGetCollisionShape(BlockGetter level, BlockPos pos, CollisionContext context,
                                     CallbackInfoReturnable<VoxelShape> cir) {
        BlockStateShapeEvents.INSTANCE.getCOLLISION_SHAPE().invoker().getCollisionShape(asState(),
                level, pos, context, cir);
    }

    @Inject(method = "getEntityInsideCollisionShape", at = @At("RETURN"), cancellable = true)
    private void onGetEntityInsideCollisionShape(BlockGetter level, BlockPos pos, Entity entity,
                                                 CallbackInfoReturnable<VoxelShape> cir) {
        BlockStateShapeEvents.INSTANCE.getENTITY_INSIDE_SHAPE().invoker().getEntityInsideShape(asState(),
                level, pos, entity, cir);
    }

    @Inject(method = "getBlockSupportShape", at = @At("RETURN"), cancellable = true)
    private void onGetBlockSupportShape(final BlockGetter level, final BlockPos pos,
                                                 CallbackInfoReturnable<VoxelShape> cir) {
        BlockStateShapeEvents.INSTANCE.getBLOCK_SUPPORT_SHAPE().invoker().getBlockSupportShape(asState(),
                level, pos, cir);
    }

    @Inject(method = "getVisualShape", at = @At("RETURN"), cancellable = true)
    private void onGetCameraCollisionShape(BlockGetter level, BlockPos pos, CollisionContext context,
                                           CallbackInfoReturnable<VoxelShape> cir) {
        BlockStateShapeEvents.INSTANCE.getCAMERA_COLLISION_SHAPE().invoker().getCameraCollisionShape(asState(),
                level, pos, context, cir);
    }

    @Inject(method = "getInteractionShape", at = @At("RETURN"), cancellable = true)
    private void onGetInteractionShape(BlockGetter world, BlockPos pos, CallbackInfoReturnable<VoxelShape> cir) {
        BlockStateShapeEvents.INSTANCE.getINTERACTION_SHAPE().invoker().getInteractionShape(asState(), world, pos, cir);
    }

    private BlockStateBaseMixin(Block owner, Property<?>[] propertyKeys, Comparable<?>[] propertyValues) {
        super(owner, propertyKeys, propertyValues);
    }
}

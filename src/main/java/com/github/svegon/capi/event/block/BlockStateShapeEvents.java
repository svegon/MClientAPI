package com.github.svegon.capi.event.block;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public final class BlockStateShapeEvents {
    public static final Event<OutlineShape> OUTLINE_SHAPE = EventFactory.createArrayBacked(OutlineShape.class,
            (state, world, pos, context, cir) -> {}, listeners -> (state, world, pos, context, cir) -> {
                for (OutlineShape listener : listeners) {
                    listener.getOutlineShape(state, world, pos, context, cir);

                    if (cir.isCancelled()) {
                        return;
                    }
                }
            });
    public static final Event<CollisionShape> COLLISION_SHAPE = EventFactory.createArrayBacked(CollisionShape.class,
            (state, world, pos, context, cir) -> {}, listeners -> (state, world, pos, context, cir) -> {
                for (CollisionShape listener : listeners) {
                    listener.getCollisionShape(state, world, pos, context, cir);

                    if (cir.isCancelled()) {
                        return;
                    }
                }
            });
    public static final Event<CameraCollisionShape> CAMERA_COLLISION_SHAPE = EventFactory.createArrayBacked(CameraCollisionShape.class,
            (state, world, pos, context, cir) -> {}, listeners -> (state, world, pos, context, cir) -> {
                for (CameraCollisionShape listener : listeners) {
                    listener.getCameraCollisionShape(state, world, pos, context, cir);

                    if (cir.isCancelled()) {
                        return;
                    }
                }
            });
    public static final Event<RaycastShape> RAYCAST_SHAPE = EventFactory.createArrayBacked(RaycastShape.class,
            (state, world, pos, cir) -> {}, listeners -> (state, world, pos, cir) -> {
                for (RaycastShape listener : listeners) {
                    listener.getRaycastShape(state, world, pos, cir);

                    if (cir.isCancelled()) {
                        return;
                    }
                }
            });

    @FunctionalInterface
    public interface OutlineShape {
        void getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context,
                             CallbackInfoReturnable<VoxelShape> cir);
    }

    @FunctionalInterface
    public interface CollisionShape {
        void getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context,
                               CallbackInfoReturnable<VoxelShape> cir);
    }

    @FunctionalInterface
    public interface CameraCollisionShape {
        void getCameraCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context,
                               CallbackInfoReturnable<VoxelShape> cir);
    }

    @FunctionalInterface
    public interface RaycastShape {
        void getRaycastShape(BlockState state, BlockView world, BlockPos pos, CallbackInfoReturnable<VoxelShape> cir);
    }

    private BlockStateShapeEvents() {
        throw new UnsupportedOperationException();
    }
}

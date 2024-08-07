package io.github.svegon.capi.event.block

import io.github.svegon.capi.event.block.BlockStateShapeEvents.*
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

class BlockStateShapeEvents private constructor() {
    fun interface OutlineShape {
        fun getOutlineShape(
            state: BlockState?, world: BlockView?, pos: BlockPos?, context: ShapeContext?,
            cir: CallbackInfoReturnable<VoxelShape?>?
        )
    }

    fun interface CollisionShape {
        fun getCollisionShape(
            state: BlockState?, world: BlockView?, pos: BlockPos?, context: ShapeContext?,
            cir: CallbackInfoReturnable<VoxelShape?>?
        )
    }

    fun interface CameraCollisionShape {
        fun getCameraCollisionShape(
            state: BlockState?, world: BlockView?, pos: BlockPos?, context: ShapeContext?,
            cir: CallbackInfoReturnable<VoxelShape?>?
        )
    }

    fun interface RaycastShape {
        fun getRaycastShape(
            state: BlockState?,
            world: BlockView?,
            pos: BlockPos?,
            cir: CallbackInfoReturnable<VoxelShape?>?
        )
    }

    init {
        throw UnsupportedOperationException()
    }

    companion object {
        @JvmField
        val OUTLINE_SHAPE: Event<OutlineShape> = EventFactory.createArrayBacked(
            OutlineShape::class.java,
            OutlineShape { state: BlockState?, world: BlockView?, pos: BlockPos?, context: ShapeContext?, cir: CallbackInfoReturnable<VoxelShape?>? -> }) { listeners: Array<OutlineShape> ->
            OutlineShape { state: BlockState?, world: BlockView?, pos: BlockPos?, context: ShapeContext?, cir: CallbackInfoReturnable<VoxelShape?> ->
                for (listener in listeners) {
                    listener.getOutlineShape(state, world, pos, context, cir)

                    if (cir.isCancelled) {
                        return@OutlineShape
                    }
                }
            }
        }
        @JvmField
        val COLLISION_SHAPE: Event<CollisionShape> = EventFactory.createArrayBacked(
            CollisionShape::class.java,
            CollisionShape { state: BlockState?, world: BlockView?, pos: BlockPos?, context: ShapeContext?, cir: CallbackInfoReturnable<VoxelShape?>? -> }) { listeners: Array<CollisionShape> ->
            CollisionShape { state: BlockState?, world: BlockView?, pos: BlockPos?, context: ShapeContext?, cir: CallbackInfoReturnable<VoxelShape?> ->
                for (listener in listeners) {
                    listener.getCollisionShape(state, world, pos, context, cir)

                    if (cir.isCancelled) {
                        return@CollisionShape
                    }
                }
            }
        }
        @JvmField
        val CAMERA_COLLISION_SHAPE: Event<CameraCollisionShape> = EventFactory.createArrayBacked(
            CameraCollisionShape::class.java,
            CameraCollisionShape { state: BlockState?, world: BlockView?, pos: BlockPos?, context: ShapeContext?, cir: CallbackInfoReturnable<VoxelShape?>? -> }) { listeners: Array<CameraCollisionShape> ->
            CameraCollisionShape { state: BlockState?, world: BlockView?, pos: BlockPos?, context: ShapeContext?, cir: CallbackInfoReturnable<VoxelShape?> ->
                for (listener in listeners) {
                    listener.getCameraCollisionShape(state, world, pos, context, cir)

                    if (cir.isCancelled) {
                        return@CameraCollisionShape
                    }
                }
            }
        }
        @JvmField
        val RAYCAST_SHAPE: Event<RaycastShape> = EventFactory.createArrayBacked(
            RaycastShape::class.java,
            RaycastShape { state: BlockState?, world: BlockView?, pos: BlockPos?, cir: CallbackInfoReturnable<VoxelShape?>? -> }) { listeners: Array<RaycastShape> ->
            RaycastShape { state: BlockState?, world: BlockView?, pos: BlockPos?, cir: CallbackInfoReturnable<VoxelShape?> ->
                for (listener in listeners) {
                    listener.getRaycastShape(state, world, pos, cir)

                    if (cir.isCancelled) {
                        return@RaycastShape
                    }
                }
            }
        }
    }
}

package io.github.svegon.mclientapi.event.block

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

object BlockStateShapeEvents {
    fun interface FaceOcclusionShapeCallback {
        fun getFaceOcclusionShape(
            state: BlockState, direction: Direction,
            callback: CallbackInfoReturnable<RenderShape>
        )
    }

    fun interface OcclusionShapeCallback {
        fun getOcclusionShape(state: BlockState, callback: CallbackInfoReturnable<RenderShape>)
    }

    fun interface RenderShapeCallback {
        fun getRenderShape(state: BlockState, callback: CallbackInfoReturnable<RenderShape>)
    }

    fun interface OutlineShapeCallback {
        fun getOutlineShape(
            state: BlockState, world: BlockGetter, pos: BlockPos, context: CollisionContext,
            cir: CallbackInfoReturnable<VoxelShape>
        )
    }

    fun interface CollisionShapeCallback {
        fun getCollisionShape(
            state: BlockState, world: BlockGetter, pos: BlockPos, context: CollisionContext,
            cir: CallbackInfoReturnable<VoxelShape>
        )
    }

    fun interface EntityInsideShapeCallback {
        fun getEntityInsideShape(
            state: BlockState, level: BlockGetter, pos: BlockPos, entity: Entity,
            cir: CallbackInfoReturnable<VoxelShape>
        )
    }

    fun interface BlockSupportShapeCallback {
        fun getBlockSupportShape(
            state: BlockState, world: BlockGetter, pos: BlockPos, cir: CallbackInfoReturnable<VoxelShape>
        )
    }

    fun interface CameraCollisionShapeCallback {
        fun getCameraCollisionShape(
            state: BlockState, world: BlockGetter, pos: BlockPos, context: CollisionContext,
            cir: CallbackInfoReturnable<VoxelShape>
        )
    }

    fun interface BlockInteractionShapeCallback {
        fun getInteractionShape(
            state: BlockState, world: BlockGetter, pos: BlockPos,  cir: CallbackInfoReturnable<VoxelShape>
        )
    }

    val FACE_OCCLUSION_SHAPE: Event<FaceOcclusionShapeCallback> = EventFactory.createArrayBacked(
        FaceOcclusionShapeCallback::class.java, FaceOcclusionShapeCallback { _, _, _ -> }
    ) {
            listeners -> FaceOcclusionShapeCallback { state, direction, callback ->
            for (listener in listeners) {
                listener.getFaceOcclusionShape(state, direction, callback)

                if (callback.isCancelled) {
                    return@FaceOcclusionShapeCallback
                }
            }
        }
    }

    val OCCLUSION_SHAPE: Event<OcclusionShapeCallback> = EventFactory.createArrayBacked(
        OcclusionShapeCallback::class.java, OcclusionShapeCallback { _, _ -> }
    ) {
                listeners -> OcclusionShapeCallback { state, callback ->
            for (listener in listeners) {
                listener.getOcclusionShape(state, callback)

                if (callback.isCancelled) {
                    return@OcclusionShapeCallback
                }
            }
        }
    }

    val RENDER_SHAPE: Event<RenderShapeCallback> = EventFactory.createArrayBacked(
        RenderShapeCallback::class.java, RenderShapeCallback { _, _ -> }
    ) {
            listeners -> RenderShapeCallback { state, callback ->
            for (listener in listeners) {
                listener.getRenderShape(state, callback)

                if (callback.isCancelled) {
                    return@RenderShapeCallback
                }
            }
        }
    }

    val OUTLINE_SHAPE: Event<OutlineShapeCallback> = EventFactory.createArrayBacked(
        OutlineShapeCallback::class.java,
        OutlineShapeCallback { state: BlockState, world: BlockGetter, pos: BlockPos, context: CollisionContext,
                               cir: CallbackInfoReturnable<VoxelShape> -> }) { listeners: Array<OutlineShapeCallback> ->
        OutlineShapeCallback { state: BlockState, world: BlockGetter, pos: BlockPos, context: CollisionContext,
                               cir: CallbackInfoReturnable<VoxelShape> ->
            for (listener in listeners) {
                listener.getOutlineShape(state, world, pos, context, cir)

                if (cir.isCancelled) {
                    return@OutlineShapeCallback
                }
            }
        }
    }
    
    val COLLISION_SHAPE: Event<CollisionShapeCallback> = EventFactory.createArrayBacked(
        CollisionShapeCallback::class.java,
        CollisionShapeCallback { state: BlockState, world: BlockGetter, pos: BlockPos, context: CollisionContext,
                                 cir: CallbackInfoReturnable<VoxelShape> -> }) { listeners: Array<CollisionShapeCallback> ->
        CollisionShapeCallback { state: BlockState, world: BlockGetter, pos: BlockPos, context: CollisionContext,
                                 cir: CallbackInfoReturnable<VoxelShape> ->
            for (listener in listeners) {
                listener.getCollisionShape(state, world, pos, context, cir)

                if (cir.isCancelled) {
                    return@CollisionShapeCallback
                }
            }
        }
    }

    val ENTITY_INSIDE_SHAPE: Event<EntityInsideShapeCallback> = EventFactory.createArrayBacked(
        EntityInsideShapeCallback::class.java,
        EntityInsideShapeCallback { state: BlockState, level: BlockGetter, pos: BlockPos, entity: Entity,
                                    cir: CallbackInfoReturnable<VoxelShape> -> }) { listeners: Array<EntityInsideShapeCallback> ->
        EntityInsideShapeCallback { state: BlockState, level: BlockGetter, pos: BlockPos, entity: Entity,
                                    cir: CallbackInfoReturnable<VoxelShape> ->
            for (listener in listeners) {
                listener.getEntityInsideShape(state, level, pos, entity, cir)

                if (cir.isCancelled) {
                    return@EntityInsideShapeCallback
                }
            }
        }
    }

    val BLOCK_SUPPORT_SHAPE: Event<BlockSupportShapeCallback> = EventFactory.createArrayBacked(
        BlockSupportShapeCallback::class.java,
        BlockSupportShapeCallback { state: BlockState, world: BlockGetter, pos: BlockPos,
                                    cir: CallbackInfoReturnable<VoxelShape> -> }) {
                listeners: Array<BlockSupportShapeCallback> -> BlockSupportShapeCallback {
                    state: BlockState, world: BlockGetter, pos: BlockPos, cir: CallbackInfoReturnable<VoxelShape> ->
            for (listener in listeners) {
                listener.getBlockSupportShape(state, world, pos, cir)

                if (cir.isCancelled) {
                    return@BlockSupportShapeCallback
                }
            }
        }
    }

    val CAMERA_COLLISION_SHAPE: Event<CameraCollisionShapeCallback> = EventFactory.createArrayBacked(
        CameraCollisionShapeCallback::class.java,
        CameraCollisionShapeCallback { state: BlockState, world: BlockGetter, pos: BlockPos,
                                       context: CollisionContext, cir: CallbackInfoReturnable<VoxelShape> -> }) {
            listeners: Array<CameraCollisionShapeCallback> -> CameraCollisionShapeCallback { state: BlockState, world: BlockGetter,
                                                                                             pos: BlockPos, context: CollisionContext,
                                                                                             cir: CallbackInfoReturnable<VoxelShape> ->
            for (listener in listeners) {
                listener.getCameraCollisionShape(state, world, pos, context, cir)

                if (cir.isCancelled) {
                    return@CameraCollisionShapeCallback
                }
            }
        }
    }

    val INTERACTION_SHAPE: Event<BlockInteractionShapeCallback> = EventFactory.createArrayBacked(
        BlockInteractionShapeCallback::class.java,
        BlockInteractionShapeCallback { state: BlockState, world: BlockGetter, pos: BlockPos,
                                    cir: CallbackInfoReturnable<VoxelShape> -> }) {
            listeners: Array<BlockInteractionShapeCallback> -> BlockInteractionShapeCallback {
            state: BlockState, world: BlockGetter, pos: BlockPos, cir: CallbackInfoReturnable<VoxelShape> ->
        for (listener in listeners) {
            listener.getInteractionShape(state, world, pos, cir)

            if (cir.isCancelled) {
                return@BlockInteractionShapeCallback
            }
        }
    }
    }
}

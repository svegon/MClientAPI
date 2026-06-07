package io.github.svegon.mclientapi.util

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.Vec3
import net.minecraft.world.phys.shapes.VoxelShape
import org.joml.Vector2f

object InteractionUtil {
    fun targetBlockNoClip(
        world: BlockGetter,
        eyePos: Vec3,
        blockPos: BlockPos
    ): Pair<BlockHitResult, Vector2f> {
        val state: BlockState = world.getBlockState(blockPos)
        val shape: VoxelShape = state.getShape(world, blockPos)
        val relativeEyePos = eyePos.subtract(blockPos.x.toDouble(), blockPos.y.toDouble(),
            blockPos.z.toDouble())

        if (shape.toAabbs().parallelStream().anyMatch { box: AABB -> box.contains(relativeEyePos) }) {
            return Pair(
                BlockHitResult(
                    eyePos, GeometryUtil.getFacing(eyePos),
                    blockPos, true
                ), Vector2f(0f, 0f)
            )
        }

        val optional = shape.closestPointTo(relativeEyePos)
        val collisionPoint = optional.orElse(GeometryUtil.ORIGIN_CENTER_VEC)
            .add(blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble())
        val rotation: Vector2f = GeometryUtil.lookVector(eyePos, collisionPoint)
        val side = Direction.fromYRot(rotation.y.toDouble()).opposite

        return Pair(
            if (optional.isEmpty) BlockHitResult.miss(
                collisionPoint,
                side, blockPos
            ) else BlockHitResult(collisionPoint, side, blockPos, false), rotation
        )
    }

    fun targetBlockNoClip(
        entity: Entity,
        blockPos: BlockPos
    ): Pair<BlockHitResult, Vector2f> {
        return targetBlockNoClip(entity.level(), entity.eyePosition, blockPos)
    }
}

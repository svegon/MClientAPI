package io.github.svegon.mclientapi.util

import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.*
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.WorldView

object InteractionUtil {
    fun targetBlockNoClip(
        world: WorldView,
        eyePos: Vec3d,
        blockPos: BlockPos
    ): Pair<BlockHitResult, Vec2f> {
        val state: BlockState = world.getBlockState(blockPos)
        val shape: VoxelShape = state.getOutlineShape(world, blockPos)
        val relativeEyePos = eyePos.subtract(blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble())

        if (shape.boundingBoxes.parallelStream().anyMatch { box: Box -> box.contains(relativeEyePos) }) {
            return Pair(
                BlockHitResult(
                    eyePos, RotationUtil.getFacing(eyePos),
                    blockPos, true
                ), Vec2f.ZERO
            )
        }

        val optional = shape.getClosestPointTo(relativeEyePos)
        val collisionPoint = optional.orElseGet { Vec3d(0.5, 0.5, 0.5) }
            .add(blockPos.getX().toDouble(), blockPos.getY().toDouble(), blockPos.getZ().toDouble())
        val rotation: Vec2f = RotationUtil.lookVector(eyePos, collisionPoint)
        val side = Direction.fromRotation(rotation.y.toDouble()).opposite

        return Pair(
            if (optional.isEmpty) BlockHitResult.createMissed(
                collisionPoint,
                side, blockPos
            ) else BlockHitResult(collisionPoint, side, blockPos, false), rotation
        )
    }

    fun targetBlockNoClip(
        entity: Entity,
        blockPos: BlockPos
    ): Pair<BlockHitResult, Vec2f> {
        return targetBlockNoClip(entity.world, entity.eyePos, blockPos)
    }
}

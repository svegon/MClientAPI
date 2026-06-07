package io.github.svegon.mclientapi.client.util

import io.github.svegon.mclientapi.util.GeometryUtil
import net.minecraft.client.multiplayer.MultiPlayerGameMode
import net.minecraft.client.player.LocalPlayer
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntitySelector
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.component.AttackRange
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3
import org.joml.Vector2f

object ClientInteractionUtil {
    fun clampHitResult(hitResult: HitResult, from: Vec3, reach: Double): HitResult {
        val hitLocation: Vec3 = hitResult.getLocation()
        if (!hitLocation.closerThan(from, reach)) {
            val location: Vec3 = hitResult.getLocation()
            val direction = Direction.getApproximateNearest(location.x - from.x, location.y - from.y,
                location.z - from.z)
            return BlockHitResult.miss(location, direction, BlockPos.containing(location))
        } else {
            return hitResult
        }
    }

    /**
     * based on {@code LocalPlayer.raycastHitResult}
     */
    fun targetBlock(
        camera: Entity, blockPos: BlockPos, reachDistance: Double, attackRange: AttackRange? = null
    ): BlockHitResult {
        val eyePos = camera.eyePosition
        val center = Vec3.atCenterOf(blockPos)
        var hitResult: HitResult? = null

        if (attackRange != null) {
            hitResult = attackRange.getClosesetHit(camera, 1F, EntitySelector.CAN_BE_PICKED)

            if (hitResult is BlockHitResult) {
                hitResult = clampHitResult(hitResult, eyePos, reachDistance)
            }
        }

        if (hitResult == null || hitResult.type == HitResult.Type.MISS) {
            hitResult = clampHitResult(camera.pick(reachDistance, 1F, false),
                eyePos, reachDistance)
        }

        return hitResult as? BlockHitResult ?: BlockHitResult.miss(center,
                GeometryUtil.getFacing(eyePos.subtract(center)), blockPos)
    }

    /**
     * based on {@code LocalPlayer.raycastHitResult}
     * uses referentialPlayer's reach distance in calculation; TODO remove the dependency
     */
    fun targetEntity(
        referentialPlayer: LocalPlayer, camera: Entity, target: Entity
    ): EntityHitResult? {
        val currentTarget: HitResult = referentialPlayer.raycastHitResult(1f, camera)

        if (currentTarget is EntityHitResult && currentTarget.entity === target) {
            return currentTarget
        }

        val yaw = camera.xRot
        val pitch = camera.yRot
        val eyePos = target.eyePosition
        val bb = target.boundingBox
        val corners = arrayOf(
            Vec3(bb.minX, bb.minY, bb.minZ), Vec3(bb.minX, bb.minY, bb.maxZ),
            Vec3(bb.minX, bb.maxY, bb.minZ), Vec3(bb.minX, bb.maxY, bb.maxZ),
            Vec3(bb.maxX, bb.minY, bb.minZ), Vec3(bb.maxX, bb.minY, bb.maxZ),
            Vec3(bb.maxX, bb.maxY, bb.minZ), Vec3(bb.maxX, bb.maxY, bb.maxZ)
        )
        var collision: EntityHitResult? = null
        var sqDistanceToCollision = Double.MAX_VALUE

        for (corner in corners) {
            val rot: Vector2f = GeometryUtil.lookVector(eyePos, corner)

            camera.xRot = rot.y
            camera.yRot = rot.x

            val hitResult: HitResult = referentialPlayer.raycastHitResult(1f, camera)

            if (hitResult is EntityHitResult && hitResult.entity === target) {
                val distanceSq = eyePos.distanceToSqr(hitResult.location)

                if (distanceSq < sqDistanceToCollision) {
                    collision = hitResult
                    sqDistanceToCollision = distanceSq
                }
            }
        }

        camera.xRot = yaw
        camera.yRot = pitch

        return collision
    }

    fun MultiPlayerGameMode.attackAndSwing(player: Player, target: Entity) {
        attack(player, target)
        player.swing(InteractionHand.MAIN_HAND)
    }
}

package io.github.svegon.mclientapi.client.util

import com.google.common.util.concurrent.AtomicDouble
import io.github.svegon.mclientapi.client.mixinterface.IGameRenderer
import io.github.svegon.mclientapi.util.RotationUtil
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.network.ClientPlayerInteractionManager
import net.minecraft.client.render.GameRenderer
import net.minecraft.entity.Entity
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec2f
import net.minecraft.util.math.Vec3d
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.World
import java.util.concurrent.atomic.AtomicReference

object ClientInteractionUtil {
    fun targetBlock(
        camera: Entity, blockPos: BlockPos,
        reachDistance: Double
    ): BlockHitResult {
        val gameRenderer: IGameRenderer = MinecraftClient.getInstance().gameRenderer as IGameRenderer
        val yaw = camera.yaw
        val pitch = camera.pitch
        val world: World = camera.world
        val eyePos = camera.eyePos
        val shape: VoxelShape = world.getBlockState(blockPos).getOutlineShape(world, blockPos)
        val collision: AtomicReference<BlockHitResult?> = AtomicReference(
            shape.raycast(
                eyePos,
                camera.getRotationVec(1f), blockPos
            )
        )
        val sqDistanceToCollision = AtomicDouble(Double.MAX_VALUE)

        if (collision.get() != null) {
            return collision.get()!!
        }

        shape.forEachEdge((VoxelShapes.BoxConsumer { minX: Double, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double ->
            val corners = arrayOf(
                Vec3d(minX, minY, minZ), Vec3d(minX, minY, maxZ), Vec3d(minX, maxY, minZ),
                Vec3d(minX, maxY, maxZ), Vec3d(maxX, minY, minZ), Vec3d(maxX, minY, maxZ),
                Vec3d(maxX, maxY, minZ), Vec3d(maxX, maxY, maxZ)
            )
            for (corner in corners) {
                val rot: Vec2f = RotationUtil.lookVector(eyePos, corner)

                camera.yaw = rot.y
                camera.pitch = rot.x

                val hitResult: HitResult = gameRenderer.`mClientAPI$raycast`(camera, reachDistance, reachDistance,
                    1f)

                if (hitResult.type == HitResult.Type.BLOCK && (hitResult as BlockHitResult).blockPos == blockPos) {
                    val distanceSq = eyePos.squaredDistanceTo(hitResult.getPos())

                    if (distanceSq < sqDistanceToCollision.get()) {
                        collision.set(hitResult)
                        sqDistanceToCollision.set(distanceSq)
                    }
                }
            }
        }))
        camera.yaw = yaw
        camera.pitch = pitch

        val center = Vec3d.ofCenter(blockPos)

        return collision.get() ?: BlockHitResult.createMissed(center,
            RotationUtil.getFacing(eyePos.subtract(center)), blockPos)
    }

    fun targetEntity(
        camera: Entity, entity: Entity,
        reachDistance: Double
    ): EntityHitResult? {
        val gameRenderer: IGameRenderer = MinecraftClient.getInstance().gameRenderer as IGameRenderer
        val currentTarget: HitResult = gameRenderer.`mClientAPI$raycast`(camera, reachDistance, reachDistance,
            1f)

        if (currentTarget is EntityHitResult && currentTarget.entity === entity) {
            return currentTarget
        }

        val yaw = camera.yaw
        val pitch = camera.pitch
        val eyePos = entity.eyePos
        val bb = entity.boundingBox
        val corners = arrayOf(
            Vec3d(bb.minX, bb.minY, bb.minZ), Vec3d(bb.minX, bb.minY, bb.maxZ),
            Vec3d(bb.minX, bb.maxY, bb.minZ), Vec3d(bb.minX, bb.maxY, bb.maxZ),
            Vec3d(bb.maxX, bb.minY, bb.minZ), Vec3d(bb.maxX, bb.minY, bb.maxZ),
            Vec3d(bb.maxX, bb.maxY, bb.minZ), Vec3d(bb.maxX, bb.maxY, bb.maxZ)
        )
        var collision: EntityHitResult? = null
        var sqDistanceToCollision = Double.MAX_VALUE

        for (corner in corners) {
            val rot: Vec2f = RotationUtil.lookVector(eyePos, corner)

            camera.yaw = rot.y
            camera.pitch = rot.x

            val hitResult: HitResult = gameRenderer.`mClientAPI$raycast`(camera, reachDistance, reachDistance,
                1f)

            if (hitResult is EntityHitResult && hitResult.entity === entity) {
                val distanceSq = eyePos.squaredDistanceTo(hitResult.getPos())

                if (distanceSq < sqDistanceToCollision) {
                    collision = hitResult
                    sqDistanceToCollision = distanceSq
                }
            }
        }

        camera.yaw = yaw
        camera.pitch = pitch

        return collision
    }

    fun MinecraftClient.useItem(
        crosshairTarget: HitResult?, player: ClientPlayerEntity,
        interactionManager: ClientPlayerInteractionManager, gameRenderer: GameRenderer
    ): ActionResult {
        var result: ActionResult = ActionResult.PASS

        for (hand in Hand.entries) {
            val itemStack: ItemStack = player.getStackInHand(hand)

            if (!itemStack.isItemEnabled(player.clientWorld.enabledFeatures)) {
                return ActionResult.FAIL
            }

            if (crosshairTarget != null) {
                when (crosshairTarget.type) {
                    HitResult.Type.ENTITY -> {
                        val entityHitResult: EntityHitResult = crosshairTarget as EntityHitResult
                        val entity: Entity = entityHitResult.entity

                        if (!player.clientWorld.worldBorder.contains(entity.blockPos)) {
                            return ActionResult.FAIL
                        }

                        var actionResult: ActionResult = interactionManager.interactEntityAtLocation(
                            player, entity, entityHitResult, hand
                        )

                        if (!actionResult.isAccepted) {
                            actionResult = interactionManager.interactEntity(player, entity, hand)
                        }

                        if (!actionResult.isAccepted) {
                            break
                        }

                        if (actionResult.shouldSwingHand()) {
                            player.swingHand(hand)
                        }

                        return actionResult
                    }

                    HitResult.Type.BLOCK -> {
                        val blockHitResult: BlockHitResult = crosshairTarget as BlockHitResult
                        val i: Int = itemStack.count
                        val actionResult2: ActionResult =
                            interactionManager.interactBlock(player, hand, blockHitResult)

                        if (actionResult2.isAccepted) {
                            if (actionResult2.shouldSwingHand()) {
                                player.swingHand(hand)

                                if (!itemStack.isEmpty && (itemStack.count != i
                                            || interactionManager.hasCreativeInventory())
                                ) {
                                    gameRenderer.firstPersonRenderer.resetEquipProgress(hand)
                                }
                            }

                            return actionResult2
                        }

                        if (actionResult2 != ActionResult.FAIL) {
                            break
                        }

                        return actionResult2
                    }

                    HitResult.Type.MISS -> {}
                }
            }

            if (itemStack.isEmpty || !interactionManager.interactItem(player, hand).also { result = it }.isAccepted) {
                continue
            }

            if (result.shouldSwingHand()) {
                player.swingHand(hand)
            }

            gameRenderer.firstPersonRenderer.resetEquipProgress(hand)
            return result
        }

        return ActionResult.PASS
    }

    fun MinecraftClient.useItem(): ActionResult {
        return useItem(crosshairTarget, player!!, interactionManager!!, gameRenderer)
    }
}

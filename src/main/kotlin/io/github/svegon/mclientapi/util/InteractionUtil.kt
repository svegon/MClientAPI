package io.github.svegon.capi.util

import com.google.common.util.concurrent.AtomicDouble
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.Entity
import net.minecraft.util.math.Box
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec2f
import net.minecraft.util.math.Vec3d
import net.minecraft.util.shape.VoxelShape
import java.util.concurrent.atomic.AtomicReference

class InteractionUtil private constructor() {
    init {
        throw UnsupportedOperationException()
    }

    companion object {
        private val DIRECTIONS = Direction.entries.toTypedArray()

        fun targetBlock(
            camera: Entity, blockPos: BlockPos,
            reachDistance: Double, fluids: Boolean
        ): BlockHitResult? {
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
                return collision.get()
            }

            shape.forEachEdge((BoxConsumer { minX: Double, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double ->
                val corners = arrayOf(
                    Vec3d(minX, minY, minZ), Vec3d(minX, minY, maxZ), Vec3d(minX, maxY, minZ),
                    Vec3d(minX, maxY, maxZ), Vec3d(maxX, minY, minZ), Vec3d(maxX, minY, maxZ),
                    Vec3d(maxX, maxY, minZ), Vec3d(maxX, maxY, maxZ)
                )
                for (corner in corners) {
                    val rot: Vec2f = RotationUtil.lookVector(eyePos, corner)

                    camera.yaw = rot.y
                    camera.pitch = rot.x

                    val hitResult: HitResult = WorldUtil.raycast(camera, reachDistance, 1,
                        { e -> e.canHit() && !e.isSpectator() }, fluids
                    )

                    if (hitResult.getType() == HitResult.Type.BLOCK && (hitResult as BlockHitResult).getBlockPos() == blockPos) {
                        val distanceSq = eyePos.squaredDistanceTo(hitResult.getPos())

                        if (distanceSq < sqDistanceToCollision.get()) {
                            collision.set(hitResult as BlockHitResult)
                            sqDistanceToCollision.set(distanceSq)
                        }
                    }
                }
            }))
            camera.yaw = yaw
            camera.pitch = pitch

            val center = Vec3d.ofCenter(blockPos)

            return if (collision.get() != null) collision.get() else BlockHitResult.createMissed(
                center,
                RotationUtil.getFacing(eyePos.subtract(center)), blockPos
            )
        }

        fun targetBlock(
            entity: Entity, blockPos: BlockPos,
            reachDistance: Double
        ): BlockHitResult? {
            return targetBlock(entity, blockPos, reachDistance, false)
        }

        fun targetBlockNoClip(
            world: WorldView,
            eyePos: Vec3d,
            blockPos: BlockPos
        ): ObjectObjectImmutablePair<BlockHitResult, Vec2f> {
            val state: BlockState = world.getBlockState(blockPos)
            val shape: VoxelShape = state.getOutlineShape(world, blockPos)
            val relativeEyePos =
                eyePos.subtract(blockPos.getX().toDouble(), blockPos.getY().toDouble(), blockPos.getZ().toDouble())

            if (shape.boundingBoxes.parallelStream().anyMatch { box: Box -> box.contains(relativeEyePos) }) {
                return ObjectObjectImmutablePair<BlockHitResult, Vec2f>(
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

            return ObjectObjectImmutablePair<BlockHitResult, Vec2f>(
                if (optional.isEmpty) BlockHitResult.createMissed(
                    collisionPoint,
                    side, blockPos
                ) else BlockHitResult(collisionPoint, side, blockPos, false), rotation
            )
        }

        fun targetBlockNoClip(
            world: WorldView,
            entity: Entity,
            blockPos: BlockPos
        ): ObjectObjectImmutablePair<BlockHitResult, Vec2f> {
            return targetBlockNoClip(world, entity.eyePos, blockPos)
        }

        @JvmOverloads
        fun targetEntity(
            camera: Entity, entity: Entity,
            reachDistance: Double, fluids: Boolean = false
        ): EntityHitResult? {
            val currentTarget: HitResult = WorldUtil.raycast(camera, reachDistance, 1,
                { e -> e === entity || (e.canHit() && !e.isSpectator()) }, fluids
            )

            if (currentTarget is EntityHitResult && currentTarget.getEntity() === entity) {
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

                val hitResult: HitResult = WorldUtil.raycast(camera, reachDistance, 1,
                    { e -> e.canHit() && !e.isSpectator() }, fluids
                )

                if (hitResult is EntityHitResult && hitResult.getEntity() === entity) {
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

        @Environment(EnvType.CLIENT)
        fun useItem(
            crosshairTarget: HitResult?, player: ClientPlayerEntity,
            interactionManager: ClientPlayerInteractionManager, gameRenderer: GameRenderer
        ): ActionResult {
            var result: ActionResult

            for (hand in Hand.entries) {
                val itemStack: ItemStack = player.getStackInHand(hand)

                if (!itemStack.isItemEnabled(player.clientWorld.getEnabledFeatures())) {
                    return ActionResult.FAIL
                }

                if (crosshairTarget != null) {
                    when (crosshairTarget.getType()) {
                        HitResult.Type.ENTITY -> {
                            val entityHitResult: EntityHitResult? = crosshairTarget as EntityHitResult?
                            val entity: Entity = entityHitResult.getEntity()

                            if (!player.clientWorld.getWorldBorder().contains(entity.blockPos)) {
                                return ActionResult.FAIL
                            }

                            var actionResult: ActionResult = interactionManager.interactEntityAtLocation(
                                player, entity,
                                entityHitResult, hand
                            )

                            if (!actionResult.isAccepted()) {
                                actionResult = interactionManager.interactEntity(player, entity, hand)
                            }

                            if (!actionResult.isAccepted()) {
                                break
                            }

                            if (actionResult.shouldSwingHand()) {
                                player.swingHand(hand)
                            }

                            return actionResult
                        }

                        HitResult.Type.BLOCK -> {
                            val blockHitResult: BlockHitResult? = crosshairTarget as BlockHitResult?
                            val i: Int = itemStack.getCount()
                            val actionResult2: ActionResult =
                                interactionManager.interactBlock(player, hand, blockHitResult)

                            if (actionResult2.isAccepted()) {
                                if (actionResult2.shouldSwingHand()) {
                                    player.swingHand(hand)

                                    if (!itemStack.isEmpty() && (itemStack.getCount() != i
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
                    }
                }

                if (itemStack.isEmpty() || !interactionManager.interactItem(player, hand).also { result = it }
                        .isAccepted()) {
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

        @Environment(EnvType.CLIENT)
        fun useItem(client: MinecraftClient): ActionResult {
            return useItem(client.crosshairTarget, client.player, client.interactionManager, client.gameRenderer)
        }

        @Environment(EnvType.CLIENT)
        fun useItem(): ActionResult {
            return useItem(MinecraftClient.getInstance())
        }
    }
}

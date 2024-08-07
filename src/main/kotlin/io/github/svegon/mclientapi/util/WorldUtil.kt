package io.github.svegon.capi.util

import net.minecraft.client.MinecraftClient
import net.minecraft.entity.Entity
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import java.util.function.Predicate

class WorldUtil private constructor() {
    init {
        throw UnsupportedOperationException()
    }

    companion object {
        @JvmOverloads
        fun raycast(
            camera: Entity,
            reachDistance: Double,
            tickDelta: Float,
            hittableEntities: Predicate<Entity>? = Predicate { e: Entity -> e.canHit() && !e.isSpectator },
            fluids: Boolean = false
        ): HitResult? {
            var reachDistance = reachDistance
            val blockHitResult: BlockHitResult = camera.raycast(reachDistance, tickDelta, fluids) as BlockHitResult
            val cameraPos = camera.getCameraPosVec(tickDelta)
            var bl = false
            var squaredReach = reachDistance

            if (reachDistance >= 5) {
                squaredReach = 6.0
                reachDistance = squaredReach
            } else if (reachDistance > 3.0) {
                bl = true
            }

            squaredReach *= squaredReach
            val rotVec = camera.getRotationVec(1.0f)
            val max = cameraPos.add(
                rotVec.x * reachDistance, rotVec.y * reachDistance,
                rotVec.z * reachDistance
            )
            val box = camera.boundingBox.stretch(rotVec.multiply(reachDistance)).expand(1.0, 1.0, 1.0)
            val entityHitResult: EntityHitResult = ProjectileUtil.raycast(
                camera, cameraPos, max, box, hittableEntities,
                squaredReach
            )

            if (entityHitResult != null) {
                val targetEntityPos: Vec3d = entityHitResult.getPos()
                val d = cameraPos.squaredDistanceTo(targetEntityPos)

                if (bl && d > 9.0) {
                    return BlockHitResult.createMissed(
                        targetEntityPos,
                        Direction.getFacing(rotVec.x, rotVec.y, rotVec.z), GeometryUtil.toBlockPos(targetEntityPos)
                    )
                } else if (d < squaredReach) {
                    return entityHitResult
                }
            }

            return blockHitResult
        }

        fun raycast(camera: Entity, reachDistance: Double): HitResult? {
            return raycast(camera, reachDistance, MinecraftClient.getInstance().getTickDelta())
        }

        fun raycast(
            world: World, player: Entity, reach: Double,
            fluidHandling: FluidHandling?
        ): BlockHitResult {
            val yaw = player.pitch
            val pitch = player.yaw
            val eyePos = player.eyePos
            val h: Float = MathHelper.cos(-pitch * (Math.PI.toFloat() / 180) - Math.PI.toFloat())
            val i: Float = MathHelper.sin(-pitch * (Math.PI.toFloat() / 180) - Math.PI.toFloat())
            val j: Float = -MathHelper.cos(-yaw * (Math.PI.toFloat() / 180))
            val normalY: Double = MathHelper.sin(-yaw * (Math.PI.toFloat() / 180)).toDouble()
            val normalX = (i * j).toDouble()
            val normalZ = (h * j).toDouble()
            val endPos = eyePos.add(normalX * reach, normalY * reach, normalZ * reach)
            return world.raycast(
                RaycastContext(
                    eyePos, endPos, RaycastContext.ShapeType.OUTLINE, fluidHandling,
                    player
                )
            )
        }

        fun raycast(world: World, player: Entity, fluidHandling: FluidHandling?): BlockHitResult {
            return raycast(world, player, 5.0, fluidHandling)
        }
    }
}

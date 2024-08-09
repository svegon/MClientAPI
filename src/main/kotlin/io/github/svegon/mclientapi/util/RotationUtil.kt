package io.github.svegon.mclientapi.util

import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec2f
import net.minecraft.util.math.Vec3d
import kotlin.math.sqrt

object RotationUtil {
    fun getFacing(pos: Vec3d): Direction {
        return Direction.getFacing(pos.getX(), pos.getY(), pos.getZ())
    }

    /**
     * @see net.minecraft.entity.Entity
     *
     *
     * @param eyes position of the eyes looking at the angle
     * @param target the target point where they are pointed
     * @return a new Vec2f(yaw, pitch) of the angles the eyes are rotated to look at the target
     */
    fun lookVector(eyes: Vec3d, target: Vec3d): Vec2f {
        return lookVector(target.subtract(eyes))
    }

    fun lookVector(target: Vec3d): Vec2f {
        val xDiff = target.x
        val yDiff = target.y
        val zDiff = target.z
        val horizontal = sqrt(xDiff * xDiff + zDiff * zDiff)

        val pitch = MathHelper.wrapDegrees((MathHelper.atan2(yDiff, horizontal) * -57.2957763671875).toFloat())
        val yaw = MathHelper.wrapDegrees((MathHelper.atan2(zDiff, xDiff) * 57.2957763671875).toFloat() - 90.0f)

        return Vec2f(pitch, yaw)
    }
}

package io.github.svegon.mclientapi.util

import com.google.common.collect.Lists
import net.minecraft.core.BlockBox
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.util.Mth
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import net.minecraft.world.phys.shapes.VoxelShape
import org.joml.Vector2f
import java.util.stream.Stream
import kotlin.math.sqrt
import kotlin.streams.asStream

object GeometryUtil {
    val UNBOUND_BOX: AABB = AABB(
        Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY,
        Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY
    )
    val ORIGIN_CENTER_VEC: Vec3 = Vec3(0.5, 0.5, 0.5)

    fun centeredBox(center: BlockPos, range: Int): BlockBox {
        return BlockBox(center.offset(-range, -range, -range),
            center.offset(range, range, range))
    }

    fun stream(box: BlockBox): Stream<BlockPos> {
        return box.asSequence().asStream();
    }

    fun parallelStream(AABB: BlockBox): Stream<BlockPos> {
        return stream(AABB).parallel()
    }

    fun neighboringBlocks(pos: BlockPos): List<BlockPos> {
        return Lists.newArrayList(pos.above(), pos.below(), pos.north(), pos.east(), pos.south(), pos.west())
    }

    fun toSimpleString(pos: BlockPos): String {
        return pos.x.toString() + " " + pos.y + " " + pos.z
    }

    fun start(box: AABB): Vec3 {
        return Vec3(box.minX, box.minY, box.minZ)
    }

    fun dimensions(box: AABB): Vec3 {
        return Vec3(box.xsize, box.ysize, box.zsize)
    }

    fun scale(box: AABB, scale: Double): AABB {
        return AABB(Vec3.ZERO, dimensions(box).scale(scale)).move(start(box))
    }

    fun closestPoint(eyes: Vec3, blockPos: BlockPos, blockShape: VoxelShape): Vec3 {
        return closestPoint(eyes, Vec3.atLowerCornerOf(blockPos), blockShape)
    }

    fun closestPoint(eyes: Vec3, pos: Vec3, blockShape: VoxelShape): Vec3 {
        return blockShape.closestPointTo(eyes.subtract(pos)).orElse(ORIGIN_CENTER_VEC).add(pos)
    }

    fun rectContains(left: Int, top: Int, width: Int, height: Int, x: Int, y: Int): Boolean {
        var width = width
        var height = height
        if ((width or height) < 0) {
            // At least one of the dimensions is negative...
            return false
        }
        // Note: if either dimension is zero, tests below must return false...
        if (x < left || y < top) {
            return false
        }
        width += left
        height += top
        //    overflow || intersect
        return ((width < left || width > x) &&
                (height < top || height > y))
    }

    fun rectContains(left: Float, top: Float, width: Float, height: Float, x: Float, y: Float): Boolean {
        var width = width
        var height = height
        if ((java.lang.Float.floatToRawIntBits(width) or java.lang.Float.floatToRawIntBits(height)) < 0) {
            // At least one of the dimensions is negative...
            return false
        }
        // Note: if either dimension is zero, tests below must return false...
        if (x < left || y < top) {
            return false
        }
        width += left
        height += top
        //    overflow || intersect
        return ((width < left || width > x) &&
                (height < top || height > y))
    }

    fun rectContains(left: Double, top: Double, width: Double, height: Double, x: Double, y: Double): Boolean {
        var width = width
        var height = height
        if ((java.lang.Double.doubleToRawLongBits(width) or java.lang.Double.doubleToRawLongBits(height)) < 0) {
            // At least one of the dimensions is negative...
            return false
        }
        // Note: if either dimension is zero, tests below must return false...
        if (x < left || y < top) {
            return false
        }
        width += left
        height += top
        //    overflow || intersect
        return ((width < left || width > x) &&
                (height < top || height > y))
    }

    fun toBlockPos(x: Double, y: Double, z: Double): BlockPos {
        return BlockPos(Math.floor(x).toInt(), Math.floor(y).toInt(), Math.floor(z).toInt())
    }

    fun toBlockPos(pos: Vec3): BlockPos {
        return toBlockPos(pos.x, pos.y, pos.z)
    }
    fun getFacing(pos: Vec3): Direction {
        return Direction.getApproximateNearest(pos.x(), pos.y(), pos.z())
    }

    /**
     * @see net.minecraft.entity.Entity
     *
     *
     * @param eyes position of the eyes looking at the angle
     * @param target the target point where they are pointed
     * @return a new Vec2f(yaw, pitch) of the angles the eyes are rotated to look at the target
     */
    fun lookVector(eyes: Vec3, target: Vec3): Vector2f {
        return lookVector(target.subtract(eyes))
    }

    fun lookVector(target: Vec3): Vector2f {
        val xDiff = target.x
        val yDiff = target.y
        val zDiff = target.z
        val horizontal = sqrt(xDiff * xDiff + zDiff * zDiff)

        val pitch = Mth.wrapDegrees((Mth.atan2(yDiff, horizontal) * -57.2957763671875).toFloat())
        val yaw = Mth.wrapDegrees((Mth.atan2(zDiff, xDiff) * 57.2957763671875).toFloat() - 90.0f)

        return Vector2f(pitch, yaw)
    }
}

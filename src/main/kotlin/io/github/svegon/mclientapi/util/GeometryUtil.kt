package io.github.svegon.capi.util

import com.google.common.collect.Lists
import io.github.svegon.utils.collections.ListUtil
import net.minecraft.util.math.*
import net.minecraft.util.shape.VoxelShape
import java.util.stream.Stream
import kotlin.math.max
import kotlin.math.min

class GeometryUtil private constructor() {
    init {
        throw UnsupportedOperationException()
    }

    companion object {
        val UNBOUND_BOX: Box = Box(
            Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY,
            Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY
        )
        val ORIGIN_CENTER_VEC: Vec3d = Vec3d(0.5, 0.5, 0.5)

        fun axisMinPos(box: BlockBox): BlockPos {
            return BlockPos(box.minX, box.minY, box.minZ)
        }

        fun axisMaxPos(box: BlockBox): BlockPos {
            return BlockPos(box.maxX, box.maxY, box.maxZ)
        }

        fun allInBox(start: BlockPos, end: BlockPos): List<BlockPos> {
            val minX = min(start.x.toDouble(), end.x.toDouble()).toInt()
            val minY = min(start.y.toDouble(), end.y.toDouble()).toInt()
            val minZ = min(start.z.toDouble(), end.z.toDouble()).toInt()
            val xLength = (max(start.x.toDouble(), end.x.toDouble()) - minX).toInt()
            val yLength = (max(start.y.toDouble(), end.y.toDouble()) - minY).toInt()
            val zLength = (max(start.z.toDouble(), end.z.toDouble()) - minZ).toInt()

            return ListUtil.iterate(
                { i: Int, j: Int, k: Int -> BlockPos(minX + i, minY + j, minZ + k) },
                xLength, yLength, zLength
            )
        }

        fun allInBox(box: BlockBox): List<BlockPos> {
            return allInBox(axisMinPos(box), axisMaxPos(box))
        }

        fun allInBox(center: BlockPos, range: Int): List<BlockPos> {
            return allInBox(center.add(-range, -range, -range), center.add(range, range, range))
        }

        fun allInBoxStream(from: BlockPos, to: BlockPos): Stream<BlockPos> {
            return allInBox(from, to).stream()
        }

        fun allInBoxStream(center: BlockPos, range: Int): Stream<BlockPos> {
            return allInBox(center, range).stream()
        }

        fun allInBoxParallelStream(from: BlockPos, to: BlockPos): Stream<BlockPos> {
            return allInBoxStream(from, to).parallel()
        }

        fun allInBoxParallelStream(center: BlockPos, range: Int): Stream<BlockPos> {
            return allInBoxStream(center, range).parallel()
        }

        fun stream(box: BlockBox): Stream<BlockPos> {
            return allInBoxStream(
                BlockPos(box.minX, box.minY, box.minZ),
                BlockPos(box.maxX, box.maxY, box.maxZ)
            )
        }

        fun parallelStream(box: BlockBox): Stream<BlockPos> {
            return stream(box).parallel()
        }

        fun neighboringBlocks(pos: BlockPos): List<BlockPos> {
            return Lists.newArrayList(pos.up(), pos.down(), pos.north(), pos.east(), pos.south(), pos.west())
        }

        fun toSimpleString(pos: BlockPos): String {
            return pos.x.toString() + " " + pos.y + " " + pos.z
        }

        fun start(box: Box): Vec3d {
            return Vec3d(box.minX, box.minY, box.minZ)
        }

        fun dimensions(box: Box): Vec3d {
            return Vec3d(box.lengthX, box.lengthY, box.lengthZ)
        }

        fun scale(box: Box, scale: Double): Box {
            return Box(Vec3d.ZERO, dimensions(box).multiply(scale)).offset(start(box))
        }

        fun closestPoint(eyes: Vec3d, blockPos: BlockPos?, blockShape: VoxelShape): Vec3d {
            return closestPoint(eyes, Vec3d.of(blockPos), blockShape)
        }

        fun closestPoint(eyes: Vec3d, pos: Vec3d?, blockShape: VoxelShape): Vec3d {
            return blockShape.getClosestPointTo(eyes.subtract(pos)).orElse(ORIGIN_CENTER_VEC).add(pos)
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
            return BlockPos(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z))
        }

        fun toBlockPos(pos: Vec3d): BlockPos {
            return toBlockPos(pos.x, pos.y, pos.z)
        }
    }
}

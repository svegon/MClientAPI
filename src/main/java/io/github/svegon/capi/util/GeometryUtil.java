package io.github.svegon.capi.util;

import com.google.common.collect.Lists;
import io.github.svegon.utils.collections.ListUtil;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Stream;

public final class GeometryUtil {
    private GeometryUtil() {
        throw new UnsupportedOperationException();
    }

    public static final Box UNBOUND_BOX = new Box(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY,
            Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    public static final Vec3d ORIGIN_CENTER_VEC = new Vec3d(0.5, 0.5, 0.5);

    public static BlockPos axisMinPos(BlockBox box) {
        return new BlockPos(box.getMinX(), box.getMinY(), box.getMinZ());
    }

    public static BlockPos axisMaxPos(BlockBox box) {
        return new BlockPos(box.getMaxX(), box.getMaxY(), box.getMaxZ());
    }

    public static List<BlockPos> allInBox(BlockPos start, BlockPos end) {
        final int minX = Math.min(start.getX(), end.getX());
        final int minY = Math.min(start.getY(), end.getY());
        final int minZ = Math.min(start.getZ(), end.getZ());
        final int xLength = Math.max(start.getX(), end.getX()) - minX;
        final int yLength = Math.max(start.getY(), end.getY()) - minY;
        final int zLength = Math.max(start.getZ(), end.getZ()) - minZ;

        return ListUtil.iterate((i, j, k) -> new BlockPos(minX + i, minY + j, minZ + k),
                xLength, yLength, zLength);
    }

    public static List<BlockPos> allInBox(BlockBox box) {
        return allInBox(axisMinPos(box), axisMaxPos(box));
    }

    public static List<BlockPos> allInBox(BlockPos center, int range) {
        return allInBox(center.add(-range, -range, -range), center.add(range, range, range));
    }

    public static Stream<BlockPos> allInBoxStream(BlockPos from, BlockPos to) {
        return allInBox(from, to).stream();
    }

    public static Stream<BlockPos> allInBoxStream(BlockPos center, int range) {
        return allInBox(center, range).stream();
    }

    public static Stream<BlockPos> allInBoxParallelStream(BlockPos from, BlockPos to) {
        return allInBoxStream(from, to).parallel();
    }

    public static Stream<BlockPos> allInBoxParallelStream(BlockPos center, int range) {
        return allInBoxStream(center, range).parallel();
    }

    public static Stream<BlockPos> stream(BlockBox box) {
        return allInBoxStream(new BlockPos(box.getMinX(), box.getMinY(), box.getMinZ()),
                new BlockPos(box.getMaxX(), box.getMaxY(), box.getMaxZ()));
    }

    public static Stream<BlockPos> parallelStream(BlockBox box) {
        return stream(box).parallel();
    }

    public static List<BlockPos> neighboringBlocks(BlockPos pos) {
        return Lists.newArrayList(pos.up(), pos.down(), pos.north(), pos.east(), pos.south(), pos.west());
    }

    public static String toSimpleString(@NotNull final BlockPos pos) {
        return pos.getX() + " " + pos.getY() + " " + pos.getZ();
    }

    public static Vec3d start(Box box) {
        return new Vec3d(box.minX, box.minY, box.minZ);
    }

    public static Vec3d dimensions(Box box) {
        return new Vec3d(box.getLengthX(), box.getLengthY(), box.getLengthZ());
    }

    public static Box scale(final Box box, final double scale) {
        return new Box(Vec3d.ZERO, dimensions(box).multiply(scale)).offset(start(box));
    }

    public static Vec3d closestPoint(Vec3d eyes, BlockPos blockPos, VoxelShape blockShape) {
        return closestPoint(eyes, Vec3d.of(blockPos), blockShape);
    }

    public static Vec3d closestPoint(Vec3d eyes, Vec3d pos, VoxelShape blockShape) {
        return blockShape.getClosestPointTo(eyes.subtract(pos)).orElse(ORIGIN_CENTER_VEC).add(pos);
    }

    public static boolean rectContains(int left, int top, int width, int height, int x, int y) {
        if ((width | height) < 0) {
            // At least one of the dimensions is negative...
            return false;
        }
        // Note: if either dimension is zero, tests below must return false...
        if (x < left || y < top) {
            return false;
        }
        width += left;
        height += top;
        //    overflow || intersect
        return ((width < left || width > x) &&
                (height < top || height > y));
    }

    public static boolean rectContains(float left, float top, float width, float height, float x, float y) {
        if ((Float.floatToRawIntBits(width) | Float.floatToRawIntBits(height)) < 0) {
            // At least one of the dimensions is negative...
            return false;
        }
        // Note: if either dimension is zero, tests below must return false...
        if (x < left || y < top) {
            return false;
        }
        width += left;
        height += top;
        //    overflow || intersect
        return ((width < left || width > x) &&
                (height < top || height > y));
    }

    public static boolean rectContains(double left, double top, double width, double height, double x, double y) {
        if ((Double.doubleToRawLongBits(width) | Double.doubleToRawLongBits(height)) < 0) {
            // At least one of the dimensions is negative...
            return false;
        }
        // Note: if either dimension is zero, tests below must return false...
        if (x < left || y < top) {
            return false;
        }
        width += left;
        height += top;
        //    overflow || intersect
        return ((width < left || width > x) &&
                (height < top || height > y));
    }

    public static BlockPos toBlockPos(double x, double y, double z) {
        return new BlockPos(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z));
    }

    public static BlockPos toBlockPos(Vec3d pos) {
        return toBlockPos(pos.x, pos.y, pos.z);
    }
}

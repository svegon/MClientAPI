package io.github.svegon.utils.math.geometry.space;

import io.github.svegon.utils.hash.HashUtil;
import io.github.svegon.utils.math.geometry.space.shape.Shape;
import io.github.svegon.utils.math.geometry.vector.Vec3d;
import io.github.svegon.utils.math.geometry.vector.Vec3i;
import org.jetbrains.annotations.NotNull;

public class Block6d extends Blocknt<double[], Vec3d, Block6d> {
    public static final Block6d EMPTY = new Block6d(0, 0, 0, 0, 0, 0) {
        @Override
        public Block6d offset(double x, double y, double z) {
            return this;
        }

        @Override
        public Shape getShape() {
            return super.getShape();
        }
    };
    public static final Block6d UNBOUND = create(Vec3d.MIN, Vec3d.MAX);

    private final double minX;
    private final double minY;
    private final double minZ;
    private final double maxX;
    private final double maxY;
    private final double maxZ;

    private Block6d(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public Block6d shrink(double x, double y, double z) {
        double a = getMinX();
        double b = getMinY();
        double c = getMinZ();
        double d = getMaxX();
        double e = getMaxY();
        double f = getMaxZ();

        if (x < 0.0D) {
            a += x;
        } else if (x > 0.0D) {
            d += x;
        }

        if (y < 0.0D) {
            b += y;
        } else if (y > 0.0D) {
            e += y;
        }

        if (z < 0.0D) {
            c += z;
        } else if (z > 0.0D) {
            f += z;
        }

        return a == d || b == e || c == f ? EMPTY : new Block6d(a, b, c, d, e, f);
    }

    public Block6d shrink(Vec3d sizes) {
        return shrink(sizes.getX(), sizes.getY(), sizes.getZ());
    }

    public Block6d stretch(double x, double y, double z) {
        double a = getMinX();
        double b = getMinY();
        double c = getMinZ();
        double d = getMaxX();
        double e = getMaxY();
        double f = getMaxZ();

        if (x < 0.0D) {
            a += x;
        } else if (x > 0.0D) {
            d += x;
        }

        if (y < 0.0D) {
            b += y;
        } else if (y > 0.0D) {
            e += y;
        }

        if (z < 0.0D) {
            c += z;
        } else if (z > 0.0D) {
            f += z;
        }

        return new Block6d(a, b, c, d, e, f);
    }

    public Block6d expand(double x, double y, double z) {
        return create(getMinX() - x, getMinY() - y, getMinZ() - z, getMaxX() + x,
                getMaxY() + y, getMaxZ() + z);
    }

    public Block6d expand(double size) {
        return expand(size, size, size);
    }

    public Block6d contract(double x, double y, double z) {
        return expand(-x, -y, -z);
    }

    public Block6d contract(double size) {
        return contract(size, size, size);
    }

    public Block6d offset(double x, double y, double z) {
        return new Block6d(getMinX() + x, getMinY() + y, getMinZ() + z, getMaxX() + x,
                getMaxY() + y, getMaxZ() + z);
    }

    public boolean intersects(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return getMinX() < maxX && getMaxX() > minX && getMinY() < maxY && getMaxY() > minY && getMinZ() < maxZ
                && getMaxZ() > minZ;
    }

    public boolean contains(double x, double y, double z) {
        return getMinX() <= x && x < getMaxX() && getMinY() <= y && y < getMaxY() && getMinZ() <= z && z < getMaxZ();
    }

    public double getMin(Direction.Axis axis) {
        return axis.choose(getMinX(), getMinY(), getMinZ());
    }

    public double getMax(Direction.Axis axis) {
        return axis.choose(getMaxX(), getMaxY(), getMaxZ());
    }

    public double getSize(Direction.Axis axis) {
        return switch (axis) {
            default -> getSizeX();
            case Y -> getSizeY();
            case Z -> getSizeZ();
        };
    }

    public double getMinX() {
        return minX;
    }

    public double getMaxZ() {
        return maxZ;
    }

    public double getMaxY() {
        return maxY;
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMinZ() {
        return minZ;
    }

    public double getMinY() {
        return minY;
    }

    public double getSizeX() {
        return getMaxX() - getMinX();
    }

    public double getSizeY() {
        return getMaxY() - getMinY();
    }

    public double getSizeZ() {
        return getMaxZ() - getMinZ();
    }

    @Override
    public Vec3d getMinPos() {
        return new Vec3d(getMinX(), getMinY(), getMinZ());
    }

    @Override
    public Vec3d getMaxPos() {
        return new Vec3d(getMaxX(), getMaxY(), getMaxZ());
    }

    @Override
    public Block6d stretch(@NotNull Vec3d sizes) {
        return stretch(sizes.getX(), sizes.getY(), sizes.getZ());
    }

    @Override
    public Block6d expand(Vec3d sizes) {
        return expand(sizes.getX(), sizes.getY(), sizes.getZ());
    }

    @Override
    public Block6d contract(@NotNull Vec3d sizes) {
        return expand(-sizes.getX(), -sizes.getY(), -sizes.getZ());
    }

    @Override
    public Block6d offset(@NotNull Vec3d values) {
        return offset(values.getX(), values.getY(), values.getZ());
    }

    @Override
    public Block6d intersect(@NotNull Block6d other) {
        if (!intersects(other)) {
            return EMPTY;
        }

        return intersection(other);
    }

    private Block6d intersection(Block6d other) {
        return new Block6d(max(getMinX(), other.getMinX()), max(getMinY(), other.getMinY()),
                max(getMinZ(), other.getMinZ()), min(getMaxX(), other.getMaxX()),
                min(getMaxY(), other.getMaxY()), min(getMaxZ(), other.getMaxZ()));
    }

    @Override
    public Block6d union(Block6d other) {
        return new Block6d(min(getMinX(), other.getMinX()), min(getMinY(), other.getMinY()),
                min(getMinZ(), other.getMinZ()), max(getMaxX(), other.getMaxX()),
                max(getMaxY(), other.getMaxY()), max(getMaxZ(), other.getMaxZ()));
    }

    @Override
    public boolean intersects(@NotNull Block6d other) {
        return intersects(other.getMinX(), other.getMinY(), other.getMinZ(), other.getMaxX(), other.getMaxY(),
                other.getMaxZ());
    }

    @Override
    public boolean intersects(@NotNull Vec3d pos0, @NotNull Vec3d pos1) {
        return intersects(create(pos0, pos1));
    }

    @Override
    public boolean contains(@NotNull Vec3d point) {
        return contains(point.getX(), point.getY(), point.getZ());
    }

    @Override
    public Shape getShape() {
        return Block3d.of(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof Block6d block6d))
            return false;

        return getMinX() == block6d.getMinX() && getMinY() == block6d.getMinY() && getMinZ() == block6d.getMinZ()
                && getMaxX() == block6d.getMaxX() && getMaxY() == block6d.getMaxY() && getMaxZ() == block6d.getMaxZ();
    }

    @Override
    public int hashCode() {
        return HashUtil.hashOrdered(getMinX(), getMinY(), getMinZ(), getMaxX(), getMaxY(), getMaxZ());
    }

    public static Block6d create(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        if (minX == maxX || minY == maxY || minZ == maxZ) {
            return EMPTY;
        }

        return new Block6d(min(minX, maxX), min(minY, maxY), min(minZ, maxZ), max(minX, maxX),
                max(minY, maxY), max(minZ, maxZ));
    }

    public static Block6d create(Vec3d bound0, Vec3d bound1) {
        return create(bound0.getX(), bound0.getY(), bound0.getZ(), bound1.getX(), bound1.getY(), bound1.getZ());
    }

    public static Block6d of(Vec3i block) {
        return new Block6d(block.getX(), block.getY(), block.getZ(), block.getX() + 1, block.getY() + 1,
                block.getZ() + 1);
    }

    public static Block6d of(Block6i block) {
        return new Block6d(block.getMinX(), block.getMinY(), block.getMinZ(), block.getMaxX() + 1,
                block.getMaxY() + 1, block.getMaxZ() + 1);
    }
}

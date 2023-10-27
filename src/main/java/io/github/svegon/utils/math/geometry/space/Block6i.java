package io.github.svegon.utils.math.geometry.space;

import io.github.svegon.utils.hash.HashUtil;
import io.github.svegon.utils.math.geometry.space.shape.Shape;
import io.github.svegon.utils.math.geometry.vector.Vec3i;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.Optional;

public final class Block6i extends Blocknt<int[], Vec3i, Block6i> {
    public static final Block6i ORIGIN = of(Vec3i.ZERO);

    private final int minX;
    private final int minY;
    private final int minZ;
    private final int maxX;
    private final int maxY;
    private final int maxZ;

    private Block6i(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public int getMin(Direction.Axis axis) {
        return axis.choose(getMinX(), getMinY(), getMinZ());
    }

    public int getMax(Direction.Axis axis) {
        return axis.choose(getMaxX(), getMaxY(), getMaxZ());
    }

    public int getSize(Direction.Axis axis) {
        return switch (axis) {
            default -> getSizeX();
            case Y -> getSizeY();
            case Z -> getSizeZ();
        };
    }

    public Block6i shrink(int x, int y, int z) {
        int a = getMinX();
        int b = getMinY();
        int c = getMinZ();
        int d = getMaxX();
        int e = getMaxY();
        int f = getMaxZ();

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

        return new Block6i(a, b, c, d, e, f);
    }

    public Block6i shrink(Vec3i scales) {
        return shrink(scales.getX(), scales.getY(), scales.getZ());
    }

    public Block6i stretch(int x, int y, int z) {
        int a = getMinX();
        int b = getMinY();
        int c = getMinZ();
        int d = getMaxX();
        int e = getMaxY();
        int f = getMaxZ();

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

        return new Block6i(a, b, c, d, e, f);
    }

    public Block6i expand(int x, int y, int z) {
        return create(getMinX() - x, getMinY() - y, getMinZ() - z, getMaxX() + x,
                getMaxY() + y, getMaxZ() + z);
    }

    public Block6i expand(int size) {
        return expand(size, size, size);
    }

    public Block6i contract(int x, int y, int z) {
        return expand(-x, -y, -z);
    }

    public Block6i contract(int size) {
        return contract(size, size, size);
    }

    public Block6i offset(int x, int y, int z) {
        return new Block6i(getMinX() + x, getMinY() + y, getMinZ() + z, getMaxX() + x,
                getMaxY() + y, getMaxZ() + z);
    }

    public boolean intersects(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        return getMinX() <= maxX && getMaxX() >= minX && getMinY() <= maxY && getMaxY() >= minY && getMinZ() <= maxZ
                && getMaxZ() >= minZ;
    }

    public Optional<Block6i> getIntersection(Block6i other) {
        return Optional.ofNullable(intersects(other) ? intersection(other) : null);
    }

    public boolean contains(int x, int y, int z) {
        return getMinX() <= x && x <= getMaxX() && getMinY() <= y && y <= getMaxY() && getMinZ() <= z && z <= getMaxZ();
    }

    public int getMinX() {
        return minX;
    }

    public int getMaxZ() {
        return maxZ;
    }

    public int getMaxY() {
        return maxY;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMinZ() {
        return minZ;
    }

    public int getMinY() {
        return minY;
    }

    public int getSizeX() {
        return getMaxX() - getMinX() + 1;
    }

    public int getSizeY() {
        return getMaxY() - getMinY() + 1;
    }

    public int getSizeZ() {
        return getMaxZ() - getMinZ() + 1;
    }

    @Override
    public Vec3i getMinPos() {
        return new Vec3i(getMinX(), getMinY(), getMinZ());
    }

    @Override
    public Vec3i getMaxPos() {
        return new Vec3i(getMaxX(), getMaxY(), getMaxZ());
    }

    @Override
    public Block6i stretch(@NotNull Vec3i sizes) {
        return stretch(sizes.getX(), sizes.getY(), sizes.getZ());
    }

    @Override
    public Block6i expand(Vec3i sizes) {
        return expand(sizes.getX(), sizes.getY(), sizes.getZ());
    }

    @Override
    public Block6i contract(@NotNull Vec3i sizes) {
        return expand(-sizes.getX(), -sizes.getY(), -sizes.getZ());
    }

    @Override
    public Block6i offset(@NotNull Vec3i values) {
        return offset(values.getX(), values.getY(), values.getZ());
    }

    @Override
    public Block6i intersect(@NotNull Block6i other) {
        if (!intersects(other)) {
            throw new NoSuchElementException("I'm too lazy to make an empty Block6i.");
        }

        return intersection(other);
    }

    private Block6i intersection(Block6i other) {
        return new Block6i(Math.max(getMinX(), other.getMinX()), Math.max(getMinY(), other.getMinY()),
                Math.max(getMinZ(), other.getMinZ()), Math.min(getMaxX(), other.getMaxX()),
                Math.min(getMaxY(), other.getMaxY()), Math.min(getMaxZ(), other.getMaxZ()));
    }

    @Override
    public Block6i union(Block6i other) {
        return new Block6i(Math.min(getMinX(), other.getMinX()), Math.min(getMinY(), other.getMinY()),
                Math.min(getMinZ(), other.getMinZ()), Math.max(getMaxX(), other.getMaxX()),
                Math.max(getMaxY(), other.getMaxY()), Math.max(getMaxZ(), other.getMaxZ()));
    }

    @Override
    public boolean intersects(@NotNull Block6i other) {
        return intersects(other.getMinX(), other.getMinY(), other.getMinZ(), other.getMaxX(), other.getMaxY(),
                other.getMaxZ());
    }

    @Override
    public boolean intersects(@NotNull Vec3i pos0, @NotNull Vec3i pos1) {
        return intersects(create(pos0, pos1));
    }

    @Override
    public boolean contains(@NotNull Vec3i point) {
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

        if (!(o instanceof Block6i block6i))
            return false;

        return getMinX() == block6i.getMinX() && getMinY() == block6i.getMinY() && getMinZ() == block6i.getMinZ()
                && getMaxX() == block6i.getMaxX() && getMaxY() == block6i.getMaxY() && getMaxZ() == block6i.getMaxZ();
    }

    @Override
    public int hashCode() {
        return HashUtil.hashOrdered(getMinX(), getMinY(), getMinZ(), getMaxX(), getMaxY(), getMaxZ());
    }

    public static Block6i create(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        return new Block6i(Math.min(minX, maxX), Math.min(minY, maxY), Math.min(minZ, maxZ), Math.max(minX, maxX),
                Math.max(minY, maxY), Math.max(minZ, maxZ));
    }

    public static Block6i create(Vec3i bound0, Vec3i bound1) {
        return create(bound0.getX(), bound0.getY(), bound0.getZ(), bound1.getX(), bound1.getY(), bound1.getZ());
    }

    public static Block6i of(Vec3i block) {
        return new Block6i(block.getX(), block.getY(), block.getZ(), block.getX(), block.getY(), block.getZ());
    }
}

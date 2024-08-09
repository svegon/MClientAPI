package io.github.svegon.utils.math.geometry.vector;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public final class Vec3i extends Vec3t<int[], Vec3i> {
    public static final Vec3i ZERO = new Vec3i(0, 0, 0);
    public static final Vec3i MIN = new Vec3i(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
    public static final Vec3i MAX = new Vec3i(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);

    private final int x;
    private final int y;
    private final int z;

    public Vec3i(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3i(Vec3l original) {
        this((int) original.getX(), (int) original.getY(), (int) original.getZ());
    }

    public Vec3i(Vec3d original) {
        this((int) original.getX(), (int) original.getY(), (int) original.getZ());
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public Vec3i add(int x, int y, int z) {
        return new Vec3i(getX() + x, getY() + y, getZ() + z);
    }

    public Vec3i substract(int x, int y, int z) {
        return new Vec3i(getX() - x, getY() - y, getZ() - z);
    }

    public Vec3i multiplyEach(int x, int y, int z) {
        return new Vec3i(getX() * x, getY() * y, getZ() * z);
    }

    @Override
    public int[] toPrimitiveArray() {
        return new int[]{getX(), getY(), getZ()};
    }

    @Override
    public int[] toPrimitiveArray(int @Nullable [] ints) {
        if (ints == null || ints.length < 3) {
            return toPrimitiveArray();
        }

        ints[0] = getX();
        ints[1] = getY();
        ints[2] = getZ();

        return ints;
    }

    @Override
    public Vec3i add(Vec3i other) {
        return add(other.getX(), other.getY(), other.getZ());
    }

    @Override
    public Vec3i substract(Vec3i other) {
        return add(-other.getX(), -other.getY(), -other.getZ());
    }

    @Override
    public Vec3i multiply(double multiplier) {
        return new Vec3i((int) (getX() * multiplier), (int) (getY() * multiplier), (int) (getZ() * multiplier));
    }

    @Override
    public Vec3i multiplyEach(Vec3i other) {
        return multiplyEach(other.getX(), other.getY(), other.getZ());
    }

    @Override
    public Vec3i divide(double divider) {
        return new Vec3i((int) (getX() / divider), (int) (getY() / divider), (int) (getZ() / divider));
    }

    @Override
    public Vec3i neg() {
        return new Vec3i(-getX(), -getY(), -getZ());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof Vec3i vec3i))
            return false;

        return getX() == vec3i.getX() && getY() == vec3i.getY() && getZ() == vec3i.getZ();
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(toPrimitiveArray());
    }

    @Override
    public String toString() {
        return getX() + " " + getY() + " " + getZ();
    }

    @Override
    public Vec3i crossProduct(Vec3i multiplier) {
        return new Vec3i(getY() * multiplier.getZ() - getZ() * multiplier.getY(),
                getZ() * multiplier.getX() - getX() * multiplier.getZ(),
                getX() * multiplier.getY() + getY() * multiplier.getX());
    }

    @Override
    public Vec4i to4D() {
        return new Vec4i(getX(), getY(), getZ(), 0);
    }

    @Override
    public double getDouble(int index) {
        return switch (index) {
            case 0 -> getX();
            case 1 -> getY();
            case 2 -> getZ();
            default -> throw new IndexOutOfBoundsException();
        };
    }
}

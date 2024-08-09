package io.github.svegon.utils.math.geometry.vector;

import io.github.svegon.utils.fast.util.tuple.triplet.DoubleDoubleDoubleTriplet;
import org.jetbrains.annotations.Nullable;

public final class Vec3d extends Vec3t<double[], Vec3d> implements DoubleDoubleDoubleTriplet {
    public static final Vec3d ZERO = new Vec3d(0, 0, 0);
    public static final Vec3d MIN = new Vec3d(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY,
            Double.NEGATIVE_INFINITY);
    public static final Vec3d MAX = new Vec3d(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY,
            Double.POSITIVE_INFINITY);
    public static final Vec3d NAN = new Vec3d(Double.NaN, Double.NaN, Double.NaN);

    private final double x;
    private final double y;
    private final double z;

    public Vec3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3d(Vec3i original) {
        this(original.getX(), original.getY(), original.getZ());
    }

    public Vec3d(Vec3l original) {
        this(original.getX(), original.getY(), original.getZ());
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public Vec3d add(double x, double y, double z) {
        return new Vec3d(getX() + x, getY() + y, getZ() + z);
    }

    public Vec3d multiplyEach(double x, double y, double z) {
        return new Vec3d(getX() * x, getY() * y, getZ() * z);
    }

    @Override
    public double[] toPrimitiveArray() {
        return toPrimitiveArray(null);
    }

    @Override
    public double[] toPrimitiveArray(double @Nullable [] doubles) {
        if (doubles == null || doubles.length < 3) {
            doubles = new double[3];
        }

        doubles[0] = getX();
        doubles[1] = getY();
        doubles[2] = getZ();

        return doubles;
    }

    @Override
    public Vec3d add(Vec3d other) {
        return add(other.getX(), other.getY(), other.getZ());
    }

    @Override
    public Vec3d substract(Vec3d other) {
        return add(-other.getX(), -other.getY(), -other.getZ());
    }

    @Override
    public Vec3d multiply(double multiplier) {
        return new Vec3d(getX() * multiplier, getY() * multiplier, getZ() * multiplier);
    }

    @Override
    public Vec3d multiplyEach(Vec3d other) {
        return multiplyEach(other.getX(), other.getY(), other.getZ());
    }

    @Override
    public Vec3d divide(double divider) {
        return new Vec3d(getX() / divider, getY() / divider, getZ() / divider);
    }

    @Override
    public Vec3d neg() {
        return new Vec3d(-getX(), -getY(), -getZ());
    }

    @Override
    public String toString() {
        return "Vec3d(" + getX() + " " + getY() + " " + getZ() + ")";
    }

    @Override
    public Vec3d crossProduct(Vec3d multiplier) {
        return new Vec3d(getY() * multiplier.getZ() - getZ() * multiplier.getY(),
                getZ() * multiplier.getX() - getX() * multiplier.getZ(),
                getX() * multiplier.getY() - getY() * multiplier.getX());
    }

    @Override
    public Vec4d to4D() {
        return new Vec4d(getX(), getY(), getZ(), 0);
    }

    @Override
    public double firstDouble() {
        return getX();
    }

    @Override
    public double secondDouble() {
        return getY();
    }

    @Override
    public double thirdDouble() {
        return getZ();
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

package io.github.svegon.utils.math.geometry.vector;

import org.jetbrains.annotations.Nullable;

public final class Vec4d extends Vec4t<double[], Vec4d> {
    public static final Vec4d ZERO = new Vec4d(0, 0, 0, 0);
    public static final Vec4d MIN = new Vec4d(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY,
            Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
    public static final Vec4d MAX = new Vec4d(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY,
            Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    public static final Vec4d NAN = new Vec4d(Double.NaN, Double.NaN, Double.NaN, Double.NaN);

    private final double x;
    private final double y;
    private final double z;
    private final double w;

    public Vec4d(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
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

    public double getW() {
        return w;
    }

    public Vec4d add(double x, double y, double z, double w) {
        return new Vec4d(getX() + x, getY() + y, getZ() + z, getW() + w);
    }

    public Vec4d multiplyEach(double x, double y, double z, double w) {
        return new Vec4d(getX() * x, getY() * y, getZ() * z, getW() * w);
    }

    @Override
    public double[] toPrimitiveArray() {
        return new double[]{getX(), getY(), getZ(), getW()};
    }

    @Override
    public double[] toPrimitiveArray(double @Nullable [] doubles) {
        if (doubles == null || doubles.length < 4) {
            return toPrimitiveArray();
        }

        doubles[0] = getX();
        doubles[1] = getY();
        doubles[2] = getZ();
        doubles[3] = getW();

        return doubles;
    }

    @Override
    public Vec4d add(Vec4d other) {
        return add(other.getX(), other.getY(), other.getZ(), other.getW());
    }

    @Override
    public Vec4d substract(Vec4d other) {
        return add(-other.getX(), -other.getY(), -other.getZ(), -other.getW());
    }

    @Override
    public Vec4d multiply(double multiplier) {
        return new Vec4d(getX() * multiplier, getY() * multiplier, getZ() * multiplier, getW() * multiplier);
    }

    @Override
    public Vec4d multiplyEach(Vec4d other) {
        return multiplyEach(other.getX(), other.getY(), other.getZ(), other.getW());
    }

    @Override
    public Vec4d divide(double divider) {
        return new Vec4d(getX() / divider, getY() / divider, getZ() / divider, getW() * divider);
    }

    @Override
    public Vec4d neg() {
        return new Vec4d(-getX(), -getY(), -getZ(), -getW());
    }

    @Override
    public String toString() {
        return getX() + " " + getY() + " " + getZ() + " " + getW();
    }

    @Override
    public double getDouble(int index) {
        return switch (index) {
            case 0 -> getX();
            case 1 -> getY();
            case 2 -> getZ();
            case 3 -> getW();
            default -> throw new IndexOutOfBoundsException();
        };
    }
}

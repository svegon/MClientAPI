package io.github.svegon.utils.math.geometry.vector;

import it.unimi.dsi.fastutil.doubles.DoubleDoublePair;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.Nullable;

@Immutable
public final class Vec2d extends Vec2t<double[], Vec2d> implements DoubleDoublePair {
    public static final Vec2d ZERO = new Vec2d(0, 0);
    public static final Vec2d MIN = new Vec2d(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
    public static final Vec2d MAX = new Vec2d(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    public static final Vec2d INVALID = new Vec2d(Double.NaN, Double.NaN);

    private final double x;
    private final double y;

    public Vec2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Vec2d add(double x, double y) {
        return new Vec2d(getX() + x, getY() + y);
    }

    public Vec2d multiplyEach(double x, double y) {
        return new Vec2d(getX() * x, getY() * y);
    }

    @Override
    public double[] toPrimitiveArray() {
        return new double[]{getX(), getY()};
    }

    @Override
    public double[] toPrimitiveArray(double @Nullable [] doubles) {
        if (doubles == null || doubles.length < 2) {
            return toPrimitiveArray();
        }

        doubles[0] = getX();
        doubles[1] = getY();

        return doubles;
    }

    @Override
    public Vec2d add(Vec2d other) {
        return add(other.getX(), other.getY());
    }

    @Override
    public Vec2d substract(Vec2d other) {
        return add(-other.getX(), -other.getY());
    }

    @Override
    public Vec2d multiply(double multiplier) {
        return new Vec2d(getX() * multiplier, getY() * multiplier);
    }

    @Override
    public Vec2d multiplyEach(Vec2d other) {
        return multiplyEach(other.getX(), other.getY());
    }

    @Override
    public Vec2d divide(double divider) {
        return new Vec2d(getX() / divider, getY() / divider);
    }

    @Override
    public Vec2d neg() {
        return new Vec2d(-getX(), -getY());
    }

    @Override
    public double leftDouble() {
        return getX();
    }

    @Override
    public double rightDouble() {
        return getY();
    }

    @Override
    public String toString() {
        return getX() + " " + getY();
    }

    @Override
    public Vec3d crossProduct(Vec2d mult) {
        return new Vec3d(0, 0, getX() * mult.getY() - getY() * mult.getX());
    }

    @Override
    public Vec3d to3D() {
        return new Vec3d(getX(), getY(), 0);
    }

    @Override
    public Vec4d to4D() {
        return new Vec4d(getX(), getY(), 0, 0);
    }

    @Override
    public double getDouble(int index) {
        return switch (index) {
            case 0 -> getX();
            case 1 -> getY();
            default -> throw new IndexOutOfBoundsException();
        };
    }
}

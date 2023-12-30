package io.github.svegon.utils.math.geometry.vector;

import it.unimi.dsi.fastutil.floats.FloatFloatPair;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.Nullable;

@Immutable
public final class Vec2f extends Vec2t<float[], Vec2f> implements FloatFloatPair {
    public static final Vec2f ZERO = new Vec2f(0, 0);
    public static final Vec2f MIN = new Vec2f(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);
    public static final Vec2f MAX = new Vec2f(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
    public static final Vec2f NAN = new Vec2f(Float.NaN, Float.NaN);

    private final float x;
    private final float y;

    public Vec2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Vec2f add(float x, float y) {
        return new Vec2f(getX() + x, getY() + y);
    }

    public Vec2f multiplyEach(float x, float y) {
        return new Vec2f(getX() * x, getY() * y);
    }

    @Override
    public float[] toPrimitiveArray() {
        return new float[]{getX(), getY()};
    }

    @Override
    public float[] toPrimitiveArray(float @Nullable [] floats) {
        if (floats == null || floats.length < 2) {
            return toPrimitiveArray();
        }

        floats[0] = getX();
        floats[1] = getY();

        return floats;
    }

    @Override
    public Vec2f add(Vec2f other) {
        return add(other.getX(), other.getY());
    }

    @Override
    public Vec2f substract(Vec2f other) {
        return add(-other.getX(), -other.getY());
    }

    @Override
    public Vec2f multiply(double multiplier) {
        return new Vec2f((float) (getX() * multiplier), (float) (getY() * multiplier));
    }

    @Override
    public Vec2f multiplyEach(Vec2f other) {
        return multiplyEach(other.getX(), other.getY());
    }

    @Override
    public Vec2f divide(double divider) {
        return new Vec2f((float) (getX() / divider), (float) (getY() / divider));
    }

    @Override
    public Vec2f neg() {
        return new Vec2f(-getX(), -getY());
    }

    @Override
    public float leftFloat() {
        return getX();
    }

    @Override
    public float rightFloat() {
        return getY();
    }

    @Override
    public String toString() {
        return getX() + " " + getY();
    }

    @Override
    public Vec3f crossProduct(Vec2f mult) {
        return new Vec3f(0, 0, getX() * mult.getY() - getY() * mult.getX());
    }

    @Override
    public Vec3f to3D() {
        return new Vec3f(getX(), getY(), 0);
    }

    @Override
    public Vec4f to4D() {
        return new Vec4f(getX(), getY(), 0, 0);
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

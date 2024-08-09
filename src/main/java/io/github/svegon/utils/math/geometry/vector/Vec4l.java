package io.github.svegon.utils.math.geometry.vector;

import org.jetbrains.annotations.Nullable;

public final class Vec4l extends Vec4t<long[], Vec4l> {
    public static final Vec4l ZERO = new Vec4l(0, 0, 0, 0);
    public static final Vec4l MIN = new Vec4l(Long.MIN_VALUE, Long.MIN_VALUE, Long.MIN_VALUE, Long.MIN_VALUE);
    public static final Vec4l MAX = new Vec4l(Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE);

    private final long x;
    private final long y;
    private final long z;
    private final long w;

    public Vec4l(long x, long y, long z, long w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public long getX() {
        return x;
    }

    public long getY() {
        return y;
    }

    public long getZ() {
        return z;
    }

    public long getW() {
        return w;
    }

    public Vec4l add(long x, long y, long z, long w) {
        return new Vec4l(getX() + x, getY() + y, getZ() + z, getW() + w);
    }

    public Vec4l multiplyEach(double x, double y, double z, double w) {
        return new Vec4l((long) (getX() * x), (long) (getY() * y), (long) (getZ() * z), (long) (getW() * w));
    }

    @Override
    public long[] toPrimitiveArray() {
        return new long[]{getX(), getY(), getZ(), getW()};
    }

    @Override
    public long[] toPrimitiveArray(long @Nullable [] longs) {
        if (longs == null || longs.length < 4) {
            return toPrimitiveArray();
        }

        longs[0] = getX();
        longs[1] = getY();
        longs[2] = getZ();
        longs[3] = getW();

        return longs;
    }

    @Override
    public Vec4l add(Vec4l other) {
        return add(other.getX(), other.getY(), other.getZ(), other.getW());
    }

    @Override
    public Vec4l substract(Vec4l other) {
        return add(-other.getX(), -other.getY(), -other.getZ(), -other.getW());
    }

    @Override
    public Vec4l multiply(double multiplier) {
        return multiplyEach(multiplier, multiplier, multiplier, multiplier);
    }

    @Override
    public Vec4l multiplyEach(Vec4l other) {
        return multiplyEach(other.getX(), other.getY(), other.getZ(), other.getW());
    }

    @Override
    public Vec4l divide(double divider) {
        return new Vec4l((long) (getX() / divider), (long) (getY() / divider), (long) (getZ() / divider),
                (long) (getW() * divider));
    }

    @Override
    public Vec4l neg() {
        return new Vec4l(-getX(), -getY(), -getZ(), -getW());
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

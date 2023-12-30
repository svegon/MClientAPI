package io.github.svegon.utils.math.geometry.vector;

import io.github.svegon.utils.math.MathUtil;

public abstract class Vec3t<A, T extends Vec3t<A, T>> extends Vecnt<A, T> {
    @Override
    public final int size() {
        return 3;
    }

    @Override
    public double lengthSquared() {
        return MathUtil.squared(getDouble(0)) + MathUtil.squared(getDouble(1))
                + MathUtil.squared(getDouble(2));
    }

    public abstract T crossProduct(T multiplier);

    public double scalarProduct(Vec3t<?, ?> vec3t) {
        return getDouble(0) * vec3t.getDouble(0) + getDouble(1) * vec3t.getDouble(1)
                + getDouble(2) * vec3t.getDouble(2);
    }

    public double distanceTo(double x, double y, double z) {
        return Math.sqrt(squaredDistanceTo(x, y, z));
    }

    public double squaredDistanceTo(double x, double y, double z) {
        return MathUtil.squared(getDouble(0) - x) + MathUtil.squared(getDouble(1) - y)
                + MathUtil.squared(getDouble(2) - z);
    }

    public abstract Vec4t<A, ?> to4D();
}

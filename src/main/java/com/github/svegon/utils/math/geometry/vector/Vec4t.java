package com.github.svegon.utils.math.geometry.vector;

import com.github.svegon.utils.math.MathUtil;

public abstract class Vec4t<A, T extends Vec4t<A, T>> extends Vecnt<A, T> {
    @Override
    public int hashCode() {
        return 31 * 31 * 31 * Double.hashCode(getDouble(0)) + 31 * 31 * Double.hashCode(getDouble(1))
                + 31 * Double.hashCode(getDouble(2)) + Double.hashCode(getDouble(3));
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public double lengthSquared() {
        return MathUtil.squared(getDouble(0)) + MathUtil.squared(getDouble(1)) + MathUtil.squared(getDouble(2))
                + MathUtil.squared(getDouble(3));
    }

    public double scalarProduct(Vec4t<?, ?> vec4t) {
        return getDouble(0) * vec4t.getDouble(0) + getDouble(1) * vec4t.getDouble(1)
                + getDouble(2) * vec4t.getDouble(2) + getDouble(3) * vec4t.getDouble(3);
    }
}

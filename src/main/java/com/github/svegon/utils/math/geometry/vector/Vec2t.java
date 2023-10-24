package com.github.svegon.utils.math.geometry.vector;

import com.github.svegon.utils.math.MathUtil;
import net.jcip.annotations.Immutable;

@Immutable
public abstract class Vec2t<A, T extends Vec2t<A, T>> extends Vecnt<A, T> {
    @Override
    public int size() {
        return 2;
    }

    @Override
    public double lengthSquared() {
        return MathUtil.squared(getDouble(0)) + MathUtil.squared(getDouble(1));
    }

    public abstract Vec3t<A, ?> crossProduct(T mult);

    public double scalarProduct(Vec2t<?, ?> vec2t) {
        return getDouble(0) * vec2t.getDouble(0) + getDouble(1) * vec2t.getDouble(1);
    }

    public abstract Vec3t<A, ?> to3D();

    public abstract Vec4t<A, ?> to4D();
}

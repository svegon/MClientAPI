package io.github.svegon.utils.math.geometry.space.shape;

import io.github.svegon.utils.fast.util.objects.immutable.ImmutableObjectSet;
import io.github.svegon.utils.math.geometry.vector.Vec3d;

public abstract class AbstractShape implements Shape {
    private ImmutableObjectSet<Vec3d> vertexes;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Shape shape)) {
            return false;
        }

        return shape instanceof ComplexBehaviorShape ? shape.equals(this) : equals(shape);
    }

    @Override
    public int hashCode() {
        return vertexes().hashCode();
    }

    public ImmutableObjectSet<Vec3d> vertexes() {
        if (vertexes == null) {
            vertexes = getVertexes();
        }

        return vertexes;
    }

    protected abstract ImmutableObjectSet<Vec3d> getVertexes();
}

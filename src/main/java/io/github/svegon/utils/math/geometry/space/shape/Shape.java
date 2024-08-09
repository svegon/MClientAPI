package io.github.svegon.utils.math.geometry.space.shape;

import io.github.svegon.utils.fast.util.objects.immutable.ImmutableObjectSet;
import io.github.svegon.utils.interfaces.function.DoubleDoubleDoubleTriConsumer;
import io.github.svegon.utils.interfaces.function.HexaDoubleConsumer;
import io.github.svegon.utils.interfaces.function.NonaDoubleConsumer;
import io.github.svegon.utils.math.MathUtil;
import io.github.svegon.utils.math.geometry.vector.Vec3d;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.AtomicDouble;
import com.google.common.util.concurrent.AtomicDoubleArray;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Immutable
public interface Shape {
    @Override
    boolean equals(@Nullable Object o);

    /**
     * hashCode of a set of vertexes of this shape
     *
     * @return hash code
     */
    @Override
    int hashCode();

    @Override
    String toString();

    default boolean equals(Shape other) {
        return other instanceof ComplexBehaviorShape ? other.equals(this) : vertexes().equals(other.vertexes());
    }

    ImmutableObjectSet<Vec3d> vertexes();

    default void forEachVertex(final @NotNull DoubleDoubleDoubleTriConsumer consumer) {
        Preconditions.checkNotNull(consumer);
        vertexes().forEach((vec3d) -> consumer.consume(vec3d.getX(), vec3d.getY(), vec3d.getZ()));
    }

    default void forEachEdge(final @NotNull HexaDoubleConsumer consumer) {
        Preconditions.checkNotNull(consumer);

        AtomicDouble prevX = new AtomicDouble(Double.NaN);
        AtomicDouble prevY = new AtomicDouble();
        AtomicDouble prevZ = new AtomicDouble();

        forEachVertex((x, y, z) -> {
            if (!Double.isNaN(prevX.get())) {
                consumer.consume(prevX.get(), prevY.get(), prevZ.get(), x, y, z);
            }

            prevX.set(x);
            prevY.set(y);
            prevZ.set(z);
        });
    }

    /**
     * Iterates over each triangle on the surface sorting its vertexes counterclockwise
     * (from the perspective of a viewer watching plumb on the surface outside the shape).
     *
     * @param consumer
     */
    default void forEachSurface(final @NotNull NonaDoubleConsumer consumer) {
        final AtomicDoubleArray prevVertexes = new AtomicDoubleArray(new double[]{0, 0, Double.NaN, 0, 0, Double.NaN});

        forEachVertex((x, y, z) -> {
            if (Double.doubleToRawLongBits(prevVertexes.get(2)) != MathUtil.DOUBLE_CANONICAL_NaN_BITS) {
                consumer.consume(prevVertexes.getAndSet(0, prevVertexes.get(3)),
                        prevVertexes.getAndSet(1, prevVertexes.get(4)),
                        prevVertexes.getAndSet(2, prevVertexes.get(5)), prevVertexes.getAndSet(3, x),
                        prevVertexes.getAndSet(4, y), prevVertexes.getAndSet(5, z), x, y, z);
            } else {
                prevVertexes.set(0, prevVertexes.getAndSet(3, x));
                prevVertexes.set(1, prevVertexes.getAndSet(4, y));
                prevVertexes.set(2, prevVertexes.getAndSet(5, z));
            }
        });
    }

    @Deprecated
    void forEachBox(@NotNull HexaDoubleConsumer consumer);

    default Optional<Vec3d> closestPoint(Vec3d relativeTarget) {
        AtomicReference<Vec3d> vec3d = new AtomicReference<>();

        this.forEachBox((minX, minY, minZ, maxX, maxY, maxZ) -> {
            double d = MathUtil.clamp(relativeTarget.getX(), minX, maxX);
            double e = MathUtil.clamp(relativeTarget.getY(), minY, maxY);
            double f = MathUtil.clamp(relativeTarget.getZ(), minZ, maxZ);

            if (vec3d.get() == null || relativeTarget.squaredDistanceTo(d, e, f)
                    < relativeTarget.squaredDistanceTo(vec3d.get())) {
                vec3d.set(new Vec3d(d, e, f));
            }
        });

        return Optional.ofNullable(vec3d.get());
    }

    default Optional<Vec3d> closestPoint(final @NotNull Vec3d pos, Vec3d viewer) {
        return closestPoint(viewer.substract(pos)).map(pos::add);
    }

    default Shape intersect(@NotNull Vec3d offset, @NotNull Shape other) {
        return ShapeIntersection.of(ImmutableList.of(new ObjectObjectImmutablePair<>(Vec3d.ZERO, this),
                new ObjectObjectImmutablePair<>(offset, other)));
    }

    default Shape intersect(Shape other) {
        return intersect(Vec3d.ZERO, other);
    }

    default Shape union(@NotNull Vec3d offset, @NotNull Shape other) {
        return new ShapeUnion(ImmutableList.of(new ObjectObjectImmutablePair<>(Vec3d.ZERO, this),
                new ObjectObjectImmutablePair<>(offset, other)));
    }

    default Shape union(Shape other) {
        return union(Vec3d.ZERO, other);
    }
}

package com.github.svegon.utils.math.geometry.space.shape;

import com.github.svegon.utils.fast.util.objects.immutable.ImmutableObjectSet;
import com.github.svegon.utils.interfaces.function.DoubleDoubleDoubleTriConsumer;
import com.github.svegon.utils.interfaces.function.HexaDoubleConsumer;
import com.github.svegon.utils.math.geometry.vector.Vec3d;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;

@Immutable
public final class EmptyShape implements Shape {
    EmptyShape() {

    }

    @Override
    public ImmutableObjectSet<Vec3d> vertexes() {
        return ImmutableObjectSet.of();
    }

    @Override
    public void forEachVertex(@NotNull DoubleDoubleDoubleTriConsumer consumer) {

    }

    @Override
    public void forEachEdge(@NotNull HexaDoubleConsumer consumer) {

    }

    @Override
    public void forEachBox(@NotNull HexaDoubleConsumer consumer) {

    }

    @Override
    public Shape intersect(@NotNull Vec3d offset, @NotNull Shape other) {
        return this;
    }

    @Override
    public Shape union(@NotNull Vec3d offset, @NotNull Shape other) {
        return other;
    }
}

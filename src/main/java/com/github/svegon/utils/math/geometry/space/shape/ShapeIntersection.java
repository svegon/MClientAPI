package com.github.svegon.utils.math.geometry.space.shape;

import com.github.svegon.utils.annotations.Unfinished;
import com.github.svegon.utils.collections.SetUtil;
import com.github.svegon.utils.fast.util.objects.immutable.ImmutableObjectSet;
import com.github.svegon.utils.interfaces.function.HexaDoubleConsumer;
import com.github.svegon.utils.math.geometry.vector.Vec3d;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Stream;

@Unfinished
@Immutable
public final class ShapeIntersection extends AbstractShape {
    private final ImmutableSet<ObjectObjectImmutablePair<Vec3d, Shape>> shapes;

    ShapeIntersection(ImmutableSet<ObjectObjectImmutablePair<Vec3d, Shape>> shapes) {
        this.shapes = shapes;
    }

    @Override
    public String toString() {
        return "ShapeIntersection" + shapes;
    }

    @Override
    public void forEachEdge(@NotNull HexaDoubleConsumer consumer) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public void forEachBox(@NotNull HexaDoubleConsumer consumer) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    protected ImmutableObjectSet<Vec3d> getVertexes() {
        if (shapes.isEmpty()) {
            return ImmutableObjectSet.of();
        }

        Iterator<ObjectObjectImmutablePair<Vec3d, Shape>> it = shapes.iterator();
        Pair<Vec3d, Shape> pair = it.next();
        Vec3d firstOffset = pair.first();
        LinkedHashSet<Vec3d> builder = Sets.newLinkedHashSet(firstOffset.equals(Vec3d.ZERO)
                ? pair.second().vertexes() : SetUtil.transformToObj(pair.second().vertexes(), firstOffset::add));

        while (it.hasNext()) {
            pair = it.next();
            final Vec3d offset = pair.first();

            builder.retainAll(offset.equals(Vec3d.ZERO) ? pair.second().vertexes()
                    : SetUtil.transformToObj(pair.second().vertexes(), offset::add));
        }

        return ImmutableObjectSet.copyOf(builder);
    }

    public ImmutableSet<ObjectObjectImmutablePair<Vec3d, Shape>> getShapes() {
        return shapes;
    }

    @Override
    public Shape intersect(@NotNull Vec3d offset, @NotNull Shape other) {
        Set<ObjectObjectImmutablePair<Vec3d, Shape>> mutable = Sets.newHashSet(getShapes());

        mutable.add(new ObjectObjectImmutablePair<>(offset, other));

        return new ShapeIntersection(ImmutableSet.copyOf(mutable));
    }

    @Contract(value = "_ -> new", pure = true)
    public static ShapeIntersection of(@NotNull Collection<ObjectObjectImmutablePair<Vec3d, Shape>> shapes) {
        return new ShapeIntersection(shapes.parallelStream().flatMap(s -> s.right() instanceof ShapeIntersection si
                ? si.getShapes().parallelStream().map(p -> new ObjectObjectImmutablePair<>(p.left().substract(s.left()),
                p.right())) : Stream.of(s)).collect(ImmutableSet.toImmutableSet()));
    }
}

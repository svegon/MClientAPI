package io.github.svegon.utils.math.geometry.space.shape;

import io.github.svegon.utils.annotations.Unfinished;
import io.github.svegon.utils.fast.util.objects.immutable.ImmutableObjectSet;
import io.github.svegon.utils.interfaces.function.HexaDoubleConsumer;
import io.github.svegon.utils.math.geometry.vector.Vec3d;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;
import java.util.List;

@Unfinished
@Immutable
public final class ShapeUnion extends AbstractShape {
    private final ImmutableList<ObjectObjectImmutablePair<Vec3d, Shape>> shapes;

    ShapeUnion(ImmutableList<ObjectObjectImmutablePair<Vec3d, Shape>> shapes) {
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

    @Deprecated
    @Override
    public void forEachBox(@NotNull HexaDoubleConsumer consumer) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public Shape union(@NotNull Vec3d offset, @NotNull Shape other) {
        List<ObjectObjectImmutablePair<Vec3d, Shape>> mutable = Lists.newArrayList(getShapes());

        mutable.add(new ObjectObjectImmutablePair<>(offset, other));

        return of(mutable);
    }

    @Override
    protected ImmutableObjectSet<Vec3d> getVertexes() {
        LinkedHashSet<Vec3d> vertexes = Sets.newLinkedHashSet();

        for (Pair<Vec3d, Shape> shapePair : shapes) {
            final Vec3d offset = shapePair.first();
            final Shape shape = shapePair.right();

            shape.forEachVertex((x, y, z) -> vertexes.add(offset.add(x, y, z)));
        }

        return ImmutableObjectSet.copyOf(vertexes);
    }

    public ImmutableList<ObjectObjectImmutablePair<Vec3d, Shape>> getShapes() {
        return shapes;
    }

    public static ShapeUnion of(List<ObjectObjectImmutablePair<Vec3d, Shape>> shapes) {
        return new ShapeUnion(ImmutableList.copyOf(shapes));
    }
}

package io.github.svegon.utils.math.geometry.space;

import io.github.svegon.utils.fast.util.objects.immutable.ImmutableObjectSet;
import io.github.svegon.utils.hash.HashUtil;
import io.github.svegon.utils.interfaces.function.DoubleDoubleDoubleTriConsumer;
import io.github.svegon.utils.interfaces.function.HexaDoubleConsumer;
import io.github.svegon.utils.interfaces.function.NonaDoubleConsumer;
import io.github.svegon.utils.math.MathUtil;
import io.github.svegon.utils.math.geometry.space.shape.Shape;
import io.github.svegon.utils.math.geometry.space.shape.Shapes;
import io.github.svegon.utils.math.geometry.vector.Vec3d;
import com.google.common.collect.Sets;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Set;

@Immutable
public class Block3d extends Blocknt<double[], Vec3d, Block3d> implements Shape {
    public static final Block3d ONE_METER_CUBE = new Block3d(1, 1, 1);
    public static final Block3d UNBOUND = create(Vec3d.MAX);

    private final double sizeX;
    private final double sizeY;
    private final double sizeZ;
    private ImmutableObjectSet<Vec3d> vertexes;

    private Block3d(double sizeX, double sizeY, double sizeZ) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
    }

    public Block3d shrink(double x, double y, double z) {
        return create(getSizeX() - x, getSizeY() - y, getSizeZ() - z);
    }

    public Block3d shrink(Vec3d sizes) {
        return shrink(sizes.getX(), sizes.getY(), sizes.getZ());
    }

    public Block3d stretch(double x, double y, double z) {
        return create(getSizeX() + x, getSizeY() + y, getSizeZ() + z);
    }

    public Block3d expand(double x, double y, double z) {
        return stretch(2 * x, 2 * y, 2 * z);
    }

    public Block3d expand(double size) {
        return expand(size, size, size);
    }

    public Block3d contract(double x, double y, double z) {
        return expand(-x, -y, -z);
    }

    public Block3d contract(double size) {
        return contract(size, size, size);
    }

    public Block6d setInSpace(@NotNull Vec3d offset) {
        return Block6d.create(offset, offset.add(getSizeX(), getSizeY(), getSizeZ()));
    }

    public boolean contains(double x, double y, double z) {
        return x < getSizeX() && y < getSizeY() && z < getSizeZ();
    }

    public double getSize(@NotNull Direction.Axis axis) {
        return axis.choose(getSizeX(), getSizeY(), getSizeZ());
    }

    public double getSizeX() {
        return sizeX;
    }

    public double getSizeZ() {
        return sizeZ;
    }

    public double getSizeY() {
        return sizeY;
    }

    @Override
    public Vec3d getMinPos() {
        return Vec3d.ZERO;
    }

    @Override
    public Vec3d getMaxPos() {
        return new Vec3d(getSizeX(), getSizeY(), getSizeZ());
    }

    @Override
    public Block3d stretch(@NotNull Vec3d sizes) {
        return stretch(sizes.getX(), sizes.getY(), sizes.getZ());
    }

    @Override
    public Block3d expand(@NotNull Vec3d sizes) {
        return expand(sizes.getX(), sizes.getY(), sizes.getZ());
    }

    @Override
    public Block3d contract(@NotNull Vec3d sizes) {
        return expand(-sizes.getX(), -sizes.getY(), -sizes.getZ());
    }

    @Override
    public Block3d offset(@NotNull Vec3d values) {
        return this;
    }

    @Override
    public Block3d intersect(@NotNull Block3d other) {
        return create(min(getSizeX(), other.getSizeX()), min(getSizeY(), other.getSizeY()),
                min(getSizeZ(), other.getSizeZ()));
    }

    @Override
    public Block3d union(@NotNull Block3d other) {
        return create(max(getSizeX(), other.getSizeX()), max(getSizeY(), other.getSizeY()),
                max(getSizeZ(), other.getSizeZ()));
    }

    @Override
    public boolean intersects(@NotNull Block3d other) {
        return true;
    }

    @Override
    public boolean intersects(@NotNull Vec3d pos0, @NotNull Vec3d pos1) {
        return contains(min(pos0.getX(), pos1.getX()), min(pos0.getY(), pos1.getY()), min(pos0.getZ(), pos1.getZ()));
    }

    @Override
    public boolean contains(@NotNull Vec3d point) {
        return contains(point.getX(), point.getY(), point.getZ());
    }

    @Override
    public Shape getShape() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof Block3d block6d))
            return false;

        return getSizeX() == block6d.getSizeX() && getSizeY() == block6d.getSizeY() && getSizeZ() == block6d.getSizeZ();
    }

    @Override
    public int hashCode() {
        return HashUtil.hashOrdered(getSizeX(), getSizeY(), getSizeZ());
    }

    @Override
    public ImmutableObjectSet<Vec3d> vertexes() {
        if (vertexes == null) {
            Set<Vec3d> builder = Sets.newLinkedHashSet();

            forEachVertex((x, y, z) -> builder.add(new Vec3d(x, y, z)));

            vertexes = ImmutableObjectSet.copyOf(builder);
        }

        return vertexes;
    }

    @Override
    public void forEachVertex(@NotNull DoubleDoubleDoubleTriConsumer consumer) {
        consumer.consume(0, 0, 0);
        consumer.consume(getSizeX(), 0, 0);
        consumer.consume(getSizeX(), getSizeY(), 0);
        consumer.consume(0, getSizeY(), 0);
        consumer.consume(0, 0, getSizeZ());
        consumer.consume(getSizeX(), 0, getSizeZ());
        consumer.consume(getSizeX(), getSizeY(), getSizeZ());
        consumer.consume(0, getSizeY(), getSizeZ());
    }

    @Override
    public void forEachEdge(@NotNull HexaDoubleConsumer consumer) {
        Shapes.forEachRectEdge(0, 0, getSizeX(), getSizeY(), 0, consumer);
        Shapes.forEachRectEdge(0, 0, getSizeX(), getSizeY(), getSizeZ(), consumer);

        consumer.consume(0, 0, 0, 0, 0, getSizeZ());
        consumer.consume(getSizeX(), 0, 0, getSizeX(), 0, getSizeZ());
        consumer.consume(getSizeX(), getSizeY(), 0, getSizeX(), getSizeY(), getSizeZ());
        consumer.consume(0, getSizeY(), 0, 0, getSizeY(), getSizeZ());
    }

    @Override
    public void forEachSurface(@NotNull NonaDoubleConsumer consumer) {
        consumer.consume(getSizeX(), 0, 0, 0, 0, 0, getSizeX(), 0, getSizeZ());
        consumer.consume(0, 0, 0, 0, 0, getSizeZ(), getSizeX(), 0, getSizeZ());
        consumer.consume(0, 0, 0, 0, getSizeY(), getSizeZ(), 0, 0, getSizeZ());
        consumer.consume(0, 0, 0, 0, 0, getSizeY(), 0, getSizeY(), getSizeZ());
        consumer.consume(0, 0, 0, getSizeX(), 0, 0, getSizeX(), getSizeY(), 0);
        consumer.consume(0, 0, 0, getSizeX(), getSizeY(), 0, 0, getSizeY(), 0);
        consumer.consume(getSizeX(), 0, 0, getSizeX(), 0, getSizeZ(), getSizeX(), getSizeY(), getSizeZ());
        consumer.consume(getSizeX(), 0, 0, getSizeX(), getSizeY(), getSizeZ(), getSizeX(), getSizeY(), 0);
        consumer.consume(getSizeX(), 0, getSizeZ(), 0, 0, getSizeZ(), 0, getSizeY(), getSizeZ());
        consumer.consume(getSizeX(), 0, getSizeZ(), 0, getSizeY(), getSizeZ(),
                getSizeX(), getSizeY(), getSizeZ());
        consumer.consume(0, getSizeY(), 0, getSizeX(), getSizeY(), 0, getSizeX(), getSizeY(), getSizeZ());
        consumer.consume(0, getSizeY(), 0, getSizeX(), getSizeY(), getSizeZ(), 0, getSizeY(), getSizeZ());
    }

    @Override
    public void forEachBox(@NotNull HexaDoubleConsumer consumer) {
        consumer.consume(0, 0, 0, getSizeX(), getSizeY(), getSizeZ());
    }

    @Override
    public Optional<Vec3d> closestPoint(Vec3d relativeTarget) {
        return (Double.doubleToRawLongBits(getSizeX()) | Double.doubleToRawLongBits(getSizeY())
                | Double.doubleToRawLongBits(getSizeZ())) == 0 ? Optional.empty()
                : Optional.of(new Vec3d(MathUtil.clamp(relativeTarget.getX(), 0, getSizeX()),
                MathUtil.clamp(relativeTarget.getY(), 0, getSizeY()),
                MathUtil.clamp(relativeTarget.getZ(), 0, getSizeZ())));
    }

    @Override
    public Shape intersect(final @NotNull Vec3d offset, final @NotNull Shape other) {
        if (other instanceof Block3d b) {
            return intersect(b.shrink(offset));
        }

        return Shape.super.intersect(offset, other);
    }

    public static Block3d create(double x, double y, double z) {
        if (Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(z)) {
            throw new ArithmeticException();
        }

        return new Block3d(Math.abs(x), Math.abs(y), Math.abs(z));
    }

    public static Block3d create(@NotNull Vec3d diagonal) {
        return create(diagonal.getX(), diagonal.getY(), diagonal.getZ());
    }

    public static Block3d of(Block6i block) {
        return new Block3d(block.getSizeX(), block.getSizeY(), block.getSizeZ());
    }

    public static Block3d of(Block6d block) {
        return new Block3d(block.getSizeX(), block.getSizeY(), block.getSizeZ());
    }
}

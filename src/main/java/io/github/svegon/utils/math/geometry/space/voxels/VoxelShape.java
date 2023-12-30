package io.github.svegon.utils.math.geometry.space.voxels;

import io.github.svegon.utils.FunctionUtil;
import io.github.svegon.utils.annotations.TrustedMutableArg;
import io.github.svegon.utils.collections.ArrayUtil;
import io.github.svegon.utils.fast.util.doubles.doubles.FractionalDoubleList;
import io.github.svegon.utils.fast.util.objects.immutable.ImmutableObjectSet;
import io.github.svegon.utils.interfaces.function.HexaDoubleConsumer;
import io.github.svegon.utils.math.MathUtil;
import io.github.svegon.utils.math.geometry.space.Direction;
import io.github.svegon.utils.math.geometry.space.shape.AbstractShape;
import io.github.svegon.utils.math.geometry.space.shape.Shape;
import io.github.svegon.utils.math.geometry.vector.Vec3d;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.Arrays;
import it.unimi.dsi.fastutil.booleans.BooleanBinaryOperator;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleArrays;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.BitSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@Immutable
public class VoxelShape extends AbstractShape {
    public static final VoxelShape EMPTY;
    public static final VoxelShape FULL_CUBE;
    public static final VoxelShape UNBOUND;

    private final int sizeX;
    private final int sizeY;
    private final int sizeZ;
    private final DoubleList xPoints;
    private final DoubleList yPoints;
    private final DoubleList zPoints;
    private final BitSet voxelSet;

    public VoxelShape(final @NotNull VoxelShape model) {
        this(model.sizeX, model.sizeY, model.sizeZ, model.xPoints, model.yPoints, model.zPoints,
                (BitSet) model.voxelSet.clone());
    }

    protected VoxelShape(final @Range(from = 0, to = Arrays.MAX_ARRAY_SIZE - 1) int sizeX,
                         final @Range(from = 0, to = Arrays.MAX_ARRAY_SIZE - 1) int sizeY,
                         final @Range(from = 0, to = Arrays.MAX_ARRAY_SIZE - 1) int sizeZ,
                         final @NotNull @TrustedMutableArg DoubleList xPoints,
                         final @NotNull @TrustedMutableArg DoubleList yPoints,
                         final @NotNull @TrustedMutableArg DoubleList zPoints,
                         final @NotNull @TrustedMutableArg BitSet voxelSet) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
        this.xPoints = xPoints;
        this.yPoints = yPoints;
        this.zPoints = zPoints;
        this.voxelSet = voxelSet;
    }

    protected VoxelShape(final double @TrustedMutableArg [] xPoints, final double @TrustedMutableArg [] yPoints,
                         final double @TrustedMutableArg [] zPoints,
                         final @NotNull @TrustedMutableArg BitSet voxelSet) {
        this(xPoints.length - 1, yPoints.length - 1, zPoints.length - 1,
                ArrayUtil.asList(xPoints), ArrayUtil.asList(yPoints), ArrayUtil.asList(zPoints), voxelSet);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Shape shape)) {
            return false;
        }

        if (shape instanceof VoxelShape voxelShape) {
            if (sizeX == voxelShape.sizeX && sizeY == voxelShape.sizeY) {
                return voxelSet.equals(voxelShape.voxelSet);
            }

            for (int x = 0; x < sizeX; x++) {
                for (int y = 0; y < sizeY; y++) {
                    for (int z = 0; z < sizeZ; z++) {
                        if (contains(x, y, z) != voxelShape.contains(x, y, z)) {
                            return false;
                        }
                    }
                }
            }

            return true;
        }

        return equals(shape);
    }

    @Override
    protected VoxelShape clone() {
        return new VoxelShape(sizeX, sizeY, sizeZ, clone(xPoints), clone(yPoints), clone(zPoints), clone(voxelSet));
    }

    @Override
    public void forEachEdge(@NotNull HexaDoubleConsumer consumer) {
        forEachEdge(consumer, 0);
        forEachEdge(consumer, 2);
        forEachEdge(consumer, 1);
    }

    private void forEachEdge(final @NotNull HexaDoubleConsumer consumer, int cycle) {
        Direction.Axis xAxis = Direction.Axis.X.cycle(cycle);
        Direction.Axis yAxis = Direction.Axis.Y.cycle(cycle);
        Direction.Axis zAxis = Direction.Axis.Z.cycle(cycle);
        DoubleList xAxisPoints = getPoints(xAxis);
        DoubleList yAxisPoints = getPoints(yAxis);
        DoubleList zAxisPoints = getPoints(zAxis);
        int xSize = xAxisPoints.size();
        int ySize = yAxisPoints.size();
        int zSize = zAxisPoints.size();

        for (int x = 0; x <= xSize; ++x) {
            for (int y = 0; y <= ySize; ++y) {
                int n = -1;

                for (int z = 0; z <= zSize; ++z) {
                    int p = 0;
                    int q = 0;

                    for (int r = 0; r < 2; ++r) {
                        for (int s = 0; s < 2; ++s) {
                            int xCoord = x + r - 1;
                            int yCoord = y + s - 1;

                            if (!contains(xAxis.choose(xCoord, yCoord, z), yAxis.choose(xCoord, yCoord, z),
                                    zAxis.choose(xCoord, yCoord, z))) {
                                continue;
                            }

                            ++p;
                            q ^= r ^ s;
                        }
                    }

                    if (0 < p && p < 4 && (q & 1) == 0) {
                        if (n == -1) {
                            n = z;
                        }

                        continue;
                    }

                    if (n == -1) {
                        continue;
                    }

                    consumer.consume(xAxisPoints.getDouble(xAxis.choose(x, y, n)),
                            yAxisPoints.getDouble(yAxis.choose(x, y, n)), zAxisPoints.getDouble(zAxis.choose(x, y, n)),
                            xAxisPoints.getDouble(xAxis.choose(x, y, z)), yAxisPoints.getDouble(yAxis.choose(x, y, z)),
                                    zAxisPoints.getDouble(zAxis.choose(x, y, z)));
                    n = -1;
                }
            }
        }
    }

    @Override
    public void forEachBox(@NotNull HexaDoubleConsumer consumer) {
        VoxelShape copy = clone();

        for (int y = 0; y < sizeY; ++y) {
            for (int x = 0; x < sizeX; ++x) {
                int minSetZ = -1;

                for (int z = 0; z <= this.sizeZ; ++z) {
                    if (copy.contains(x, y, z)) {
                        if (minSetZ < 0) {
                            minSetZ = z;
                        }

                        continue;
                    }

                    if (minSetZ == -1) {
                        continue;
                    }

                    int m = x + 1;
                    int n = y + 1;
                    copy.clearZColumn(x, y, minSetZ, z);

                    while (isColumnFull(m, y, minSetZ, z)) {
                        copy.clearZColumn(m, y, minSetZ, z);
                        ++m;
                    }

                    while (copy.isFlatRectFull(x, m, minSetZ, z, n)) {
                        for (int o = x; o < m; ++o) {
                            copy.clearZColumn(o, n, minSetZ, z);
                        }

                        ++n;
                    }

                    consumer.consume(xPoints.getDouble(x), yPoints.getDouble(y), zPoints.getDouble(minSetZ),
                            xPoints.getDouble(m), yPoints.getDouble(n), zPoints.getDouble(z));
                    minSetZ = -1;
                }
            }
        }
    }

    @Override
    public Shape intersect(@NotNull Vec3d offset, @NotNull Shape other) {
        if (other instanceof VoxelShape shape) {
            return combine(offset, shape, FunctionUtil.and());
        }

        return super.intersect(offset, other);
    }

    @Override
    public Shape union(@NotNull Vec3d offset, @NotNull Shape other) {
        if (other instanceof VoxelShape shape) {
            return combine(offset, shape, FunctionUtil.or());
        }

        return super.union(offset, other);
    }

    @Override
    protected ImmutableObjectSet<Vec3d> getVertexes() {
        Set<Vec3d> builder = Sets.newHashSet();

        forEachEdge((x0, y0, z0, x1, y1, z1) -> {
            builder.add(new Vec3d(x0, y0, z0));
            builder.add(new Vec3d(x1, y1, z1));
        });

        return ImmutableObjectSet.copyOf(builder);
    }

    public Optional<Vec3d> closestPoint(Vec3d relativeTarget) {
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

    public VoxelShape combine(final @NotNull Vec3d offset, @NotNull VoxelShape other,
                              final @NotNull BooleanBinaryOperator function) {
        Preconditions.checkNotNull(function);

        final VoxelShape finalOther;

        if (function.apply(false, false)) {
            throw new IllegalArgumentException();
        }

        if (!offset.equals(Vec3d.ZERO)) {
            finalOther = other.clone();

            finalOther.xPoints.replaceAll(d -> d + offset.getX());
            finalOther.yPoints.replaceAll(d -> d + offset.getY());
            finalOther.zPoints.replaceAll(d -> d + offset.getZ());
        } else if (this == other) {
            return function.apply(true, true) ? this : EMPTY;
        } else {
            finalOther = other;
        }

        boolean first = function.apply(true, false);
        boolean second = function.apply(false, true);

        if (equals((Object) EMPTY)) {
            return second ? other : EMPTY;
        }

        if (other.equals((Object) EMPTY)) {
            return first ? this : EMPTY;
        }

        final CoordPairList xPoints = combine(this.xPoints, other.xPoints, first, second);
        final CoordPairList yPoints = combine(this.yPoints, other.yPoints, first, second);
        final CoordPairList zPoints = combine(this.zPoints, other.zPoints, first, second);
        final int sizeX = xPoints.size() - 1;
        final int sizeY = yPoints.size() - 1;
        final int sizeZ = zPoints.size() - 1;
        final VoxelShape shape = new VoxelShape(sizeX, sizeY, sizeZ, xPoints, yPoints, zPoints,
                new BitSet(sizeX * sizeY * sizeZ));

        xPoints.forEachIndexPair((i, j, k) -> {
            yPoints.forEachIndexPair((l, m, n) -> {
                zPoints.forEachIndexPair((o, p, q) -> {
                    if (function.apply(contains(i, l, o), finalOther.contains(j, m, p))) {
                        shape.voxelSet.set(shape.getIndex(k, n, q));
                    }

                    return true;
                });

                return true;
            });

            return true;
        });

        return shape;
    }

    public VoxelShape simplify() {
        AtomicReference<VoxelShape> result = new AtomicReference<>(EMPTY);

        forEachBox((minX, minY, minZ, maxX, maxY, maxZ) -> result.getAndUpdate(shape -> shape.combine(Vec3d.ZERO,
                cuboid(minX, minY, minZ, maxX, maxY, maxZ), FunctionUtil.or())));

        return result.get();
    }

    public boolean contains(int x, int y, int z) {
        if ((x | y | z) < 0) {
            return false;
        }

        if (y >= sizeY || z >= sizeZ) {
            return false;
        }

        return voxelSet.get(getIndex(x, y, z));
    }

    public boolean contains(int x, int y, int z, int directionCycle) {
        return switch (Math.floorMod(directionCycle, 3)) {
            case 0 -> contains(x, y, z);
            case 1 -> contains(y, z, x);
            case 2 -> contains(z, x, y);
            default -> throw new IllegalStateException();
        };
    }

    public boolean isColumnFull(int minZ, int maxZ, int x, int y) {
        if (x >= this.sizeX || y >= this.sizeY) {
            return false;
        }

        return voxelSet.nextClearBit(getIndex(x, y, minZ)) >= getIndex(x, y, maxZ);
    }

    public int getIndex(int x, int y, int z) {
        return (x * this.sizeY + y) * this.sizeZ + z;
    }

    protected DoubleList getPoints(Direction.Axis axis) {
        return switch (axis) {
            case X -> xPoints;
            case Y -> yPoints;
            case Z -> zPoints;
        };
    }

    private void clearZColumn(int x, int y, int from, int to) {
        int index = (x * this.sizeY + y) * this.sizeZ;
        voxelSet.clear(index + from, index + to);
    }

    private boolean isFlatRectFull(int from, int to, int minZ, int maxZ, int y) {
        for (int x = from; x < to; ++x) {
            if (this.isColumnFull(minZ, maxZ, x, y)) {
                continue;
            }

            return false;
        }

        return true;
    }

    public static CoordPairList combine(DoubleList first, DoubleList second,
                                        boolean includeFirst, boolean includeSecond) {
        int i = first.size() - 1;
        int j = second.size() - 1;

        if (first.getDouble(i) < second.getDouble(0)) {
            return new DisjointCoordPairList(first, second, false);
        }

        if (second.getDouble(j) < first.getDouble(0) - 1.0E-7) {
            return new DisjointCoordPairList(first, second, true);
        }

        if (i == j && first.equals(second)) {
            return new IdenticalCoordPairList(first);
        }

        return new UnsimplifiedCoordPairList(first, second, includeFirst, includeSecond);
    }

    public static DoubleList clone(DoubleList list) {
        if (list instanceof DoubleArrayList doubleArrayList) {
            return doubleArrayList.clone();
        } else if (list instanceof ArrayUtil.DoubleArrayAsList arrayAsList) {
            return arrayAsList.clone();
        } else {
            return ArrayUtil.asList(list.toDoubleArray());
        }
    }

    public static BitSet clone(BitSet bitSet) {
        return (BitSet) bitSet.clone();
    }

    public static int findRequiredBitResolution(double min, double max) {
        if (min < -1.0E-7 || max > 1.0000001) {
            return -1;
        }

        for (int i = 0; i <= 3; ++i) {
            int j = 1 << i;
            double d = min * (double)j;
            double e = max * (double)j;

            if (Math.abs(d - (double)Math.round(d)) >= 1.0E-7 * (double)j
                    || Math.abs(e - (double) Math.round(e)) >= 1.0E-7 * (double) j) {
                continue;
            }

            return i;
        }

        return -1;
    }

    public static VoxelShape create(final DoubleList xPoints, final DoubleList yPoints, final DoubleList zPoints,
                                    BitSet voxelSet) {
        double[] x = xPoints.toDoubleArray();
        double[] y = yPoints.toDoubleArray();
        double[] z = zPoints.toDoubleArray();
        int size = (x.length - 1) * (y.length - 1) * (z.length - 1);
        voxelSet = (BitSet) voxelSet.clone();

        if (voxelSet.size() > size) {
            voxelSet.clear(size, voxelSet.size());
        }

        return new VoxelShape(x, y, z, voxelSet);
    }

    /**
     * creates a positioned block VoxeShape
     * @param minX
     * @param minY
     * @param minZ
     * @param maxX
     * @param maxY
     * @param maxZ
     * @return
     */
    protected static VoxelShape cuboid(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        if (minX > maxX || minY > maxY || minZ > maxZ) {
            throw new IllegalArgumentException("The min values need to be smaller or equals to the max values");
        }

        return cuboidUnchecked(minX, minY, minZ, maxX, maxY, maxZ);
    }

    private static VoxelShape cuboidUnchecked(double minX, double minY, double minZ, double maxX, double maxY,
                                              double maxZ) {
        if (maxX == minX || maxY == minY || maxZ == minZ) {
            return EMPTY;
        }

        int i = findRequiredBitResolution(minX, maxX);
        int j = findRequiredBitResolution(minY, maxY);
        int k = findRequiredBitResolution(minZ, maxZ);
        int combination = i | j | k;

        if (combination < 0) {
            return new VoxelShape(2, 2, 2, DoubleArrayList.wrap(new double[]{minX, maxX}),
                    DoubleArrayList.wrap(new double[]{minY, maxY}), DoubleArrayList.wrap(new double[]{minZ, maxZ}),
                    FULL_CUBE.voxelSet);
        }

        if (combination == 0) {
            return FULL_CUBE;
        }

        int sizeX = 1 << i;
        int sizeY = 1 << j;
        int sizeZ = 1 << k;
        int size = sizeX * sizeY * sizeZ;
        BitSet voxelSet = new BitSet(size);

        voxelSet.set(0, size);

        return new VoxelShape(sizeX, sizeY, sizeZ, new FractionalDoubleList(sizeX), new FractionalDoubleList(sizeY),
                new FractionalDoubleList(sizeZ), voxelSet);
    }

    static {
        double[] cubeList = {1};
        EMPTY = new VoxelShape(DoubleArrays.EMPTY_ARRAY, DoubleArrays.EMPTY_ARRAY, DoubleArrays.EMPTY_ARRAY,
                new BitSet());
        FULL_CUBE = new VoxelShape(cubeList, cubeList, cubeList, new BitSet(1));
        UNBOUND = cuboid(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY,
                Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);

        FULL_CUBE.voxelSet.set(0);
    }
}

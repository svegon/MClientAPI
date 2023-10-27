package io.github.svegon.utils.math.geometry.space.shape;

import io.github.svegon.utils.interfaces.function.HexaDoubleConsumer;
import io.github.svegon.utils.math.geometry.space.Block3d;
import org.jetbrains.annotations.NotNull;

public final class Shapes {
    private Shapes() {
        throw new UnsupportedOperationException();
    }

    public static final Shape EMPTY = new EmptyShape();

    public static Shape empty() {
        return EMPTY;
    }

    public static Block3d fullCube() {
        return Block3d.ONE_METER_CUBE;
    }

    public static void forEachRectEdge(double x1, double y1, double x2, double y2, double z,
                                       final @NotNull HexaDoubleConsumer consumer) {
        consumer.consume(x1, y1, z, x2, y1, z);
        consumer.consume(x2, y1, z, x2, y2, z);
        consumer.consume(x2, y2, z, x1, y2, z);
        consumer.consume(x1, y2, z, x1, y1, z);
    }
}

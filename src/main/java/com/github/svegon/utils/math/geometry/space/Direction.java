package com.github.svegon.utils.math.geometry.space;

import com.github.svegon.utils.math.geometry.vector.Vec3i;

public enum Direction {
    DOWN(Axis.Y, AxisDirection.NEGATIVE) {
        @Override
        public Direction getOpposite() {
            return UP;
        }
    },
    UP(Axis.Y, AxisDirection.POSITIVE) {
        @Override
        public Direction getOpposite() {
            return DOWN;
        }
    },
    NORTH(Axis.Z, AxisDirection.NEGATIVE) {
        @Override
        public Direction getOpposite() {
            return SOUTH;
        }
    },
    SOUTH(Axis.Z, AxisDirection.POSITIVE) {
        @Override
        public Direction getOpposite() {
            return NORTH;
        }
    },
    WEST(Axis.X, AxisDirection.NEGATIVE) {
        @Override
        public Direction getOpposite() {
            return EAST;
        }
    },
    EAST(Axis.X, AxisDirection.POSITIVE) {
        @Override
        public Direction getOpposite() {
            return WEST;
        }
    };

    private static final Direction[] DIRECTIONS = Direction.values();
    private static final Axis[] AXISES = Axis.values();
    private static final AxisDirection[] AXIS_DIRECTIONS = AxisDirection.values();

    private final Vec3i vector;
    private final Axis axis;
    private final AxisDirection axisDirection;

    Direction(Axis axis, AxisDirection axisDirection) {
        this.axis = axis;
        this.axisDirection = axisDirection;
        this.vector = axisDirection == AxisDirection.POSITIVE ? axis.getDimensions() : axis.getDimensions().neg();
    }

    public Vec3i getDimensionVector() {
        return vector;
    }

    public Axis getAxis() {
        return axis;
    }

    public AxisDirection getAxisDirection() {
        return axisDirection;
    }

    public abstract Direction getOpposite();

    public static Direction byId(int ordinal) {
        return DIRECTIONS[ordinal];
    }

    public enum Axis {
        X(new Vec3i(1, 0, 0)) {
            @Override
            public int choose(int x, int y, int z) {
                return x;
            }

            @Override
            public long choose(long x, long y, long z) {
                return x;
            }

            @Override
            public float choose(float x, float y, float z) {
                return x;
            }

            @Override
            public double choose(double x, double y, double z) {
                return x;
            }

            @Override
            public <T> T choose(T x, T y, T z) {
                return x;
            }

            @Override
            public Direction situated(AxisDirection direction) {
                return direction == AxisDirection.NEGATIVE ? WEST : EAST;
            }
        },
        Y(new Vec3i(0, 0, 1)) {
            @Override
            public int choose(int x, int y, int z) {
                return y;
            }

            @Override
            public long choose(long x, long y, long z) {
                return y;
            }

            @Override
            public float choose(float x, float y, float z) {
                return y;
            }

            @Override
            public double choose(double x, double y, double z) {
                return y;
            }

            @Override
            public <T> T choose(T x, T y, T z) {
                return y;
            }

            @Override
            public Direction situated(AxisDirection direction) {
                return direction == AxisDirection.NEGATIVE ? DOWN : UP;
            }
        },
        Z(new Vec3i(0, 0, 1)) {
            @Override
            public int choose(int x, int y, int z) {
                return z;
            }

            @Override
            public long choose(long x, long y, long z) {
                return z;
            }

            @Override
            public float choose(float x, float y, float z) {
                return z;
            }

            @Override
            public double choose(double x, double y, double z) {
                return z;
            }

            @Override
            public <T> T choose(T x, T y, T z) {
                return z;
            }

            @Override
            public Direction situated(AxisDirection direction) {
                return direction == AxisDirection.NEGATIVE ? NORTH : SOUTH;
            }
        };

        private final Vec3i vector;

        Axis(Vec3i vector) {
            this.vector = vector;
        }

        public Vec3i getDimensions() {
            return vector;
        }

        public Axis cycle(int axisCycleDirection) {
            return AXISES[Math.floorMod(ordinal() + axisCycleDirection, AXISES.length)];
        }

        public int cycleDirection(Axis other) {
            int i = ordinal() - other.ordinal();

            if (i == -2) {
                return 1;
            }

            if (i == 2) {
                return -1;
            }

            return i;
        }

        public abstract int choose(int x, int y, int z);

        public abstract long choose(long x, long y, long z);

        public abstract float choose(float x, float y, float z);

        public abstract double choose(double x, double y, double z);

        public abstract <T> T choose(T x, T y, T z);

        public abstract Direction situated(AxisDirection direction);
    }

    public enum AxisDirection {
        POSITIVE(1),
        NEGATIVE(-1);

        private final int multiplier;

        AxisDirection(int multiplier) {
            this.multiplier = multiplier;
        }

        public int getMultiplier() {
            return multiplier;
        }

        public Direction inAxis(Axis axis) {
            return axis.situated(this);
        }
    }
}

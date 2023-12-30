package io.github.svegon.utils.collections.stream;

import io.github.svegon.utils.fast.util.booleans.BooleanPipeline;
import io.github.svegon.utils.fast.util.floats.FloatPipeline;
import io.github.svegon.utils.fast.util.shorts.ShortPipeline;
import io.github.svegon.utils.interfaces.function.Object2ByteFunction;
import io.github.svegon.utils.interfaces.function.Object2CharFunction;
import io.github.svegon.utils.interfaces.function.Object2FloatFunction;
import io.github.svegon.utils.interfaces.function.Object2ShortFunction;
import io.github.svegon.utils.fast.util.bytes.BytePipeline;
import io.github.svegon.utils.fast.util.chars.CharPipeline;
import io.github.svegon.utils.fuck_modifiers.*;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import it.unimi.dsi.fastutil.booleans.BooleanSpliterator;
import it.unimi.dsi.fastutil.bytes.ByteConsumer;
import it.unimi.dsi.fastutil.bytes.ByteSpliterator;
import it.unimi.dsi.fastutil.chars.CharSpliterator;
import it.unimi.dsi.fastutil.floats.FloatSpliterator;
import it.unimi.dsi.fastutil.shorts.ShortConsumer;
import it.unimi.dsi.fastutil.shorts.ShortSpliterator;

import java.util.Objects;
import java.util.function.*;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public final class StreamUtil {
    private StreamUtil() {
        throw new UnsupportedOperationException();
    }

    private static <E> ReferencePipeline<?, E> adapt(Stream<E> stream) {
        return stream instanceof ReferencePipeline pipeline ? pipeline
                : new ReferencePipeline.Head<>(stream.spliterator(),
                StreamOpFlag.fromCharacteristics(stream.spliterator()), stream.isParallel());
    }

    private static BooleanPipeline<?> adapt(BooleanStream stream) {
        return stream instanceof BooleanPipeline pipeline ? pipeline
                : new BooleanPipeline.Head<>(stream.spliterator(),
                StreamOpFlag.fromCharacteristics(stream.spliterator()), stream.isParallel());
    }

    private static BytePipeline<?> adapt(ByteStream stream) {
        return stream instanceof BytePipeline pipeline ? pipeline
                : new BytePipeline.Head<>(stream.spliterator(),
                StreamOpFlag.fromCharacteristics(stream.spliterator()), stream.isParallel());
    }

    private static ShortPipeline<?> adapt(ShortStream stream) {
        return stream instanceof ShortPipeline pipeline ? pipeline
                : new ShortPipeline.Head<>(stream.spliterator(),
                StreamOpFlag.fromCharacteristics(stream.spliterator()), stream.isParallel());
    }

    private static IntPipeline<?> adapt(IntStream stream) {
        return stream instanceof IntPipeline pipeline ? pipeline
                : new IntPipeline.Head<>(stream.spliterator(),
                StreamOpFlag.fromCharacteristics(stream.spliterator()), stream.isParallel());
    }

    private static LongPipeline<?> adapt(LongStream stream) {
        return stream instanceof LongPipeline pipeline ? pipeline
                : new LongPipeline.Head<>(stream.spliterator(),
                StreamOpFlag.fromCharacteristics(stream.spliterator()), stream.isParallel());
    }

    private static CharPipeline<?> adapt(CharStream stream) {
        return stream instanceof CharPipeline pipeline ? pipeline
                : new CharPipeline.Head<>(stream.spliterator(),
                StreamOpFlag.fromCharacteristics(stream.spliterator()), stream.isParallel());
    }

    private static FloatPipeline<?> adapt(FloatStream stream) {
        return stream instanceof FloatPipeline pipeline ? pipeline
                : new FloatPipeline.Head<>(stream.spliterator(),
                StreamOpFlag.fromCharacteristics(stream.spliterator()), stream.isParallel());
    }

    private static DoublePipeline<?> adapt(DoubleStream stream) {
        return stream instanceof DoublePipeline pipeline ? pipeline
                : new DoublePipeline.Head<>(stream.spliterator(),
                StreamOpFlag.fromCharacteristics(stream.spliterator()), stream.isParallel());
    }

    /**
     * Creates a new sequential or parallel {@code BooleanStream} from a
     * {@code Spliterator.BooleanSpliterator}.
     *
     * <p>The spliterator is only traversed, split, or queried for estimated size
     * after the terminal operation of the stream pipeline commences.
     *
     * <p>It is strongly recommended the spliterator report a characteristic of
     * {@code IMMUTABLE} or {@code CONCURRENT}, or be
     * <a href="../Spliterator.html#binding">late-binding</a>.  Otherwise,
     * {@link #booleanStream(Supplier, int, boolean)} should
     * be used to reduce the scope of potential interference with the source.  See
     * <a href="package-summary.html#NonInterference">Non-Interference</a> for
     * more details.
     *
     * @param spliterator A {@code BooleanSpliterator} describing the stream elements
     * @param parallel if {@code true} then the returned stream is a parallel
     *        stream; if {@code false} the returned stream is a sequential
     *        stream.
     * @return a new sequential or parallel {@code BooleanStream}
     */
    public static BooleanStream booleanStream(BooleanSpliterator spliterator, boolean parallel) {
        return new BooleanPipeline.Head<>(spliterator, StreamOpFlag.fromCharacteristics(spliterator), parallel);
    }

    /**
     * Creates a new sequential or parallel {@code BooleanStream} from a
     * {@code Supplier} of {@code BooleanSpliterator}.
     *
     * <p>The {@link Supplier#get()} method will be invoked on the supplier no
     * more than once, and only after the terminal operation of the stream pipeline
     * commences.
     *
     * <p>For spliterators that report a characteristic of {@code IMMUTABLE}
     * or {@code CONCURRENT}, or that are
     * <a href="../Spliterator.html#binding">late-binding</a>, it is likely
     * more efficient to use {@link #booleanStream(BooleanSpliterator, boolean)}
     * instead.
     * <p>The use of a {@code Supplier} in this form provides a level of
     * indirection that reduces the scope of potential interference with the
     * source.  Since the supplier is only invoked after the terminal operation
     * commences, any modifications to the source up to the start of the
     * terminal operation are reflected in the stream result.  See
     * <a href="package-summary.html#NonInterference">Non-Interference</a> for
     * more details.
     *
     * @param supplier A {@code Supplier} of a {@code Spliterator.OfBoolean}
     * @param characteristics Spliterator characteristics of the supplied
     *        {@code BooleanSpliterator}.  The characteristics must be equal to
     *        {@code supplier.get().characteristics()}, otherwise undefined
     *        behavior may occur when terminal operation commences.
     * @param parallel if {@code true} then the returned stream is a parallel
     *        stream; if {@code false} the returned stream is a sequential
     *        stream.
     * @return a new sequential or parallel {@code BooleanStream}
     * @see #booleanStream(BooleanSpliterator, boolean)
     */
    public static BooleanStream booleanStream(Supplier<? extends BooleanSpliterator> supplier,
                                        int characteristics, boolean parallel) {
        return new BooleanPipeline.Head<>(supplier, StreamOpFlag.fromCharacteristics(characteristics), parallel);
    }

    /**
     * Creates a new sequential or parallel {@code ByteStream} from a
     * {@code Spliterator.ByteSpliterator}.
     *
     * <p>The spliterator is only traversed, split, or queried for estimated size
     * after the terminal operation of the stream pipeline commences.
     *
     * <p>It is strongly recommended the spliterator report a characteristic of
     * {@code IMMUTABLE} or {@code CONCURRENT}, or be
     * <a href="../Spliterator.html#binding">late-binding</a>.  Otherwise,
     * {@link #byteStream(Supplier, int, boolean)} should
     * be used to reduce the scope of potential interference with the source.  See
     * <a href="package-summary.html#NonInterference">Non-Interference</a> for
     * more details.
     *
     * @param spliterator A {@code ByteSpliterator} describing the stream elements
     * @param parallel if {@code true} then the returned stream is a parallel
     *        stream; if {@code false} the returned stream is a sequential
     *        stream.
     * @return a new sequential or parallel {@code ByteStream}
     */
    public static ByteStream byteStream(ByteSpliterator spliterator, boolean parallel) {
        return new BytePipeline.Head<>(spliterator, StreamOpFlag.fromCharacteristics(spliterator), parallel);
    }

    /**
     * Creates a new sequential or parallel {@code ByteStream} from a
     * {@code Supplier} of {@code ByteSpliterator}.
     *
     * <p>The {@link Supplier#get()} method will be invoked on the supplier no
     * more than once, and only after the terminal operation of the stream pipeline
     * commences.
     *
     * <p>For spliterators that report a characteristic of {@code IMMUTABLE}
     * or {@code CONCURRENT}, or that are
     * <a href="../Spliterator.html#binding">late-binding</a>, it is likely
     * more efficient to use {@link #byteStream(ByteSpliterator, boolean)}
     * instead.
     * <p>The use of a {@code Supplier} in this form provides a level of
     * indirection that reduces the scope of potential interference with the
     * source.  Since the supplier is only invoked after the terminal operation
     * commences, any modifications to the source up to the start of the
     * terminal operation are reflected in the stream result.  See
     * <a href="package-summary.html#NonInterference">Non-Interference</a> for
     * more details.
     *
     * @param supplier A {@code Supplier} of a {@code Spliterator.OfByte}
     * @param characteristics Spliterator characteristics of the supplied
     *        {@code ByteSpliterator}.  The characteristics must be equal to
     *        {@code supplier.get().characteristics()}, otherwise undefined
     *        behavior may occur when terminal operation commences.
     * @param parallel if {@code true} then the returned stream is a parallel
     *        stream; if {@code false} the returned stream is a sequential
     *        stream.
     * @return a new sequential or parallel {@code ByteStream}
     * @see #byteStream(ByteSpliterator, boolean)
     */
    public static ByteStream byteStream(Supplier<? extends ByteSpliterator> supplier,
                                        int characteristics, boolean parallel) {
        return new BytePipeline.Head<>(supplier, StreamOpFlag.fromCharacteristics(characteristics), parallel);
    }

    /**
     * Creates a new sequential or parallel {@code ShortStream} from a
     * {@code Spliterator.ShortSpliterator}.
     *
     * <p>The spliterator is only traversed, split, or queried for estimated size
     * after the terminal operation of the stream pipeline commences.
     *
     * <p>It is strongly recommended the spliterator report a characteristic of
     * {@code IMMUTABLE} or {@code CONCURRENT}, or be
     * <a href="../Spliterator.html#binding">late-binding</a>.  Otherwise,
     * {@link #shortStream(Supplier, int, boolean)} should
     * be used to reduce the scope of potential interference with the source.  See
     * <a href="package-summary.html#NonInterference">Non-Interference</a> for
     * more details.
     *
     * @param spliterator A {@code ShortSpliterator} describing the stream elements
     * @param parallel if {@code true} then the returned stream is a parallel
     *        stream; if {@code false} the returned stream is a sequential
     *        stream.
     * @return a new sequential or parallel {@code ShortStream}
     */
    public static ShortStream shortStream(ShortSpliterator spliterator, boolean parallel) {
        return new ShortPipeline.Head<>(spliterator, StreamOpFlag.fromCharacteristics(spliterator), parallel);
    }

    /**
     * Creates a new sequential or parallel {@code ShortStream} from a
     * {@code Supplier} of {@code ShortSpliterator}.
     *
     * <p>The {@link Supplier#get()} method will be invoked on the supplier no
     * more than once, and only after the terminal operation of the stream pipeline
     * commences.
     *
     * <p>For spliterators that report a characteristic of {@code IMMUTABLE}
     * or {@code CONCURRENT}, or that are
     * <a href="../Spliterator.html#binding">late-binding</a>, it is likely
     * more efficient to use {@link #shortStream(ShortSpliterator, boolean)}
     * instead.
     * <p>The use of a {@code Supplier} in this form provides a level of
     * indirection that reduces the scope of potential interference with the
     * source.  Since the supplier is only invoked after the terminal operation
     * commences, any modifications to the source up to the start of the
     * terminal operation are reflected in the stream result.  See
     * <a href="package-summary.html#NonInterference">Non-Interference</a> for
     * more details.
     *
     * @param supplier A {@code Supplier} of a {@code Spliterator.OfShort}
     * @param characteristics Spliterator characteristics of the supplied
     *        {@code ShortSpliterator}.  The characteristics must be equal to
     *        {@code supplier.get().characteristics()}, otherwise undefined
     *        behavior may occur when terminal operation commences.
     * @param parallel if {@code true} then the returned stream is a parallel
     *        stream; if {@code false} the returned stream is a sequential
     *        stream.
     * @return a new sequential or parallel {@code ShortStream}
     * @see #shortStream(ShortSpliterator, boolean)
     */
    public static ShortStream shortStream(Supplier<? extends ShortSpliterator> supplier,
                                          int characteristics, boolean parallel) {
        return new ShortPipeline.Head<>(supplier, StreamOpFlag.fromCharacteristics(characteristics), parallel);
    }

    /**
     * Creates a new sequential or parallel {@code CharStream} from a
     * {@code Spliterator.CharSpliterator}.
     *
     * <p>The spliterator is only traversed, split, or queried for estimated size
     * after the terminal operation of the stream pipeline commences.
     *
     * <p>It is strongly recommended the spliterator report a characteristic of
     * {@code IMMUTABLE} or {@code CONCURRENT}, or be
     * <a href="../Spliterator.html#binding">late-binding</a>.  Otherwise,
     * {@link #charStream(Supplier, int, boolean)} should
     * be used to reduce the scope of potential interference with the source.  See
     * <a href="package-summary.html#NonInterference">Non-Interference</a> for
     * more details.
     *
     * @param spliterator A {@code CharSpliterator} describing the stream elements
     * @param parallel if {@code true} then the returned stream is a parallel
     *        stream; if {@code false} the returned stream is a sequential
     *        stream.
     * @return a new sequential or parallel {@code CharStream}
     */
    public static CharStream charStream(CharSpliterator spliterator, boolean parallel) {
        return new CharPipeline.Head<>(spliterator, StreamOpFlag.fromCharacteristics(spliterator), parallel);
    }

    /**
     * Creates a new sequential or parallel {@code CharStream} from a
     * {@code Supplier} of {@code CharSpliterator}.
     *
     * <p>The {@link Supplier#get()} method will be invoked on the supplier no
     * more than once, and only after the terminal operation of the stream pipeline
     * commences.
     *
     * <p>For spliterators that report a characteristic of {@code IMMUTABLE}
     * or {@code CONCURRENT}, or that are
     * <a href="../Spliterator.html#binding">late-binding</a>, it is likely
     * more efficient to use {@link #charStream(CharSpliterator, boolean)}
     * instead.
     * <p>The use of a {@code Supplier} in this form provides a level of
     * indirection that reduces the scope of potential interference with the
     * source.  Since the supplier is only invoked after the terminal operation
     * commences, any modifications to the source up to the start of the
     * terminal operation are reflected in the stream result.  See
     * <a href="package-summary.html#NonInterference">Non-Interference</a> for
     * more details.
     *
     * @param supplier A {@code Supplier} of a {@code Spliterator.OfChar}
     * @param characteristics Spliterator characteristics of the supplied
     *        {@code CharSpliterator}.  The characteristics must be equal to
     *        {@code supplier.get().characteristics()}, otherwise undefined
     *        behavior may occur when terminal operation commences.
     * @param parallel if {@code true} then the returned stream is a parallel
     *        stream; if {@code false} the returned stream is a sequential
     *        stream.
     * @return a new sequential or parallel {@code CharStream}
     * @see #charStream(CharSpliterator, boolean)
     */
    public static CharStream charStream(Supplier<? extends CharSpliterator> supplier,
                                          int characteristics, boolean parallel) {
        return new CharPipeline.Head<>(supplier, StreamOpFlag.fromCharacteristics(characteristics), parallel);
    }

    /**
     * Creates a new sequential or parallel {@code FloatStream} from a
     * {@code Spliterator.FloatSpliterator}.
     *
     * <p>The spliterator is only traversed, split, or queried for estimated size
     * after the terminal operation of the stream pipeline commences.
     *
     * <p>It is strongly recommended the spliterator report a characteristic of
     * {@code IMMUTABLE} or {@code CONCURRENT}, or be
     * <a href="../Spliterator.html#binding">late-binding</a>.  Otherwise,
     * {@link #floatStream(Supplier, int, boolean)} should
     * be used to reduce the scope of potential interference with the source.  See
     * <a href="package-summary.html#NonInterference">Non-Interference</a> for
     * more details.
     *
     * @param spliterator A {@code FloatSpliterator} describing the stream elements
     * @param parallel if {@code true} then the returned stream is a parallel
     *        stream; if {@code false} the returned stream is a sequential
     *        stream.
     * @return a new sequential or parallel {@code FloatStream}
     */
    public static FloatStream floatStream(FloatSpliterator spliterator, boolean parallel) {
        return new FloatPipeline.Head<>(spliterator, StreamOpFlag.fromCharacteristics(spliterator), parallel);
    }

    /**
     * Creates a new sequential or parallel {@code FloatStream} from a
     * {@code Supplier} of {@code FloatSpliterator}.
     *
     * <p>The {@link Supplier#get()} method will be invoked on the supplier no
     * more than once, and only after the terminal operation of the stream pipeline
     * commences.
     *
     * <p>For spliterators that report a characteristic of {@code IMMUTABLE}
     * or {@code CONCURRENT}, or that are
     * <a href="../Spliterator.html#binding">late-binding</a>, it is likely
     * more efficient to use {@link #floatStream(FloatSpliterator, boolean)}
     * instead.
     * <p>The use of a {@code Supplier} in this form provides a level of
     * indirection that reduces the scope of potential interference with the
     * source.  Since the supplier is only invoked after the terminal operation
     * commences, any modifications to the source up to the start of the
     * terminal operation are reflected in the stream result.  See
     * <a href="package-summary.html#NonInterference">Non-Interference</a> for
     * more details.
     *
     * @param supplier A {@code Supplier} of a {@code Spliterator.OfFloat}
     * @param characteristics Spliterator characteristics of the supplied
     *        {@code FloatSpliterator}.  The characteristics must be equal to
     *        {@code supplier.get().characteristics()}, otherwise undefined
     *        behavior may occur when terminal operation commences.
     * @param parallel if {@code true} then the returned stream is a parallel
     *        stream; if {@code false} the returned stream is a sequential
     *        stream.
     * @return a new sequential or parallel {@code FloatStream}
     * @see #floatStream(FloatSpliterator, boolean)
     */
    public static FloatStream floatStream(Supplier<? extends FloatSpliterator> supplier,
                                          int characteristics, boolean parallel) {
        return new FloatPipeline.Head<>(supplier, StreamOpFlag.fromCharacteristics(characteristics), parallel);
    }

    public static <E> BooleanStream mapToBoolean(final Stream<E> stream, final Predicate<? super E> mapper) {
        Objects.requireNonNull(mapper);
        return new BooleanPipeline.StatelessOp<E>(stream instanceof AbstractPipeline pipeline ? pipeline
                : new ReferencePipeline.Head<>(stream.spliterator(),
                StreamOpFlag.fromCharacteristics(stream.spliterator()), stream.isParallel()), StreamShape.REFERENCE,
                StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) {
            @Override
            public Sink<E> opWrapSink(int flags, Sink<Boolean> sink) {
                return new Sink.ChainedReference<>(sink) {
                    @Override
                    public void accept(E u) {
                        downstream.accept(mapper.test(u));
                    }
                };
            }
        };
    }

    public static <E> ByteStream mapToByte(final Stream<E> stream, final Object2ByteFunction<? super E> mapper) {
        Objects.requireNonNull(mapper);
        return new BytePipeline.StatelessOp<E>(stream instanceof AbstractPipeline pipeline ? pipeline
                : new ReferencePipeline.Head<>(stream.spliterator(),
                StreamOpFlag.fromCharacteristics(stream.spliterator()), stream.isParallel()), StreamShape.REFERENCE,
                StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) {
            @Override
            public Sink<E> opWrapSink(int flags, Sink<Byte> sink) {
                return new Sink.ChainedReference<>(sink) {
                    @Override
                    public void accept(E u) {
                        downstream.accept(mapper.applyAsByte(u));
                    }
                };
            }
        };
    }

    public static <E> ShortStream mapToShort(final Stream<E> stream, final Object2ShortFunction<? super E> mapper) {
        Objects.requireNonNull(mapper);
        return new ShortPipeline.StatelessOp<E>(stream instanceof AbstractPipeline pipeline ? pipeline
                : new ReferencePipeline.Head<>(stream.spliterator(),
                StreamOpFlag.fromCharacteristics(stream.spliterator()), stream.isParallel()), StreamShape.REFERENCE,
                StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) {
            @Override
            public Sink<E> opWrapSink(int flags, Sink<Short> sink) {
                return new Sink.ChainedReference<>(sink) {
                    @Override
                    public void accept(E u) {
                        downstream.accept(mapper.applyAsShort(u));
                    }
                };
            }
        };
    }

    public static <E> CharStream mapToChar(final Stream<E> stream, final Object2CharFunction<? super E> mapper) {
        Objects.requireNonNull(mapper);
        return new CharPipeline.StatelessOp<E>(stream instanceof AbstractPipeline pipeline ? pipeline
                : new ReferencePipeline.Head<>(stream.spliterator(),
                StreamOpFlag.fromCharacteristics(stream.spliterator()), stream.isParallel()), StreamShape.REFERENCE,
                StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) {
            @Override
            public Sink<E> opWrapSink(int flags, Sink<Character> sink) {
                return new Sink.ChainedReference<>(sink) {
                    @Override
                    public void accept(E u) {
                        downstream.accept(mapper.applyAsChar(u));
                    }
                };
            }
        };
    }

    public static <E> FloatStream mapToFloat(final Stream<E> stream, final Object2FloatFunction<? super E> mapper) {
        Objects.requireNonNull(mapper);
        return new FloatPipeline.StatelessOp<E>(stream instanceof AbstractPipeline pipeline ? pipeline
                : new ReferencePipeline.Head<>(stream.spliterator(),
                StreamOpFlag.fromCharacteristics(stream.spliterator()), stream.isParallel()), StreamShape.REFERENCE,
                StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) {
            @Override
            public Sink<E> opWrapSink(int flags, Sink<Float> sink) {
                return new Sink.ChainedReference<>(sink) {
                    @Override
                    public void accept(E u) {
                        downstream.accept(mapper.applyAsFloat(u));
                    }
                };
            }
        };
    }

    public static <E> BooleanStream flatMapToBoolean(final Stream<E> stream,
                                               final Function<? super E, ? extends BooleanStream> mapper) {
        Objects.requireNonNull(mapper);
        return new BooleanPipeline.StatelessOp<>(adapt(stream), StreamShape.REFERENCE,
                StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT | StreamOpFlag.NOT_SIZED) {
            @Override
            public Sink<E> opWrapSink(int flags, Sink<Boolean> sink) {
                return new Sink.ChainedReference<E, Boolean>(sink) {
                    // true if cancellationRequested() has been called
                    boolean cancellationRequestedCalled;

                    // cache the consumer to avoid creation on every accepted element
                    BooleanConsumer downstreamAsInt = downstream::accept;

                    @Override
                    public void begin(long size) {
                        downstream.begin(-1);
                    }

                    @Override
                    public void accept(E u) {
                        try (BooleanStream result = mapper.apply(u)) {
                            if (result != null) {
                                if (!cancellationRequestedCalled) {
                                    result.sequential().forEach(downstreamAsInt);
                                } else {
                                    var s = result.sequential().spliterator();
                                    do {
                                    } while (!downstream.cancellationRequested() && s.tryAdvance(downstreamAsInt));
                                }
                            }
                        }
                    }

                    @Override
                    public boolean cancellationRequested() {
                        cancellationRequestedCalled = true;
                        return downstream.cancellationRequested();
                    }
                };
            }
        };
    }

    public static <E> ByteStream flatMapToByte(final Stream<E> stream,
                                               final Function<? super E, ? extends ByteStream> mapper) {
        Objects.requireNonNull(mapper);
        return new BytePipeline.StatelessOp<>(adapt(stream), StreamShape.REFERENCE,
                StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT | StreamOpFlag.NOT_SIZED) {
            @Override
            public Sink<E> opWrapSink(int flags, Sink<Byte> sink) {
                return new Sink.ChainedReference<>(sink) {
                    // true if cancellationRequested() has been called
                    boolean cancellationRequestedCalled;

                    // cache the consumer to avoid creation on every accepted element
                    final ByteConsumer downstreamAsInt = downstream::accept;

                    @Override
                    public void begin(long size) {
                        downstream.begin(-1);
                    }

                    @Override
                    public void accept(E u) {
                        try (ByteStream result = mapper.apply(u)) {
                            if (result != null) {
                                if (!cancellationRequestedCalled) {
                                    result.sequential().forEach(downstreamAsInt);
                                } else {
                                    var s = result.sequential().spliterator();
                                    do {
                                    } while (!downstream.cancellationRequested() && s.tryAdvance(downstreamAsInt));
                                }
                            }
                        }
                    }

                    @Override
                    public boolean cancellationRequested() {
                        cancellationRequestedCalled = true;
                        return downstream.cancellationRequested();
                    }
                };
            }
        };
    }

    public static <E> ShortStream flatMapToShort(final Stream<E> stream,
                                               final Function<? super E, ? extends ShortStream> mapper) {
        Objects.requireNonNull(mapper);
        return new ShortPipeline.StatelessOp<>(adapt(stream), StreamShape.REFERENCE,
                StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT | StreamOpFlag.NOT_SIZED) {
            @Override
            public Sink<E> opWrapSink(int flags, Sink<Short> sink) {
                return new Sink.ChainedReference<>(sink) {
                    // true if cancellationRequested() has been called
                    boolean cancellationRequestedCalled;

                    // cache the consumer to avoid creation on every accepted element
                    final ShortConsumer downstreamAsInt = downstream::accept;

                    @Override
                    public void begin(long size) {
                        downstream.begin(-1);
                    }

                    @Override
                    public void accept(E u) {
                        try (ShortStream result = mapper.apply(u)) {
                            if (result != null) {
                                if (!cancellationRequestedCalled) {
                                    result.sequential().forEach(downstreamAsInt);
                                } else {
                                    var s = result.sequential().spliterator();
                                    do {
                                    } while (!downstream.cancellationRequested() && s.tryAdvance(downstreamAsInt));
                                }
                            }
                        }
                    }

                    @Override
                    public boolean cancellationRequested() {
                        cancellationRequestedCalled = true;
                        return downstream.cancellationRequested();
                    }
                };
            }
        };
    }

    public static <E> Stream<E> flatMapToObj(final IntStream stream,
                                             final IntFunction<? extends Stream<? extends E>> mapper) {
        Objects.requireNonNull(mapper);
        return new ReferencePipeline.StatelessOp<>(adapt(stream), StreamShape.INT_VALUE,
                StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT | StreamOpFlag.NOT_SIZED) {
            @Override
            public Sink<Integer> opWrapSink(int flags, Sink<E> sink) {
                return new Sink.ChainedInt<>(sink) {
                    // true if cancellationRequested() has been called
                    boolean cancellationRequestedCalled;

                    // cache the consumer to avoid creation on every accepted element
                    final Consumer<? super E> downstreamAsInt = downstream;

                    @Override
                    public void begin(long size) {
                        downstream.begin(-1);
                    }

                    @Override
                    public void accept(int u) {
                        try (Stream<? extends E> result = mapper.apply(u)) {
                            if (result != null) {
                                if (!cancellationRequestedCalled) {
                                    result.sequential().forEach(downstreamAsInt);
                                } else {
                                    var s = result.sequential().spliterator();

                                    while (!downstream.cancellationRequested() && s.tryAdvance(downstreamAsInt));
                                }
                            }
                        }
                    }

                    @Override
                    public boolean cancellationRequested() {
                        cancellationRequestedCalled = true;
                        return downstream.cancellationRequested();
                    }
                };
            }
        };
    }

    public static ByteStream flatMapToByte(final IntStream stream, final IntFunction<? extends ByteStream> mapper) {
        Objects.requireNonNull(mapper);
        return new BytePipeline.StatelessOp<>(adapt(stream), StreamShape.INT_VALUE,
                StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT | StreamOpFlag.NOT_SIZED) {
            @Override
            public Sink<Integer> opWrapSink(int flags, Sink<Byte> sink) {
                return new Sink.ChainedInt<>(sink) {
                    // true if cancellationRequested() has been called
                    boolean cancellationRequestedCalled;

                    // cache the consumer to avoid creation on every accepted element
                    final ByteConsumer downstreamAsInt = downstream::accept;

                    @Override
                    public void begin(long size) {
                        downstream.begin(-1);
                    }

                    @Override
                    public void accept(int u) {
                        try (ByteStream result = mapper.apply(u)) {
                            if (result != null) {
                                if (!cancellationRequestedCalled) {
                                    result.sequential().forEach(downstreamAsInt);
                                } else {
                                    var s = result.sequential().spliterator();

                                    while (!downstream.cancellationRequested() && s.tryAdvance(downstreamAsInt));
                                }
                            }
                        }
                    }

                    @Override
                    public boolean cancellationRequested() {
                        cancellationRequestedCalled = true;
                        return downstream.cancellationRequested();
                    }
                };
            }
        };
    }

    public static ShortStream flatMapToShort(final IntStream stream, final IntFunction<? extends ShortStream> mapper) {
        Objects.requireNonNull(mapper);
        return new ShortPipeline.StatelessOp<>(adapt(stream), StreamShape.INT_VALUE,
                StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT | StreamOpFlag.NOT_SIZED) {
            @Override
            public Sink<Integer> opWrapSink(int flags, Sink<Short> sink) {
                return new Sink.ChainedInt<>(sink) {
                    // true if cancellationRequested() has been called
                    boolean cancellationRequestedCalled;

                    // cache the consumer to avoid creation on every accepted element
                    final ShortConsumer downstreamAsInt = downstream::accept;

                    @Override
                    public void begin(long size) {
                        downstream.begin(-1);
                    }

                    @Override
                    public void accept(int u) {
                        try (ShortStream result = mapper.apply(u)) {
                            if (result != null) {
                                if (!cancellationRequestedCalled) {
                                    result.sequential().forEach(downstreamAsInt);
                                } else {
                                    var s = result.sequential().spliterator();

                                    while (!downstream.cancellationRequested() && s.tryAdvance(downstreamAsInt));
                                }
                            }
                        }
                    }

                    @Override
                    public boolean cancellationRequested() {
                        cancellationRequestedCalled = true;
                        return downstream.cancellationRequested();
                    }
                };
            }
        };
    }

    public static LongStream flatMapToLong(final IntStream stream, final IntFunction<? extends LongStream> mapper) {
        Objects.requireNonNull(mapper);

        return new LongPipeline.StatelessOp<>(adapt(stream), StreamShape.INT_VALUE,
                StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT | StreamOpFlag.NOT_SIZED) {
            @Override
            public Sink<Integer> opWrapSink(int flags, Sink<Long> sink) {
                return new Sink.ChainedInt<>(sink) {
                    // true if cancellationRequested() has been called
                    boolean cancellationRequestedCalled;

                    // cache the consumer to avoid creation on every accepted element
                    final LongConsumer downstreamAsInt = downstream::accept;

                    @Override
                    public void begin(long size) {
                        downstream.begin(-1);
                    }

                    @Override
                    public void accept(int u) {
                        try (LongStream result = mapper.apply(u)) {
                            if (result != null) {
                                if (!cancellationRequestedCalled) {
                                    result.sequential().forEach(downstreamAsInt);
                                } else {
                                    var s = result.sequential().spliterator();

                                    while (!downstream.cancellationRequested() && s.tryAdvance(downstreamAsInt));
                                }
                            }
                        }
                    }

                    @Override
                    public boolean cancellationRequested() {
                        cancellationRequestedCalled = true;
                        return downstream.cancellationRequested();
                    }
                };
            }
        };
    }
}

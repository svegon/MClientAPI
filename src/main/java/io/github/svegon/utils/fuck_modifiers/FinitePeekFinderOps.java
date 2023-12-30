package io.github.svegon.utils.fuck_modifiers;

import io.github.svegon.utils.optional.OptionalBoolean;
import io.github.svegon.utils.optional.OptionalByte;

import java.util.Comparator;
import java.util.Spliterator;
import java.util.concurrent.CountedCompleter;
import java.util.function.Function;
import java.util.function.Supplier;

public final class FinitePeekFinderOps {
    private FinitePeekFinderOps() {
        throw new UnsupportedOperationException();
    }

    public static FinitePeekFindOp<Boolean, OptionalBoolean> minBoolean() {
        return FinitePeekFindSink.OfBoolean.MIN_OP;
    }

    public static FinitePeekFindOp<Boolean, OptionalBoolean> maxBoolean() {
        return FinitePeekFindSink.OfBoolean.MAX_OP;
    }

    public static final class FinitePeekFindOp<T, O> implements TerminalOp<T, O> {
        private final StreamShape inputShape;
        private final int opFlags;
        public final Function<? super T, ? extends O> optionalWrapper;
        public final Comparator<? super O> comparator;
        public final O emptyResult;
        public final O optionalPeek;
        public final Supplier<? extends FinitePeekFindSink<T, O>> sinkSupplier;

        FinitePeekFindOp(StreamShape inputShape, Function<? super T, ? extends O> optionalWrapper,
                                Comparator<? super O> comparator, O optionalPeek,
                                Supplier<? extends FinitePeekFindSink<T, O>> sinkSupplier) {
            this.inputShape = inputShape;
            this.opFlags = StreamOpFlag.IS_SHORT_CIRCUIT;
            this.comparator = comparator;
            this.sinkSupplier = sinkSupplier;
            this.emptyResult = optionalWrapper.apply(null);
            this.optionalWrapper = optionalWrapper;
            this.optionalPeek = optionalPeek;
        }

        @Override
        public StreamShape inputShape() {
            return inputShape;
        }

        @Override
        public int getOpFlags() {
            return opFlags;
        }

        @Override
        public <P_IN> O evaluateParallel(PipelineHelper<T> helper, Spliterator<P_IN> spliterator) {
            return new FiniteComparisonReduceTask<>(helper, spliterator, this).invoke();
        }

        @Override
        public <P_IN> O evaluateSequential(PipelineHelper<T> helper, Spliterator<P_IN> spliterator) {
            return helper.wrapAndCopyInto(sinkSupplier.get(), spliterator).get();
        }
    }

    public static abstract class FinitePeekFindSink<T, O> implements TerminalSink<T, O> {
        boolean hasValue;

        FinitePeekFindSink() {

        }

        // no sink for ref since it might have no finite peeks of comparison

        public static final class OfBoolean extends FinitePeekFindSink<Boolean, OptionalBoolean>
                implements Sink.OfBoolean {
            final boolean peek;
            boolean value;

            OfBoolean(boolean peek) {
                this.peek = peek;
            }

            @Override
            public void accept(boolean value) {
                hasValue = true;
                this.value = value;
            }

            @Override
            public boolean cancellationRequested() {
                return value == peek;
            }

            @Override
            public OptionalBoolean get() {
                return hasValue ? OptionalBoolean.of(value) : OptionalBoolean.empty();
            }

            public static final FinitePeekFindOp<Boolean, OptionalBoolean> MIN_OP =
                    new FinitePeekFindOp<>(StreamShape.BOOLEAN_VALUE, OptionalBoolean::of, OptionalBoolean::compareTo,
                            OptionalBoolean.FALSE, () -> new FinitePeekFindSink.OfBoolean(false));
            public static final FinitePeekFindOp<Boolean, OptionalBoolean> MAX_OP =
                    new FinitePeekFindOp<>(StreamShape.BOOLEAN_VALUE, OptionalBoolean::of, OptionalBoolean::compareTo,
                            OptionalBoolean.TRUE, () -> new FinitePeekFindSink.OfBoolean(true));
        }

        public static final class OfByte extends FinitePeekFindSink<Byte, OptionalByte>
                implements Sink.OfByte {
            final byte peek;
            byte value;

            OfByte(byte peek) {
                this.peek = peek;
            }

            @Override
            public void accept(byte value) {
                hasValue = true;
                this.value = value;
            }

            @Override
            public boolean cancellationRequested() {
                return value == peek;
            }

            @Override
            public OptionalByte get() {
                return hasValue ? OptionalByte.of(value) : OptionalByte.empty();
            }

            public static final FinitePeekFindOp<Byte, OptionalByte> MIN_OP =
                    new FinitePeekFindOp<>(StreamShape.BYTE_VALUE, OptionalByte::of, OptionalByte::compareTo,
                            OptionalByte.of(Byte.MIN_VALUE), () -> new FinitePeekFindSink.OfByte(Byte.MIN_VALUE));
            public static final FinitePeekFindOp<Byte, OptionalByte> MAX_OP =
                    new FinitePeekFindOp<>(StreamShape.BOOLEAN_VALUE, OptionalByte::of, OptionalByte::compareTo,
                            OptionalByte.of(Byte.MAX_VALUE), () -> new FinitePeekFindSink.OfByte(Byte.MAX_VALUE));
        }
    }

    public static class FiniteComparisonReduceTask<P_IN, P_OUT, R>
            extends AbstractShortCircuitTask<P_IN, P_OUT, R, FiniteComparisonReduceTask<P_IN, P_OUT, R>> {
        private final FinitePeekFindOp<P_OUT, R> op;

        protected FiniteComparisonReduceTask(PipelineHelper<P_OUT> helper, Spliterator<P_IN> spliterator,
                                             FinitePeekFindOp<P_OUT, R> op) {
            super(helper, spliterator);
            this.op = op;
        }

        protected FiniteComparisonReduceTask(FiniteComparisonReduceTask<P_IN, P_OUT, R> parent,
                                             Spliterator<P_IN> spliterator) {
            super(parent, spliterator);
            this.op = parent.op;
        }

        @Override
        protected R getEmptyResult() {
            return op.emptyResult;
        }

        @Override
        protected FiniteComparisonReduceTask<P_IN, P_OUT, R> makeChild(Spliterator<P_IN> spliterator) {
            return new FiniteComparisonReduceTask<>(this, spliterator);
        }

        @Override
        protected R doLeaf() {
            R ret = helper.wrapAndCopyInto(op.sinkSupplier.get(), spliterator).get();
            R local;

            if (op.optionalPeek.equals(ret)) {
                shortCircuit(op.optionalPeek);
                cancelLaterNodes();
                return ret;
            }

            return op.comparator.compare((local = getLocalResult()), ret) > 0 ? local : ret;
        }

        @Override
        public void onCompletion(CountedCompleter<?> caller) {
            R result;

            if (op.comparator.compare(result = leftChild.join(), getLocalResult()) > 0) {
                setLocalResult(result);
            }

            super.onCompletion(caller);
        }
    }
}

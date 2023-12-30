package io.github.svegon.utils;

import it.unimi.dsi.fastutil.booleans.Boolean2ObjectFunction;
import it.unimi.dsi.fastutil.booleans.BooleanBinaryOperator;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;
import it.unimi.dsi.fastutil.booleans.BooleanUnaryOperator;
import it.unimi.dsi.fastutil.bytes.*;
import it.unimi.dsi.fastutil.chars.*;
import it.unimi.dsi.fastutil.doubles.Double2ObjectFunction;
import it.unimi.dsi.fastutil.floats.Float2ObjectFunction;
import it.unimi.dsi.fastutil.floats.FloatPredicate;
import it.unimi.dsi.fastutil.floats.FloatUnaryOperator;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.longs.Long2ObjectFunction;
import it.unimi.dsi.fastutil.objects.Object2ObjectFunction;
import it.unimi.dsi.fastutil.shorts.*;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.*;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;

public final class FunctionUtil {
    private FunctionUtil() {
        throw new UnsupportedOperationException();
    }

    private static final UnaryOperator<?> IDENTITY_FUNC = UnaryOperator.identity();
    private static final BooleanUnaryOperator BOOLEAN_IDENTITY_FUNC = BooleanUnaryOperator.identity();
    private static final ByteUnaryOperator BYTE_IDENTITY_FUNC = ByteUnaryOperator.identity();
    private static final CharUnaryOperator CHAR_IDENTITY_FUNC = CharUnaryOperator.identity();
    private static final ShortUnaryOperator SHORT_IDENTITY_FUNC = ShortUnaryOperator.identity();
    private static final it.unimi.dsi.fastutil.ints.IntUnaryOperator INT_IDENTITY_FUNC =
            it.unimi.dsi.fastutil.ints.IntUnaryOperator.identity();
    private static final it.unimi.dsi.fastutil.longs.LongUnaryOperator LONG_IDENTITY_FUNC =
            it.unimi.dsi.fastutil.longs.LongUnaryOperator.identity();
    private static final FloatUnaryOperator FLOAT_IDENTITY_FUNC = FloatUnaryOperator.identity();
    private static final it.unimi.dsi.fastutil.doubles.DoubleUnaryOperator DOUBLE_IDENTITY_FUNC =
            it.unimi.dsi.fastutil.doubles.DoubleUnaryOperator.identity();
    private static final BooleanBinaryOperator FALSE_OPERATOR = (a, b) -> false;
    private static final BooleanBinaryOperator TRUE_OPERATOR = (a, b) -> true;
    private static final BooleanBinaryOperator AND = (a, b) -> a && b;
    private static final BooleanBinaryOperator OR = (a, b) -> a || b;
    private static final BooleanBinaryOperator XOR = (a, b) -> a != b;
    private static final BooleanBinaryOperator INVERTED_AND = (a, b) -> !a || !b;
    private static final BooleanBinaryOperator INVERTED_OR = (a, b) -> !a && !b;
    private static final BooleanBinaryOperator EQUAL = (a, b) -> a == b;
    private static final BooleanBinaryOperator IMPLIES = (a, b) -> !a || b;
    private static final BooleanBinaryOperator FIRST_BOOLEAN = (a, b) -> a;
    private static final BooleanBinaryOperator SECOND_BOOLEAN = (a, b) -> b;
    private static final BooleanBinaryOperator NOT_FIRST_BOOLEAN = (a, b) -> !a;
    private static final BooleanBinaryOperator NOT_SECOND_BOOLEAN = (a, b) -> !b;
    private static final UnaryOperator<?> UNSUPPORTED_FUNCTION = (o) -> {
        throw new UnsupportedOperationException();
    };
    private static final Byte2ObjectFunction<Object> UNSUPPORTED_B2L_FUNC = (v) -> {
        throw new UnsupportedOperationException();
    };
    private static final ByteUnaryOperator UNSUPPORTED_B2B_FUNC = (v) -> {
        throw new UnsupportedOperationException();
    };
    private static final Byte2ShortFunction UNSUPPORTED_B2S_FUNC = (v) -> {
        throw new UnsupportedOperationException();
    };
    private static final Byte2IntFunction UNSUPPORTED_B2I_FUNC = (v) -> {
        throw new UnsupportedOperationException();
    };
    private static final Char2ObjectFunction<?> UNSUPPORTED_C2L_FUNC = (v) -> {
        throw new UnsupportedOperationException();
    };
    private static final Char2ByteFunction UNSUPPORTED_C2B_FUNC = (v) -> {
        throw new UnsupportedOperationException();
    };
    private static final CharUnaryOperator UNSUPPORTED_C2C_FUNC = (v) -> {
        throw new UnsupportedOperationException();
    };
    private static final Char2ShortFunction UNSUPPORTED_C2S_FUNC = (v) -> {
        throw new UnsupportedOperationException();
    };
    private static final Char2IntFunction UNSUPPORTED_C2I_FUNC = (v) -> {
        throw new UnsupportedOperationException();
    };
    private static final Short2ObjectFunction<Object> UNSUPPORTED_S2L_FUNC = (v) -> {
        throw new UnsupportedOperationException();
    };
    private static final Short2ByteFunction UNSUPPORTED_S2B_FUNC = (v) -> {
        throw new UnsupportedOperationException();
    };
    private static final Short2CharFunction UNSUPPORTED_S2C_FUNC = (v) -> {
        throw new UnsupportedOperationException();
    };
    private static final ShortUnaryOperator UNSUPPORTED_S2S_FUNC = (v) -> {
        throw new UnsupportedOperationException();
    };
    private static final Short2IntFunction UNSUPPORTED_S2I_FUNC = (v) -> {
        throw new UnsupportedOperationException();
    };
    private static final Short2LongFunction UNSUPPORTED_S2J_FUNC = (v) -> {
        throw new UnsupportedOperationException();
    };
    private static final Short2FloatFunction UNSUPPORTED_S2F_FUNC = (v) -> {
        throw new UnsupportedOperationException();
    };
    private static final Short2DoubleFunction UNSUPPORTED_S2D_FUNC = (v) -> {
        throw new UnsupportedOperationException();
    };
    private static final Int2ObjectFunction<?> UNSUPPORTED_I2L_FUNC = (v) -> {
        throw new UnsupportedOperationException();
    };
    private static final Int2ByteFunction UNSUPPORTED_I2B_FUNC = (v) -> {
        throw new UnsupportedOperationException();
    };
    private static final Int2CharFunction UNSUPPORTED_I2C_FUNC = (v) -> {
        throw new UnsupportedOperationException();
    };
    private static final Int2ShortFunction UNSUPPORTED_I2S_FUNC = (v) -> {
        throw new UnsupportedOperationException();
    };
    private static final Int2LongFunction UNSUPPORTED_I2J_FUNC = (v) -> {
        throw new UnsupportedOperationException();
    };
    private static final Int2FloatFunction UNSUPPORTED_I2F_FUNC = (v) -> {
        throw new UnsupportedOperationException();
    };
    private static final Int2DoubleFunction UNSUPPORTED_I2D_FUNC = (v) -> {
        throw new UnsupportedOperationException();
    };
    private static final Object2ObjectFunction<Object, Object> EMPTY_FUNCTION = (o) -> null;
    private static final Boolean2ObjectFunction<Object> EMPTY_BOOLEAN_FUNCTION = (bl) -> null;
    private static final Byte2ObjectFunction<Object> EMPTY_BYTE_FUNCTION = (b) -> null;
    private static final Char2ObjectFunction<Object> EMPTY_CHAR_FUNCTION = (c) -> null;
    private static final Short2ObjectFunction<Object> EMPTY_SHORT_FUNCTION = (s) -> null;
    private static final Int2ObjectFunction<Object> EMPTY_INT_FUNCTION = (i) -> null;
    private static final Long2ObjectFunction<Object> EMPTY_LONG_FUNCTION = (l) -> null;
    private static final Float2ObjectFunction<Object> EMPTY_FLOAT_FUNCTION = (f) -> null;
    private static final Double2ObjectFunction<Object> EMPTY_DOUBLE_FUNCTION = (d) -> null;
    private static final com.google.common.base.Supplier<Object> EMPTY_SUPPLIER = () -> null;
    private static final Predicate<Object> ALWAYS_FALSE_PREDICATE = new AlwaysFalsePredicate();
    private static final Predicate<Object> ALWAYS_TRUE_PREDICATE = new AlwaysTruePredicate();
    private static final it.unimi.dsi.fastutil.ints.IntPredicate ALWAYS_FALSE_INT_PREDICATE =
            new AlwaysFalseIntPredicate();
    private static final it.unimi.dsi.fastutil.ints.IntPredicate ALWAYS_TRUE_INT_PREDICATE =
            new AlwaysTrueIntPredicate();
    private static final it.unimi.dsi.fastutil.longs.LongPredicate ALWAYS_FALSE_LONG_PREDICATE =
            new AlwaysFalseLongPredicate();
    private static final it.unimi.dsi.fastutil.longs.LongPredicate ALWAYS_TRUE_LONG_PREDICATE =
            new AlwaysTrueLongPredicate();
    private static final FloatPredicate ALWAYS_FALSE_FLOAT_PREDICATE =
            new AlwaysFalseFloatPredicate();
    private static final FloatPredicate ALWAYS_TRUE_FLOAT_PREDICATE =
            new AlwaysTrueFloatPredicate();
    private static final it.unimi.dsi.fastutil.doubles.DoublePredicate ALWAYS_FALSE_DOUBLE_PREDICATE =
            new AlwaysFalseDoublePredicate();
    private static final it.unimi.dsi.fastutil.doubles.DoublePredicate ALWAYS_TRUE_DOUBLE_PREDICATE =
            new AlwaysTrueDoublePredicate();
    private static final BooleanSupplier FALSE = () -> false;
    private static final BooleanSupplier TRUE = () -> true;
    private static final BinaryOperator<Collection<Object>> COLLECTION_COMBINER =
            (c1, c2) -> {c1.addAll(c2); return c1;};
    private static final BinaryOperator<BooleanCollection> BOOLEAN_COLLECTION_COMBINER =
            (c1, c2) -> {c1.addAll(c2); return c1;};
    private static final BinaryOperator<ByteCollection> BYTE_COLLECTION_COMBINER =
            (c1, c2) -> {c1.addAll(c2); return c1;};
    private static final BinaryOperator<ShortCollection> SHORT_COLLECTION_COMBINER =
            (c1, c2) -> {c1.addAll(c2); return c1;};
    private static final BinaryOperator<CharCollection> CHAR_COLLECTION_COMBINER =
            (c1, c2) -> {c1.addAll(c2); return c1;};
    private static final BinaryOperator<AtomicInteger> ATOMIC_INTEGER_COMBINER = (a, b) -> {
        a.getAndAdd(b.get());
        return a;
    };

    @SuppressWarnings("unchecked")
    public static <T, R> Function<T, R> identityFunction() {
        return (Function<T, R>) IDENTITY_FUNC;
    }

    @SuppressWarnings("unchecked")
    public static <T> UnaryOperator<T> identityOperator() {
        return (UnaryOperator<T>) IDENTITY_FUNC;
    }

    public static BooleanUnaryOperator booleanIdentityOperator() {
        return BOOLEAN_IDENTITY_FUNC;
    }

    public static ByteUnaryOperator byteIdentityOperator() {
        return BYTE_IDENTITY_FUNC;
    }

    public static CharUnaryOperator charIdentityOperator() {
        return CHAR_IDENTITY_FUNC;
    }

    public static ShortUnaryOperator shortIdentityOperator() {
        return SHORT_IDENTITY_FUNC;
    }

    public static IntUnaryOperator intIdentityOperator() {
        return INT_IDENTITY_FUNC;
    }

    public static LongUnaryOperator longIdentityOperator() {
        return LONG_IDENTITY_FUNC;
    }

    public static FloatUnaryOperator floatIdentityOperator() {
        return FLOAT_IDENTITY_FUNC;
    }

    public static DoubleUnaryOperator doubleIdentityOperator() {
        return DOUBLE_IDENTITY_FUNC;
    }

    @SuppressWarnings("unchecked")
    public static <T, R> Function<T, R> unsupportedFunction() {
        return (Function<T, R>) UNSUPPORTED_FUNCTION;
    }

    @SuppressWarnings("unchecked")
    public static <T> UnaryOperator<T> unsupportedUnaryOperator() {
        return (UnaryOperator<T>) UNSUPPORTED_FUNCTION;
    }

    @SuppressWarnings("unchecked")
    public static <T> Byte2ObjectFunction<T> unsupportedByte2ObjectFunction() {
        return (Byte2ObjectFunction<T>) UNSUPPORTED_B2L_FUNC;
    }

    public static ByteUnaryOperator unsupportedByte2ByteFunction() {
        return UNSUPPORTED_B2B_FUNC;
    }

    public static Byte2ShortFunction unsupportedByte2ShortFunction() {
        return UNSUPPORTED_B2S_FUNC;
    }

    public static Byte2IntFunction unsupportedByte2IntFunction() {
        return UNSUPPORTED_B2I_FUNC;
    }

    @SuppressWarnings("unchecked")
    public static <T> Char2ObjectFunction<T> unsupportedChar2ObjectFunction() {
        return (Char2ObjectFunction<T>) UNSUPPORTED_C2L_FUNC;
    }

    public static Char2ByteFunction unsupportedChar2ByteFunction() {
        return UNSUPPORTED_C2B_FUNC;
    }

    public static Char2ShortFunction unsupportedChar2ShortFunction() {
        return UNSUPPORTED_C2S_FUNC;
    }

    public static CharUnaryOperator unsupportedChar2CharFunction() {
        return UNSUPPORTED_C2C_FUNC;
    }

    public static Char2IntFunction unsupportedChar2IntFunction() {
        return UNSUPPORTED_C2I_FUNC;
    }

    @SuppressWarnings("unchecked")
    public static <T> Short2ObjectFunction<T> unsupportedShort2ObjectFunction() {
        return (Short2ObjectFunction<T>) UNSUPPORTED_S2L_FUNC;
    }

    public static Short2ByteFunction unsupportedShort2ByteFunction() {
        return UNSUPPORTED_S2B_FUNC;
    }

    public static Short2CharFunction unsupportedShort2CharFunction() {
        return UNSUPPORTED_S2C_FUNC;
    }

    public static ShortUnaryOperator unsupportedShort2ShortFunction() {
        return UNSUPPORTED_S2S_FUNC;
    }

    public static Short2IntFunction unsupportedShort2IntFunction() {
        return UNSUPPORTED_S2I_FUNC;
    }

    public static Short2LongFunction unsupportedShort2LongFunction() {
        return UNSUPPORTED_S2J_FUNC;
    }

    public static Short2FloatFunction unsupportedShort2FloatFunction() {
        return UNSUPPORTED_S2F_FUNC;
    }

    public static Short2DoubleFunction unsupportedShort2DoubleFunction() {
        return UNSUPPORTED_S2D_FUNC;
    }

    @SuppressWarnings("unchecked")
    public static <T> Int2ObjectFunction<T> unsupportedInt2ObjectFunction() {
        return (Int2ObjectFunction<T>) UNSUPPORTED_I2L_FUNC;
    }

    public static Int2ByteFunction unsupportedInt2ByteFunction() {
        return UNSUPPORTED_I2B_FUNC;
    }

    public static Int2CharFunction unsupportedInt2CharFunction() {
        return UNSUPPORTED_I2C_FUNC;
    }

    public static Int2ShortFunction unsupportedInt2ShortFunction() {
        return UNSUPPORTED_I2S_FUNC;
    }

    public static IntUnaryOperator unsupportedInt2IntFunction() {
        return UNSUPPORTED_I2S_FUNC;
    }

    public static Int2LongFunction unsupportedInt2LongFunction() {
        return UNSUPPORTED_I2J_FUNC;
    }

    public static Int2FloatFunction unsupportedInt2FloatFunction() {
        return UNSUPPORTED_I2F_FUNC;
    }

    public static Int2DoubleFunction unsupportedInt2DoubleFunction() {
        return UNSUPPORTED_I2D_FUNC;
    }

    @SuppressWarnings("unchecked")
    public static <T, R> Function<T, R> emptyFunction() {
        return (Function<T, R>) EMPTY_FUNCTION;
    }

    @SuppressWarnings("unchecked")
    public static <R> Boolean2ObjectFunction<R> emptyBooleanFunction() {
        return (Boolean2ObjectFunction<R>) EMPTY_BOOLEAN_FUNCTION;
    }

    @SuppressWarnings("unchecked")
    public static <R> Byte2ObjectFunction<R> emptyByteFunction() {
        return (Byte2ObjectFunction<R>) EMPTY_BYTE_FUNCTION;
    }

    @SuppressWarnings("unchecked")
    public static <R> Char2ObjectFunction<R> emptyCharFunction() {
        return (Char2ObjectFunction<R>) EMPTY_CHAR_FUNCTION;
    }

    @SuppressWarnings("unchecked")
    public static <R> Short2ObjectFunction<R> emptyShortFunction() {
        return (Short2ObjectFunction<R>) EMPTY_SHORT_FUNCTION;
    }

    @SuppressWarnings("unchecked")
    public static <R> Int2ObjectFunction<R> emptyIntFunction() {
        return (Int2ObjectFunction<R>) EMPTY_INT_FUNCTION;
    }

    @SuppressWarnings("unchecked")
    public static <R> Long2ObjectFunction<R> emptyLongFunction() {
        return (Long2ObjectFunction<R>) EMPTY_LONG_FUNCTION;
    }

    @SuppressWarnings("unchecked")
    public static <R> Float2ObjectFunction<R> emptyFloatFunction() {
        return (Float2ObjectFunction<R>) EMPTY_FLOAT_FUNCTION;
    }

    @SuppressWarnings("unchecked")
    public static <R> Double2ObjectFunction<R> emptyDoubleFunction() {
        return (Double2ObjectFunction<R>) EMPTY_DOUBLE_FUNCTION;
    }

    @SuppressWarnings("unchecked")
    public static <R> com.google.common.base.Supplier<R> emptySupplier() {
        return (com.google.common.base.Supplier<R>) EMPTY_SUPPLIER;
    }

    @SuppressWarnings("unchecked")
    public static <T> Predicate<T> alwaysFalsePredicate() {
        return (Predicate<T>) ALWAYS_FALSE_PREDICATE;
    }

    @SuppressWarnings("unchecked")
    public static <T> Predicate<T> alwaysTruePredicate() {
        return (Predicate<T>) ALWAYS_TRUE_PREDICATE;
    }

    public static it.unimi.dsi.fastutil.ints.IntPredicate alwaysFalseIntPredicate() {
        return ALWAYS_FALSE_INT_PREDICATE;
    }

    public static it.unimi.dsi.fastutil.ints.IntPredicate alwaysTrueIntPredicate() {
        return ALWAYS_TRUE_INT_PREDICATE;
    }

    public static it.unimi.dsi.fastutil.longs.LongPredicate alwaysFalseLongPredicate() {
        return ALWAYS_FALSE_LONG_PREDICATE;
    }

    public static it.unimi.dsi.fastutil.longs.LongPredicate alwaysTrueLongPredicate() {
        return ALWAYS_TRUE_LONG_PREDICATE;
    }

    public static FloatPredicate alwaysFalseFloatPredicate() {
        return ALWAYS_FALSE_FLOAT_PREDICATE;
    }

    public static FloatPredicate alwaysTrueFloatPredicate() {
        return ALWAYS_TRUE_FLOAT_PREDICATE;
    }

    public static it.unimi.dsi.fastutil.doubles.DoublePredicate alwaysFalseDoublePredicate() {
        return ALWAYS_FALSE_DOUBLE_PREDICATE;
    }

    public static it.unimi.dsi.fastutil.doubles.DoublePredicate alwaysTrueDoublePredicate() {
        return ALWAYS_TRUE_DOUBLE_PREDICATE;
    }

    public static BooleanBinaryOperator falseOperator() {
        return FALSE_OPERATOR;
    }

    public static BooleanBinaryOperator trueOperator() {
        return TRUE_OPERATOR;
    }

    public static BooleanBinaryOperator and() {
        return AND;
    }

    public static BooleanBinaryOperator or() {
        return OR;
    }

    public static BooleanBinaryOperator xor() {
        return XOR;
    }

    public static BooleanBinaryOperator invertedAnd() {
        return INVERTED_AND;
    }

    public static BooleanBinaryOperator invertedOr() {
        return INVERTED_OR;
    }

    public static BooleanBinaryOperator booleansEqual() {
        return EQUAL;
    }

    public static BooleanBinaryOperator implies() {
        return IMPLIES;
    }

    public static BooleanBinaryOperator firstBooleanOfTwo() {
        return FIRST_BOOLEAN;
    }

    public static BooleanBinaryOperator secondBooleanOfTwo() {
        return SECOND_BOOLEAN;
    }

    public static BooleanBinaryOperator notFirstBooleanOfTwo() {
        return NOT_FIRST_BOOLEAN;
    }

    public static BooleanBinaryOperator notSecondBooleanOfTwo() {
        return NOT_SECOND_BOOLEAN;
    }

    @SuppressWarnings("unchecked")
    public static <E, C extends Collection<E>> BinaryOperator<C> collectionCombiner() {
        return (BinaryOperator<C>) COLLECTION_COMBINER;
    }

    @SuppressWarnings("unchecked")
    public static <C extends BooleanCollection> BinaryOperator<C> booleanCollectionCombiner() {
        return (BinaryOperator<C>) BOOLEAN_COLLECTION_COMBINER;
    }

    @SuppressWarnings("unchecked")
    public static <C extends ByteCollection> BinaryOperator<C> byteCollectionCombiner() {
        return (BinaryOperator<C>) BYTE_COLLECTION_COMBINER;
    }

    @SuppressWarnings("unchecked")
    public static <C extends CharCollection> BinaryOperator<C> charCollectionCombiner() {
        return (BinaryOperator<C>) CHAR_COLLECTION_COMBINER;
    }

    @SuppressWarnings("unchecked")
    public static <C extends ShortCollection> BinaryOperator<C> shortCollectionCombiner() {
        return (BinaryOperator<C>) SHORT_COLLECTION_COMBINER;
    }

    public static BinaryOperator<AtomicInteger> atomicIntegerCombiner() {
        return ATOMIC_INTEGER_COMBINER;
    }

    private static class AlwaysFalsePredicate implements Predicate<Object> {
        @Override
        public boolean test(Object o) {
            return false;
        }

        @NotNull
        @Override
        public Predicate<Object> and(@NotNull Predicate<? super Object> other) {
            return this;
        }

        @NotNull
        @Override
        public Predicate<Object> negate() {
            return ALWAYS_TRUE_PREDICATE;
        }

        @NotNull
        @Override
        public Predicate<Object> or(@NotNull Predicate<? super Object> other) {
            return other;
        }
    }

    private static class AlwaysTruePredicate implements Predicate<Object> {
        @Override
        public boolean test(Object o) {
            return true;
        }

        @NotNull
        @Override
        public Predicate<Object> and(@NotNull Predicate<? super Object> other) {
            return other;
        }

        @NotNull
        @Override
        public Predicate<Object> negate() {
            return ALWAYS_FALSE_PREDICATE;
        }

        @NotNull
        @Override
        public Predicate<Object> or(@NotNull Predicate<? super Object> other) {
            return this;
        }
    }

    private static class AlwaysFalseIntPredicate implements it.unimi.dsi.fastutil.ints.IntPredicate {
        @Override
        public boolean test(int value) {
            return false;
        }

        @Override
        public it.unimi.dsi.fastutil.ints.IntPredicate and(@NotNull IntPredicate other) {
            return this;
        }

        @Override
        public it.unimi.dsi.fastutil.ints.IntPredicate negate() {
            return ALWAYS_TRUE_INT_PREDICATE;
        }

        @Override
        public it.unimi.dsi.fastutil.ints.IntPredicate or(@NotNull IntPredicate other) {
            return other instanceof it.unimi.dsi.fastutil.ints.IntPredicate
                    ? (it.unimi.dsi.fastutil.ints.IntPredicate) other : other::test;
        }
    }

    private static class AlwaysTrueIntPredicate implements it.unimi.dsi.fastutil.ints.IntPredicate {
        @Override
        public boolean test(int value) {
            return true;
        }

        @Override
        public it.unimi.dsi.fastutil.ints.IntPredicate and(@NotNull IntPredicate other) {
            return other instanceof it.unimi.dsi.fastutil.ints.IntPredicate
                    ? (it.unimi.dsi.fastutil.ints.IntPredicate) other : other::test;
        }

        @Override
        public it.unimi.dsi.fastutil.ints.IntPredicate negate() {
            return alwaysFalseIntPredicate();
        }

        @Override
        public it.unimi.dsi.fastutil.ints.IntPredicate or(@NotNull IntPredicate other) {
            return this;
        }
    }

    private static class AlwaysFalseLongPredicate implements it.unimi.dsi.fastutil.longs.LongPredicate {
        @Override
        public boolean test(long value) {
            return false;
        }

        @Override
        public it.unimi.dsi.fastutil.longs.LongPredicate and(@NotNull LongPredicate other) {
            return this;
        }

        @Override
        public it.unimi.dsi.fastutil.longs.LongPredicate negate() {
            return alwaysTrueLongPredicate();
        }

        @Override
        public it.unimi.dsi.fastutil.longs.LongPredicate or(@NotNull LongPredicate other) {
            return other instanceof it.unimi.dsi.fastutil.longs.LongPredicate
                    ? (it.unimi.dsi.fastutil.longs.LongPredicate) other : other::test;
        }
    }

    private static class AlwaysTrueLongPredicate implements it.unimi.dsi.fastutil.longs.LongPredicate {
        @Override
        public boolean test(long value) {
            return true;
        }

        @Override
        public it.unimi.dsi.fastutil.longs.LongPredicate and(@NotNull LongPredicate other) {
            return other instanceof it.unimi.dsi.fastutil.longs.LongPredicate
                    ? (it.unimi.dsi.fastutil.longs.LongPredicate) other : other::test;
        }

        @Override
        public it.unimi.dsi.fastutil.longs.LongPredicate negate() {
            return alwaysFalseLongPredicate();
        }

        @Override
        public it.unimi.dsi.fastutil.longs.LongPredicate or(@NotNull LongPredicate other) {
            return this;
        }
    }

    private static class AlwaysFalseFloatPredicate implements FloatPredicate {
        @Override
        public boolean test(float value) {
            return false;
        }

        @Override
        public FloatPredicate and(@NotNull FloatPredicate other) {
            return this;
        }

        @Override
        public FloatPredicate negate() {
            return alwaysTrueFloatPredicate();
        }

        @Override
        public FloatPredicate or(@NotNull FloatPredicate other) {
            return other;
        }
    }

    private static class AlwaysTrueFloatPredicate implements FloatPredicate {
        @Override
        public boolean test(float value) {
            return true;
        }

        @Override
        public FloatPredicate and(@NotNull FloatPredicate other) {
            return other;
        }

        @Override
        public FloatPredicate negate() {
            return alwaysFalseFloatPredicate();
        }

        @Override
        public FloatPredicate or(@NotNull FloatPredicate other) {
            return this;
        }
    }

    private static class AlwaysFalseDoublePredicate implements it.unimi.dsi.fastutil.doubles.DoublePredicate {
        @Override
        public boolean test(double value) {
            return false;
        }

        @Override
        public it.unimi.dsi.fastutil.doubles.DoublePredicate and(@NotNull DoublePredicate other) {
            return this;
        }

        @Override
        public it.unimi.dsi.fastutil.doubles.DoublePredicate negate() {
            return alwaysTrueDoublePredicate();
        }

        @Override
        public it.unimi.dsi.fastutil.doubles.DoublePredicate or(@NotNull DoublePredicate other) {
            return other instanceof it.unimi.dsi.fastutil.doubles.DoublePredicate
                    ? (it.unimi.dsi.fastutil.doubles.DoublePredicate) other : other::test;
        }
    }

    private static class AlwaysTrueDoublePredicate implements it.unimi.dsi.fastutil.doubles.DoublePredicate {
        @Override
        public boolean test(double value) {
            return true;
        }

        @Override
        public it.unimi.dsi.fastutil.doubles.DoublePredicate and(@NotNull DoublePredicate other) {
            return other instanceof it.unimi.dsi.fastutil.doubles.DoublePredicate
                    ? (it.unimi.dsi.fastutil.doubles.DoublePredicate) other : other::test;
        }

        @Override
        public it.unimi.dsi.fastutil.doubles.DoublePredicate negate() {
            return alwaysFalseDoublePredicate();
        }

        @Override
        public it.unimi.dsi.fastutil.doubles.DoublePredicate or(@NotNull DoublePredicate other) {
            return this;
        }
    }
}

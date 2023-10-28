package io.github.svegon.utils.collections.iteration;

import io.github.svegon.utils.collections.ArrayUtil;
import io.github.svegon.utils.fast.util.booleans.OnNextComputeBooleanIterator;
import io.github.svegon.utils.fast.util.bytes.OnNextComputeByteIterator;
import io.github.svegon.utils.fast.util.bytes.transform.bytes.B2BTransformingIterator;
import io.github.svegon.utils.fast.util.bytes.transform.chars.C2BTransformingIterator;
import io.github.svegon.utils.fast.util.bytes.transform.objects.L2BTransformingIterator;
import io.github.svegon.utils.fast.util.bytes.transform.shorts.S2BTransformingIterator;
import io.github.svegon.utils.fast.util.chars.OnNextComputeCharIterator;
import io.github.svegon.utils.fast.util.chars.transform.booleans.Z2CTransformingBidirectionalIterator;
import io.github.svegon.utils.fast.util.chars.transform.booleans.Z2CTransformingIterator;
import io.github.svegon.utils.fast.util.chars.transform.booleans.Z2CTransformingListIterator;
import io.github.svegon.utils.fast.util.chars.transform.bytes.B2CTransformingBidirectionalIterator;
import io.github.svegon.utils.fast.util.chars.transform.bytes.B2CTransformingIterator;
import io.github.svegon.utils.fast.util.chars.transform.bytes.B2CTransformingListIterator;
import io.github.svegon.utils.fast.util.chars.transform.chars.C2CTransformingBidirectionalIterator;
import io.github.svegon.utils.fast.util.chars.transform.chars.C2CTransformingIterator;
import io.github.svegon.utils.fast.util.chars.transform.chars.C2CTransformingListIterator;
import io.github.svegon.utils.fast.util.chars.transform.ints.I2CTransformingBidirectionalIterator;
import io.github.svegon.utils.fast.util.chars.transform.ints.I2CTransformingIterator;
import io.github.svegon.utils.fast.util.chars.transform.ints.I2CTransformingListIterator;
import io.github.svegon.utils.fast.util.chars.transform.objects.L2CTransformingIterator;
import io.github.svegon.utils.fast.util.chars.transform.objects.L2CTransformingListIterator;
import io.github.svegon.utils.fast.util.chars.transform.shorts.S2CTransformingBidirectionalIterator;
import io.github.svegon.utils.fast.util.chars.transform.shorts.S2CTransformingIterator;
import io.github.svegon.utils.fast.util.chars.transform.shorts.S2CTransformingListIterator;
import io.github.svegon.utils.fast.util.doubles.OnNextComputeDoubleIterator;
import io.github.svegon.utils.fast.util.floats.OnNextComputeFloatIterator;
import io.github.svegon.utils.fast.util.ints.OnNextComputeIntIterator;
import io.github.svegon.utils.fast.util.ints.transform.booleans.Z2ITransformingIterator;
import io.github.svegon.utils.fast.util.ints.transform.bytes.B2ITransformingIterator;
import io.github.svegon.utils.fast.util.ints.transform.chars.C2ITransformingIterator;
import io.github.svegon.utils.fast.util.ints.transform.ints.I2ITransformingIterator;
import io.github.svegon.utils.fast.util.ints.transform.objects.L2ITransformingBidirectionalIterator;
import io.github.svegon.utils.fast.util.ints.transform.objects.L2ITransformingIterator;
import io.github.svegon.utils.fast.util.ints.transform.objects.L2ITransformingListIterator;
import io.github.svegon.utils.fast.util.ints.transform.shorts.S2ITransformingIterator;
import io.github.svegon.utils.fast.util.longs.OnNextComputeLongIterator;
import io.github.svegon.utils.fast.util.objects.OnNextComputeObjectIterator;
import io.github.svegon.utils.fast.util.objects.transform.booleans.Z2LTransformingBidirectionalIterator;
import io.github.svegon.utils.fast.util.objects.transform.booleans.Z2LTransformingIterator;
import io.github.svegon.utils.fast.util.objects.transform.booleans.Z2LTransformingListIterator;
import io.github.svegon.utils.fast.util.objects.transform.bytes.B2LTransformingBidirectionalIterator;
import io.github.svegon.utils.fast.util.objects.transform.bytes.B2LTransformingIterator;
import io.github.svegon.utils.fast.util.objects.transform.bytes.B2LTransformingListIterator;
import io.github.svegon.utils.fast.util.objects.transform.chars.C2LTransformingBidirectionalIterator;
import io.github.svegon.utils.fast.util.objects.transform.chars.C2LTransformingIterator;
import io.github.svegon.utils.fast.util.objects.transform.chars.C2LTransformingListIterator;
import io.github.svegon.utils.fast.util.objects.transform.ints.I2LTransformingBidirectionalIterator;
import io.github.svegon.utils.fast.util.objects.transform.ints.I2LTransformingIterator;
import io.github.svegon.utils.fast.util.objects.transform.ints.I2LTransformingListIterator;
import io.github.svegon.utils.fast.util.objects.transform.objects.L2LTransformingIterator;
import io.github.svegon.utils.fast.util.objects.transform.objects.L2LTransformingListIterator;
import io.github.svegon.utils.fast.util.objects.transform.shorts.S2LTransformingBidirectionalIterator;
import io.github.svegon.utils.fast.util.objects.transform.shorts.S2LTransformingIterator;
import io.github.svegon.utils.fast.util.objects.transform.shorts.S2LTransformingListIterator;
import io.github.svegon.utils.fast.util.shorts.OnNextComputeShortIterator;
import io.github.svegon.utils.fast.util.shorts.transform.booleans.Z2STransformingBidirectionalIterator;
import io.github.svegon.utils.fast.util.shorts.transform.booleans.Z2STransformingIterator;
import io.github.svegon.utils.fast.util.shorts.transform.booleans.Z2STransformingListIterator;
import io.github.svegon.utils.fast.util.shorts.transform.bytes.B2STransformingBidirectionalIterator;
import io.github.svegon.utils.fast.util.shorts.transform.bytes.B2STransformingIterator;
import io.github.svegon.utils.fast.util.shorts.transform.bytes.B2STransformingListIterator;
import io.github.svegon.utils.fast.util.shorts.transform.chars.C2STransformingBidirectionalIterator;
import io.github.svegon.utils.fast.util.shorts.transform.chars.C2STransformingIterator;
import io.github.svegon.utils.fast.util.shorts.transform.chars.C2STransformingListIterator;
import io.github.svegon.utils.fast.util.shorts.transform.ints.I2STransformingBidirectionalIterator;
import io.github.svegon.utils.fast.util.shorts.transform.ints.I2STransformingIterator;
import io.github.svegon.utils.fast.util.shorts.transform.ints.I2STransformingListIterator;
import io.github.svegon.utils.fast.util.shorts.transform.objects.L2STransformingIterator;
import io.github.svegon.utils.fast.util.shorts.transform.shorts.S2STransformingBidirectionalIterator;
import io.github.svegon.utils.fast.util.shorts.transform.shorts.S2STransformingIterator;
import io.github.svegon.utils.fast.util.shorts.transform.shorts.S2STransformingListIterator;
import io.github.svegon.utils.interfaces.function.*;
import com.google.common.base.Preconditions;
import io.github.svegon.utils.interfaces.function.Object2ByteFunction;
import io.github.svegon.utils.interfaces.function.Object2CharFunction;
import io.github.svegon.utils.interfaces.function.Object2ShortFunction;
import it.unimi.dsi.fastutil.booleans.*;
import it.unimi.dsi.fastutil.bytes.*;
import it.unimi.dsi.fastutil.chars.*;
import it.unimi.dsi.fastutil.doubles.DoubleIterator;
import it.unimi.dsi.fastutil.doubles.DoubleIterators;
import it.unimi.dsi.fastutil.floats.FloatIterator;
import it.unimi.dsi.fastutil.floats.FloatIterators;
import it.unimi.dsi.fastutil.floats.FloatPredicate;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongIterators;
import it.unimi.dsi.fastutil.objects.*;
import it.unimi.dsi.fastutil.shorts.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.*;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;

public final class IterationUtil {
    private IterationUtil() {
        throw new AssertionError();
    }

    public static <E> Iterable<E> suppressComodCheck(final @NotNull List<E> of) {
        return () -> simpleListIterator(of);
    }

    public static <E> ListIterator<E> simpleListIterator(@NotNull List<E> of) {
        return simpleListIterator(of, 0);
    }

    public static <E> ListIterator<E> simpleListIterator(final @NotNull List<E> of, final int startingIndex) {
        return new ListIterator<>() {
            private int index = startingIndex;

            @Override
            public boolean hasNext() {
                return index < of.size();
            }

            @Override
            public E next() {
                try {
                    return of.get(index++);
                } catch (IndexOutOfBoundsException e) {
                    throw new ConcurrentModificationException();
                }
            }

            @Override
            public boolean hasPrevious() {
                return index > 0;
            }

            @Override
            public E previous() {
                try {
                    return of.get(--index);
                } catch (IndexOutOfBoundsException e) {
                    throw new ConcurrentModificationException();
                }
            }

            @Override
            public int nextIndex() {
                return index;
            }

            @Override
            public int previousIndex() {
                return index - 1;
            }

            @Override
            public void remove() {
                of.remove(--index);
            }

            @Override
            public void set(E e) {
                of.set(previousIndex(), e);
            }

            @Override
            public void add(E e) {
                of.add(nextIndex(), e);
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static <T> ObjectIterator<T> concatObj(final @NotNull Iterator<? extends @Nullable ObjectIterator<T>>
                                                                 iterators) {
        Preconditions.checkNotNull(iterators);

        return iterators.hasNext() ? new OnNextComputeObjectIterator<>() {
            ObjectIterator<T> it = iterators.next();

            @Override
            protected T computeNext() {
                while (it == null || !it.hasNext()) {
                    if (!iterators.hasNext()) {
                        finish();
                        return null;
                    }

                    it = iterators.next();
                }

                return it.next();
            }
        } : ObjectIterators.EMPTY_ITERATOR;
    }

    public static <T> ObjectIterator<T> concatObj(final @NotNull Iterable<? extends @Nullable ObjectIterator<T>>
                                                          iterators) {
        return concatObj(iterators.iterator());
    }

    @SuppressWarnings("unchecked")
    public static <T> ObjectIterator<T> concatObj(final @Nullable ObjectIterator<T> @NotNull ... iterators) {
        return concatObj(Arrays.asList(iterators));
    }

    public static BooleanIterator concatBoolean(final @NotNull Iterator<? extends @Nullable BooleanIterator> iterators) {
        Preconditions.checkNotNull(iterators);

        return iterators.hasNext() ? new OnNextComputeBooleanIterator() {
            BooleanIterator it = iterators.next();

            @Override
            protected boolean computeNext() {
                while (it == null || !it.hasNext()) {
                    if (!iterators.hasNext()) {
                        finish();
                        return false;
                    }

                    it = iterators.next();
                }

                return it.nextBoolean();
            }
        } : BooleanIterators.EMPTY_ITERATOR;
    }

    public static BooleanIterator concatBoolean(final @NotNull Iterable<? extends @Nullable BooleanIterator> iterators) {
        return concatBoolean(iterators.iterator());
    }

    public static BooleanIterator concatBoolean(final @Nullable BooleanIterator @NotNull ... iterators) {
        return concatBoolean(ArrayUtil.asList(iterators));
    }

    public static ByteIterator concatByte(final @NotNull Iterator<? extends @Nullable ByteIterator> iterators) {
        Preconditions.checkNotNull(iterators);

        return iterators.hasNext() ? new OnNextComputeByteIterator() {
            ByteIterator it = iterators.next();

            @Override
            protected byte computeNext() {
                while (it == null || !it.hasNext()) {
                    if (!iterators.hasNext()) {
                        finish();
                        return 0;
                    }

                    it = iterators.next();
                }

                return it.nextByte();
            }
        } : ByteIterators.EMPTY_ITERATOR;
    }

    public static ByteIterator concatByte(final @NotNull Iterable<? extends @Nullable ByteIterator> iterators) {
        return concatByte(iterators.iterator());
    }

    public static ByteIterator concatByte(final @Nullable ByteIterator @NotNull ... iterators) {
        return concatByte(ArrayUtil.asList(iterators));
    }

    public static CharIterator concatChar(final @NotNull Iterator<? extends @Nullable CharIterator> iterators) {
        Preconditions.checkNotNull(iterators);

        return iterators.hasNext() ? new OnNextComputeCharIterator() {
            CharIterator it = iterators.next();

            @Override
            protected char computeNext() {
                while (it == null || !it.hasNext()) {
                    if (!iterators.hasNext()) {
                        finish();
                        return 0;
                    }

                    it = iterators.next();
                }

                return it.nextChar();
            }
        } : CharIterators.EMPTY_ITERATOR;
    }

    public static CharIterator concatChar(final @NotNull Iterable<? extends @Nullable CharIterator> iterators) {
        return concatChar(iterators.iterator());
    }

    public static CharIterator concatChar(final @Nullable CharIterator @NotNull ... iterators) {
        return concatChar(ArrayUtil.asList(iterators));
    }

    public static ShortIterator concatShort(final @NotNull Iterator<? extends @Nullable ShortIterator> iterators) {
        Preconditions.checkNotNull(iterators);

        return iterators.hasNext() ? new OnNextComputeShortIterator() {
            ShortIterator it = iterators.next();

            @Override
            protected short computeNext() {
                while (it == null || !it.hasNext()) {
                    if (!iterators.hasNext()) {
                        finish();
                        return 0;
                    }

                    it = iterators.next();
                }

                return it.nextShort();
            }
        } : ShortIterators.EMPTY_ITERATOR;
    }

    public static ShortIterator concatShort(final @NotNull Iterable<? extends @Nullable ShortIterator> iterators) {
        return concatShort(iterators.iterator());
    }

    public static ShortIterator concatShort(final @Nullable ShortIterator @NotNull ... iterators) {
        return concatShort(ArrayUtil.asList(iterators));
    }

    public static IntIterator concatInt(final @NotNull Iterator<? extends @Nullable IntIterator> iterators) {
        Preconditions.checkNotNull(iterators);

        return iterators.hasNext() ? new OnNextComputeIntIterator() {
            IntIterator it = iterators.next();

            @Override
            protected int computeNext() {
                while (it == null || !it.hasNext()) {
                    if (!iterators.hasNext()) {
                        finish();
                        return 0;
                    }

                    it = iterators.next();
                }

                return it.nextInt();
            }
        } : IntIterators.EMPTY_ITERATOR;
    }

    public static IntIterator concatInt(final @NotNull Iterable<? extends @Nullable IntIterator> iterators) {
        return concatInt(iterators.iterator());
    }

    public static IntIterator concatInt(final @Nullable IntIterator @NotNull ... iterators) {
        return concatInt(ArrayUtil.asList(iterators));
    }

    public static LongIterator concatLong(final @NotNull Iterator<? extends @Nullable LongIterator> iterators) {
        Preconditions.checkNotNull(iterators);

        return iterators.hasNext() ? new OnNextComputeLongIterator() {
            LongIterator it = iterators.next();

            @Override
            protected long computeNext() {
                while (it == null || !it.hasNext()) {
                    if (!iterators.hasNext()) {
                        finish();
                        return 0;
                    }

                    it = iterators.next();
                }

                return it.nextLong();
            }
        } : LongIterators.EMPTY_ITERATOR;
    }

    public static LongIterator concatLong(final @NotNull Iterable<? extends @Nullable LongIterator> iterators) {
        return concatLong(iterators.iterator());
    }

    public static LongIterator concatLong(final @Nullable LongIterator @NotNull ... iterators) {
        return concatLong(ArrayUtil.asList(iterators));
    }

    public static FloatIterator concatFloat(final @NotNull Iterator<? extends @Nullable FloatIterator> iterators) {
        Preconditions.checkNotNull(iterators);

        return iterators.hasNext() ? new OnNextComputeFloatIterator() {
            FloatIterator it = iterators.next();

            @Override
            protected float computeNext() {
                while (it == null || !it.hasNext()) {
                    if (!iterators.hasNext()) {
                        finish();
                        return 0;
                    }

                    it = iterators.next();
                }

                return it.nextFloat();
            }
        } : FloatIterators.EMPTY_ITERATOR;
    }

    public static FloatIterator concatFloat(final @NotNull Iterable<? extends @Nullable FloatIterator> iterators) {
        return concatFloat(iterators.iterator());
    }

    public static FloatIterator concatFloat(final @Nullable FloatIterator @NotNull ... iterators) {
        return concatFloat(ArrayUtil.asList(iterators));
    }

    public static DoubleIterator concatDouble(final @NotNull Iterator<? extends @Nullable DoubleIterator> iterators) {
        Preconditions.checkNotNull(iterators);

        return iterators.hasNext() ? new OnNextComputeDoubleIterator() {
            DoubleIterator it = iterators.next();

            @Override
            protected double computeNext() {
                while (it == null || !it.hasNext()) {
                    if (!iterators.hasNext()) {
                        finish();
                        return 0;
                    }

                    it = iterators.next();
                }

                return it.nextDouble();
            }
        } : DoubleIterators.EMPTY_ITERATOR;
    }

    public static DoubleIterator concatDouble(final @NotNull Iterable<? extends @Nullable DoubleIterator> iterators) {
        return concatDouble(iterators.iterator());
    }

    public static DoubleIterator concatDouble(final @Nullable DoubleIterator @NotNull ... iterators) {
        return concatDouble(ArrayUtil.asList(iterators));
    }

    public static <T> ObjectIterator<T> skipIf(final Iterator<T> itr, final Predicate<T> filter) {
        Preconditions.checkNotNull(itr);
        Preconditions.checkNotNull(filter);

        return new OnNextComputeObjectIterator<>() {
            @Override
            protected T computeNext() {
                T t;

                do {
                    if (!itr.hasNext()) {
                        finish();
                        return null;
                    }

                    t = itr.next();
                } while (!filter.test(t));

                return t;
            }
        };
    }

    public static BooleanIterator filter(final BooleanIterator itr, final BooleanPredicate filter) {
        Preconditions.checkNotNull(itr);
        Preconditions.checkNotNull(filter);

        return new OnNextComputeBooleanIterator() {
            @Override
            protected boolean computeNext() {
                boolean t;

                do {
                    if (!itr.hasNext()) {
                        finish();
                        return false;
                    }

                    t = itr.nextBoolean();
                } while (!filter.test(t));

                return t;
            }
        };
    }

    public static ByteIterator filter(final ByteIterator itr, final BytePredicate filter) {
        Preconditions.checkNotNull(itr);
        Preconditions.checkNotNull(filter);

        return new OnNextComputeByteIterator() {
            @Override
            protected byte computeNext() {
                byte t;

                do {
                    if (!itr.hasNext()) {
                        finish();
                        return 0;
                    }

                    t = itr.nextByte();
                } while (!filter.test(t));

                return t;
            }
        };
    }

    public static CharIterator filter(final CharIterator itr, final CharPredicate filter) {
        Preconditions.checkNotNull(itr);
        Preconditions.checkNotNull(filter);

        return new OnNextComputeCharIterator() {
            @Override
            protected char computeNext() {
                char t;

                do {
                    if (!itr.hasNext()) {
                        finish();
                        return 0;
                    }

                    t = itr.nextChar();
                } while (!filter.test(t));

                return t;
            }
        };
    }

    public static ShortIterator filter(final ShortIterator itr, final ShortPredicate filter) {
        Preconditions.checkNotNull(itr);
        Preconditions.checkNotNull(filter);

        return new OnNextComputeShortIterator() {
            @Override
            protected short computeNext() {
                short t;

                do {
                    if (!itr.hasNext()) {
                        finish();
                        return 0;
                    }

                    t = itr.nextShort();
                } while (!filter.test(t));

                return t;
            }
        };
    }

    public static IntIterator filter(final IntIterator itr, final IntPredicate filter) {
        Preconditions.checkNotNull(itr);
        Preconditions.checkNotNull(filter);

        return new OnNextComputeIntIterator() {
            @Override
            protected int computeNext() {
                int t;

                do {
                    if (!itr.hasNext()) {
                        finish();
                        return 0;
                    }

                    t = itr.nextInt();
                } while (!filter.test(t));

                return t;
            }
        };
    }

    public static LongIterator filter(final LongIterator itr, final LongPredicate filter) {
        Preconditions.checkNotNull(itr);
        Preconditions.checkNotNull(filter);

        return new OnNextComputeLongIterator() {
            @Override
            protected long computeNext() {
                long t;

                do {
                    if (!itr.hasNext()) {
                        finish();
                        return 0;
                    }

                    t = itr.nextLong();
                } while (!filter.test(t));

                return t;
            }
        };
    }

    public static FloatIterator filter(final FloatIterator itr, final FloatPredicate filter) {
        Preconditions.checkNotNull(itr);
        Preconditions.checkNotNull(filter);

        return new OnNextComputeFloatIterator() {
            @Override
            protected float computeNext() {
                float t;

                do {
                    if (!itr.hasNext()) {
                        finish();
                        return 0;
                    }

                    t = itr.nextFloat();
                } while (!filter.test(t));

                return t;
            }
        };
    }

    public static DoubleIterator filter(final DoubleIterator itr, final DoublePredicate filter) {
        Preconditions.checkNotNull(itr);
        Preconditions.checkNotNull(filter);

        return new OnNextComputeDoubleIterator() {
            @Override
            protected double computeNext() {
                double t;

                do {
                    if (!itr.hasNext()) {
                        finish();
                        return 0;
                    }

                    t = itr.nextDouble();
                } while (!filter.test(t));

                return t;
            }
        };
    }

    public static <E, T> ObjectIterator<T> transformToObj(Iterator<E> itr, Function<? super E, ? extends T> transformer) {
        return new L2LTransformingIterator<>(itr, transformer);
    }

    public static <E, T> ObjectListIterator<T> transformToObj(final @NotNull ListIterator<E> itr,
                                                              final @NotNull Function<? super E, ? extends T> transformer) {
        Preconditions.checkNotNull(transformer);
        return transformToObj(itr, (i, o) -> transformer.apply(o));
    }

    public static <E, T> ObjectListIterator<T> transformToObj(final @NotNull ListIterator<E> itr,
                                              final @NotNull IntObjectBiFunction<? super E, ? extends T> transformer) {
        return new L2LTransformingListIterator<>(itr, transformer);
    }

    public static <T> ObjectIterator<T> mapToObj(BooleanIterator itr, Boolean2ObjectFunction<? extends T> transformer) {
        return new Z2LTransformingIterator<>(itr, transformer);
    }

    public static <T> Z2LTransformingBidirectionalIterator<T> mapToObj(BooleanBidirectionalIterator itr,
                                                                       Boolean2ObjectFunction<? extends T> transformer) {
        return new Z2LTransformingBidirectionalIterator<>(itr, transformer);
    }

    public static <T> Z2LTransformingListIterator<T> mapToObj(BooleanListIterator itr,
                                                              Boolean2ObjectFunction<? extends T> transformer) {
        return new Z2LTransformingListIterator<>(itr, transformer);
    }

    public static <T> ObjectIterator<T> mapToObj(ByteIterator itr, Byte2ObjectFunction<? extends T> transformer) {
        return new B2LTransformingIterator<>(itr, transformer);
    }

    public static <T> B2LTransformingBidirectionalIterator<T> mapToObj(ByteBidirectionalIterator itr,
                                                                       Byte2ObjectFunction<? extends T> transformer) {
        return new B2LTransformingBidirectionalIterator<>(itr, transformer);
    }

    public static <T> ObjectListIterator<T> mapToObj(ByteListIterator itr,
                                                     Byte2ObjectFunction<? extends T> transformer) {
        return new B2LTransformingListIterator<>(itr, transformer);
    }

    public static <T> ObjectIterator<T> mapToObj(CharIterator itr, Char2ObjectFunction<? extends T> transformer) {
        return new C2LTransformingIterator<>(itr, transformer);
    }

    public static <T> C2LTransformingBidirectionalIterator<T> mapToObj(CharBidirectionalIterator itr,
                                                                       Char2ObjectFunction<? extends T> transformer) {
        return new C2LTransformingBidirectionalIterator<>(itr, transformer);
    }

    public static <T> ObjectListIterator<T> mapToObj(final @NotNull CharListIterator itr,
                                                     final @NotNull Char2ObjectFunction<? extends T> transformer) {
        Preconditions.checkNotNull(transformer);
        return mapToObj(itr, (int i, char c) -> transformer.get(c));
    }

    public static <T> ObjectListIterator<T> mapToObj(final @NotNull CharListIterator itr,
                                                     final @NotNull IntCharBiFunction<? extends T> transformer) {
        return new C2LTransformingListIterator<>(itr, transformer);
    }

    public static <T> ObjectIterable<T> mapToObj(final @NotNull ShortIterable it,
                                                 final @NotNull Short2ObjectFunction<? extends T> transformer) {
        Preconditions.checkNotNull(transformer);
        return () -> mapToObj(it.iterator(), transformer);
    }

    public static <T> ObjectIterator<T> mapToObj( final @NotNull ShortIterator itr,  final @NotNull Short2ObjectFunction<? extends T> transformer) {
        return new S2LTransformingIterator<>(itr, transformer);
    }

    public static <T> S2LTransformingBidirectionalIterator<T> mapToObj(final @NotNull ShortBidirectionalIterator itr,
                                                                       final @NotNull Short2ObjectFunction<? extends T> transformer) {
        return new S2LTransformingBidirectionalIterator<>(itr, transformer);
    }

    public static <T> ObjectListIterator<T> mapToObj(final @NotNull ShortListIterator itr,
                                                     final @NotNull Short2ObjectFunction<? extends T> transformer) {
        return new S2LTransformingListIterator<>(itr, transformer);
    }

    public static <T> ObjectIterable<T> mapToObj(final @NotNull IntIterable it,
                                                 final @NotNull IntFunction<? extends T> transformer) {
        Preconditions.checkNotNull(transformer);
        return () -> mapToObj(it.iterator(), transformer);
    }

    public static <T> I2LTransformingIterator<T> mapToObj(final @NotNull IntIterator itr,
                                                          final @NotNull IntFunction<? extends T> transformer) {
        return new I2LTransformingIterator<>(itr, transformer);
    }

    public static <T> I2LTransformingBidirectionalIterator<T> mapToObj(final @NotNull IntBidirectionalIterator itr,
                                                                       final @NotNull IntFunction<? extends T> transformer) {
        return new I2LTransformingBidirectionalIterator<>(itr, transformer);
    }

    public static <T> ObjectListIterator<T> mapToObj(final @NotNull IntListIterator itr,
                                                     final @NotNull IntFunction<? extends T> transformer) {
        return new I2LTransformingListIterator<>(itr, transformer);
    }

    public static <E> L2BTransformingIterator<E> transformToByte(final @NotNull Iterator<E> itr,
                                                                 final @NotNull Object2ByteFunction<? super E> transformer) {
        return new L2BTransformingIterator<>(itr, transformer);
    }

    public static B2BTransformingIterator mapToByte(final @NotNull ByteIterator itr,
                                                    final @NotNull ByteUnaryOperator transformer) {
        return new B2BTransformingIterator(itr, transformer);
    }

    public static C2BTransformingIterator mapToByte(final @NotNull CharIterator itr,
                                                    final @NotNull Char2ByteFunction transformer) {
        return new C2BTransformingIterator(itr, transformer);
    }

    public static S2BTransformingIterator mapToByte(final @NotNull ShortIterator itr,
                                                    final @NotNull Short2ByteFunction transformer) {
        return new S2BTransformingIterator(itr, transformer);
    }

    public static <E> L2CTransformingIterator<E> transformToChar(final @NotNull Iterator<E> itr,
                                                                 final @NotNull Object2CharFunction<? super E> transformer) {
        return new L2CTransformingIterator<>(itr, transformer);
    }

    public static <T> L2CTransformingListIterator<T> transformToChar(final @NotNull ListIterator<T> itr,
                                                                     final @NotNull Object2CharFunction<? super T> transformer) {
        return new L2CTransformingListIterator<>(itr, transformer);
    }

    public static Z2CTransformingIterator mapToChar(final @NotNull BooleanIterator itr,
                                                    final @NotNull Boolean2CharFunction transformer) {
        return new Z2CTransformingIterator(itr, transformer);
    }

    public static Z2CTransformingBidirectionalIterator mapToChar(final @NotNull BooleanBidirectionalIterator itr,
                                                                 final @NotNull Boolean2CharFunction transformer) {
        return new Z2CTransformingBidirectionalIterator(itr, transformer);
    }

    public static Z2CTransformingListIterator mapToChar(final @NotNull BooleanListIterator itr,
                                                        final @NotNull Boolean2CharFunction transformer) {
        return new Z2CTransformingListIterator(itr, transformer);
    }

    public static B2CTransformingIterator mapToChar(final @NotNull ByteIterator itr,
                                                    final @NotNull Byte2CharFunction transformer) {
        return new B2CTransformingIterator(itr, transformer);
    }

    public static B2CTransformingBidirectionalIterator mapToChar(final @NotNull ByteBidirectionalIterator itr,
                                                                 final @NotNull Byte2CharFunction transformer) {
        return new B2CTransformingBidirectionalIterator(itr, transformer);
    }

    public static B2CTransformingListIterator mapToChar(final @NotNull ByteListIterator itr,
                                                        final @NotNull Byte2CharFunction transformer) {
        return new B2CTransformingListIterator(itr, transformer);
    }

    public static C2CTransformingIterator mapToChar(final @NotNull CharIterator itr,
                                                    final @NotNull CharUnaryOperator transformer) {
        return new C2CTransformingIterator(itr, transformer);
    }

    public static C2CTransformingBidirectionalIterator mapToChar(final @NotNull CharBidirectionalIterator itr,
                                                                 final @NotNull CharUnaryOperator transformer) {
        return new C2CTransformingBidirectionalIterator(itr, transformer);
    }

    public static C2CTransformingListIterator mapToChar(final @NotNull CharListIterator itr,
                                                        final @NotNull CharUnaryOperator transformer) {
        return new C2CTransformingListIterator(itr, transformer);
    }

    public static S2CTransformingIterator mapToChar(final @NotNull ShortIterator itr,
                                                    final @NotNull Short2CharFunction transformer) {
        return new S2CTransformingIterator(itr, transformer);
    }

    public static S2CTransformingBidirectionalIterator mapToChar(final @NotNull ShortBidirectionalIterator itr,
                                                                 final @NotNull Short2CharFunction transformer) {
        return new S2CTransformingBidirectionalIterator(itr, transformer);
    }

    public static S2CTransformingListIterator mapToChar(final @NotNull ShortListIterator itr,
                                                        final @NotNull Short2CharFunction transformer) {
        return new S2CTransformingListIterator(itr, transformer);
    }

    public static I2CTransformingIterator mapToChar(final @NotNull IntIterator itr,
                                                    final @NotNull Int2CharFunction transformer) {
        return new I2CTransformingIterator(itr, transformer);
    }

    public static I2CTransformingBidirectionalIterator mapToChar(final @NotNull IntBidirectionalIterator itr,
                                                                 final @NotNull Int2CharFunction transformer) {
        return new I2CTransformingBidirectionalIterator(itr, transformer);
    }

    public static I2CTransformingListIterator mapToChar(final @NotNull IntListIterator itr,
                                                        final @NotNull Int2CharFunction transformer) {
        return new I2CTransformingListIterator(itr, transformer);
    }

    public static <E> L2STransformingIterator<E> transformToShort(final @NotNull Iterator<E> itr,
                                                                  final @NotNull Object2ShortFunction<? super E> transformer) {
        return new L2STransformingIterator<>(itr, transformer);
    }

    public static ShortIterable mapToShort(final @NotNull BooleanIterable itr,
                                                     final @NotNull Boolean2ShortFunction transformer) {
        Preconditions.checkNotNull(itr);
        Preconditions.checkNotNull(transformer);
        return () -> mapToShort(itr.iterator(), transformer);
    }

    public static Z2STransformingIterator mapToShort(final @NotNull BooleanIterator itr,
                                                     final @NotNull Boolean2ShortFunction transformer) {
        return new Z2STransformingIterator(itr, transformer);
    }

    public static Z2STransformingBidirectionalIterator mapToShort(final @NotNull BooleanBidirectionalIterator itr,
                                                                  final @NotNull Boolean2ShortFunction transformer) {
        return new Z2STransformingBidirectionalIterator(itr, transformer);
    }

    public static Z2STransformingListIterator mapToShort(final @NotNull BooleanListIterator itr,
                                                         final @NotNull Boolean2ShortFunction transformer) {
        return new Z2STransformingListIterator(itr, transformer);
    }

    public static B2STransformingIterator mapToShort(final @NotNull ByteIterator itr,
                                                     final @NotNull Byte2ShortFunction transformer) {
        return new B2STransformingIterator(itr, transformer);
    }

    public static B2STransformingBidirectionalIterator mapToShort(final @NotNull ByteBidirectionalIterator itr,
                                                                  final @NotNull Byte2ShortFunction transformer) {
        return new B2STransformingBidirectionalIterator(itr, transformer);
    }

    public static B2STransformingListIterator mapToShort(final @NotNull ByteListIterator itr,
                                                         final @NotNull Byte2ShortFunction transformer) {
        return new B2STransformingListIterator(itr, transformer);
    }

    public static C2STransformingIterator mapToShort(final @NotNull CharIterator itr,
                                                     final @NotNull Char2ShortFunction transformer) {
        return new C2STransformingIterator(itr, transformer);
    }

    public static C2STransformingBidirectionalIterator mapToShort(final @NotNull CharBidirectionalIterator itr,
                                                                  final @NotNull Char2ShortFunction transformer) {
        return new C2STransformingBidirectionalIterator(itr, transformer);
    }

    public static C2STransformingListIterator mapToShort(final @NotNull CharListIterator itr,
                                                         final @NotNull Char2ShortFunction transformer) {
        return new C2STransformingListIterator(itr, transformer);
    }

    public static S2STransformingIterator mapToShort(final @NotNull ShortIterator itr,
                                                     final @NotNull ShortUnaryOperator transformer) {
        return new S2STransformingIterator(itr, transformer);
    }

    public static S2STransformingBidirectionalIterator mapToShort(final @NotNull ShortBidirectionalIterator itr,
                                                                  final @NotNull ShortUnaryOperator transformer) {
        return new S2STransformingBidirectionalIterator(itr, transformer);
    }

    public static S2STransformingListIterator mapToShort(final @NotNull ShortListIterator itr,
                                                         final @NotNull ShortUnaryOperator transformer) {
        return new S2STransformingListIterator(itr, transformer);
    }

    public static I2STransformingIterator mapToShort(final @NotNull IntIterator itr,
                                                     final @NotNull Int2ShortFunction transformer) {
        return new I2STransformingIterator(itr, transformer);
    }

    public static I2STransformingBidirectionalIterator mapToShort(final @NotNull IntBidirectionalIterator itr,
                                                                  final @NotNull Int2ShortFunction transformer) {
        return new I2STransformingBidirectionalIterator(itr, transformer);
    }

    public static I2STransformingListIterator mapToShort(final @NotNull IntListIterator itr,
                                                         final @NotNull Int2ShortFunction transformer) {
        return new I2STransformingListIterator(itr, transformer);
    }

    public static <E> L2ITransformingIterator<E> transformToInt(final @NotNull Iterator<E> itr,
                                                                final @NotNull ToIntFunction<? super E> transformer) {
        return new L2ITransformingIterator<>(itr, transformer);
    }

    public static <E> L2ITransformingListIterator<E> transformToInt(final @NotNull ListIterator<E> itr,
                                                                    final @NotNull ToIntFunction<? super E> transformer) {
        return new L2ITransformingListIterator<>(itr, transformer);
    }

    public static <E> L2ITransformingIterator<E> mapToInt(final @NotNull ObjectIterator<E> itr,
                                                          final @NotNull ToIntFunction<? super E> transformer) {
        return transformToInt(itr, transformer);
    }

    public static <E> L2ITransformingBidirectionalIterator<E> mapToInt(final @NotNull
                                                                               ObjectBidirectionalIterator<E> itr,
                                                                       final @NotNull ToIntFunction<? super E> transformer) {
        return new L2ITransformingBidirectionalIterator<E>(itr, transformer);
    }

    public static <E> L2ITransformingListIterator<E> mapToInt(final @NotNull ObjectListIterator<E> itr,
                                                       final @NotNull ToIntFunction<? super E> transformer) {
        return transformToInt(itr, transformer);
    }

    public static Z2ITransformingIterator mapToInt(final @NotNull BooleanIterator itr,
                                                   final @NotNull Boolean2IntFunction transformer) {
        return new Z2ITransformingIterator(itr, transformer);
    }

    public static B2ITransformingIterator mapToInt(final @NotNull ByteIterator itr,
                                                   final @NotNull Byte2IntFunction transformer) {
        return new B2ITransformingIterator(itr, transformer);
    }

    public static C2ITransformingIterator mapToInt(final @NotNull CharIterator itr,
                                                   final @NotNull Char2IntFunction transformer) {
        return new C2ITransformingIterator(itr, transformer);
    }

    public static S2ITransformingIterator mapToInt(final @NotNull ShortIterator itr,
                                                   final @NotNull Short2IntFunction transformer) {
        return new S2ITransformingIterator(itr, transformer);
    }

    public static I2ITransformingIterator mapToInt(final @NotNull IntIterator itr,
                                                   final @NotNull IntUnaryOperator transformer) {
        return new I2ITransformingIterator(itr, transformer);
    }

    public static <T, U> ObjectIterator<U> flatMapToObj(final @NotNull Iterator<T> itr,
                                                  final @NotNull Function<? super T,
                                                          ? extends @Nullable ObjectIterator<U>> mapper) {
        return concatObj(transformToObj(itr, mapper));
    }

    public static <T> ObjectIterator<T> flatMapToObj(final @NotNull BooleanIterator itr,
                                              final @NotNull Boolean2ObjectFunction<? extends @Nullable
                                                      ObjectIterator<T>> mapper) {
        return concatObj(mapToObj(itr, mapper));
    }

    public static <T> ObjectIterator<T> flatMapToObj(final @NotNull ByteIterator itr,
                                              final @NotNull Byte2ObjectFunction<? extends @Nullable ObjectIterator<T>>
                                                       mapper) {
        return concatObj(mapToObj(itr, mapper));
    }

    public static <T> ObjectIterator<T> flatMapToObj(final @NotNull CharIterator itr,
                                              final @NotNull Char2ObjectFunction<? extends @Nullable ObjectIterator<T>>
                                                       mapper) {
        return concatObj(mapToObj(itr, mapper));
    }

    public static <T> ObjectIterator<T> flatMapToObj(final @NotNull ObjectIterator<T> itr,
                                              final @NotNull Function<? super T, ? extends @Nullable ObjectIterator<T>>
                                                       mapper) {
        return concatObj(transformToObj(itr, mapper));
    }

    public static BooleanIterator flatMapToBoolean(final @NotNull BooleanIterator itr,
                                             final @NotNull Boolean2ObjectFunction<? extends @Nullable BooleanIterator> mapper) {
        return concatBoolean(mapToObj(itr, mapper));
    }

    public static BooleanIterator flatMapToBoolean(final @NotNull ShortIterator itr,
                                             final @NotNull Short2ObjectFunction<? extends @Nullable BooleanIterator> mapper) {
        return concatBoolean(mapToObj(itr, mapper));
    }

    public static ByteIterator flatMapToByte(final @NotNull ByteIterator itr,
                                             final @NotNull Byte2ObjectFunction<? extends @Nullable ByteIterator> mapper) {
        return concatByte(mapToObj(itr, mapper));
    }

    public static ByteIterator flatMapToByte(final @NotNull ShortIterator itr,
                                             final @NotNull Short2ObjectFunction<? extends @Nullable ByteIterator> mapper) {
        return concatByte(mapToObj(itr, mapper));
    }

    public static <T> ShortIterator flatMapToShort(final @NotNull Iterator<T> itr,
                                               final @NotNull Function<? super T, ? extends @Nullable ShortIterator>
                                                       mapper) {
        return concatShort(transformToObj(itr, mapper));
    }

    public static ShortIterator flatMapToShort(final @NotNull BooleanIterator itr,
                                               final @NotNull Boolean2ObjectFunction<? extends @Nullable ShortIterator>
                                                       mapper) {
        return concatShort(mapToObj(itr, mapper));
    }

    public static ShortIterator flatMapToShort(final @NotNull ByteIterator itr,
                                               final @NotNull Byte2ObjectFunction<? extends @Nullable ShortIterator>
                                                       mapper) {
        return concatShort(mapToObj(itr, mapper));
    }

    public static ShortIterator flatMapToShort(final @NotNull CharIterator itr,
                                               final @NotNull Char2ObjectFunction<? extends @Nullable ShortIterator>
                                                       mapper) {
        return concatShort(mapToObj(itr, mapper));
    }

    public static ShortIterator flatMapToShort(final @NotNull ShortIterator itr,
                                               final @NotNull Short2ObjectFunction<? extends @Nullable ShortIterator>
                                                       mapper) {
        return concatShort(mapToObj(itr, mapper));
    }

    public static IntIterator flatMapToInt(final @NotNull ByteIterator itr,
                                           final @NotNull Byte2ObjectFunction<? extends @Nullable IntIterator> mapper) {
        return concatInt(mapToObj(itr, mapper));
    }

    public static IntIterator flatMapToInt(final @NotNull ShortIterator itr,
                                           final @NotNull Short2ObjectFunction<? extends @Nullable IntIterator>
                                                   mapper) {
        return concatInt(mapToObj(itr, mapper));
    }

    public static IntIterator flatMapToInt(final @NotNull IntIterator itr,
                                           final @NotNull IntFunction<? extends @Nullable IntIterator> mapper) {
        return concatInt(mapToObj(itr, mapper));
    }
}

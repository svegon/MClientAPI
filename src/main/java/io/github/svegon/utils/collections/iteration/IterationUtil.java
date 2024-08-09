package io.github.svegon.utils.collections.iteration;

import io.github.svegon.utils.fast.util.booleans.OnNextComputeBooleanIterator;
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
import io.github.svegon.utils.fast.util.ints.transform.booleans.Z2ITransformingIterator;
import io.github.svegon.utils.fast.util.ints.transform.chars.C2ITransformingIterator;
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
import io.github.svegon.utils.fast.util.objects.transform.shorts.S2LTransformingBidirectionalIterator;
import io.github.svegon.utils.fast.util.objects.transform.shorts.S2LTransformingIterator;
import io.github.svegon.utils.fast.util.objects.transform.shorts.S2LTransformingListIterator;
import io.github.svegon.utils.fast.util.shorts.transform.booleans.Z2STransformingBidirectionalIterator;
import io.github.svegon.utils.fast.util.shorts.transform.booleans.Z2STransformingIterator;
import io.github.svegon.utils.fast.util.shorts.transform.booleans.Z2STransformingListIterator;
import io.github.svegon.utils.fast.util.shorts.transform.bytes.B2STransformingListIterator;
import io.github.svegon.utils.fast.util.shorts.transform.chars.C2STransformingBidirectionalIterator;
import io.github.svegon.utils.fast.util.shorts.transform.chars.C2STransformingIterator;
import io.github.svegon.utils.fast.util.shorts.transform.chars.C2STransformingListIterator;
import io.github.svegon.utils.fast.util.shorts.transform.ints.I2STransformingListIterator;
import io.github.svegon.utils.interfaces.function.*;
import com.google.common.base.Preconditions;
import io.github.svegon.utils.interfaces.function.Object2CharFunction;
import it.unimi.dsi.fastutil.booleans.*;
import it.unimi.dsi.fastutil.bytes.*;
import it.unimi.dsi.fastutil.chars.*;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.*;
import it.unimi.dsi.fastutil.shorts.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.*;

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
        return concatBoolean(Arrays.asList(iterators));
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
        return concatChar(Arrays.asList(iterators));
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

    public static ShortIterator mapToShort(final @NotNull ByteIterator itr,
                                           final @NotNull Byte2ShortFunction transformer) {
        return mapToShort(mapToChar(itr, i -> (char) i), i -> transformer.get((byte) i));
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

    public static I2STransformingListIterator mapToShort(final @NotNull IntListIterator itr,
                                                         final @NotNull Int2ShortFunction transformer) {
        return new I2STransformingListIterator(itr, transformer);
    }

    public static Z2ITransformingIterator mapToInt(final @NotNull BooleanIterator itr,
                                                   final @NotNull Boolean2IntFunction transformer) {
        return new Z2ITransformingIterator(itr, transformer);
    }

    public static C2ITransformingIterator mapToInt(final @NotNull CharIterator itr,
                                                   final @NotNull Char2IntFunction transformer) {
        return new C2ITransformingIterator(itr, transformer);
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

    public static BooleanIterator flatMapToBoolean(final @NotNull BooleanIterator itr,
                                             final @NotNull Boolean2ObjectFunction<? extends @Nullable BooleanIterator> mapper) {
        return concatBoolean(mapToObj(itr, mapper));
    }

    public static BooleanIterator flatMapToBoolean(final @NotNull ShortIterator itr,
                                             final @NotNull Short2ObjectFunction<? extends @Nullable BooleanIterator> mapper) {
        return concatBoolean(mapToObj(itr, mapper));
    }
}

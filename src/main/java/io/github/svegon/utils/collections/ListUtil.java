package io.github.svegon.utils.collections;

import io.github.svegon.utils.fast.util.booleans.transform.bytes.B2ZRATransformingList;
import io.github.svegon.utils.fast.util.booleans.transform.bytes.B2ZTransformingList;
import io.github.svegon.utils.fast.util.booleans.transform.objects.L2ZRATransformingList;
import io.github.svegon.utils.fast.util.booleans.transform.objects.L2ZTransformingList;
import io.github.svegon.utils.fast.util.chars.transform.objects.L2CRATransformingList;
import io.github.svegon.utils.fast.util.chars.transform.objects.L2CTransformingList;
import io.github.svegon.utils.fast.util.doubles.transform.booleans.Z2DRATransformingList;
import io.github.svegon.utils.fast.util.doubles.transform.booleans.Z2DTransformingList;
import io.github.svegon.utils.fast.util.doubles.transform.bytes.B2DRATransformingList;
import io.github.svegon.utils.fast.util.doubles.transform.bytes.B2DTransformingList;
import io.github.svegon.utils.fast.util.doubles.transform.chars.C2DRATransformingList;
import io.github.svegon.utils.fast.util.doubles.transform.chars.C2DTransformingList;
import io.github.svegon.utils.fast.util.doubles.transform.doubles.D2DRATransformingList;
import io.github.svegon.utils.fast.util.doubles.transform.doubles.D2DTransformingList;
import io.github.svegon.utils.fast.util.doubles.transform.floats.F2DRATransformingList;
import io.github.svegon.utils.fast.util.doubles.transform.floats.F2DTransformingList;
import io.github.svegon.utils.fast.util.doubles.transform.ints.I2DRATransformingList;
import io.github.svegon.utils.fast.util.doubles.transform.ints.I2DTransformingList;
import io.github.svegon.utils.fast.util.doubles.transform.longs.J2DRATransformingList;
import io.github.svegon.utils.fast.util.doubles.transform.longs.J2DTransformingList;
import io.github.svegon.utils.fast.util.doubles.transform.objects.L2DRATransformingList;
import io.github.svegon.utils.fast.util.doubles.transform.objects.L2DTransformingList;
import io.github.svegon.utils.fast.util.doubles.transform.shorts.S2DRATransformingList;
import io.github.svegon.utils.fast.util.doubles.transform.shorts.S2DTransformingList;
import io.github.svegon.utils.fast.util.floats.transform.booleans.Z2FRATransformingList;
import io.github.svegon.utils.fast.util.floats.transform.booleans.Z2FTransformingList;
import io.github.svegon.utils.fast.util.floats.transform.bytes.B2FRATransformingList;
import io.github.svegon.utils.fast.util.floats.transform.bytes.B2FTransformingList;
import io.github.svegon.utils.fast.util.floats.transform.chars.C2FRATransformingList;
import io.github.svegon.utils.fast.util.floats.transform.chars.C2FTransformingList;
import io.github.svegon.utils.fast.util.floats.transform.doubles.D2FRATransformingList;
import io.github.svegon.utils.fast.util.floats.transform.doubles.D2FTransformingList;
import io.github.svegon.utils.fast.util.floats.transform.floats.F2FRATransformingList;
import io.github.svegon.utils.fast.util.floats.transform.floats.F2FTransformingList;
import io.github.svegon.utils.fast.util.floats.transform.ints.I2FRATransformingList;
import io.github.svegon.utils.fast.util.floats.transform.ints.I2FTransformingList;
import io.github.svegon.utils.fast.util.floats.transform.longs.J2FRATransformingList;
import io.github.svegon.utils.fast.util.floats.transform.longs.J2FTransformingList;
import io.github.svegon.utils.fast.util.floats.transform.objects.L2FRATransformingList;
import io.github.svegon.utils.fast.util.floats.transform.objects.L2FTransformingList;
import io.github.svegon.utils.fast.util.floats.transform.shorts.S2FRATransformingList;
import io.github.svegon.utils.fast.util.floats.transform.shorts.S2FTransformingList;
import io.github.svegon.utils.fast.util.ints.transform.booleans.Z2IRATransformingList;
import io.github.svegon.utils.fast.util.ints.transform.booleans.Z2ITransformingList;
import io.github.svegon.utils.fast.util.ints.transform.bytes.B2IRATransformingList;
import io.github.svegon.utils.fast.util.ints.transform.bytes.B2ITransformingList;
import io.github.svegon.utils.fast.util.ints.transform.doubles.D2IRATransformingList;
import io.github.svegon.utils.fast.util.ints.transform.doubles.D2ITransformingList;
import io.github.svegon.utils.fast.util.ints.transform.ints.I2IRATranformingList;
import io.github.svegon.utils.fast.util.ints.transform.ints.I2ITransformingList;
import io.github.svegon.utils.fast.util.ints.transform.longs.J2IRATranformingList;
import io.github.svegon.utils.fast.util.ints.transform.longs.J2ITransformingList;
import io.github.svegon.utils.fast.util.ints.transform.objects.L2IRATransformingList;
import io.github.svegon.utils.fast.util.ints.transform.objects.L2ITransformingList;
import io.github.svegon.utils.fast.util.longs.transform.doubles.D2JRATransformingList;
import io.github.svegon.utils.fast.util.longs.transform.doubles.D2JTransformingList;
import io.github.svegon.utils.fast.util.longs.transform.objects.L2JRATransformingList;
import io.github.svegon.utils.fast.util.longs.transform.objects.L2JTransformingList;
import io.github.svegon.utils.fast.util.objects.OnNextComputeObjectIterator;
import io.github.svegon.utils.fast.util.shorts.transform.objects.L2SRATransformingList;
import io.github.svegon.utils.fast.util.shorts.transform.objects.L2STransformingList;
import io.github.svegon.utils.interfaces.function.*;
import io.github.svegon.utils.math.MathUtil;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.math.IntMath;
import it.unimi.dsi.fastutil.BigList;
import it.unimi.dsi.fastutil.booleans.Boolean2DoubleFunction;
import it.unimi.dsi.fastutil.booleans.Boolean2FloatFunction;
import it.unimi.dsi.fastutil.booleans.Boolean2IntFunction;
import it.unimi.dsi.fastutil.booleans.BooleanList;
import it.unimi.dsi.fastutil.bytes.*;
import it.unimi.dsi.fastutil.chars.Char2DoubleFunction;
import it.unimi.dsi.fastutil.chars.Char2FloatFunction;
import it.unimi.dsi.fastutil.chars.CharList;
import it.unimi.dsi.fastutil.doubles.Double2FloatFunction;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.floats.Float2DoubleFunction;
import it.unimi.dsi.fastutil.floats.FloatList;
import it.unimi.dsi.fastutil.floats.FloatUnaryOperator;
import it.unimi.dsi.fastutil.ints.Int2FloatFunction;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntLists;
import it.unimi.dsi.fastutil.longs.Long2FloatFunction;
import it.unimi.dsi.fastutil.longs.LongList;
import it.unimi.dsi.fastutil.objects.AbstractObjectBigList;
import it.unimi.dsi.fastutil.shorts.Short2DoubleFunction;
import it.unimi.dsi.fastutil.shorts.Short2FloatFunction;
import it.unimi.dsi.fastutil.shorts.ShortList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import io.github.svegon.utils.fast.util.bytes.transform.objects.L2BRATransformingList;
import io.github.svegon.utils.fast.util.bytes.transform.objects.L2BTransformingList;

import java.math.RoundingMode;
import java.util.*;
import java.util.function.*;

public final class ListUtil {
    private ListUtil() {
        throw new AssertionError();
    }

    public static <E> Vector<E> newSyncedList() {
        return new Vector<>();
    }

    public static <E> FixedCacheCombinedList<E> newFixedCacheCombinedList(ImmutableList<List<E>> cache) {
        return new FixedCacheCombinedList<>(cache);
    }

    public static <E> FixedCacheCombinedList<E> newFixedCacheCombinedList(List<? extends E>... collections) {
        return new FixedCacheCombinedList<>(collections);
    }

    @SuppressWarnings("unchecked")
    public static <E> FixedCacheCombinedList<E> newFixedCacheCombinedList(Iterable<? extends Collection<? extends E>>
                                                                                  collections) {
        return collections instanceof Collection
                ? new FixedCacheCombinedList<>((Collection<? extends Collection<? extends E>>) collections)
                : new FixedCacheCombinedList<>(Lists.newArrayList(collections));
    }

    public static <E> FixedCacheCombinedList<E> newFixedCacheCombinedList(final Iterator<? extends Collection<? extends E>>
                                                                                  collections) {
        return new FixedCacheCombinedList<>(Lists.newArrayList(collections));
    }

    public static <E> ExposedArrayList<E> newExposedArrayList() {
        return new ExposedArrayList<>();
    }

    public static <E> ExposedArrayList<E> newExposedArrayList(@NotNull final Iterable<E> c) {
        return c instanceof Collection ? new ExposedArrayList<>((Collection<? extends E>) c)
                : newExposedArrayList(c.iterator());
    }


    public static <E> ExposedArrayList<E> newExposedArrayList(@NotNull final Iterator<E> iterator) {
        ExposedArrayList<E> a = new ExposedArrayList<>();

        iterator.forEachRemaining(a::add);

        return a;
    }

    public static <E> ExposedArrayList<E> newExposedArrayListWithCapacity(final int capacity) {
        return capacity == 0 ? new ExposedArrayList<>() : new ExposedArrayList<>(new Object[capacity]);
    }

    @SafeVarargs
    public static <E, L extends List<E>> L concat(IntFunction<L> resultInstanceSupplier, List<E>... lists) {
        L list = resultInstanceSupplier.apply(Arrays.stream(lists).mapToInt(List::size).sum());

        for (List<E> merged : lists) {
            list.addAll(merged);
        }

        return list;
    }

    @SafeVarargs
    public static <E, L extends List<E>> L concat(final Supplier<L> resultInstanceSupplier, final List<E>... lists) {
        L list = resultInstanceSupplier.get();

        for (List<E> merged : lists) {
            list.addAll(merged);
        }

        return list;
    }

    @SafeVarargs
    public static <E> ArrayList<E> concat(final List<E>... lists) {
        return concat(Lists::newArrayListWithCapacity, lists);
    }

    public static <E, L extends List<E>> List<L> partition(L list, int size) {
        Preconditions.checkNotNull(list);
        Preconditions.checkArgument(size > 0);
        return new Partition<>(list, size);
    }

    public static <E> BooleanList transformToBoolean(List<E> list, IntObjectBiPredicate<? super E> function) {
        return list instanceof RandomAccess ? new L2ZRATransformingList<>(list, function)
                : new L2ZTransformingList<>(list, function);
    }

    public static <E> BooleanList transformToBoolean(List<E> list, Predicate<? super E> transformer) {
        Preconditions.checkNotNull(transformer);
        return transformToBoolean(list, (i, o) -> transformer.test(o));
    }

    public static BooleanList mapToBoolean(ByteList list, BytePredicate function) {
        return list instanceof RandomAccess ? new B2ZRATransformingList(list, function)
                : new B2ZTransformingList(list, function);
    }

    public static <E> ByteList transformToByte(List<E> list, Object2ByteFunction<? super E> function) {
        return list instanceof RandomAccess ? new L2BRATransformingList<>(list, function)
                : new L2BTransformingList<>(list, function);
    }

    public static <E> CharList transformToChar(List<E> list, Object2CharFunction<? super E> function) {
        return list instanceof RandomAccess ? new L2CRATransformingList<>(list, function)
                : new L2CTransformingList<>(list, function);
    }

    public static <E> ShortList transformToShort(List<E> list, Object2ShortFunction<? super E> function) {
        return list instanceof RandomAccess ? new L2SRATransformingList<>(list, function)
                : new L2STransformingList<>(list, function);
    }

    public static <E> IntList transformToInt(List<E> list, ToIntFunction<? super E> function) {
        return list instanceof RandomAccess ? new L2IRATransformingList<>(list, function)
                : new L2ITransformingList<>(list, function);
    }

    public static IntList mapToInt(BooleanList list, Boolean2IntFunction function) {
        return list instanceof RandomAccess ? new Z2IRATransformingList(list, function)
                : new Z2ITransformingList(list, function);
    }

    public static IntList mapToInt(ByteList list, Byte2IntFunction function) {
        return list instanceof RandomAccess ? new B2IRATransformingList(list, function)
                : new B2ITransformingList(list, function);
    }

    public static IntList mapToInt(IntList list, IntUnaryOperator function) {
        return list instanceof RandomAccess ? new I2IRATranformingList(list, function)
                : new I2ITransformingList(list, function);
    }

    public static IntList mapToInt(LongList list, LongToIntFunction function) {
        return list instanceof RandomAccess ? new J2IRATranformingList(list, function)
                : new J2ITransformingList(list, function);
    }

    public static IntList mapToInt(DoubleList list, DoubleToIntFunction function) {
        return list instanceof RandomAccess ? new D2IRATransformingList(list, function)
                : new D2ITransformingList(list, function);
    }

    public static <E> LongList transformToLong(List<E> list, ToLongFunction<? super E> function) {
        return list instanceof RandomAccess ? new L2JRATransformingList<>(list, function)
                : new L2JTransformingList<>(list, function);
    }

    public static LongList mapToLong(DoubleList list, DoubleToLongFunction function) {
        return list instanceof RandomAccess ? new D2JRATransformingList(list, function)
                : new D2JTransformingList(list, function);
    }

    public static <E> FloatList transformToFloat(List<E> list, Object2FloatFunction<? super E> function) {
        return list instanceof RandomAccess ? new L2FRATransformingList<>(list, function)
                : new L2FTransformingList<>(list, function);
    }

    public static FloatList mapToFloat(BooleanList list, Boolean2FloatFunction function) {
        return list instanceof RandomAccess ? new Z2FRATransformingList(list, function)
                : new Z2FTransformingList(list, function);
    }

    public static FloatList mapToFloat(ByteList list, Byte2FloatFunction function) {
        return list instanceof RandomAccess ? new B2FRATransformingList(list, function)
                : new B2FTransformingList(list, function);
    }

    public static FloatList mapToFloat(CharList list, Char2FloatFunction function) {
        return list instanceof RandomAccess ? new C2FRATransformingList(list, function)
                : new C2FTransformingList(list, function);
    }

    public static FloatList mapToFloat(ShortList list, Short2FloatFunction function) {
        return list instanceof RandomAccess ? new S2FRATransformingList(list, function)
                : new S2FTransformingList(list, function);
    }

    public static FloatList mapToFloat(IntList list, Int2FloatFunction function) {
        return list instanceof RandomAccess ? new I2FRATransformingList(list, function)
                : new I2FTransformingList(list, function);
    }

    public static FloatList mapToFloat(LongList list, Long2FloatFunction function) {
        return list instanceof RandomAccess ? new J2FRATransformingList(list, function)
                : new J2FTransformingList(list, function);
    }

    public static FloatList mapToFloat(FloatList list, FloatUnaryOperator function) {
        return list instanceof RandomAccess ? new F2FRATransformingList(list, function)
                : new F2FTransformingList(list, function);
    }

    public static FloatList mapToFloat(DoubleList list, Double2FloatFunction function) {
        return list instanceof RandomAccess ? new D2FRATransformingList(list, function)
                : new D2FTransformingList(list, function);
    }

    public static <E> DoubleList transformToDouble(List<E> list, ToDoubleFunction<? super E> function) {
        return list instanceof RandomAccess ? new L2DRATransformingList<>(list, function)
                : new L2DTransformingList<>(list, function);
    }

    public static DoubleList mapToDouble(BooleanList list, Boolean2DoubleFunction function) {
        return list instanceof RandomAccess ? new Z2DRATransformingList(list, function)
                : new Z2DTransformingList(list, function);
    }

    public static DoubleList mapToDouble(ByteList list, Byte2DoubleFunction function) {
        return list instanceof RandomAccess ? new B2DRATransformingList(list, function)
                : new B2DTransformingList(list, function);
    }

    public static DoubleList mapToDouble(CharList list, Char2DoubleFunction function) {
        return list instanceof RandomAccess ? new C2DRATransformingList(list, function)
                : new C2DTransformingList(list, function);
    }

    public static DoubleList mapToDouble(ShortList list, Short2DoubleFunction function) {
        return list instanceof RandomAccess ? new S2DRATransformingList(list, function)
                : new S2DTransformingList(list, function);
    }

    public static DoubleList mapToDouble(IntList list, IntToDoubleFunction function) {
        return list instanceof RandomAccess ? new I2DRATransformingList(list, function)
                : new I2DTransformingList(list, function);
    }

    public static DoubleList mapToDouble(LongList list, LongToDoubleFunction function) {
        return list instanceof RandomAccess ? new J2DRATransformingList(list, function)
                : new J2DTransformingList(list, function);
    }

    public static DoubleList mapToDouble(FloatList list, Float2DoubleFunction function) {
        return list instanceof RandomAccess ? new F2DRATransformingList(list, function)
                : new F2DTransformingList(list, function);
    }

    public static DoubleList mapToDouble(DoubleList list, DoubleUnaryOperator function) {
        return list instanceof RandomAccess ? new D2DRATransformingList(list, function)
                : new D2DTransformingList(list, function);
    }

    public static <E> void replaceAll(List<E> list,
                                      IntObjectBiFunction<? super E, ? extends E> transformingOperator) {
        Objects.requireNonNull(transformingOperator);

        ListIterator<E> listIterator = list.listIterator();

        while (listIterator.hasNext()) {
            listIterator.set(transformingOperator.apply(listIterator.nextIndex(), listIterator.next()));
        }
    }

    public static <E> List<E> repeat(final @Nullable E element, final int times) {
        if (times < 0) {
            throw new IllegalArgumentException("amount is negative: " + times);
        }

        return new ElementRepetition<>(element, times);
    }

    public static <E> BigList<E> repeat(final @Nullable E element, final long times) {
        if (times < 0) {
            throw new IllegalArgumentException("amount is negative: " + times);
        }

        return new BigElementRepetition<>(element, times);
    }

    public static <E> List<E> iterate(IntFunction<? extends E> elementAccessor, int size) {
        Objects.requireNonNull(elementAccessor);

        if (size <= 0) {
            throw new IllegalArgumentException();
        }

        return new IterationBasedList<>(elementAccessor, size);
    }

    public static <E> List<E> iterate(IntInt2ObjectFunction<E> elementAccessor, int i, int j) {
        Objects.requireNonNull(elementAccessor);

        if (i <= 0 || j <= 0) {
            throw new IllegalArgumentException();
        }

        return new VectorIterationBasedList<>(new int[]{i, j}, (matrixIndices) -> elementAccessor.apply(
                matrixIndices.getInt(0), matrixIndices.getInt(1)));
    }

    public static <E> List<E> iterate(IntIntInt2ObjectFunction<E> elementAccessor, int i, int j, int k) {
        Objects.requireNonNull(elementAccessor);

        if (i <= 0 || j <= 0 || k <= 0) {
            throw new IllegalArgumentException();
        }

        return new VectorIterationBasedList<>(new int[]{i, j, k}, (matrixIndices) -> elementAccessor.apply(
                matrixIndices.getInt(0), matrixIndices.getInt(1), matrixIndices.getInt(2)));
    }

    public static <E> List<E> iterate(Function<? super IntList, ? extends E> elementAccessor,
                                      int... dimensions) {
        dimensions = dimensions.clone();

        for (int i : dimensions) {
            if (i <= 0) {
                throw new IllegalArgumentException();
            }
        }

        return new VectorIterationBasedList<>(dimensions, Objects.requireNonNull(elementAccessor));
    }

    private static class ElementRepetition<E> extends AbstractList<E> implements RandomAccess {
        private final E element;
        private int size;

        private ElementRepetition(E element, int size) {
            this.element = element;
            this.size = size;
        }

        @Override
        public E get(int index) {
            Preconditions.checkElementIndex(index, size());
            return element;
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public void add(int index, E element) {
            Preconditions.checkPositionIndex(index, size());

            if (Objects.equals(this.element, element)) {
                size++;
            }
        }

        @Override
        public boolean add(E e) {
            if (Objects.equals(element, e)) {
                size++;
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean remove(Object o) {
            if (!isEmpty() && Objects.equals(element, o)) {
                size--;
                return true;
            } else {
                return false;
            }
        }

        @Override
        public E remove(int index) {
            Preconditions.checkElementIndex(index, size());
            size--;
            return element;
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            if (!isEmpty() && c.contains(element)) {
                clear();
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean removeIf(Predicate<? super E> filter) {
            Preconditions.checkNotNull(filter);

            if (!isEmpty() && filter.test(element)) {
                size--;

                while (!isEmpty() && filter.test(element)) {
                    size--;
                }

                return true;
            } else {
                return false;
            }
        }
    }

    private static class BigElementRepetition<E> extends AbstractObjectBigList<E> implements RandomAccess {
        private final E element;
        private long size;

        private BigElementRepetition(E element, long size) {
            this.element = element;
            this.size = size;
        }

        @Override
        public E get(long index) {
            if (index < 0) {
                throw new IndexOutOfBoundsException(Strings.lenientFormat("index ("+ index
                                + ") must not be negative"));
            }

            if (index >= size) {
                throw new IndexOutOfBoundsException(Strings.lenientFormat("index (" + index
                                + ") must be less than size (" + size + ")"));
            }

            return element;
        }

        @Override
        public long size64() {
            return size;
        }

        @Override
        public void add(long index, E element) {
            if (index < 0) {
                throw new IndexOutOfBoundsException(Strings.lenientFormat("index ("+ index
                        + ") must not be negative"));
            }

            if (index > size) {
                throw new IndexOutOfBoundsException(Strings.lenientFormat("index (" + index
                        + ") must be less than size (" + size + ")"));
            }

            if (Objects.equals(this.element, element)) {
                size++;
            }
        }

        @Override
        public boolean add(E e) {
            if (Objects.equals(element, e)) {
                size++;
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean remove(Object o) {
            if (!isEmpty() && Objects.equals(element, o)) {
                size--;
                return true;
            } else {
                return false;
            }
        }

        @Override
        public E remove(long index) {
            if (index < 0) {
                throw new IndexOutOfBoundsException(Strings.lenientFormat("index ("+ index
                        + ") must not be negative"));
            }

            if (index >= size) {
                throw new IndexOutOfBoundsException(Strings.lenientFormat("index (" + index
                        + ") must be less than size (" + size + ")"));
            }

            size--;
            return element;
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            if (!isEmpty() && c.contains(element)) {
                clear();
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean removeIf(Predicate<? super E> filter) {
            Preconditions.checkNotNull(filter);

            if (!isEmpty() && filter.test(element)) {
                size--;

                while (!isEmpty() && filter.test(element)) {
                    size--;
                }

                return true;
            } else {
                return false;
            }
        }
    }

    private static final class IterationBasedList<E> extends AbstractList<E> implements RandomAccess {
        private final IntFunction<? extends E> elementAccessor;
        private final int size;

        private IterationBasedList(@NotNull final IntFunction<? extends E> elementAccessor, final int size) {
            this.elementAccessor = elementAccessor;
            this.size = size;
        }

        @Override
        public E get(int index) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException(index);
            }

            return elementAccessor.apply(index);
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public void forEach(Consumer<? super E> action) {
            for (int i = 0; i < size; i++) {
                action.accept(elementAccessor.apply(i));
            }
        }
    }

    private static final class VectorIterationBasedList<E> extends AbstractList<E> implements RandomAccess {
        private final int[] dimensions;
        private final Function<? super IntList, ? extends E> elementAccessor;
        private final int size;

        private VectorIterationBasedList(final int[] dimensions,
                                         @NotNull final Function<? super IntList, ? extends E> elementAccessor) {
            this.dimensions = dimensions;
            this.elementAccessor = elementAccessor;
            this.size = (int) Math.min(MathUtil.product(dimensions), Integer.MAX_VALUE);
        }

        @Override
        public E get(int index) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException(index);
            }

            return elementAccessor.apply(ArrayUtil.asList(MathUtil.compressedIndexToMatrixIndexes(index, dimensions)));
        }

        @Override
        public int size() {
            return size;
        }

        /**
         * @return an optimized iterator for matrices
         */
        @Override
        public @NotNull Iterator<E> iterator() {
            return new VectorIndexedIterator<>(elementAccessor, dimensions);
        }

        @Override
        public boolean equals(Object o) {
            if (o == this)
                return true;

            if (!(o instanceof List<?> other))
                return false;

            if (other.size() != size()) {
                return false;
            }

            Iterator<E> e1 = iterator();
            Iterator<?> e2 = other.iterator();

            while (e1.hasNext() && e2.hasNext()) {
                if (!Objects.equals(e1.next(), e2.next()))
                    return false;
            }

            return !(e1.hasNext() || e2.hasNext());
        }
    }

    private static final class VectorIndexedIterator<E> extends OnNextComputeObjectIterator<E> {
        private final Function<? super IntList, ? extends E> elementAccessor;
        private final int[] dimensions;
        private final int[] matrixIndexes;
        private final IntList exposedMatrixIndexes;

        private VectorIndexedIterator(Function<? super IntList, ? extends E> elementAccessor, int[] dimensions) {
            this.elementAccessor = elementAccessor;
            this.dimensions = dimensions;
            this.matrixIndexes = new int[dimensions.length];
            this.exposedMatrixIndexes = IntLists.unmodifiable(ArrayUtil.asList(matrixIndexes));
        }

        @Override
        protected E computeNext() {
            try {
                E result = elementAccessor.apply(exposedMatrixIndexes);
                matrixIndexes[matrixIndexes.length - 1]++;

                for (int i = matrixIndexes.length - 1; matrixIndexes[i] >= dimensions[i]; --i) {
                    matrixIndexes[i] = 0;
                    matrixIndexes[i - 1]++;
                }

                return result;
            } catch (IndexOutOfBoundsException e) {
                finish();
                return null;
            }
        }
    }

    private static class SubList<E> extends AbstractList<E> {
        private final List<E> root;
        private final int offset;
        protected int size;

        /**
         * Constructs a sublist of an arbitrary AbstractList, which is
         * not a RandomAccessSubList itself.
         */
        public SubList(List<E> root, int fromIndex, int toIndex) {
            this.root = root;
            this.offset = fromIndex;
            this.size = toIndex - fromIndex;
        }

        /**
         * Constructs a sublist of another RandomAccessSubList.
         */
        protected SubList(SubList<E> parent, int fromIndex, int toIndex) {
            this.root = parent.root;
            this.offset = parent.offset + fromIndex;
            this.size = toIndex - fromIndex;
        }

        public E set(int index, E element) {
            Objects.checkIndex(index, size);
            return root.set(offset + index, element);
        }

        public E get(int index) {
            Objects.checkIndex(index, size);
            return root.get(offset + index);
        }

        public int size() {
            return size;
        }

        public void add(int index, E element) {
            Objects.checkIndex(index, size);
            root.add(offset + index, element);
        }

        public E remove(int index) {
            Objects.checkIndex(index, size);
            return root.remove(offset + index);
        }

        protected void removeRange(int fromIndex, int toIndex) {
            int index = offset + fromIndex;

            for (int i = fromIndex; i < toIndex; ++i) {
                root.remove(index);
            }
        }

        public boolean addAll(Collection<? extends E> c) {
            return addAll(size, c);
        }

        public boolean addAll(int index, Collection<? extends E> c) {
            Objects.checkIndex(index, size);
            int cSize = c.size();

            if (cSize == 0)
                return false;

            root.addAll(offset + index, c);
            return true;
        }

        public Iterator<E> iterator() {
            return listIterator();
        }

        public ListIterator<E> listIterator(int index) {
            Objects.checkIndex(index, size);

            return new ListIterator<E>() {
                private final ListIterator<E> i = root.listIterator(offset + index);

                public boolean hasNext() {
                    return nextIndex() < size;
                }

                public E next() {
                    if (hasNext()) {
                        return i.next();
                    } else {
                        throw new NoSuchElementException();
                    }
                }

                public boolean hasPrevious() {
                    return previousIndex() >= 0;
                }

                public E previous() {
                    if (hasPrevious()) {
                        return i.previous();
                    } else {
                        throw new NoSuchElementException();
                    }
                }

                public int nextIndex() {
                    return i.nextIndex() - offset;
                }

                public int previousIndex() {
                    return i.previousIndex() - offset;
                }

                public void remove() {
                    i.remove();
                }

                public void set(E e) {
                    i.set(e);
                }

                public void add(E e) {
                    i.add(e);
                }
            };
        }

        public List<E> subList(int fromIndex, int toIndex) {
            return new SubList<>(this, fromIndex, toIndex);
        }
    }

    private static class RandomAccessSubList<E> extends SubList<E> implements RandomAccess {

        /**
         * Constructs a sublist of an arbitrary AbstractList, which is
         * not a RandomAccessSubList itself.
         */
        RandomAccessSubList(List<E> root,
                            int fromIndex, int toIndex) {
            super(root, fromIndex, toIndex);
        }

        /**
         * Constructs a sublist of another RandomAccessSubList.
         */
        RandomAccessSubList(RandomAccessSubList parent, int fromIndex, int toIndex) {
            super(parent, fromIndex, toIndex);
        }

        @Override
        public List<E> subList(int fromIndex, int toIndex) {
            Objects.checkFromToIndex(fromIndex, toIndex, size);
            return new RandomAccessSubList(this, fromIndex, toIndex);
        }
    }

    private static class ListItr<E> implements ListIterator<E> {
        final List<E> backReference;

        /**
         * Index of element to be returned by subsequent call to next.
         */
        int cursor;

        /**
         * Index of element returned by most recent call to next or
         * previous.  Reset to -1 if this element is deleted by a call
         * to remove.
         */
        int lastRet = -1;

        ListItr(List<E> backReference, int index) {
            this.backReference = backReference;
            this.cursor = index;
        }

        public boolean hasNext() {
            return cursor != backReference.size();
        }

        public E next() {
            try {
                int i = cursor;
                E next = backReference.get(i);
                lastRet = i;
                cursor = i + 1;
                return next;
            } catch (IndexOutOfBoundsException e) {
                throw new ConcurrentModificationException(e);
            }
        }

        public void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();

            try {
                backReference.remove(lastRet);

                if (lastRet < cursor)
                    cursor--;

                lastRet = -1;
            } catch (IndexOutOfBoundsException e) {
                throw new ConcurrentModificationException();
            }
        }

        public boolean hasPrevious() {
            return cursor != 0;
        }

        public E previous() {
            try {
                int i = cursor - 1;
                E previous = backReference.get(i);
                lastRet = cursor = i;
                return previous;
            } catch (IndexOutOfBoundsException e) {
                throw new ConcurrentModificationException(e);
            }
        }

        public int nextIndex() {
            return cursor;
        }

        public int previousIndex() {
            return cursor - 1;
        }

        public void set(E e) {
            if (lastRet < 0)
                throw new IllegalStateException();

            try {
                backReference.set(lastRet, e);
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        public void add(E e) {
            try {
                int i = cursor;
                backReference.add(i, e);
                lastRet = -1;
                cursor = i + 1;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
    }

    private static class Partition<T, L extends List<T>> extends AbstractList<L> implements RandomAccess {
        final L list;
        final int size;

        Partition(L list, int size) {
            this.list = list;
            this.size = size;
        }

        @SuppressWarnings("unchecked")
        public L get(int index) {
            Preconditions.checkElementIndex(index, this.size());
            int start = index * this.size;
            int end = Math.min(start + this.size, this.list.size());
            return (L) this.list.subList(start, end);
        }

        public int size() {
            return IntMath.divide(this.list.size(), this.size, RoundingMode.CEILING);
        }

        public boolean isEmpty() {
            return this.list.isEmpty();
        }
    }
}

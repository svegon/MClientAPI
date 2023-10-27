package io.github.svegon.utils.collections.collecting;

import io.github.svegon.utils.FunctionUtil;
import io.github.svegon.utils.collections.ListUtil;
import io.github.svegon.utils.fast.util.chars.CharCollector;
import io.github.svegon.utils.fast.util.floats.FloatSummaryStatistics;
import io.github.svegon.utils.fast.util.objects.immutable.ImmutableObjectList;
import io.github.svegon.utils.fast.util.shorts.ShortSummaryStatistics;
import io.github.svegon.utils.interfaces.function.Object2FloatFunction;
import io.github.svegon.utils.interfaces.function.Object2ShortFunction;
import com.google.common.base.Preconditions;
import io.github.svegon.utils.fast.util.chars.immutable.ImmutableCharList;
import it.unimi.dsi.fastutil.chars.CharArrayList;
import it.unimi.dsi.fastutil.chars.CharList;
import it.unimi.dsi.fastutil.chars.CharLists;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public final class CollectingUtil {
    private CollectingUtil() {
        throw new AssertionError();
    }

    public static final Set<Collector.Characteristics> CH_CONCURRENT_ID
            = Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.CONCURRENT,
            Collector.Characteristics.UNORDERED,
            Collector.Characteristics.IDENTITY_FINISH));
    public static final Set<Collector.Characteristics> CH_CONCURRENT_NOID
            = Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.CONCURRENT,
            Collector.Characteristics.UNORDERED));
    public static final Set<Collector.Characteristics> CH_ID
            = Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH));
    public static final Set<Collector.Characteristics> CH_UNORDERED_ID
            = Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.UNORDERED,
            Collector.Characteristics.IDENTITY_FINISH));
    public static final Set<Collector.Characteristics> CH_NOID = Collections.emptySet();
    public static final Set<Collector.Characteristics> CH_UNORDERED_NOID
            = Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.UNORDERED));

    private static final CharCollector<CharList, ImmutableCharList> IMMUTABLE_CHAR_LIST_COLLECTOR
            = CharCollector.of(CharArrayList::new, CharList::add, FunctionUtil.charCollectionCombiner(),
            ImmutableCharList::copyOf, Collector.Characteristics.CONCURRENT);
    private static final Collector<Object, List<Object>, ImmutableObjectList<Object>> IMMUTABLE_OBJECT_LIST_COLLECTOR
            = Collector.of(ListUtil::newSyncedList, List::add, FunctionUtil.collectionCombiner(),
            ImmutableObjectList::copyOf, Collector.Characteristics.CONCURRENT);
    private static final Collector<?, AtomicInteger, AtomicInteger> COUNTING_COLLECTOR =
            Collector.of(AtomicInteger::new, (a, o) -> a.getAndIncrement(), FunctionUtil.atomicIntegerCombiner());

    /**
     * Returns a {@code Collector} which applies an {@code short}-producing
     * mapping function to each input element, and returns summary statistics
     * for the resulting values.
     *
     * @param <T> the type of the input elements
     * @param mapper the mapping function to apply to each element
     * @return a {@code Collector} implementing the summary-statistics reduction
     */
    public static <T> Collector<T, ?, ShortSummaryStatistics> summarizingShort(final Object2ShortFunction<T> mapper) {
        return Collector.of(ShortSummaryStatistics::new, (r, t) -> r.accept(mapper.applyAsShort(t)),
                (l, r) -> { l.combine(r); return l; }, CH_ID.toArray(Collector.Characteristics[]::new));
    }

    /**
     * Returns a {@code Collector} which applies an {@code float}-producing
     * mapping function to each input element, and returns summary statistics
     * for the resulting values.
     *
     * @param <T> the type of the input elements
     * @param mapper the mapping function to apply to each element
     * @return a {@code Collector} implementing the summary-statistics reduction
     */
    public static <T> Collector<T, ?, FloatSummaryStatistics> summarizingFloat(final Object2FloatFunction<T> mapper) {
        return Collector.of(FloatSummaryStatistics::new, (r, t) -> r.accept(mapper.applyAsFloat(t)),
                (l, r) -> { l.combine(r); return l; }, CH_ID.toArray(Collector.Characteristics[]::new));
    }

    public static <A extends CharList> CharCollector<A, CharList> toUnmodifiableCharList(Supplier<A> supplier) {
        return CharCollector.of(supplier, CharList::add, FunctionUtil.charCollectionCombiner(),
                CharLists::unmodifiable);
    }

    public static CharCollector<CharList, ImmutableCharList> toImmutableCharList() {
        return IMMUTABLE_CHAR_LIST_COLLECTOR;
    }

    @SuppressWarnings("unchecked")
    public static <E> Collector<E, List<E>, ImmutableObjectList<E>> toImmutableObjectList() {
        return (Collector) IMMUTABLE_OBJECT_LIST_COLLECTOR;
    }

    @SuppressWarnings("unchecked")
    public static <E> Collector<E, AtomicInteger, AtomicInteger> counting() {
        return (Collector<E, AtomicInteger, AtomicInteger>) COUNTING_COLLECTOR;
    }

    /**
     * note: the result passed to the finisher may be null if the stream is empty
     *
     * @param accumulator
     * @param finisher
     * @param <E>
     * @param <R>
     * @return
     */
    public static <E, R> Collector<E, AtomicReference<E>, R> unorderedCumulation(final BinaryOperator<E> accumulator,
                                                                                 final Function<E, R> finisher) {
        Preconditions.checkNotNull(accumulator);
        Preconditions.checkNotNull(finisher);

        final BinaryOperator<E> acc = (e, f) -> {
            if (e == null) {
                return f;
            }

            if (f == null) {
                return e;
            }

            return accumulator.apply(e, f);
        };

        return Collector.of(AtomicReference::new, (r, e) -> {
            r.getAndAccumulate(e, acc);
        }, (a, b) -> {
            a.getAndAccumulate(b.getAcquire(), acc);

            return a;
        }, finisher.compose(AtomicReference::getAcquire),
                Collector.Characteristics.CONCURRENT, Collector.Characteristics.UNORDERED);
    }

    public static <E> Collector<E, AtomicReference<E>, E> unorderedCumulation(final BinaryOperator<E> accumulator) {
        return unorderedCumulation(accumulator, FunctionUtil.identityOperator());
    }

    public static <E, R> Collector<E, AtomicReference<E>, R> unorderedCumulation(
            final Supplier<? extends E> identitySupplier, final BinaryOperator<E> accumulator,
            final Function<E, R> finisher) {
        Preconditions.checkNotNull(identitySupplier);
        Preconditions.checkNotNull(accumulator);
        Preconditions.checkNotNull(finisher);

        return Collector.of(() -> new AtomicReference<>(identitySupplier.get()), (r, e) -> {
                    r.getAndAccumulate(e, accumulator);
                }, (a, b) -> {
                    a.getAndAccumulate(b.getAcquire(), accumulator);

                    return a;
                }, finisher.compose(AtomicReference::getAcquire),
                Collector.Characteristics.CONCURRENT, Collector.Characteristics.UNORDERED);
    }

    public static <E, R> Collector<E, AtomicReference<E>, R> unorderedCumulation(
            final Supplier<? extends E> identitySupplier, final BinaryOperator<E> accumulator) {
        return unorderedCumulation(identitySupplier, accumulator, FunctionUtil.identityFunction());
    }
}

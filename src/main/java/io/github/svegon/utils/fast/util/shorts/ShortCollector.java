package io.github.svegon.utils.fast.util.shorts;

import com.google.common.base.Preconditions;
import io.github.svegon.utils.fast.util.objects.immutable.ImmutableEnumSet;
import io.github.svegon.utils.interfaces.function.ObjectShortConsumer;

import java.util.Collection;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public interface ShortCollector<A, R> {
    /**
     * A function that creates and returns a new mutable result container.
     *
     * @return a function which returns a new, mutable result container
     */
    Supplier<A> supplier();

    /**
     * A function that folds a value into a mutable result container.
     *
     * @return a function which folds a value into a mutable result container
     */
    ObjectShortConsumer<A> accumulator();

    /**
     * A function that accepts two partial results and merges them.  The
     * combiner function may fold state from one argument into the other and
     * return that, or may return a new result container.
     *
     * @return a function which combines two partial results into a combined
     * result
     */
    BinaryOperator<A> combiner();

    /**
     * Perform the final transformation from the intermediate accumulation type
     * {@code A} to the final result type {@code R}.
     *
     * <p>If the characteristic {@code IDENTITY_FINISH} is
     * set, this function may be presumed to be an identity transform with an
     * unchecked cast from {@code A} to {@code R}.
     *
     * @return a function which transforms the intermediate result to the final
     * result
     */
    Function<A, R> finisher();

    /**
     * Returns a {@code Set} of {@code Collector.Characteristics} indicating
     * the characteristics of this Collector.  This set should be immutable.
     *
     * @return an immutable set of collector characteristics
     */
    Set<Collector.Characteristics> characteristics();

    static <A, R> ShortCollector<A, R> of(Supplier<A> supplier, ObjectShortConsumer<A> accumulator,
                                          BinaryOperator<A> combiner, Function<A, R> finisher,
                                          ImmutableEnumSet<Collector.Characteristics> characteristics) {
        return new ShortCollectorImpl<>(Preconditions.checkNotNull(supplier), Preconditions.checkNotNull(accumulator),
                Preconditions.checkNotNull(combiner), Preconditions.checkNotNull(finisher),
                Preconditions.checkNotNull(characteristics));
    }

    static <A, R> ShortCollector<A, R> of(Supplier<A> supplier, ObjectShortConsumer<A> accumulator,
                                          BinaryOperator<A> combiner, Function<A, R> finisher,
                                          Collection<Collector.Characteristics> characteristics) {
        return new ShortCollectorImpl<>(Preconditions.checkNotNull(supplier), Preconditions.checkNotNull(accumulator),
                Preconditions.checkNotNull(combiner), Preconditions.checkNotNull(finisher),
                ImmutableEnumSet.of(characteristics));
    }

    static <A, R> ShortCollector<A, R> of(Supplier<A> supplier, ObjectShortConsumer<A> accumulator,
                                          BinaryOperator<A> combiner, Function<A, R> finisher,
                                          Collector.Characteristics... characteristics) {
        return new ShortCollectorImpl<>(Preconditions.checkNotNull(supplier), Preconditions.checkNotNull(accumulator),
                Preconditions.checkNotNull(combiner), Preconditions.checkNotNull(finisher),
                ImmutableEnumSet.of(characteristics));
    }

    final class ShortCollectorImpl<A, R> implements ShortCollector<A, R> {
        private final Supplier<A> supplier;
        private final ObjectShortConsumer<A> accumulator;
        private final BinaryOperator<A> combiner;
        private final Function<A, R> finisher;
        private final ImmutableEnumSet<Collector.Characteristics> characteristics;

        private ShortCollectorImpl(Supplier<A> supplier, ObjectShortConsumer<A> accumulator,
                                     BinaryOperator<A> combiner, Function<A, R> finisher,
                                     ImmutableEnumSet<Collector.Characteristics> characteristics) {
            this.supplier = supplier;
            this.accumulator = accumulator;
            this.combiner = combiner;
            this.finisher = finisher;
            this.characteristics = characteristics;
        }

        @Override
        public Supplier<A> supplier() {
            return supplier;
        }

        @Override
        public ObjectShortConsumer<A> accumulator() {
            return accumulator;
        }

        @Override
        public BinaryOperator<A> combiner() {
            return combiner;
        }

        @Override
        public Function<A, R> finisher() {
            return finisher;
        }

        @Override
        public Set<Collector.Characteristics> characteristics() {
            return characteristics;
        }
    }
}

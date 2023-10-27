package io.github.svegon.utils.collections.stream;

import io.github.svegon.utils.fast.util.booleans.BooleanCollector;
import com.github.svegon.utils.fuck_modifiers.*;
import io.github.svegon.utils.interfaces.function.ObjectBooleanBiConsumer;
import io.github.svegon.utils.optional.OptionalBoolean;
import io.github.svegon.utils.collections.ArrayUtil;
import io.github.svegon.utils.collections.spliterator.SpliteratorUtil;
import io.github.svegon.utils.fuck_modifiers.SpinedBuffer;
import io.github.svegon.utils.fuck_modifiers.StreamSpliterators;
import io.github.svegon.utils.fuck_modifiers.Streams;
import io.github.svegon.utils.fuck_modifiers.WhileOps;
import it.unimi.dsi.fastutil.booleans.*;

import java.util.Objects;
import java.util.OptionalDouble;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.*;

/**
 * @since 1.0.0
 */
public interface BooleanStream extends BaseStream<Boolean, BooleanStream> {
    /**
     * Returns a stream consisting of the elements of this stream that match
     * the given predicate.
     *
     * <p>This is an <a href="package-summary.html#StreamOps">intermediate
     * operation</a>.
     *
     * @param predicate a <a href="package-summary.html#NonInterference">non-interfering</a>,
     *                  <a href="package-summary.html#Statelessness">stateless</a>
     *                  predicate to apply to each element to determine if it
     *                  should be included
     * @return the new stream
     */
    BooleanStream filter(BooleanPredicate predicate);

    /**
     * Returns a stream consisting of the results of applying the given
     * function to the elements of this stream.
     *
     * <p>This is an <a href="package-summary.html#StreamOps">intermediate
     * operation</a>.
     *
     * @param mapper a <a href="package-summary.html#NonInterference">non-interfering</a>,
     *               <a href="package-summary.html#Statelessness">stateless</a>
     *               function to apply to each element
     * @return the new stream
     */
    BooleanStream map(BooleanUnaryOperator mapper);

    /**
     * Returns an objects-valued {@code Stream} consisting of the results of
     * applying the given function to the elements of this stream.
     *
     * <p>This is an <a href="package-summary.html#StreamOps">
     *     intermediate operation</a>.
     *
     * @param <U> the element type of the new stream
     * @param mapper a <a href="package-summary.html#NonInterference">non-interfering</a>,
     *               <a href="package-summary.html#Statelessness">stateless</a>
     *               function to apply to each element
     * @return the new stream
     */
    <U> Stream<U> mapToObj(Boolean2ObjectFunction<? extends U> mapper);

    /**
     * Returns a {@code ByteStream} consisting of the results of applying the
     * given function to the elements of this stream.
     *
     * <p>This is an <a href="package-summary.html#StreamOps">intermediate
     * operation</a>.
     *
     * @param mapper a <a href="package-summary.html#NonInterference">non-interfering</a>,
     *               <a href="package-summary.html#Statelessness">stateless</a>
     *               function to apply to each element
     * @return the new stream
     */
    ByteStream mapToByte(Boolean2ByteFunction mapper);

    /**
     * Returns a {@code ShortStream} consisting of the results of applying the
     * given function to the elements of this stream.
     *
     * <p>This is an <a href="package-summary.html#StreamOps">intermediate
     * operation</a>.
     *
     * @param mapper a <a href="package-summary.html#NonInterference">non-interfering</a>,
     *               <a href="package-summary.html#Statelessness">stateless</a>
     *               function to apply to each element
     * @return the new stream
     */
    ShortStream mapToShort(Boolean2ShortFunction mapper);

    /**
     * Returns an {@code IntStream} consisting of the results of applying the
     * given function to the elements of this stream.
     *
     * <p>This is an <a href="package-summary.html#StreamOps">intermediate
     * operation</a>.
     *
     * @param mapper a <a href="package-summary.html#NonInterference">non-interfering</a>,
     *               <a href="package-summary.html#Statelessness">stateless</a>
     *               function to apply to each element
     * @return the new stream
     */
    IntStream mapToInt(Boolean2IntFunction mapper);

    /**
     * Returns a {@code LongStream} consisting of the results of applying the
     * given function to the elements of this stream.
     *
     * <p>This is an <a href="package-summary.html#StreamOps">intermediate
     * operation</a>.
     *
     * @param mapper a <a href="package-summary.html#NonInterference">non-interfering</a>,
     *               <a href="package-summary.html#Statelessness">stateless</a>
     *               function to apply to each element
     * @return the new stream
     */
    LongStream mapToLong(Boolean2LongFunction mapper);

    /**
     * Returns a {@code CharStream} consisting of the results of applying the
     * given function to the elements of this stream.
     *
     * <p>This is an <a href="package-summary.html#StreamOps">intermediate
     * operation</a>.
     *
     * @param mapper a <a href="package-summary.html#NonInterference">non-interfering</a>,
     *               <a href="package-summary.html#Statelessness">stateless</a>
     *               function to apply to each element
     * @return the new stream
     */
    CharStream mapToChar(Boolean2CharFunction mapper);

    /**
     * Returns a {@code FloatStream} consisting of the results of applying the
     * given function to the elements of this stream.
     *
     * <p>This is an <a href="package-summary.html#StreamOps">intermediate
     * operation</a>.
     *
     * @param mapper a <a href="package-summary.html#NonInterference">non-interfering</a>,
     *               <a href="package-summary.html#Statelessness">stateless</a>
     *               function to apply to each element
     * @return the new stream
     */
    FloatStream mapToFloat(Boolean2FloatFunction mapper);

    /**
     * Returns a {@code LongStream} consisting of the results of applying the
     * given function to the elements of this stream.
     *
     * <p>This is an <a href="package-summary.html#StreamOps">intermediate
     * operation</a>.
     *
     * @param mapper a <a href="package-summary.html#NonInterference">non-interfering</a>,
     *               <a href="package-summary.html#Statelessness">stateless</a>
     *               function to apply to each element
     * @return the new stream
     */
    DoubleStream mapToDouble(Boolean2DoubleFunction mapper);

    /**
     * Returns a stream consisting of the results of replacing each element of
     * this stream with the contents of a mapped stream produced by applying
     * the provided mapping function to each element.  Each mapped stream is
     * {@link BaseStream#close() closed} after its contents
     * have been placed into this stream.  (If a mapped stream is {@code null}
     * an empty stream is used, instead.)
     *
     * <p>This is an <a href="package-summary.html#StreamOps">intermediate
     * operation</a>.
     *
     * @param mapper a <a href="package-summary.html#NonInterference">non-interfering</a>,
     *               <a href="package-summary.html#Statelessness">stateless</a>
     *               function to apply to each element which produces a
     *               {@code BooleanStream} of new values
     * @return the new stream
     * @see Stream#flatMap(Function)
     */
    BooleanStream flatMap(Boolean2ObjectFunction<? extends BooleanStream> mapper);

    /**
     * Returns a stream consisting of the distinct elements of this stream. The
     * elements are compared for equality according to
     * {@link Boolean#compare(boolean, boolean)}.
     *
     * <p>This is a <a href="package-summary.html#StreamOps">stateful
     * intermediate operation</a>.
     *
     * @return the result stream
     */
    BooleanStream distinct();

    /**
     * Returns a stream consisting of the elements of this stream in sorted
     * order. The elements are compared for equality according to
     * {@link Boolean#compare(boolean, boolean)}.
     *
     * <p>This is a <a href="package-summary.html#StreamOps">stateful
     * intermediate operation</a>.
     *
     * @return the result stream
     */
    BooleanStream sorted();

    /**
     * Returns a stream consisting of the elements of this stream, additionally
     * performing the provided action on each element as elements are consumed
     * from the resulting stream.
     *
     * <p>This is an <a href="package-summary.html#StreamOps">intermediate
     * operation</a>.
     *
     * <p>For parallel stream pipelines, the action may be called at
     * whatever time and in whatever thread the element is made available by the
     * upstream operation.  If the action modifies shared state,
     * it is responsible for providing the required synchronization.
     *
     * @apiNote This method exists mainly to support debugging, where you want
     * to see the elements as they flow past a certain point in a pipeline:
     * <pre>{@code
     *     BooleanStream.of(1, 2, 3, 4)
     *         .filter(e -> e > 2)
     *         .peek(e -> System.out.println("Filtered value: " + e))
     *         .map(e -> e * e)
     *         .peek(e -> System.out.println("Mapped value: " + e))
     *         .sum();
     * }</pre>
     *
     * <p>In cases where the stream implementation is able to optimize away the
     * production of some or all the elements (such as with booleans-circuiting
     * operations like {@code findFirst}, or in the example described in
     * {@link #count}), the action will not be invoked for those elements.
     *
     * @param action a <a href="package-summary.html#NonInterference">
     *               non-interfering</a> action to perform on the elements as
     *               they are consumed from the stream
     * @return the new stream
     */
    BooleanStream peek(BooleanConsumer action);

    /**
     * Returns a stream consisting of the elements of this stream, truncated
     * to be no longer than {@code maxSize} in length.
     *
     * <p>This is a <a href="package-summary.html#StreamOps">booleans-circuiting
     * stateful intermediate operation</a>.
     *
     * @apiNote
     * While {@code limit()} is generally a cheap operation on sequential
     * stream pipelines, it can be quite expensive on ordered parallel pipelines,
     * especially for large values of {@code maxSize}, since {@code limit(n)}
     * is constrained to return not just any <em>n</em> elements, but the
     * <em>first n</em> elements in the encounter order.  Using an unordered
     * stream source (such as {@link #generate(BooleanSupplier)}) or removing the
     * ordering constraint with {@link #unordered()} may result in significant
     * speedups of {@code limit()} in parallel pipelines, if the semantics of
     * your situation permit.  If consistency with encounter order is required,
     * and you are experiencing poor performance or memory utilization with
     * {@code limit()} in parallel pipelines, switching to sequential execution
     * with {@link #sequential()} may improve performance.
     *
     * @param maxSize the number of elements the stream should be limited to
     * @return the new stream
     * @throws IllegalArgumentException if {@code maxSize} is negative
     */
    BooleanStream limit(long maxSize);

    /**
     * Returns a stream consisting of the remaining elements of this stream
     * after discarding the first {@code n} elements of the stream.
     * If this stream contains fewer than {@code n} elements then an
     * empty stream will be returned.
     *
     * <p>This is a <a href="package-summary.html#StreamOps">stateful
     * intermediate operation</a>.
     *
     * @apiNote
     * While {@code skip()} is generally a cheap operation on sequential
     * stream pipelines, it can be quite expensive on ordered parallel pipelines,
     * especially for large values of {@code n}, since {@code skip(n)}
     * is constrained to skip not just any <em>n</em> elements, but the
     * <em>first n</em> elements in the encounter order.  Using an unordered
     * stream source (such as {@link #generate(BooleanSupplier)}) or removing the
     * ordering constraint with {@link #unordered()} may result in significant
     * speedups of {@code skip()} in parallel pipelines, if the semantics of
     * your situation permit.  If consistency with encounter order is required,
     * and you are experiencing poor performance or memory utilization with
     * {@code skip()} in parallel pipelines, switching to sequential execution
     * with {@link #sequential()} may improve performance.
     *
     * @param n the number of leading elements to skip
     * @return the new stream
     * @throws IllegalArgumentException if {@code n} is negative
     */
    BooleanStream skip(long n);

    /**
     * Returns, if this stream is ordered, a stream consisting of the longest
     * prefix of elements taken from this stream that match the given predicate.
     * Otherwise returns, if this stream is unordered, a stream consisting of a
     * subset of elements taken from this stream that match the given predicate.
     *
     * <p>If this stream is ordered then the longest prefix is a contiguous
     * sequence of elements of this stream that match the given predicate.  The
     * first element of the sequence is the first element of this stream, and
     * the element immediately following the last element of the sequence does
     * not match the given predicate.
     *
     * <p>If this stream is unordered, and some (but not all) elements of this
     * stream match the given predicate, then the behavior of this operation is
     * nondeterministic; it is free to take any subset of matching elements
     * (which includes the empty set).
     *
     * <p>Independent of whether this stream is ordered or unordered if all
     * elements of this stream match the given predicate then this operation
     * takes all elements (the result is the same as the input), or if no
     * elements of the stream match the given predicate then no elements are
     * taken (the result is an empty stream).
     *
     * <p>This is a <a href="package-summary.html#StreamOps">booleans-circuiting
     * stateful intermediate operation</a>.
     *
     * @implSpec
     * The default implementation obtains the {@link #spliterator() spliterator}
     * of this stream, wraps that spliterator so as to support the semantics
     * of this operation on traversal, and returns a new stream associated with
     * the wrapped spliterator.  The returned stream preserves the execution
     * booleanacteristics of this stream (namely parallel or sequential execution
     * as per {@link #isParallel()}) but the wrapped spliterator may choose to
     * not support splitting.  When the returned stream is closed, the close
     * handlers for both the returned and this stream are invoked.
     *
     * @apiNote
     * While {@code takeWhile()} is generally a cheap operation on sequential
     * stream pipelines, it can be quite expensive on ordered parallel
     * pipelines, since the operation is constrained to return not just any
     * valid prefix, but the longest prefix of elements in the encounter order.
     * Using an unordered stream source (such as
     * {@link #generate(BooleanSupplier)}) or removing the ordering constraint
     * with {@link #unordered()} may result in significant speedups of
     * {@code takeWhile()} in parallel pipelines, if the semantics of your
     * situation permit.  If consistency with encounter order is required, and
     * you are experiencing poor performance or memory utilization with
     * {@code takeWhile()} in parallel pipelines, switching to sequential
     * execution with {@link #sequential()} may improve performance.
     *
     * @param predicate a <a href="package-summary.html#NonInterference">non-interfering</a>,
     *                  <a href="package-summary.html#Statelessness">stateless</a>
     *                  predicate to apply to elements to determine the longest
     *                  prefix of elements.
     * @return the new stream
     * @since 9
     */
    default BooleanStream takeWhile(BooleanPredicate predicate) {
        Objects.requireNonNull(predicate);
        // Reuses the unordered spliterator, which, when encounter is present,
        // is safe to use as longs as it configured not to split
        return StreamUtil.booleanStream(new WhileOps.UnorderedWhileSpliterator.OfBoolean.Taking(spliterator(), true,
                predicate), isParallel()).onClose(this::close);
    }

    /**
     * Returns, if this stream is ordered, a stream consisting of the remaining
     * elements of this stream after dropping the longest prefix of elements
     * that match the given predicate.  Otherwise returns, if this stream is
     * unordered, a stream consisting of the remaining elements of this stream
     * after dropping a subset of elements that match the given predicate.
     *
     * <p>If this stream is ordered then the longest prefix is a contiguous
     * sequence of elements of this stream that match the given predicate.  The
     * first element of the sequence is the first element of this stream, and
     * the element immediately following the last element of the sequence does
     * not match the given predicate.
     *
     * <p>If this stream is unordered, and some (but not all) elements of this
     * stream match the given predicate, then the behavior of this operation is
     * nondeterministic; it is free to drop any subset of matching elements
     * (which includes the empty set).
     *
     * <p>Independent of whether this stream is ordered or unordered if all
     * elements of this stream match the given predicate then this operation
     * drops all elements (the result is an empty stream), or if no elements of
     * the stream match the given predicate then no elements are dropped (the
     * result is the same as the input).
     *
     * <p>This is a <a href="package-summary.html#StreamOps">stateful
     * intermediate operation</a>.
     *
     * @implSpec
     * The default implementation obtains the {@link #spliterator() spliterator}
     * of this stream, wraps that spliterator so as to support the semantics
     * of this operation on traversal, and returns a new stream associated with
     * the wrapped spliterator.  The returned stream preserves the execution
     * booleanacteristics of this stream (namely parallel or sequential execution
     * as per {@link #isParallel()}) but the wrapped spliterator may choose to
     * not support splitting.  When the returned stream is closed, the close
     * handlers for both the returned and this stream are invoked.
     *
     * @apiNote
     * While {@code dropWhile()} is generally a cheap operation on sequential
     * stream pipelines, it can be quite expensive on ordered parallel
     * pipelines, since the operation is constrained to return not just any
     * valid prefix, but the longest prefix of elements in the encounter order.
     * Using an unordered stream source (such as
     * {@link #generate(BooleanSupplier)}) or removing the ordering constraint
     * with {@link #unordered()} may result in significant speedups of
     * {@code dropWhile()} in parallel pipelines, if the semantics of your
     * situation permit.  If consistency with encounter order is required, and
     * you are experiencing poor performance or memory utilization with
     * {@code dropWhile()} in parallel pipelines, switching to sequential
     * execution with {@link #sequential()} may improve performance.
     *
     * @param predicate a <a href="package-summary.html#NonInterference">non-interfering</a>,
     *                  <a href="package-summary.html#Statelessness">stateless</a>
     *                  predicate to apply to elements to determine the longest
     *                  prefix of elements.
     * @return the new stream
     * @since 9
     */
    default BooleanStream dropWhile(BooleanPredicate predicate) {
        Objects.requireNonNull(predicate);
        // Reuses the unordered spliterator, which, when encounter is present,
        // is safe to use as longs as it configured not to split
        return StreamUtil.booleanStream(new WhileOps.UnorderedWhileSpliterator.OfBoolean.Dropping(spliterator(),
                true, predicate), isParallel()).onClose(this::close);
    }

    /**
     * Performs an action for each element of this stream.
     *
     * <p>This is a <a href="package-summary.html#StreamOps">terminal
     * operation</a>.
     *
     * <p>For parallel stream pipelines, this operation does <em>not</em>
     * guarantee to respect the encounter order of the stream, as doing so
     * would sacrifice the benefit of parallelism.  For any given element, the
     * action may be performed at whatever time and in whatever thread the
     * library chooses.  If the action accesses shared state, it is
     * responsible for providing the required synchronization.
     *
     * @param action a <a href="package-summary.html#NonInterference">
     *               non-interfering</a> action to perform on the elements
     */
    void forEach(BooleanConsumer action);

    /**
     * Performs an action for each element of this stream, guaranteeing that
     * each element is processed in encounter order for streams that have a
     * defined encounter order.
     *
     * <p>This is a <a href="package-summary.html#StreamOps">terminal
     * operation</a>.
     *
     * @param action a <a href="package-summary.html#NonInterference">
     *               non-interfering</a> action to perform on the elements
     * @see #forEach(BooleanConsumer)
     */
    void forEachOrdered(BooleanConsumer action);

    /**
     * Returns an array containing the elements of this stream.
     *
     * <p>This is a <a href="package-summary.html#StreamOps">terminal
     * operation</a>.
     *
     * @return an array containing the elements of this stream
     */
    boolean[] toArray();

    /**
     * Performs a <a href="package-summary.html#Reduction">reduction</a> on the
     * elements of this stream, using the provided identity value and an
     * <a href="package-summary.html#Associativity">associative</a>
     * accumulation function, and returns the reduced value.  This is equivalent
     * to:
     * <pre>{@code
     *     Boolean result = identity;
     *     for (Boolean element : this stream)
     *         result = accumulator.applyAsBoolean(result, element)
     *     return result;
     * }</pre>
     *
     * but is not constrained to execute sequentially.
     *
     * <p>The {@code identity} value must be an identity for the accumulator
     * function. This means that for all {@code x},
     * {@code accumulator.apply(identity, x)} is equal to {@code x}.
     * The {@code accumulator} function must be an
     * <a href="package-summary.html#Associativity">associative</a> function.
     *
     * <p>This is a <a href="package-summary.html#StreamOps">terminal
     * operation</a>.
     *
     * @apiNote Sum, min, max, and average are all special cases of reduction.
     * Summing a stream of numbers can be expressed as:

     * <pre>{@code
     *     Boolean sum = numbers.reduce(0, (a, b) -> a+b);
     * }</pre>
     *
     * or more compactly:
     *
     * <pre>{@code
     *     Boolean sum = numbers.reduce(0, Boolean::sum);
     * }</pre>
     *
     * <p>While this may seem a more roundabout way to perform an aggregation
     * compared to simply mutating a running total in a loop, reduction
     * operations parallelize more gracefully, without needing additional
     * synchronization and with greatly reduced risk of data races.
     *
     * @param identity the identity value for the accumulating function
     * @param op an <a href="package-summary.html#Associativity">associative</a>,
     *           <a href="package-summary.html#NonInterference">non-interfering</a>,
     *           <a href="package-summary.html#Statelessness">stateless</a>
     *           function for combining two values
     * @return the result of the reduction
     * @see #sum()
     * @see #min()
     * @see #max()
     * @see #average()
     */
    boolean reduce(boolean identity, BooleanBinaryOperator op);

    /**
     * Performs a <a href="package-summary.html#Reduction">reduction</a> on the
     * elements of this stream, using an
     * <a href="package-summary.html#Associativity">associative</a> accumulation
     * function, and returns an {@code OptionalBoolean} describing the reduced
     * value, if any. This is equivalent to:
     * <pre>{@code
     *     booleans foundAny = false;
     *     Boolean result = null;
     *     for (Boolean element : this stream) {
     *         if (!foundAny) {
     *             foundAny = true;
     *             result = element;
     *         }
     *         else
     *             result = accumulator.applyAsBoolean(result, element);
     *     }
     *     return foundAny ? OptionalBoolean.of(result) : OptionalBoolean.empty();
     * }</pre>
     *
     * but is not constrained to execute sequentially.
     *
     * <p>The {@code accumulator} function must be an
     * <a href="package-summary.html#Associativity">associative</a> function.
     *
     * <p>This is a <a href="package-summary.html#StreamOps">terminal
     * operation</a>.
     *
     * @param op an <a href="package-summary.html#Associativity">associative</a>,
     *           <a href="package-summary.html#NonInterference">non-interfering</a>,
     *           <a href="package-summary.html#Statelessness">stateless</a>
     *           function for combining two values
     * @return the result of the reduction
     * @see #reduce(boolean, BooleanBinaryOperator)
     */
    OptionalBoolean reduce(BooleanBinaryOperator op);

    /**
     * Performs a <a href="package-summary.html#MutableReduction">mutable
     * reduction</a> operation on the elements of this stream.  A mutable
     * reduction is one in which the reduced value is a mutable result container,
     * such as an {@code ArrayList}, and elements are incorporated by updating
     * the state of the result rather than by replacing the result.  This
     * produces a result equivalent to:
     * <pre>{@code
     *     R result = supplier.get();
     *     for (Boolean element : this stream)
     *         accumulator.accept(result, element);
     *     return result;
     * }</pre>
     *
     * <p>Like {@link #reduce(boolean, BooleanBinaryOperator)}, {@code collect}
     * operations can be parallelized without requiring additional
     * synchronization.
     *
     * <p>This is a <a href="package-summary.html#StreamOps">terminal
     * operation</a>.
     *
     * @param <R> the type of the mutable result container
     * @param supplier a function that creates a new mutable result container.
     *                 For a parallel execution, this function may be called
     *                 multiple times and must return a fresh value each time.
     * @param accumulator an <a href="package-summary.html#Associativity">associative</a>,
     *                    <a href="package-summary.html#NonInterference">non-interfering</a>,
     *                    <a href="package-summary.html#Statelessness">stateless</a>
     *                    function that must fold an element into a result
     *                    container.
     * @param combiner an <a href="package-summary.html#Associativity">associative</a>,
     *                    <a href="package-summary.html#NonInterference">non-interfering</a>,
     *                    <a href="package-summary.html#Statelessness">stateless</a>
     *                    function that accepts two partial result containers
     *                    and merges them, which must be compatible with the
     *                    accumulator function.  The combiner function must fold
     *                    the elements from the second result container into the
     *                    first result container.
     * @return the result of the reduction
     * @see Stream#collect(Supplier, BiConsumer, BiConsumer)
     */
    <R> R collect(Supplier<R> supplier, ObjectBooleanBiConsumer<R> accumulator, BiConsumer<R, R> combiner);

    /**
     * Performs a <a href="package-summary.html#MutableReduction">mutable
     * reduction</a> operation on the elements of this stream using a
     * {@code Collector}.  A {@code Collector}
     * encapsulates the functions used as arguments to
     * {@link #collect(Supplier, ObjectBooleanBiConsumer, BiConsumer)}, allowing for reuse of
     * collection strategies and composition of collect operations such as
     * multiple-level grouping or partitioning.
     *
     * <p>If the stream is parallel, and the {@code Collector}
     * is {@link Collector.Characteristics#CONCURRENT concurrent}, and
     * either the stream is unordered or the collector is
     * {@link Collector.Characteristics#UNORDERED unordered},
     * then a concurrent reduction will be performed (see {@link Collector} for
     * details on concurrent reduction.)
     *
     * <p>This is a <a href="package-summary.html#StreamOps">terminal
     * operation</a>.
     *
     * <p>When executed in parallel, multiple intermediate results may be
     * instantiated, populated, and merged so as to maintain isolation of
     * mutable data structures.  Therefore, even when executed in parallel
     * with non-thread-safe data structures (such as {@code ArrayList}), no
     * additional synchronization is needed for a parallel reduction.
     *
     * @apiNote
     * The following will accumulate strings into a List:
     * <pre>{@code
     *     BooleanList asList = booleanStream.collect(CollectingUtil.toBooleanList());
     * }</pre>
     *
     * <p>The following will classify {@code Person} objects by state and city,
     * cascading two {@code Collector}s together:
     * <pre>{@code
     *     Map<String, Map<String, List<Person>>> peopleByStateAndCity
     *         = personStream.collect(Collectors.groupingBy(Person::getState,
     *                                                      Collectors.groupingBy(Person::getCity)));
     * }</pre>
     *
     * @param <R> the type of the result
     * @param <A> the intermediate accumulation type of the {@code Collector}
     * @param collector the {@code Collector} describing the reduction
     * @return the result of the reduction
     * @see #collect(Supplier, ObjectBooleanBiConsumer, BiConsumer)
     * @see Collectors
     */
    <R, A> R collect(BooleanCollector<A, R> collector);

    /**
     * Returns the sum of elements in this stream.
     *
     * Summation is a special case of a <a
     * href="package-summary.html#Reduction">reduction</a>. If
     * Booleaning-point summation were exact, this method would be
     * equivalent to:
     *
     * <pre>{@code
     *     return reduce(0, Boolean::sum);
     * }</pre>
     *
     * However, since Booleaning-point summation is not exact, the above
     * code is not necessarily equivalent to the summation computation
     * done by this method.
     *
     * <p>The value of a Booleaning-point sum is a function both
     * of the input values as well as the order of addition
     * operations. The order of addition operations of this method is
     * intentionally not defined to allow for implementation
     * flexibility to improve the speed and accuracy of the computed
     * result.
     *
     * In particular, this method may be implemented using compensated
     * summation or other technique to reduce the error bound in the
     * numerical sum compared to a simple summation of {@code Boolean}
     * values.
     *
     * Because of the unspecified order of operations and the
     * possibility of using differing summation schemes, the output of
     * this method may vary on the same input elements.
     *
     * <p>Various conditions can result in a non-finite sum being
     * computed. This can occur even if the all the elements
     * being summed are finite. If any element is non-finite,
     * the sum will be non-finite:
     *
     * <ul>
     *
     * <li>If any element is a NaN, then the final sum will be
     * NaN.
     *
     * <li>If the elements contain one or more infinities, the
     * sum will be infinite or NaN.
     *
     * <ul>
     *
     * <li>If the elements contain infinities of opposite sign,
     * the sum will be NaN.
     *
     * <li>If the elements contain infinities of one sign and
     * an intermediate sum overflows to an infinity of the opposite
     * sign, the sum may be NaN.
     *
     * </ul>
     *
     * </ul>
     *
     * It is possible for intermediate sums of finite values to
     * overflow into opposite-signed infinities; if that occurs, the
     * final sum will be NaN even if the elements are all
     * finite.
     *
     * If all the elements are zero, the sign of zero is
     * <em>not</em> guaranteed to be preserved in the final sum.
     *
     * <p>This is a <a href="package-summary.html#StreamOps">terminal
     * operation</a>.
     *
     * @apiNote Elements sorted by increasing absolute magnitude tend
     * to yield more accurate results.
     *
     * @return the sum of elements in this stream
     */
    long sum();

    /**
     * Returns an {@code OptionalBoolean} describing the minimum element of this
     * stream, or an empty OptionalBoolean if this stream is empty.  The minimum
     * element will be {@code Boolean.NaN} if any stream element was NaN. Unlike
     * the numerical comparison operators, this method considers negative zero
     * to be strictly smaller than positive zero. This is a special case of a
     * <a href="package-summary.html#Reduction">reduction</a> and is
     * equivalent to:
     * <pre>{@code
     *     return reduce(Boolean::min);
     * }</pre>
     *
     * <p>This is a <a href="package-summary.html#StreamOps">terminal
     * operation</a>.
     *
     * @return an {@code OptionalBoolean} containing the minimum element of this
     * stream, or an empty optional if the stream is empty
     */
    OptionalBoolean min();

    /**
     * Returns an {@code OptionalBoolean} describing the maximum element of this
     * stream, or an empty OptionalBoolean if this stream is empty.  The maximum
     * element will be {@code Boolean.NaN} if any stream element was NaN. Unlike
     * the numerical comparison operators, this method considers negative zero
     * to be strictly smaller than positive zero. This is a
     * special case of a
     * <a href="package-summary.html#Reduction">reduction</a> and is
     * equivalent to:
     * <pre>{@code
     *     return reduce(Boolean::max);
     * }</pre>
     *
     * <p>This is a <a href="package-summary.html#StreamOps">terminal
     * operation</a>.
     *
     * @return an {@code OptionalBoolean} containing the maximum element of this
     * stream, or an empty optional if the stream is empty
     */
    OptionalBoolean max();

    /**
     * Returns the count of elements in this stream.  This is a special case of
     * a <a href="package-summary.html#Reduction">reduction</a> and is
     * equivalent to:
     * <pre>{@code
     *     return mapToLong(e -> 1L).sum();
     * }</pre>
     *
     * <p>This is a <a href="package-summary.html#StreamOps">terminal operation</a>.
     *
     * @apiNote
     * An implementation may choose to not execute the stream pipeline (either
     * sequentially or in parallel) if it is capable of computing the count
     * directly from the stream source.  In such cases no source elements will
     * be traversed and no intermediate operations will be evaluated.
     * Behavioral parameters with side-effects, which are strongly discouraged
     * except for harmless cases such as debugging, may be affected.  For
     * example, consider the following stream:
     * <pre>{@code
     *     BooleanStream s = BooleanStream.of(1, 2, 3, 4);
     *     longs count = s.peek(System.out::println).count();
     * }</pre>
     * The number of elements covered by the stream source is known and the
     * intermediate operation, {@code peek}, does not inject into or remove
     * elements from the stream (as may be the case for {@code flatMap} or
     * {@code filter} operations).  Thus the count is 4 and there is no need to
     * execute the pipeline and, as a side-effect, print out the elements.
     *
     * @return the count of elements in this stream
     */
    long count();

    /**
     * Returns an {@code OptionalBoolean} describing the arithmetic
     * mean of elements of this stream, or an empty optional if this
     * stream is empty.
     *
     * <p>The computed average can vary numerically and have the
     * special case behavior as computing the sum; see {@link #sum}
     * for details.
     *
     *  <p>The average is a special case of a <a
     *  href="package-summary.html#Reduction">reduction</a>.
     *
     * <p>This is a <a href="package-summary.html#StreamOps">terminal
     * operation</a>.
     *
     * @apiNote Elements sorted by increasing absolute magnitude tend
     * to yield more accurate results.
     *
     * @return an {@code OptionalBoolean} containing the average element of this
     * stream, or an empty optional if the stream is empty
     */
    OptionalDouble average();

    /**
     * Returns whether any elements of this stream match the provided
     * predicate.  May not evaluate the predicate on all elements if not
     * necessary for determining the result.  If the stream is empty then
     * {@code false} is returned and the predicate is not evaluated.
     *
     * <p>This is a <a href="package-summary.html#StreamOps">booleans-circuiting
     * terminal operation</a>.
     *
     * @apiNote
     * This method evaluates the <em>existential quantification</em> of the
     * predicate over the elements of the stream (for some x P(x)).
     *
     * @param predicate a <a href="package-summary.html#NonInterference">non-interfering</a>,
     *                  <a href="package-summary.html#Statelessness">stateless</a>
     *                  predicate to apply to elements of this stream
     * @return {@code true} if any elements of the stream match the provided
     * predicate, otherwise {@code false}
     */
    boolean anyMatch(BooleanPredicate predicate);

    /**
     * Returns whether all elements of this stream match the provided predicate.
     * May not evaluate the predicate on all elements if not necessary for
     * determining the result.  If the stream is empty then {@code true} is
     * returned and the predicate is not evaluated.
     *
     * <p>This is a <a href="package-summary.html#StreamOps">booleans-circuiting
     * terminal operation</a>.
     *
     * @apiNote
     * This method evaluates the <em>universal quantification</em> of the
     * predicate over the elements of the stream (for all x P(x)).  If the
     * stream is empty, the quantification is said to be <em>vacuously
     * satisfied</em> and is always {@code true} (regardless of P(x)).
     *
     * @param predicate a <a href="package-summary.html#NonInterference">non-interfering</a>,
     *                  <a href="package-summary.html#Statelessness">stateless</a>
     *                  predicate to apply to elements of this stream
     * @return {@code true} if either all elements of the stream match the
     * provided predicate or the stream is empty, otherwise {@code false}
     */
    boolean allMatch(BooleanPredicate predicate);

    /**
     * Returns whether no elements of this stream match the provided predicate.
     * May not evaluate the predicate on all elements if not necessary for
     * determining the result.  If the stream is empty then {@code true} is
     * returned and the predicate is not evaluated.
     *
     * <p>This is a <a href="package-summary.html#StreamOps">booleans-circuiting
     * terminal operation</a>.
     *
     * @apiNote
     * This method evaluates the <em>universal quantification</em> of the
     * negated predicate over the elements of the stream (for all x ~P(x)).  If
     * the stream is empty, the quantification is said to be vacuously satisfied
     * and is always {@code true}, regardless of P(x).
     *
     * @param predicate a <a href="package-summary.html#NonInterference">non-interfering</a>,
     *                  <a href="package-summary.html#Statelessness">stateless</a>
     *                  predicate to apply to elements of this stream
     * @return {@code true} if either no elements of the stream match the
     * provided predicate or the stream is empty, otherwise {@code false}
     */
    boolean noneMatch(BooleanPredicate predicate);

    /**
     * Returns an {@link OptionalBoolean} describing the first element of this
     * stream, or an empty {@code OptionalBoolean} if the stream is empty.  If
     * the stream has no encounter order, then any element may be returned.
     *
     * <p>This is a <a href="package-summary.html#StreamOps">booleans-circuiting
     * terminal operation</a>.
     *
     * @return an {@code OptionalBoolean} describing the first element of this
     * stream, or an empty {@code OptionalBoolean} if the stream is empty
     */
    OptionalBoolean findFirst();

    /**
     * Returns an {@link OptionalBoolean} describing some element of the stream,
     * or an empty {@code OptionalBoolean} if the stream is empty.
     *
     * <p>This is a <a href="package-summary.html#StreamOps">booleans-circuiting
     * terminal operation</a>.
     *
     * <p>The behavior of this operation is explicitly nondeterministic; it is
     * free to select any element in the stream.  This is to allow for maximal
     * performance in parallel operations; the cost is that multiple invocations
     * on the same source may not return the same result.  (If a stable result
     * is desired, use {@link #findFirst()} instead.)
     *
     * @return an {@code OptionalBoolean} describing some element of this stream,
     * or an empty {@code OptionalBoolean} if the stream is empty
     * @see #findFirst()
     */
    OptionalBoolean findAny();

    /**
     * Returns a {@code Stream} consisting of the elements of this stream,
     * boxed to {@code Boolean}.
     *
     * <p>This is an <a href="package-summary.html#StreamOps">intermediate
     * operation</a>.
     *
     * @return a {@code Stream} consistent of the elements of this stream,
     * each boxed to a {@code Boolean}
     */
    default Stream<Boolean> boxed() {
        return mapToObj(c -> c);
    }

    @Override
    BooleanStream sequential();

    @Override
    BooleanStream parallel();

    @Override
    BooleanIterator iterator();

    @Override
    BooleanSpliterator spliterator();


    // Static factories

    /**
     * Returns a builder for a {@code BooleanStream}.
     *
     * @return a stream builder
     */
    static Builder builder() {
        return new Builder();
    }

    /**
     * Returns an empty sequential {@code BooleanStream}.
     *
     * @return an empty sequential stream
     */
    static BooleanStream empty() {
        return StreamUtil.booleanStream(SpliteratorUtil.emptyBooleanSpliterator(), false);
    }

    /**
     * Returns a sequential {@code BooleanStream} containing a single element.
     *
     * @param t the single element
     * @return a singleton sequential stream
     */
    static BooleanStream of(boolean t) {
        return StreamUtil.booleanStream(BooleanSpliterators.singleton(t), false);
    }

    /**
     * Returns a sequential ordered stream whose elements are the specified values.
     *
     * @param values the elements of the new stream
     * @return the new stream
     */
    static BooleanStream of(boolean... values) {
        return ArrayUtil.stream(values);
    }

    /**
     * Returns an infinite sequential ordered {@code BooleanStream} produced by iterative
     * application of a function {@code f} to an initial element {@code seed},
     * producing a {@code Stream} consisting of {@code seed}, {@code f(seed)},
     * {@code f(f(seed))}, etc.
     *
     * <p>The first element (position {@code 0}) in the {@code BooleanStream}
     * will be the provided {@code seed}.  For {@code n > 0}, the element at
     * position {@code n}, will be the result of applying the function {@code f}
     *  to the element at position {@code n - 1}.
     *
     * <p>The action of applying {@code f} for one element
     * <a href="../concurrent/package-summary.html#MemoryVisibility"><i>happens-before</i></a>
     * the action of applying {@code f} for subsequent elements.  For any given
     * element the action may be performed in whatever thread the library
     * chooses.
     *
     * @param seed the initial element
     * @param f a function to be applied to the previous element to produce
     *          a new element
     * @return a new sequential {@code BooleanStream}
     */
    static BooleanStream iterate(final boolean seed, final BooleanUnaryOperator f) {
        Objects.requireNonNull(f);

        return StreamUtil.booleanStream(new SpliteratorUtil.BooleanSpliteratorImpl(Long.MAX_VALUE,
                    Spliterator.ORDERED | Spliterator.IMMUTABLE | Spliterator.NONNULL) {
            boolean prev;
            boolean started;

            @Override
            public boolean tryAdvance(BooleanConsumer action) {
                Objects.requireNonNull(action);
                boolean t;
                if (started)
                    t = f.apply(prev);
                else {
                    t = seed;
                    started = true;
                }
                action.accept(prev = t);
                return true;
            }
        }, false);
    }

    /**
     * Returns a sequential ordered {@code BooleanStream} produced by iterative
     * application of the given {@code next} function to an initial element,
     * conditioned on satisfying the given {@code hasNext} predicate.  The
     * stream terminates as soon as the {@code hasNext} predicate returns false.
     *
     * <p>{@code BooleanStream.iterate} should produce the same sequence of elements as
     * produced by the corresponding for-loop:
     * <pre>{@code
     *     for (Boolean index=seed; hasNext.test(index); index = next.applyAsBoolean(index)) {
     *         ...
     *     }
     * }</pre>
     *
     * <p>The resulting sequence may be empty if the {@code hasNext} predicate
     * does not hold on the seed value.  Otherwise the first element will be the
     * supplied {@code seed} value, the next element (if present) will be the
     * result of applying the {@code next} function to the {@code seed} value,
     * and so on iteratively until the {@code hasNext} predicate indicates that
     * the stream should terminate.
     *
     * <p>The action of applying the {@code hasNext} predicate to an element
     * <a href="../concurrent/package-summary.html#MemoryVisibility"><i>happens-before</i></a>
     * the action of applying the {@code next} function to that element.  The
     * action of applying the {@code next} function for one element
     * <i>happens-before</i> the action of applying the {@code hasNext}
     * predicate for subsequent elements.  For any given element an action may
     * be performed in whatever thread the library chooses.
     *
     * @param seed the initial element
     * @param hasNext a predicate to apply to elements to determine when the
     *                stream must terminate.
     * @param next a function to be applied to the previous element to produce
     *             a new element
     * @return a new sequential {@code BooleanStream}
     * @since 9
     */
    static BooleanStream iterate(final boolean seed, final BooleanPredicate hasNext, final BooleanUnaryOperator next) {
        Objects.requireNonNull(next);
        Objects.requireNonNull(hasNext);

        return StreamUtil.booleanStream(new SpliteratorUtil.BooleanSpliteratorImpl(Long.MAX_VALUE,
                Spliterator.ORDERED | Spliterator.IMMUTABLE | Spliterator.NONNULL) {
            boolean prev;
            boolean started, finished;

            @Override
            public boolean tryAdvance(BooleanConsumer action) {
                Objects.requireNonNull(action);

                if (finished) {
                    return false;
                }

                boolean t;

                if (started) {
                    t = next.apply(prev);
                } else {
                    t = seed;
                    started = true;
                }

                if (!hasNext.test(t)) {
                    finished = true;
                    return false;
                }

                action.accept(prev = t);
                return true;
            }

            @Override
            public void forEachRemaining(BooleanConsumer action) {
                Objects.requireNonNull(action);

                if (finished) {
                    return;
                }

                finished = true;
                boolean t = started ? next.apply(prev) : seed;

                while (hasNext.test(t)) {
                    action.accept(t);
                    t = next.apply(t);
                }
            }
        }, false);
    }

    /**
     * Returns an infinite sequential unordered stream where each element is
     * generated by the provided {@code BooleanSupplier}.  This is suitable for
     * generating constant streams, streams of random elements, etc.
     *
     * @param s the {@code BooleanSupplier} for generated elements
     * @return a new infinite sequential unordered {@code BooleanStream}
     */
    static BooleanStream generate(BooleanSupplier s) {
        Objects.requireNonNull(s);
        return StreamUtil.booleanStream(new StreamSpliterators.InfiniteSupplyingSpliterator.OfBoolean(Long.MAX_VALUE, s),
                false);
    }

    /**
     * Creates a lazily concatenated stream whose elements are all the
     * elements of the first stream followed by all the elements of the
     * second stream.  The resulting stream is ordered if both
     * of the input streams are ordered, and parallel if either of the input
     * streams is parallel.  When the resulting stream is closed, the close
     * handlers for both input streams are invoked.
     *
     * <p>This method operates on the two input streams and binds each stream
     * to its source.  As a result subsequent modifications to an input stream
     * source may not be reflected in the concatenated stream result.
     *
     * @implNote
     * Use caution when constructing streams from repeated concatenation.
     * Accessing an element of a deeply concatenated stream can result in deep
     * call chains, or even {@code StackOverflowError}.
     *
     * @apiNote
     * To preserve optimization opportunities this method binds each stream to
     * its source and accepts only two streams as parameters.  For example, the
     * exact size of the concatenated stream source can be computed if the exact
     * size of each input stream source is known.
     * To concatenate more streams without binding, or without nested calls to
     * this method, try creating a stream of streams and flat-mapping with the
     * identity function, for example:
     * <pre>{@code
     *     BooleanStream concat = Stream.of(s1, s2, s3, s4).flatMapToBoolean(s -> s);
     * }</pre>
     *
     * @param a the first stream
     * @param b the second stream
     * @return the concatenation of the two input streams
     */
    static BooleanStream concat(BooleanStream a, BooleanStream b) {
        Objects.requireNonNull(a);
        Objects.requireNonNull(b);

        BooleanSpliterator split = new Streams.ConcatSpliterator.OfBoolean(a.spliterator(), b.spliterator());
        BooleanStream stream = StreamUtil.booleanStream(split, a.isParallel() || b.isParallel());
        return stream.onClose(Streams.composedClose(a, b));
    }

    /**
     * A mutable builder for a {@code BooleanStream}.
     *
     * <p>A stream builder has a lifecycle, which starts in a building
     * phase, during which elements can be added, and then transitions to a built
     * phase, after which elements may not be added.  The built phase
     * begins when the {@link #build()} method is called, which creates an
     * ordered stream whose elements are the elements that were added to the
     * stream builder, in the order they were added.
     *
     * @see BooleanStream#builder()
     * @since 1.8
     */
    final class Builder implements BooleanConsumer, BooleanSpliterator {
        // The first element in the stream
        // valid if count == 1
        private boolean first;

        // The first and subsequent elements in the stream
        // non-null if count == 2
        private SpinedBuffer.OfBoolean buffer;
        // >= 0 when building, < 0 when built
        // -1 == no elements
        // -2 == one element, held by first
        // -3 == two or more elements, held by buffer
        private int count;

        // Spliterator implementation for 0 or 1 element
        // count == -1 for no elements
        // count == -2 for one element held by first

        @Override
        public BooleanSpliterator trySplit() {
            return null;
        }

        @Override
        public long estimateSize() {
            return -count - 1;
        }

        @Override
        public int characteristics() {
            return Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.ORDERED | Spliterator.IMMUTABLE;
        }

        /**
         * Constructor for building a stream of 0 or more elements.
         */
        public Builder() { }

        /**
         * Constructor for a singleton stream.
         *
         * @param t the single element
         */
        public Builder(boolean t) {
            first = t;
            count = -2;
        }

        // StreamBuilder implementation

        @Override
        public void accept(boolean t) {
            if (count == 0) {
                first = t;
                count++;
            }  else if (count > 0) {
                if (buffer == null) {
                    buffer = new SpinedBuffer.OfBoolean();
                    buffer.accept(first);
                    count++;
                }

                buffer.accept(t);
            }
            else {
                throw new IllegalStateException();
            }
        }

        public BooleanStream build() {
            int c = count;

            if (c >= 0) {
                // Switch count to negative value signalling the builder is built
                count = -count - 1;
                // Use this spliterator if 0 or 1 elements, otherwise use
                // the spliterator of the spined buffer
                return (c < 2) ? StreamUtil.booleanStream(this, false)
                        : StreamUtil.booleanStream(buffer.spliterator(), false);
            }

            throw new IllegalStateException();
        }

        // Spliterator implementation for 0 or 1 element
        // count == -1 for no elements
        // count == -2 for one element held by first

        @Override
        public boolean tryAdvance(BooleanConsumer action) {
            Objects.requireNonNull(action);

            if (count == -2) {
                action.accept(first);
                count = -1;
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void forEachRemaining(BooleanConsumer action) {
            Objects.requireNonNull(action);

            if (count == -2) {
                action.accept(first);
                count = -1;
            }
        }

        /**
         * Adds an element to the stream being built.
         *
         * @implSpec
         * The default implementation behaves as if:
         * <pre>{@code
         *     accept(t)
         *     return this;
         * }</pre>
         *
         * @param t the element to add
         * @return {@code this} builder
         * @throws IllegalStateException if the builder has already transitioned
         * to the built state
         */
        public Builder add(boolean t) {
            accept(t);
            return this;
        }
    }
}

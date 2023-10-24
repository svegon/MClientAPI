package com.github.svegon.utils.fuck_modifiers;

/*
 * Copyright (c) 2012, 2015, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

import com.github.svegon.utils.collections.ArrayUtil;
import com.github.svegon.utils.collections.spliterator.SpliteratorUtil;
import it.unimi.dsi.fastutil.booleans.BooleanArrays;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import it.unimi.dsi.fastutil.booleans.BooleanSpliterator;
import it.unimi.dsi.fastutil.bytes.ByteArrays;
import it.unimi.dsi.fastutil.bytes.ByteConsumer;
import it.unimi.dsi.fastutil.bytes.ByteSpliterator;
import it.unimi.dsi.fastutil.chars.CharArrays;
import it.unimi.dsi.fastutil.chars.CharConsumer;
import it.unimi.dsi.fastutil.chars.CharSpliterator;
import it.unimi.dsi.fastutil.doubles.DoubleArrays;
import it.unimi.dsi.fastutil.floats.FloatArrays;
import it.unimi.dsi.fastutil.floats.FloatConsumer;
import it.unimi.dsi.fastutil.floats.FloatSpliterator;
import it.unimi.dsi.fastutil.ints.IntArrays;
import it.unimi.dsi.fastutil.longs.LongArrays;
import it.unimi.dsi.fastutil.shorts.ShortArrays;
import it.unimi.dsi.fastutil.shorts.ShortConsumer;
import it.unimi.dsi.fastutil.shorts.ShortSpliterator;

import java.util.*;
import java.util.concurrent.CountedCompleter;
import java.util.function.*;

/**
 * Factory methods for constructing implementations of {@link Node} and
 * {@link Node.Builder} and their fast specializations.  Fork/Join tasks
 * for collecting output from a {@link PipelineHelper} to a {@link Node} and
 * flattening {@link Node}s.
 *
 * @since 1.8
 */
public final class Nodes {
    private Nodes() {
        throw new Error("no instances");
    }

    // IllegalArgumentException messages
    static final String BAD_SIZE = "Stream size exceeds max array size";

    @SuppressWarnings("rawtypes")
    private static final Node EMPTY_NODE = new EmptyNode.OfRef();
    private static final Node.OfBoolean EMPTY_BOOLEAN_NODE = new EmptyNode.OfBoolean();
    private static final Node.OfByte EMPTY_BYTE_NODE = new EmptyNode.OfByte();
    private static final Node.OfShort EMPTY_SHORT_NODE = new EmptyNode.OfShort();
    private static final Node.OfInt EMPTY_INT_NODE = new EmptyNode.OfInt();
    private static final Node.OfLong EMPTY_LONG_NODE = new EmptyNode.OfLong();
    private static final Node.OfChar EMPTY_CHAR_NODE = new EmptyNode.OfChar();
    private static final Node.OfFloat EMPTY_FLOAT_NODE = new EmptyNode.OfFloat();
    private static final Node.OfDouble EMPTY_DOUBLE_NODE = new EmptyNode.OfDouble();

    /**
     * @return an array generator for an array whose elements are of type T.
     */
    @SuppressWarnings("unchecked")
    static <T> IntFunction<T[]> castingArray() {
        return size -> (T[]) new Object[size];
    }

    // General shape-based node creation methods

    /**
     * Produces an empty node whose count is zero, has no children and no content.
     *
     * @param <T> the type of elements of the created node
     * @param shape the shape of the node to be created
     * @return an empty node.
     */
    @SuppressWarnings("unchecked")
    public static <T> Node<T> emptyNode(StreamShape shape) {
        return switch (shape) {
            case REFERENCE -> (Node<T>) EMPTY_NODE;
            case INT_VALUE -> (Node<T>) EMPTY_INT_NODE;
            case LONG_VALUE -> (Node<T>) EMPTY_LONG_NODE;
            case DOUBLE_VALUE -> (Node<T>) EMPTY_DOUBLE_NODE;
            default -> throw new IllegalStateException("Unknown shape " + shape);
        };
    }

    /**
     * Produces a concatenated {@link Node} that has two or more children.
     * <p>The count of the concatenated node is equal to the sum of the count
     * of each child. Traversal of the concatenated node traverses the content
     * of each child in encounter order of the list of children. Splitting a
     * spliterator obtained from the concatenated node preserves the encounter
     * order of the list of children.
     *
     * <p>The result may be a concatenated node, the input sole node if the size
     * of the list is 1, or an empty node.
     *
     * @param <T> the type of elements of the concatenated node
     * @param shape the shape of the concatenated node to be created
     * @param left the left input node
     * @param right the right input node
     * @return a {@code Node} covering the elements of the input nodes
     * @throws IllegalStateException if all {@link Node} elements of the list
     * are an not instance of type supported by this factory.
     */
    @SuppressWarnings("unchecked")
    public static <T> Node<T> conc(StreamShape shape, Node<T> left, Node<T> right) {
        switch (shape) {
            case REFERENCE:
                return new ConcNode<>(left, right);
            case INT_VALUE:
                return (Node<T>) new ConcNode.OfInt((Node.OfInt) left, (Node.OfInt) right);
            case LONG_VALUE:
                return (Node<T>) new ConcNode.OfLong((Node.OfLong) left, (Node.OfLong) right);
            case DOUBLE_VALUE:
                return (Node<T>) new ConcNode.OfDouble((Node.OfDouble) left, (Node.OfDouble) right);
            default:
                throw new IllegalStateException("Unknown shape " + shape);
        }
    }

    // Reference-based node methods

    /**
     * Produces a {@link Node} describing an array.
     *
     * <p>The node will hold a reference to the array and will not make a copy.
     *
     * @param <T> the type of elements held by the node
     * @param array the array
     * @return a node holding an array
     */
    static <T> Node<T> node(T[] array) {
        return new ArrayNode<>(array);
    }

    /**
     * Produces a {@link Node} describing a {@link Collection}.
     * <p>
     * The node will hold a reference to the collection and will not make a copy.
     *
     * @param <T> the type of elements held by the node
     * @param c the collection
     * @return a node holding a collection
     */
    static <T> Node<T> node(Collection<T> c) {
        return new CollectionNode<>(c);
    }

    /**
     * Produces a {@link Node.Builder}.
     *
     * @param exactSizeIfKnown -1 if a variable size builder is requested,
     * otherwise the exact capacity desired.  A fixed capacity builder will
     * fail if the wrong number of elements are added to the builder.
     * @param generator the array factory
     * @param <T> the type of elements of the node builder
     * @return a {@code Node.Builder}
     */
    public static <T> Node.Builder<T> builder(long exactSizeIfKnown, IntFunction<T[]> generator) {
        return (exactSizeIfKnown >= 0 && exactSizeIfKnown < it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE)
                ? new FixedNodeBuilder<>(exactSizeIfKnown, generator)
                : builder();
    }

    /**
     * Produces a variable size @{link Node.Builder}.
     *
     * @param <T> the type of elements of the node builder
     * @return a {@code Node.Builder}
     */
    public static <T> Node.Builder<T> builder() {
        return new SpinedNodeBuilder<>();
    }

    // Boolean nodes

    /**
     * Produces a {@link Node.OfBoolean} describing a doubles[] array.
     *
     * <p>The node will hold a reference to the array and will not make a copy.
     *
     * @param array the array
     * @return a node holding an array
     */
    public static Node.OfBoolean node(final boolean[] array) {
        return new BooleanArrayNode(array);
    }

    /**
     * Produces a {@link Node.Builder.OfBoolean}.
     *
     * @param exactSizeIfKnown -1 if a variable size builder is requested,
     * otherwise the exact capacity desired.  A fixed capacity builder will
     * fail if the wrong number of elements are added to the builder.
     * @return a {@code Node.Builder.BooleanSpliterator}
     */
    public static Node.Builder.OfBoolean booleanBuilder(long exactSizeIfKnown) {
        return (exactSizeIfKnown >= 0 && exactSizeIfKnown < it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE)
                ? new BooleanFixedNodeBuilder(exactSizeIfKnown) : booleanBuilder();
    }

    /**
     * Produces a variable size @{link Node.Builder.BooleanSpliterator}.
     *
     * @return a {@code Node.Builder.BooleanSpliterator}
     */
    public static Node.Builder.OfBoolean booleanBuilder() {
        return new BooleanSpinedNodeBuilder();
    }

    // Byte nodes

    /**
     * Produces a {@link Node.OfByte} describing a doubles[] array.
     *
     * <p>The node will hold a reference to the array and will not make a copy.
     *
     * @param array the array
     * @return a node holding an array
     */
    static Node.OfByte node(final byte[] array) {
        return new ByteArrayNode(array);
    }

    /**
     * Produces a {@link Node.Builder.OfByte}.
     *
     * @param exactSizeIfKnown -1 if a variable size builder is requested,
     * otherwise the exact capacity desired.  A fixed capacity builder will
     * fail if the wrong number of elements are added to the builder.
     * @return a {@code Node.Builder.ByteSpliterator}
     */
    public static Node.Builder.OfByte byteBuilder(long exactSizeIfKnown) {
        return (exactSizeIfKnown >= 0 && exactSizeIfKnown < it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE)
                ? new ByteFixedNodeBuilder(exactSizeIfKnown) : byteBuilder();
    }

    /**
     * Produces a variable size @{link Node.Builder.ByteSpliterator}.
     *
     * @return a {@code Node.Builder.ByteSpliterator}
     */
    public static Node.Builder.OfByte byteBuilder() {
        return new ByteSpinedNodeBuilder();
    }

    // Short nodes

    /**
     * Produces a {@link Node.OfShort} describing a doubles[] array.
     *
     * <p>The node will hold a reference to the array and will not make a copy.
     *
     * @param array the array
     * @return a node holding an array
     */
    public static Node.OfShort node(final short[] array) {
        return new ShortArrayNode(array);
    }

    /**
     * Produces a {@link Node.Builder.OfShort}.
     *
     * @param exactSizeIfKnown -1 if a variable size builder is requested,
     * otherwise the exact capacity desired.  A fixed capacity builder will
     * fail if the wrong number of elements are added to the builder.
     * @return a {@code Node.Builder.ShortSpliterator}
     */
    public static Node.Builder.OfShort shortBuilder(long exactSizeIfKnown) {
        return (exactSizeIfKnown >= 0 && exactSizeIfKnown < it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE)
                ? new ShortFixedNodeBuilder(exactSizeIfKnown) : shortBuilder();
    }

    /**
     * Produces a variable size @{link Node.Builder.ShortSpliterator}.
     *
     * @return a {@code Node.Builder.ShortSpliterator}
     */
    public static Node.Builder.OfShort shortBuilder() {
        return new ShortSpinedNodeBuilder();
    }

    // Int nodes

    /**
     * Produces a {@link Node.OfInt} describing an ints[] array.
     *
     * <p>The node will hold a reference to the array and will not make a copy.
     *
     * @param array the array
     * @return a node holding an array
     */
    public static Node.OfInt node(int[] array) {
        return new IntArrayNode(array);
    }

    /**
     * Produces a {@link Node.Builder.OfInt}.
     *
     * @param exactSizeIfKnown -1 if a variable size builder is requested,
     * otherwise the exact capacity desired.  A fixed capacity builder will
     * fail if the wrong number of elements are added to the builder.
     * @return a {@code Node.Builder.OfInt}
     */
    static Node.Builder.OfInt intBuilder(long exactSizeIfKnown) {
        return (exactSizeIfKnown >= 0 && exactSizeIfKnown < it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE)
                ? new IntFixedNodeBuilder(exactSizeIfKnown) : intBuilder();
    }

    /**
     * Produces a variable size @{link Node.Builder.OfInt}.
     *
     * @return a {@code Node.Builder.OfInt}
     */
    static Node.Builder.OfInt intBuilder() {
        return new IntSpinedNodeBuilder();
    }

    // Long nodes

    /**
     * Produces a {@link Node.OfLong} describing a longs[] array.
     * <p>
     * The node will hold a reference to the array and will not make a copy.
     *
     * @param array the array
     * @return a node holding an array
     */
    static Node.OfLong node(final long[] array) {
        return new LongArrayNode(array);
    }

    /**
     * Produces a {@link Node.Builder.OfLong}.
     *
     * @param exactSizeIfKnown -1 if a variable size builder is requested,
     * otherwise the exact capacity desired.  A fixed capacity builder will
     * fail if the wrong number of elements are added to the builder.
     * @return a {@code Node.Builder.OfLong}
     */
    static Node.Builder.OfLong longBuilder(long exactSizeIfKnown) {
        return (exactSizeIfKnown >= 0 && exactSizeIfKnown < it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE)
                ? new LongFixedNodeBuilder(exactSizeIfKnown)
                : longBuilder();
    }

    /**
     * Produces a variable size @{link Node.Builder.OfLong}.
     *
     * @return a {@code Node.Builder.OfLong}
     */
    static Node.Builder.OfLong longBuilder() {
        return new LongSpinedNodeBuilder();
    }

    // Char nodes

    /**
     * Produces a {@link Node.OfChar} describing a doubles[] array.
     *
     * <p>The node will hold a reference to the array and will not make a copy.
     *
     * @param array the array
     * @return a node holding an array
     */
    static Node.OfChar node(final char[] array) {
        return new CharArrayNode(array);
    }

    /**
     * Produces a {@link Node.Builder.OfChar}.
     *
     * @param exactSizeIfKnown -1 if a variable size builder is requested,
     * otherwise the exact capacity desired.  A fixed capacity builder will
     * fail if the wrong number of elements are added to the builder.
     * @return a {@code Node.Builder.CharSpliterator}
     */
    public static Node.Builder.OfChar charBuilder(long exactSizeIfKnown) {
        return (exactSizeIfKnown >= 0 && exactSizeIfKnown < it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE)
                ? new CharFixedNodeBuilder(exactSizeIfKnown) : charBuilder();
    }

    /**
     * Produces a variable size @{link Node.Builder.CharSpliterator}.
     *
     * @return a {@code Node.Builder.CharSpliterator}
     */
    public static Node.Builder.OfChar charBuilder() {
        return new CharSpinedNodeBuilder();
    }

    // Float nodes

    /**
     * Produces a {@link Node.OfFloat} describing a doubles[] array.
     *
     * <p>The node will hold a reference to the array and will not make a copy.
     *
     * @param array the array
     * @return a node holding an array
     */
    static Node.OfFloat node(final float[] array) {
        return new FloatArrayNode(array);
    }

    /**
     * Produces a {@link Node.Builder.OfFloat}.
     *
     * @param exactSizeIfKnown -1 if a variable size builder is requested,
     * otherwise the exact capacity desired.  A fixed capacity builder will
     * fail if the wrong number of elements are added to the builder.
     * @return a {@code Node.Builder.FloatSpliterator}
     */
    public static Node.Builder.OfFloat floatBuilder(long exactSizeIfKnown) {
        return (exactSizeIfKnown >= 0 && exactSizeIfKnown < it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE)
                ? new FloatFixedNodeBuilder(exactSizeIfKnown) : floatBuilder();
    }

    /**
     * Produces a variable size @{link Node.Builder.FloatSpliterator}.
     *
     * @return a {@code Node.Builder.FloatSpliterator}
     */
    public static Node.Builder.OfFloat floatBuilder() {
        return new FloatSpinedNodeBuilder();
    }

    // Double nodes

    /**
     * Produces a {@link Node.OfDouble} describing a doubles[] array.
     *
     * <p>The node will hold a reference to the array and will not make a copy.
     *
     * @param array the array
     * @return a node holding an array
     */
    static Node.OfDouble node(final double[] array) {
        return new DoubleArrayNode(array);
    }

    /**
     * Produces a {@link Node.Builder.OfDouble}.
     *
     * @param exactSizeIfKnown -1 if a variable size builder is requested,
     * otherwise the exact capacity desired.  A fixed capacity builder will
     * fail if the wrong number of elements are added to the builder.
     * @return a {@code Node.Builder.FloatSpliterator}
     */
    public static Node.Builder.OfDouble doubleBuilder(long exactSizeIfKnown) {
        return (exactSizeIfKnown >= 0 && exactSizeIfKnown < it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE)
                ? new DoubleFixedNodeBuilder(exactSizeIfKnown)
                : doubleBuilder();
    }

    /**
     * Produces a variable size @{link Node.Builder.FloatSpliterator}.
     *
     * @return a {@code Node.Builder.FloatSpliterator}
     */
    public static Node.Builder.OfDouble doubleBuilder() {
        return new DoubleSpinedNodeBuilder();
    }

    // Parallel evaluation of pipelines to nodes

    /**
     * Collect, in parallel, elements output from a pipeline and describe those
     * elements with a {@link Node}.
     *
     * @implSpec
     * If the exact size of the output from the pipeline is known and the source
     * {@link Spliterator} has the {@link Spliterator#SUBSIZED} characteristic,
     * then a flat {@link Node} will be returned whose content is an array,
     * since the size is known the array can be constructed in advance and
     * output elements can be placed into the array concurrently by leaf
     * tasks at the correct offsets.  If the exact size is not known, output
     * elements are collected into a conc-node whose shape mirrors that
     * of the computation. This conc-node can then be flattened in
     * parallel to produce a flat {@code Node} if desired.
     *
     * @param helper the pipeline helper describing the pipeline
     * @param flattenTree whether a conc node should be flattened into a node
     *                    describing an array before returning
     * @param generator the array generator
     * @return a {@link Node} describing the output elements
     */
    public static <P_IN, P_OUT> Node<P_OUT> collect(PipelineHelper<P_OUT> helper,
                                                    Spliterator<P_IN> spliterator,
                                                    boolean flattenTree,
                                                    IntFunction<P_OUT[]> generator) {
        long size = helper.exactOutputSizeIfKnown(spliterator);
        if (size >= 0 && spliterator.hasCharacteristics(Spliterator.SUBSIZED)) {
            if (size >= it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE)
                throw new IllegalArgumentException(BAD_SIZE);
            P_OUT[] array = generator.apply((int) size);
            new SizedCollectorTask.OfRef<>(spliterator, helper, array).invoke();
            return node(array);
        } else {
            Node<P_OUT> node = new CollectorTask.OfRef<>(helper, generator, spliterator).invoke();
            return flattenTree ? flatten(node, generator) : node;
        }
    }

    /**
     * Collect, in parallel, elements output from a booleans-valued pipeline and
     * describe those elements with a {@link Node.OfBoolean}.
     *
     * @implSpec
     * If the exact size of the output from the pipeline is known and the source
     * {@link Spliterator} has the {@link Spliterator#SUBSIZED} characteristic,
     * then a flat {@link Node} will be returned whose content is an array,
     * since the size is known the array can be constructed in advance and
     * output elements can be placed into the array concurrently by leaf
     * tasks at the correct offsets.  If the exact size is not known, output
     * elements are collected into a conc-node whose shape mirrors that
     * of the computation. This conc-node can then be flattened in
     * parallel to produce a flat {@code Node.OfLong} if desired.
     *
     * @param <P_IN> the type of elements from the source Spliterator
     * @param helper the pipeline helper describing the pipeline
     * @param flattenTree whether a conc node should be flattened into a node
     *                    describing an array before returning
     * @return a {@link Node.OfBoolean} describing the output elements
     */
    public static <P_IN> Node.OfBoolean collectBoolean(PipelineHelper<Boolean> helper, Spliterator<P_IN> spliterator,
                                                    boolean flattenTree) {
        long size = helper.exactOutputSizeIfKnown(spliterator);
        if (size >= 0 && spliterator.hasCharacteristics(Spliterator.SUBSIZED)) {
            if (size >= it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE)
                throw new IllegalArgumentException(BAD_SIZE);
            boolean[] array = new boolean[(int) size];
            new SizedCollectorTask.OfBoolean<>(spliterator, helper, array).invoke();
            return node(array);
        } else {
            Node.OfBoolean node = new CollectorTask.OfBoolean<>(helper, spliterator).invoke();
            return flattenTree ? flattenBoolean(node) : node;
        }
    }

    /**
     * Collect, in parallel, elements output from a bytes-valued pipeline and
     * describe those elements with a {@link Node.OfByte}.
     *
     * @implSpec
     * If the exact size of the output from the pipeline is known and the source
     * {@link Spliterator} has the {@link Spliterator#SUBSIZED} characteristic,
     * then a flat {@link Node} will be returned whose content is an array,
     * since the size is known the array can be constructed in advance and
     * output elements can be placed into the array concurrently by leaf
     * tasks at the correct offsets.  If the exact size is not known, output
     * elements are collected into a conc-node whose shape mirrors that
     * of the computation. This conc-node can then be flattened in
     * parallel to produce a flat {@code Node.OfByte} if desired.
     *
     * @param <P_IN> the type of elements from the source Spliterator
     * @param helper the pipeline helper describing the pipeline
     * @param flattenTree whether a conc node should be flattened into a node
     *                    describing an array before returning
     * @return a {@link Node.OfByte} describing the output elements
     */
    public static <P_IN> Node.OfByte collectByte(PipelineHelper<Byte> helper,
                                                 Spliterator<P_IN> spliterator,
                                                 boolean flattenTree) {
        long size = helper.exactOutputSizeIfKnown(spliterator);
        if (size >= 0 && spliterator.hasCharacteristics(Spliterator.SUBSIZED)) {
            if (size >= it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE)
                throw new IllegalArgumentException(BAD_SIZE);
            byte[] array = new byte[(int) size];
            new SizedCollectorTask.OfByte<>(spliterator, helper, array).invoke();
            return node(array);
        }
        else {
            Node.OfByte node = new CollectorTask.OfByte<>(helper, spliterator).invoke();
            return flattenTree ? flattenByte(node) : node;
        }
    }

    /**
     * Collect, in parallel, elements output from a short-valued pipeline and
     * describe those elements with a {@link Node.OfShort}.
     *
     * @implSpec
     * If the exact size of the output from the pipeline is known and the source
     * {@link Spliterator} has the {@link Spliterator#SUBSIZED} characteristic,
     * then a flat {@link Node} will be returned whose content is an array,
     * since the size is known the array can be constructed in advance and
     * output elements can be placed into the array concurrently by leaf
     * tasks at the correct offsets.  If the exact size is not known, output
     * elements are collected into a conc-node whose shape mirrors that
     * of the computation. This conc-node can then be flattened in
     * parallel to produce a flat {@code Node.OfShort} if desired.
     *
     * @param <P_IN> the type of elements from the source Spliterator
     * @param helper the pipeline helper describing the pipeline
     * @param flattenTree whether a conc node should be flattened into a node
     *                    describing an array before returning
     * @return a {@link Node.OfShort} describing the output elements
     */
    public static <P_IN> Node.OfShort collectShort(PipelineHelper<Short> helper,
                                                 Spliterator<P_IN> spliterator,
                                                 boolean flattenTree) {
        long size = helper.exactOutputSizeIfKnown(spliterator);
        if (size >= 0 && spliterator.hasCharacteristics(Spliterator.SUBSIZED)) {
            if (size >= it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE)
                throw new IllegalArgumentException(BAD_SIZE);
            short[] array = new short[(int) size];
            new SizedCollectorTask.OfShort<>(spliterator, helper, array).invoke();
            return node(array);
        }
        else {
            Node.OfShort node = new CollectorTask.OfShort<>(helper, spliterator).invoke();
            return flattenTree ? flattenShort(node) : node;
        }
    }

    /**
     * Collect, in parallel, elements output from an ints-valued pipeline and
     * describe those elements with a {@link Node.OfInt}.
     *
     * @implSpec
     * If the exact size of the output from the pipeline is known and the source
     * {@link Spliterator} has the {@link Spliterator#SUBSIZED} characteristic,
     * then a flat {@link Node} will be returned whose content is an array,
     * since the size is known the array can be constructed in advance and
     * output elements can be placed into the array concurrently by leaf
     * tasks at the correct offsets.  If the exact size is not known, output
     * elements are collected into a conc-node whose shape mirrors that
     * of the computation. This conc-node can then be flattened in
     * parallel to produce a flat {@code Node.OfInt} if desired.
     *
     * @param <P_IN> the type of elements from the source Spliterator
     * @param helper the pipeline helper describing the pipeline
     * @param flattenTree whether a conc node should be flattened into a node
     *                    describing an array before returning
     * @return a {@link Node.OfInt} describing the output elements
     */
    public static <P_IN> Node.OfInt collectInt(PipelineHelper<Integer> helper,
                                               Spliterator<P_IN> spliterator,
                                               boolean flattenTree) {
        long size = helper.exactOutputSizeIfKnown(spliterator);
        if (size >= 0 && spliterator.hasCharacteristics(Spliterator.SUBSIZED)) {
            if (size >= it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE)
                throw new IllegalArgumentException(BAD_SIZE);
            int[] array = new int[(int) size];
            new SizedCollectorTask.OfInt<>(spliterator, helper, array).invoke();
            return node(array);
        }
        else {
            Node.OfInt node = new CollectorTask.OfInt<>(helper, spliterator).invoke();
            return flattenTree ? flattenInt(node) : node;
        }
    }

    /**
     * Collect, in parallel, elements output from a longs-valued pipeline and
     * describe those elements with a {@link Node.OfLong}.
     *
     * @implSpec
     * If the exact size of the output from the pipeline is known and the source
     * {@link Spliterator} has the {@link Spliterator#SUBSIZED} characteristic,
     * then a flat {@link Node} will be returned whose content is an array,
     * since the size is known the array can be constructed in advance and
     * output elements can be placed into the array concurrently by leaf
     * tasks at the correct offsets.  If the exact size is not known, output
     * elements are collected into a conc-node whose shape mirrors that
     * of the computation. This conc-node can then be flattened in
     * parallel to produce a flat {@code Node.OfLong} if desired.
     *
     * @param <P_IN> the type of elements from the source Spliterator
     * @param helper the pipeline helper describing the pipeline
     * @param flattenTree whether a conc node should be flattened into a node
     *                    describing an array before returning
     * @return a {@link Node.OfLong} describing the output elements
     */
    public static <P_IN> Node.OfLong collectLong(PipelineHelper<Long> helper,
                                                 Spliterator<P_IN> spliterator,
                                                 boolean flattenTree) {
        long size = helper.exactOutputSizeIfKnown(spliterator);
        if (size >= 0 && spliterator.hasCharacteristics(Spliterator.SUBSIZED)) {
            if (size >= it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE)
                throw new IllegalArgumentException(BAD_SIZE);
            long[] array = new long[(int) size];
            new SizedCollectorTask.OfLong<>(spliterator, helper, array).invoke();
            return node(array);
        }
        else {
            Node.OfLong node = new CollectorTask.OfLong<>(helper, spliterator).invoke();
            return flattenTree ? flattenLong(node) : node;
        }
    }

    /**
     * Collect, in parallel, elements output from a char-valued pipeline and
     * describe those elements with a {@link Node.OfChar}.
     *
     * @implSpec
     * If the exact size of the output from the pipeline is known and the source
     * {@link Spliterator} has the {@link Spliterator#SUBSIZED} characteristic,
     * then a flat {@link Node} will be returned whose content is an array,
     * since the size is known the array can be constructed in advance and
     * output elements can be placed into the array concurrently by leaf
     * tasks at the correct offsets.  If the exact size is not known, output
     * elements are collected into a conc-node whose shape mirrors that
     * of the computation. This conc-node can then be flattened in
     * parallel to produce a flat {@code Node.OfChar} if desired.
     *
     * @param <P_IN> the type of elements from the source Spliterator
     * @param helper the pipeline helper describing the pipeline
     * @param flattenTree whether a conc node should be flattened into a node
     *                    describing an array before returning
     * @return a {@link Node.OfChar} describing the output elements
     */
    public static <P_IN> Node.OfChar collectChar(PipelineHelper<Character> helper,
                                                 Spliterator<P_IN> spliterator,
                                                 boolean flattenTree) {
        long size = helper.exactOutputSizeIfKnown(spliterator);
        if (size >= 0 && spliterator.hasCharacteristics(Spliterator.SUBSIZED)) {
            if (size >= it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE)
                throw new IllegalArgumentException(BAD_SIZE);
            char[] array = new char[(int) size];
            new SizedCollectorTask.OfChar<>(spliterator, helper, array).invoke();
            return node(array);
        }
        else {
            Node.OfChar node = new CollectorTask.OfChar<>(helper, spliterator).invoke();
            return flattenTree ? flattenChar(node) : node;
        }
    }

    /**
     * Collect, in parallel, elements output from n doubles-valued pipeline and
     * describe those elements with a {@link Node.OfFloat}.
     *
     * @implSpec
     * If the exact size of the output from the pipeline is known and the source
     * {@link Spliterator} has the {@link Spliterator#SUBSIZED} characteristic,
     * then a flat {@link Node} will be returned whose content is an array,
     * since the size is known the array can be constructed in advance and
     * output elements can be placed into the array concurrently by leaf
     * tasks at the correct offsets.  If the exact size is not known, output
     * elements are collected into a conc-node whose shape mirrors that
     * of the computation. This conc-node can then be flattened in
     * parallel to produce a flat {@code Node.FloatSpliterator} if desired.
     *
     * @param <P_IN> the type of elements from the source Spliterator
     * @param helper the pipeline helper describing the pipeline
     * @param flattenTree whether a conc node should be flattened into a node
     *                    describing an array before returning
     * @return a {@link Node.OfFloat} describing the output elements
     */
    public static <P_IN> Node.OfFloat collectFloat(PipelineHelper<Float> helper,
                                                   Spliterator<P_IN> spliterator,
                                                   boolean flattenTree) {
        long size = helper.exactOutputSizeIfKnown(spliterator);
        if (size >= 0 && spliterator.hasCharacteristics(Spliterator.SUBSIZED)) {
            if (size >= it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE)
                throw new IllegalArgumentException(BAD_SIZE);
            float[] array = new float[(int) size];
            new SizedCollectorTask.OfFloat<>(spliterator, helper, array).invoke();
            return node(array);
        }
        else {
            Node.OfFloat node = new CollectorTask.OfFloat<>(helper, spliterator).invoke();
            return flattenTree ? flattenFloat(node) : node;
        }
    }

    /**
     * Collect, in parallel, elements output from n doubles-valued pipeline and
     * describe those elements with a {@link Node.OfDouble}.
     *
     * @implSpec
     * If the exact size of the output from the pipeline is known and the source
     * {@link Spliterator} has the {@link Spliterator#SUBSIZED} characteristic,
     * then a flat {@link Node} will be returned whose content is an array,
     * since the size is known the array can be constructed in advance and
     * output elements can be placed into the array concurrently by leaf
     * tasks at the correct offsets.  If the exact size is not known, output
     * elements are collected into a conc-node whose shape mirrors that
     * of the computation. This conc-node can then be flattened in
     * parallel to produce a flat {@code Node.FloatSpliterator} if desired.
     *
     * @param <P_IN> the type of elements from the source Spliterator
     * @param helper the pipeline helper describing the pipeline
     * @param flattenTree whether a conc node should be flattened into a node
     *                    describing an array before returning
     * @return a {@link Node.OfDouble} describing the output elements
     */
    public static <P_IN> Node.OfDouble collectDouble(PipelineHelper<Double> helper,
                                                     Spliterator<P_IN> spliterator,
                                                     boolean flattenTree) {
        long size = helper.exactOutputSizeIfKnown(spliterator);
        if (size >= 0 && spliterator.hasCharacteristics(Spliterator.SUBSIZED)) {
            if (size >= it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE)
                throw new IllegalArgumentException(BAD_SIZE);
            double[] array = new double[(int) size];
            new SizedCollectorTask.OfDouble<>(spliterator, helper, array).invoke();
            return node(array);
        }
        else {
            Node.OfDouble node = new CollectorTask.OfDouble<>(helper, spliterator).invoke();
            return flattenTree ? flattenDouble(node) : node;
        }
    }

    // Parallel flattening of nodes

    /**
     * Flatten, in parallel, a {@link Node}.  A flattened node is one that has
     * no children.  If the node is already flat, it is simply returned.
     *
     * @implSpec
     * If a new node is to be created, the generator is used to create an array
     * whose length is {@link Node#count()}.  Then the node tree is traversed
     * and leaf node elements are placed in the array concurrently by leaf tasks
     * at the correct offsets.
     *
     * @param <T> type of elements contained by the node
     * @param node the node to flatten
     * @param generator the array factory used to create array instances
     * @return a flat {@code Node}
     */
    public static <T> Node<T> flatten(Node<T> node, IntFunction<T[]> generator) {
        if (node.getChildCount() > 0) {
            long size = node.count();
            if (size >= it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE)
                throw new IllegalArgumentException(BAD_SIZE);
            T[] array = generator.apply((int) size);
            new ToArrayTask.OfRef<>(node, array, 0).invoke();
            return node(array);
        } else {
            return node;
        }
    }

    /**
     * Flatten, in parallel, a {@link Node.OfDouble}.  A flattened node is one that
     * has no children.  If the node is already flat, it is simply returned.
     *
     * @implSpec
     * If a new node is to be created, a new doubles[] array is created whose length
     * is {@link Node#count()}.  Then the node tree is traversed and leaf node
     * elements are placed in the array concurrently by leaf tasks at the
     * correct offsets.
     *
     * @param node the node to flatten
     * @return a flat {@code Node.FloatSpliterator}
     */
    public static Node.OfBoolean flattenBoolean(Node.OfBoolean node) {
        if (node.getChildCount() > 0) {
            long size = node.count();
            if (size >= it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE)
                throw new IllegalArgumentException(BAD_SIZE);
            boolean[] array = new boolean[(int) size];
            new ToArrayTask.OfBoolean(node, array, 0).invoke();
            return node(array);
        } else {
            return node;
        }
    }

    /**
     * Flatten, in parallel, a {@link Node.OfDouble}.  A flattened node is one that
     * has no children.  If the node is already flat, it is simply returned.
     *
     * @implSpec
     * If a new node is to be created, a new doubles[] array is created whose length
     * is {@link Node#count()}.  Then the node tree is traversed and leaf node
     * elements are placed in the array concurrently by leaf tasks at the
     * correct offsets.
     *
     * @param node the node to flatten
     * @return a flat {@code Node.FloatSpliterator}
     */
    public static Node.OfByte flattenByte(Node.OfByte node) {
        if (node.getChildCount() > 0) {
            long size = node.count();
            if (size >= it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE)
                throw new IllegalArgumentException(BAD_SIZE);
            byte[] array = new byte[(int) size];
            new ToArrayTask.OfByte(node, array, 0).invoke();
            return node(array);
        } else {
            return node;
        }
    }

    /**
     * Flatten, in parallel, a {@link Node.OfDouble}.  A flattened node is one that
     * has no children.  If the node is already flat, it is simply returned.
     *
     * @implSpec
     * If a new node is to be created, a new doubles[] array is created whose length
     * is {@link Node#count()}.  Then the node tree is traversed and leaf node
     * elements are placed in the array concurrently by leaf tasks at the
     * correct offsets.
     *
     * @param node the node to flatten
     * @return a flat {@code Node.FloatSpliterator}
     */
    public static Node.OfShort flattenShort(Node.OfShort node) {
        if (node.getChildCount() > 0) {
            long size = node.count();
            if (size >= it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE)
                throw new IllegalArgumentException(BAD_SIZE);
            short[] array = new short[(int) size];
            new ToArrayTask.OfShort(node, array, 0).invoke();
            return node(array);
        } else {
            return node;
        }
    }

    /**
     * Flatten, in parallel, a {@link Node.OfInt}.  A flattened node is one that
     * has no children.  If the node is already flat, it is simply returned.
     *
     * @implSpec
     * If a new node is to be created, a new ints[] array is created whose length
     * is {@link Node#count()}.  Then the node tree is traversed and leaf node
     * elements are placed in the array concurrently by leaf tasks at the
     * correct offsets.
     *
     * @param node the node to flatten
     * @return a flat {@code Node.OfInt}
     */
    public static Node.OfInt flattenInt(Node.OfInt node) {
        if (node.getChildCount() > 0) {
            long size = node.count();

            if (size >= it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE) {
                throw new IllegalArgumentException(BAD_SIZE);
            }

            int[] array = new int[(int) size];
            new ToArrayTask.OfInt(node, array, 0).invoke();
            return node(array);
        } else {
            return node;
        }
    }

    /**
     * Flatten, in parallel, a {@link Node.OfLong}.  A flattened node is one that
     * has no children.  If the node is already flat, it is simply returned.
     *
     * @implSpec
     * If a new node is to be created, a new longs[] array is created whose length
     * is {@link Node#count()}.  Then the node tree is traversed and leaf node
     * elements are placed in the array concurrently by leaf tasks at the
     * correct offsets.
     *
     * @param node the node to flatten
     * @return a flat {@code Node.OfLong}
     */
    public static Node.OfLong flattenLong(Node.OfLong node) {
        if (node.getChildCount() > 0) {
            long size = node.count();
            if (size >= it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE)
                throw new IllegalArgumentException(BAD_SIZE);
            long[] array = new long[(int) size];
            new ToArrayTask.OfLong(node, array, 0).invoke();
            return node(array);
        } else {
            return node;
        }
    }

    /**
     * Flatten, in parallel, a {@link Node.OfChar}.  A flattened node is one that
     * has no children.  If the node is already flat, it is simply returned.
     *
     * @implSpec
     * If a new node is to be created, a new char[] array is created whose length
     * is {@link Node#count()}.  Then the node tree is traversed and leaf node
     * elements are placed in the array concurrently by leaf tasks at the
     * correct offsets.
     *
     * @param node the node to flatten
     * @return a flat {@code Node.CharSpliterator}
     */
    public static Node.OfChar flattenChar(Node.OfChar node) {
        if (node.getChildCount() > 0) {
            long size = node.count();
            if (size >= it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE)
                throw new IllegalArgumentException(BAD_SIZE);
            char[] array = new char[(int) size];
            new ToArrayTask.OfChar(node, array, 0).invoke();
            return node(array);
        } else {
            return node;
        }
    }

    /**
     * Flatten, in parallel, a {@link Node.OfFloat}.  A flattened node is one that
     * has no children.  If the node is already flat, it is simply returned.
     *
     * @implSpec
     * If a new node is to be created, a new doubles[] array is created whose length
     * is {@link Node#count()}.  Then the node tree is traversed and leaf node
     * elements are placed in the array concurrently by leaf tasks at the
     * correct offsets.
     *
     * @param node the node to flatten
     * @return a flat {@code Node.FloatSpliterator}
     */
    public static Node.OfFloat flattenFloat(Node.OfFloat node) {
        if (node.getChildCount() > 0) {
            long size = node.count();
            if (size >= it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE)
                throw new IllegalArgumentException(BAD_SIZE);
            float[] array = new float[(int) size];
            new ToArrayTask.OfFloat(node, array, 0).invoke();
            return node(array);
        } else {
            return node;
        }
    }

    /**
     * Flatten, in parallel, a {@link Node.OfDouble}.  A flattened node is one that
     * has no children.  If the node is already flat, it is simply returned.
     *
     * @implSpec
     * If a new node is to be created, a new doubles[] array is created whose length
     * is {@link Node#count()}.  Then the node tree is traversed and leaf node
     * elements are placed in the array concurrently by leaf tasks at the
     * correct offsets.
     *
     * @param node the node to flatten
     * @return a flat {@code Node.FloatSpliterator}
     */
    public static Node.OfDouble flattenDouble(Node.OfDouble node) {
        if (node.getChildCount() > 0) {
            long size = node.count();
            if (size >= it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE)
                throw new IllegalArgumentException(BAD_SIZE);
            double[] array = new double[(int) size];
            new ToArrayTask.OfDouble(node, array, 0).invoke();
            return node(array);
        } else {
            return node;
        }
    }

    // Implementations

    private abstract static class EmptyNode<T, T_ARR, T_CONS> implements Node<T> {
        EmptyNode() { }

        @Override
        public T[] asArray(IntFunction<T[]> generator) {
            return generator.apply(0);
        }

        public void copyInto(T_ARR array, int offset) { }

        @Override
        public long count() {
            return 0;
        }

        public void forEach(T_CONS consumer) { }

        private static class OfRef<T> extends EmptyNode<T, T[], Consumer<? super T>> {
            private OfRef() {
                super();
            }

            @Override
            public Spliterator<T> spliterator() {
                return Spliterators.emptySpliterator();
            }
        }

        private static final class OfBoolean extends EmptyNode<Boolean, boolean[], BooleanConsumer>
                implements Node.OfBoolean {

            private OfBoolean() { } // Avoid creation of special accessor

            @Override
            public BooleanSpliterator spliterator() {
                return SpliteratorUtil.emptyBooleanSpliterator();
            }

            @Override
            public boolean[] asPrimitiveArray() {
                return BooleanArrays.EMPTY_ARRAY;
            }
        }

        private static final class OfByte extends EmptyNode<Byte, byte[], ByteConsumer> implements Node.OfByte {
            private OfByte() { } // Avoid creation of special accessor

            @Override
            public ByteSpliterator spliterator() {
                return SpliteratorUtil.emptyByteSpliterator();
            }

            @Override
            public byte[] asPrimitiveArray() {
                return ByteArrays.EMPTY_ARRAY;
            }
        }

        private static final class OfShort extends EmptyNode<Short, short[], ShortConsumer> implements Node.OfShort {
            private OfShort() { } // Avoid creation of special accessor

            @Override
            public ShortSpliterator spliterator() {
                return SpliteratorUtil.emptyShortSpliterator();
            }

            @Override
            public short[] asPrimitiveArray() {
                return ShortArrays.EMPTY_ARRAY;
            }
        }

        private static final class OfInt
                extends EmptyNode<Integer, int[], IntConsumer>
                implements Node.OfInt {

            OfInt() { } // Avoid creation of special accessor

            @Override
            public Spliterator.OfInt spliterator() {
                return Spliterators.emptyIntSpliterator();
            }

            @Override
            public int[] asPrimitiveArray() {
                return IntArrays.EMPTY_ARRAY;
            }
        }

        private static final class OfLong
                extends EmptyNode<Long, long[], LongConsumer>
                implements Node.OfLong {

            OfLong() { } // Avoid creation of special accessor

            @Override
            public Spliterator.OfLong spliterator() {
                return Spliterators.emptyLongSpliterator();
            }

            @Override
            public long[] asPrimitiveArray() {
                return LongArrays.EMPTY_ARRAY;
            }
        }

        private static final class OfChar extends EmptyNode<Character, char[], CharConsumer> implements Node.OfChar {
            private OfChar() { } // Avoid creation of special accessor

            @Override
            public CharSpliterator spliterator() {
                return SpliteratorUtil.emptyCharSpliterator();
            }

            @Override
            public char[] asPrimitiveArray() {
                return CharArrays.EMPTY_ARRAY;
            }
        }

        private static final class OfFloat extends EmptyNode<Float, float[], FloatConsumer> implements Node.OfFloat {
            private OfFloat() { } // Avoid creation of special accessor

            @Override
            public FloatSpliterator spliterator() {
                return SpliteratorUtil.emptyFloatSpliterator();
            }

            @Override
            public float[] asPrimitiveArray() {
                return FloatArrays.EMPTY_ARRAY;
            }
        }

        private static final class OfDouble
                extends EmptyNode<Double, double[], DoubleConsumer>
                implements Node.OfDouble {

            OfDouble() { } // Avoid creation of special accessor

            @Override
            public Spliterator.OfDouble spliterator() {
                return Spliterators.emptyDoubleSpliterator();
            }

            @Override
            public double[] asPrimitiveArray() {
                return DoubleArrays.EMPTY_ARRAY;
            }
        }
    }

    /** Node class for a reference array */
    private static class ArrayNode<T> implements Node<T> {
        final T[] array;
        int curSize;

        @SuppressWarnings("unchecked")
        ArrayNode(long size, IntFunction<T[]> generator) {
            if (size >= it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE)
                throw new IllegalArgumentException(BAD_SIZE);
            this.array = generator.apply((int) size);
            this.curSize = 0;
        }

        ArrayNode(T[] array) {
            this.array = array;
            this.curSize = array.length;
        }

        // Node

        @Override
        public Spliterator<T> spliterator() {
            return Arrays.spliterator(array, 0, curSize);
        }

        @Override
        public void copyInto(T[] dest, int destOffset) {
            System.arraycopy(array, 0, dest, destOffset, curSize);
        }

        @Override
        public T[] asArray(IntFunction<T[]> generator) {
            if (array.length == curSize) {
                return array;
            } else {
                throw new IllegalStateException();
            }
        }

        @Override
        public long count() {
            return curSize;
        }

        @Override
        public void forEach(Consumer<? super T> consumer) {
            for (int i = 0; i < curSize; i++) {
                consumer.accept(array[i]);
            }
        }

        //

        @Override
        public String toString() {
            return String.format("ArrayNode[%d][%s]",
                    array.length - curSize, Arrays.toString(array));
        }
    }

    /** Node class for a Collection */
    private static final class CollectionNode<T> implements Node<T> {
        private final Collection<T> c;

        CollectionNode(Collection<T> c) {
            this.c = c;
        }

        // Node

        @Override
        public Spliterator<T> spliterator() {
            return c.stream().spliterator();
        }

        @Override
        public void copyInto(T[] array, int offset) {
            for (T t : c)
                array[offset++] = t;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T[] asArray(IntFunction<T[]> generator) {
            return c.toArray(generator.apply(c.size()));
        }

        @Override
        public long count() {
            return c.size();
        }

        @Override
        public void forEach(Consumer<? super T> consumer) {
            c.forEach(consumer);
        }

        //

        @Override
        public String toString() {
            return String.format("CollectionNode[%d][%s]", c.size(), c);
        }
    }

    /**
     * Node class for an internal node with two or more children
     */
    private abstract static class AbstractConcNode<T, T_NODE extends Node<T>> implements Node<T> {
        protected final T_NODE left;
        protected final T_NODE right;
        private final long size;

        AbstractConcNode(T_NODE left, T_NODE right) {
            this.left = left;
            this.right = right;
            // The Node count will be required when the Node spliterator is
            // obtained and it is cheaper to aggressively calculate bottom up
            // as the tree is built rather than later on from the top down
            // traversing the tree
            this.size = left.count() + right.count();
        }

        @Override
        public int getChildCount() {
            return 2;
        }

        @Override
        public T_NODE getChild(int i) {
            if (i == 0) return left;
            if (i == 1) return right;
            throw new IndexOutOfBoundsException();
        }

        @Override
        public long count() {
            return size;
        }
    }

    public static final class ConcNode<T>
            extends AbstractConcNode<T, Node<T>>
            implements Node<T> {

        public ConcNode(Node<T> left, Node<T> right) {
            super(left, right);
        }

        @Override
        public Spliterator<T> spliterator() {
            return new InternalNodeSpliterator.OfRef<>(this);
        }

        @Override
        public void copyInto(T[] array, int offset) {
            Objects.requireNonNull(array);
            left.copyInto(array, offset);
            // Cast to ints is safe since it is the callers responsibility to
            // ensure that there is sufficient room in the array
            right.copyInto(array, offset + (int) left.count());
        }

        @Override
        public T[] asArray(IntFunction<T[]> generator) {
            long size = count();
            if (size >= it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE)
                throw new IllegalArgumentException(BAD_SIZE);
            T[] array = generator.apply((int) size);
            copyInto(array, 0);
            return array;
        }

        @Override
        public void forEach(Consumer<? super T> consumer) {
            left.forEach(consumer);
            right.forEach(consumer);
        }

        @Override
        public Node<T> truncate(long from, long to, IntFunction<T[]> generator) {
            if (from == 0 && to == count())
                return this;
            long leftCount = left.count();
            if (from >= leftCount)
                return right.truncate(from - leftCount, to - leftCount, generator);
            else if (to <= leftCount)
                return left.truncate(from, to, generator);
            else {
                return Nodes.conc(getShape(), left.truncate(from, leftCount, generator),
                        right.truncate(0, to - leftCount, generator));
            }
        }

        @Override
        public String toString() {
            if (count() < 32) {
                return String.format("ConcNode[%s.%s]", left, right);
            } else {
                return String.format("ConcNode[size=%d]", count());
            }
        }

        public abstract static class OfPrimitive<E, T_CONS, T_ARR,
                T_SPLITR extends Spliterator.OfPrimitive<E, T_CONS, T_SPLITR>,
                T_NODE extends Node.OfPrimitive<E, T_CONS, T_ARR, T_SPLITR, T_NODE>>
                extends AbstractConcNode<E, T_NODE> implements Node.OfPrimitive<E, T_CONS, T_ARR, T_SPLITR, T_NODE> {
            public OfPrimitive(T_NODE left, T_NODE right) {
                super(left, right);
            }

            @Override
            public void forEach(T_CONS consumer) {
                left.forEach(consumer);
                right.forEach(consumer);
            }

            @Override
            public void copyInto(T_ARR array, int offset) {
                left.copyInto(array, offset);
                // Cast to ints is safe since it is the callers responsibility to
                // ensure that there is sufficient room in the array
                right.copyInto(array, offset + (int) left.count());
            }

            @Override
            public T_ARR asPrimitiveArray() {
                long size = count();
                if (size >= it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE)
                    throw new IllegalArgumentException(BAD_SIZE);
                T_ARR array = newArray((int) size);
                copyInto(array, 0);
                return array;
            }

            @Override
            public String toString() {
                if (count() < 32)
                    return String.format("%s[%s.%s]", this.getClass().getName(), left, right);
                else
                    return String.format("%s[size=%d]", this.getClass().getName(), count());
            }
        }

        public static final class OfBoolean extends OfPrimitive<Boolean, BooleanConsumer, boolean[],
                BooleanSpliterator, Node.OfBoolean> implements Node.OfBoolean {

            OfBoolean(Node.OfBoolean left, Node.OfBoolean right) {
                super(left, right);
            }

            @Override
            public BooleanSpliterator spliterator() {
                return new InternalNodeSpliterator.OfBoolean(this);
            }
        }

        public static final class OfByte extends OfPrimitive<Byte, ByteConsumer, byte[],
                ByteSpliterator, Node.OfByte> implements Node.OfByte {

            OfByte(Node.OfByte left, Node.OfByte right) {
                super(left, right);
            }

            @Override
            public ByteSpliterator spliterator() {
                return new InternalNodeSpliterator.OfByte(this);
            }
        }

        public static final class OfShort extends OfPrimitive<Short, ShortConsumer, short[],
                ShortSpliterator, Node.OfShort> implements Node.OfShort {

            OfShort(Node.OfShort left, Node.OfShort right) {
                super(left, right);
            }

            @Override
            public ShortSpliterator spliterator() {
                return new InternalNodeSpliterator.OfShort(this);
            }
        }

        public static final class OfInt extends OfPrimitive<Integer, IntConsumer, int[], Spliterator.OfInt, Node.OfInt>
                implements Node.OfInt {
            public OfInt(Node.OfInt left, Node.OfInt right) {
                super(left, right);
            }

            @Override
            public Spliterator.OfInt spliterator() {
                return new InternalNodeSpliterator.OfInt(this);
            }
        }

        public static final class OfLong
                extends OfPrimitive<Long, LongConsumer, long[], Spliterator.OfLong, Node.OfLong>
                implements Node.OfLong {

            OfLong(Node.OfLong left, Node.OfLong right) {
                super(left, right);
            }

            @Override
            public Spliterator.OfLong spliterator() {
                return new InternalNodeSpliterator.OfLong(this);
            }
        }

        public static final class OfChar extends OfPrimitive<Character, CharConsumer, char[],
                CharSpliterator, Node.OfChar> implements Node.OfChar {

            OfChar(Node.OfChar left, Node.OfChar right) {
                super(left, right);
            }

            @Override
            public CharSpliterator spliterator() {
                return new InternalNodeSpliterator.OfChar(this);
            }
        }

        public static final class OfFloat extends OfPrimitive<Float, FloatConsumer, float[],
                FloatSpliterator, Node.OfFloat> implements Node.OfFloat {

            OfFloat(Node.OfFloat left, Node.OfFloat right) {
                super(left, right);
            }

            @Override
            public FloatSpliterator spliterator() {
                return new InternalNodeSpliterator.OfFloat(this);
            }
        }

        public static final class OfDouble
                extends OfPrimitive<Double, DoubleConsumer, double[], Spliterator.OfDouble, Node.OfDouble>
                implements Node.OfDouble {

            OfDouble(Node.OfDouble left, Node.OfDouble right) {
                super(left, right);
            }

            @Override
            public Spliterator.OfDouble spliterator() {
                return new InternalNodeSpliterator.OfDouble(this);
            }
        }
    }

    /** Abstract class for spliterator for all internal node classes */
    private abstract static class InternalNodeSpliterator<T,
            S extends Spliterator<T>,
            N extends Node<T>>
            implements Spliterator<T> {
        // Node we are pointing to
        // null if full traversal has occurred
        N curNode;

        // next child of curNode to consume
        int curChildIndex;

        // The spliterator of the curNode if that node is last and has no children.
        // This spliterator will be delegated to for splitting and traversing.
        // null if curNode has children
        S lastNodeSpliterator;

        // spliterator used while traversing with tryAdvance
        // null if no partial traversal has occurred
        S tryAdvanceSpliterator;

        // node stack used when traversing to search and find leaf nodes
        // null if no partial traversal has occurred
        Deque<N> tryAdvanceStack;

        InternalNodeSpliterator(N curNode) {
            this.curNode = curNode;
        }

        /**
         * Initiate a stack containing, in left-to-right order, the child nodes
         * covered by this spliterator
         */
        @SuppressWarnings("unchecked")
        protected final Deque<N> initStack() {
            // Bias size to the case where leaf nodes are close to this node
            // 8 is the minimum initial capacity for the ArrayDeque implementation
            Deque<N> stack = new ArrayDeque<>(8);
            for (int i = curNode.getChildCount() - 1; i >= curChildIndex; i--)
                stack.addFirst((N) curNode.getChild(i));
            return stack;
        }

        /**
         * Depth first search, in left-to-right order, of the node tree, using
         * an explicit stack, to find the next non-empty leaf node.
         */
        @SuppressWarnings("unchecked")
        protected final N findNextLeafNode(Deque<N> stack) {
            N n = null;
            while ((n = stack.pollFirst()) != null) {
                if (n.getChildCount() == 0) {
                    if (n.count() > 0)
                        return n;
                } else {
                    for (int i = n.getChildCount() - 1; i >= 0; i--)
                        stack.addFirst((N) n.getChild(i));
                }
            }

            return null;
        }

        @SuppressWarnings("unchecked")
        protected final boolean initTryAdvance() {
            if (curNode == null)
                return false;

            if (tryAdvanceSpliterator == null) {
                if (lastNodeSpliterator == null) {
                    // Initiate the node stack
                    tryAdvanceStack = initStack();
                    N leaf = findNextLeafNode(tryAdvanceStack);
                    if (leaf != null)
                        tryAdvanceSpliterator = (S) leaf.spliterator();
                    else {
                        // A non-empty leaf node was not found
                        // No elements to traverse
                        curNode = null;
                        return false;
                    }
                }
                else
                    tryAdvanceSpliterator = lastNodeSpliterator;
            }
            return true;
        }

        @Override
        @SuppressWarnings("unchecked")
        public final S trySplit() {
            if (curNode == null || tryAdvanceSpliterator != null)
                return null; // Cannot split if fully or partially traversed
            else if (lastNodeSpliterator != null)
                return (S) lastNodeSpliterator.trySplit();
            else if (curChildIndex < curNode.getChildCount() - 1)
                return (S) curNode.getChild(curChildIndex++).spliterator();
            else {
                curNode = (N) curNode.getChild(curChildIndex);
                if (curNode.getChildCount() == 0) {
                    lastNodeSpliterator = (S) curNode.spliterator();
                    return (S) lastNodeSpliterator.trySplit();
                }
                else {
                    curChildIndex = 0;
                    return (S) curNode.getChild(curChildIndex++).spliterator();
                }
            }
        }

        @Override
        public final long estimateSize() {
            if (curNode == null)
                return 0;

            // Will not reflect the effects of partial traversal.
            // This is compliant with the specification
            if (lastNodeSpliterator != null)
                return lastNodeSpliterator.estimateSize();
            else {
                long size = 0;
                for (int i = curChildIndex; i < curNode.getChildCount(); i++)
                    size += curNode.getChild(i).count();
                return size;
            }
        }

        @Override
        public final int characteristics() {
            return Spliterator.SIZED;
        }

        private static final class OfRef<T>
                extends InternalNodeSpliterator<T, Spliterator<T>, Node<T>> {

            OfRef(Node<T> curNode) {
                super(curNode);
            }

            @Override
            public boolean tryAdvance(Consumer<? super T> consumer) {
                if (!initTryAdvance())
                    return false;

                boolean hasNext = tryAdvanceSpliterator.tryAdvance(consumer);
                if (!hasNext) {
                    if (lastNodeSpliterator == null) {
                        // Advance to the spliterator of the next non-empty leaf node
                        Node<T> leaf = findNextLeafNode(tryAdvanceStack);
                        if (leaf != null) {
                            tryAdvanceSpliterator = leaf.spliterator();
                            // Since the node is not-empty the spliterator can be advanced
                            return tryAdvanceSpliterator.tryAdvance(consumer);
                        }
                    }
                    // No more elements to traverse
                    curNode = null;
                }
                return hasNext;
            }

            @Override
            public void forEachRemaining(Consumer<? super T> consumer) {
                if (curNode == null)
                    return;

                if (tryAdvanceSpliterator == null) {
                    if (lastNodeSpliterator == null) {
                        Deque<Node<T>> stack = initStack();
                        Node<T> leaf;
                        while ((leaf = findNextLeafNode(stack)) != null) {
                            leaf.forEach(consumer);
                        }
                        curNode = null;
                    }
                    else
                        lastNodeSpliterator.forEachRemaining(consumer);
                }
                else
                    while(tryAdvance(consumer)) { }
            }
        }

        private abstract static class OfPrimitive<T, T_CONS, T_ARR,
                T_SPLITR extends Spliterator.OfPrimitive<T, T_CONS, T_SPLITR>,
                N extends Node.OfPrimitive<T, T_CONS, T_ARR, T_SPLITR, N>>
                extends InternalNodeSpliterator<T, T_SPLITR, N>
                implements Spliterator.OfPrimitive<T, T_CONS, T_SPLITR> {

            OfPrimitive(N cur) {
                super(cur);
            }

            @Override
            public boolean tryAdvance(T_CONS consumer) {
                if (!initTryAdvance())
                    return false;

                boolean hasNext = tryAdvanceSpliterator.tryAdvance(consumer);
                if (!hasNext) {
                    if (lastNodeSpliterator == null) {
                        // Advance to the spliterator of the next non-empty leaf node
                        N leaf = findNextLeafNode(tryAdvanceStack);
                        if (leaf != null) {
                            tryAdvanceSpliterator = leaf.spliterator();
                            // Since the node is not-empty the spliterator can be advanced
                            return tryAdvanceSpliterator.tryAdvance(consumer);
                        }
                    }
                    // No more elements to traverse
                    curNode = null;
                }
                return hasNext;
            }

            @Override
            public void forEachRemaining(T_CONS consumer) {
                if (curNode == null)
                    return;

                if (tryAdvanceSpliterator == null) {
                    if (lastNodeSpliterator == null) {
                        Deque<N> stack = initStack();
                        N leaf;
                        while ((leaf = findNextLeafNode(stack)) != null) {
                            leaf.forEach(consumer);
                        }
                        curNode = null;
                    }
                    else
                        lastNodeSpliterator.forEachRemaining(consumer);
                }
                else
                    while(tryAdvance(consumer)) { }
            }
        }

        private static final class OfBoolean extends OfPrimitive<Boolean, BooleanConsumer,
                boolean[], BooleanSpliterator, Node.OfBoolean> implements BooleanSpliterator {

            OfBoolean(Node.OfBoolean cur) {
                super(cur);
            }
        }

        private static final class OfByte extends OfPrimitive<Byte, ByteConsumer,
                byte[], ByteSpliterator, Node.OfByte> implements ByteSpliterator {

            OfByte(Node.OfByte cur) {
                super(cur);
            }
        }

        private static final class OfShort extends OfPrimitive<Short, ShortConsumer,
                short[], ShortSpliterator, Node.OfShort> implements ShortSpliterator {

            OfShort(Node.OfShort cur) {
                super(cur);
            }
        }

        private static final class OfInt
                extends OfPrimitive<Integer, IntConsumer, int[], Spliterator.OfInt, Node.OfInt>
                implements Spliterator.OfInt {

            OfInt(Node.OfInt cur) {
                super(cur);
            }
        }

        private static final class OfLong
                extends OfPrimitive<Long, LongConsumer, long[], Spliterator.OfLong, Node.OfLong>
                implements Spliterator.OfLong {

            OfLong(Node.OfLong cur) {
                super(cur);
            }
        }

        private static final class OfChar extends OfPrimitive<Character, CharConsumer,
                char[], CharSpliterator, Node.OfChar> implements CharSpliterator {

            OfChar(Node.OfChar cur) {
                super(cur);
            }
        }

        private static final class OfFloat extends OfPrimitive<Float, FloatConsumer,
                float[], FloatSpliterator, Node.OfFloat> implements FloatSpliterator {

            OfFloat(Node.OfFloat cur) {
                super(cur);
            }
        }

        private static final class OfDouble
                extends OfPrimitive<Double, DoubleConsumer, double[], Spliterator.OfDouble, Node.OfDouble>
                implements Spliterator.OfDouble {

            OfDouble(Node.OfDouble cur) {
                super(cur);
            }
        }
    }

    /**
     * Fixed-sized builder class for reference nodes
     */
    private static final class FixedNodeBuilder<T>
            extends ArrayNode<T>
            implements Node.Builder<T> {

        FixedNodeBuilder(long size, IntFunction<T[]> generator) {
            super(size, generator);
            assert size < it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE;
        }

        @Override
        public Node<T> build() {
            if (curSize < array.length)
                throw new IllegalStateException(String.format("Current size %d is less than fixed size %d",
                        curSize, array.length));
            return this;
        }

        @Override
        public void begin(long size) {
            if (size != array.length)
                throw new IllegalStateException(String.format("Begin size %d is not equal to fixed size %d",
                        size, array.length));
            curSize = 0;
        }

        @Override
        public void accept(T t) {
            if (curSize < array.length) {
                array[curSize++] = t;
            } else {
                throw new IllegalStateException(String.format("Accept exceeded fixed size of %d",
                        array.length));
            }
        }

        @Override
        public void end() {
            if (curSize < array.length)
                throw new IllegalStateException(String.format("End size %d is less than fixed size %d",
                        curSize, array.length));
        }

        @Override
        public String toString() {
            return String.format("FixedNodeBuilder[%d][%s]",
                    array.length - curSize, Arrays.toString(array));
        }
    }

    /**
     * Variable-sized builder class for reference nodes
     */
    private static final class SpinedNodeBuilder<T>
            extends SpinedBuffer<T>
            implements Node<T>, Node.Builder<T> {
        private boolean building = false;

        SpinedNodeBuilder() {} // Avoid creation of special accessor

        @Override
        public Spliterator<T> spliterator() {
            assert !building : "during building";
            return super.spliterator();
        }

        @Override
        public void forEach(Consumer<? super T> consumer) {
            assert !building : "during building";
            super.forEach(consumer);
        }

        //
        @Override
        public void begin(long size) {
            assert !building : "was already building";
            building = true;
            clear();
            ensureCapacity(size);
        }

        @Override
        public void accept(T t) {
            assert building : "not building";
            super.accept(t);
        }

        @Override
        public void end() {
            assert building : "was not building";
            building = false;
            // @@@ check begin(size) and size
        }

        @Override
        public void copyInto(T[] array, int offset) {
            assert !building : "during building";
            super.copyInto(array, offset);
        }

        @Override
        public T[] asArray(IntFunction<T[]> arrayFactory) {
            assert !building : "during building";
            return super.asArray(arrayFactory);
        }

        @Override
        public Node<T> build() {
            assert !building : "during building";
            return this;
        }
    }

    private static class BooleanArrayNode implements Node.OfBoolean {
        public final boolean[] array;
        public int curSize;

        public BooleanArrayNode(long size) {
            if (size >= it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE) {
                throw new IllegalArgumentException(BAD_SIZE);
            }

            this.array = new boolean[(int) size];
            this.curSize = 0;
        }

        public BooleanArrayNode(boolean[] array) {
            this.array = array;
            this.curSize = array.length;
        }

        @Override
        public BooleanSpliterator spliterator() {
            return ArrayUtil.spliterator(array, 0, curSize);
        }

        @Override
        public boolean[] asPrimitiveArray() {
            if (array.length == curSize) {
                return array;
            } else {
                return Arrays.copyOf(array, curSize);
            }
        }

        @Override
        public void copyInto(boolean[] dest, int destOffset) {
            System.arraycopy(array, 0, dest, destOffset, curSize);
        }

        @Override
        public long count() {
            return curSize;
        }

        @Override
        public void forEach(BooleanConsumer consumer) {
            for (int i = 0; i < curSize; i++) {
                consumer.accept(array[i]);
            }
        }

        @Override
        public String toString() {
            return String.format("BooleanArrayNode[%d][%s]", array.length - curSize, Arrays.toString(array));
        }
    }

    private static class ByteArrayNode implements Node.OfByte {
        public final byte[] array;
        public int curSize;

        public ByteArrayNode(long size) {
            if (size >= it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE) {
                throw new IllegalArgumentException(BAD_SIZE);
            }

            this.array = new byte[(int) size];
            this.curSize = 0;
        }

        public ByteArrayNode(byte[] array) {
            this.array = array;
            this.curSize = array.length;
        }

        @Override
        public ByteSpliterator spliterator() {
            return ArrayUtil.spliterator(array, 0, curSize);
        }

        @Override
        public byte[] asPrimitiveArray() {
            if (array.length == curSize) {
                return array;
            } else {
                return Arrays.copyOf(array, curSize);
            }
        }

        @Override
        public void copyInto(byte[] dest, int destOffset) {
            System.arraycopy(array, 0, dest, destOffset, curSize);
        }

        @Override
        public long count() {
            return curSize;
        }

        @Override
        public void forEach(ByteConsumer consumer) {
            for (int i = 0; i < curSize; i++) {
                consumer.accept(array[i]);
            }
        }

        @Override
        public String toString() {
            return String.format("ByteArrayNode[%d][%s]", array.length - curSize, Arrays.toString(array));
        }
    }

    private static class ShortArrayNode implements Node.OfShort {
        public final short[] array;
        public int curSize;

        public ShortArrayNode(long size) {
            if (size >= it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE) {
                throw new IllegalArgumentException(BAD_SIZE);
            }

            this.array = new short[(int) size];
            this.curSize = 0;
        }

        public ShortArrayNode(short[] array) {
            this.array = array;
            this.curSize = array.length;
        }

        @Override
        public ShortSpliterator spliterator() {
            return ArrayUtil.spliterator(array, 0, curSize);
        }

        @Override
        public short[] asPrimitiveArray() {
            if (array.length == curSize) {
                return array;
            } else {
                return Arrays.copyOf(array, curSize);
            }
        }

        @Override
        public void copyInto(short[] dest, int destOffset) {
            System.arraycopy(array, 0, dest, destOffset, curSize);
        }

        @Override
        public long count() {
            return curSize;
        }

        @Override
        public void forEach(ShortConsumer consumer) {
            for (int i = 0; i < curSize; i++) {
                consumer.accept(array[i]);
            }
        }

        @Override
        public String toString() {
            return String.format("ShortArrayNode[%d][%s]", array.length - curSize, Arrays.toString(array));
        }
    }

    private static class IntArrayNode implements Node.OfInt {
        final int[] array;
        int curSize;

        IntArrayNode(long size) {
            if (size >= it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE)
                throw new IllegalArgumentException(BAD_SIZE);
            this.array = new int[(int) size];
            this.curSize = 0;
        }

        IntArrayNode(int[] array) {
            this.array = array;
            this.curSize = array.length;
        }

        // Node

        @Override
        public Spliterator.OfInt spliterator() {
            return Arrays.spliterator(array, 0, curSize);
        }

        @Override
        public int[] asPrimitiveArray() {
            if (array.length == curSize) {
                return array;
            } else {
                return Arrays.copyOf(array, curSize);
            }
        }

        @Override
        public void copyInto(int[] dest, int destOffset) {
            System.arraycopy(array, 0, dest, destOffset, curSize);
        }

        @Override
        public long count() {
            return curSize;
        }

        @Override
        public void forEach(IntConsumer consumer) {
            for (int i = 0; i < curSize; i++) {
                consumer.accept(array[i]);
            }
        }

        @Override
        public String toString() {
            return String.format("IntArrayNode[%d][%s]",
                    array.length - curSize, Arrays.toString(array));
        }
    }

    private static class LongArrayNode implements Node.OfLong {
        final long[] array;
        int curSize;

        LongArrayNode(long size) {
            if (size >= it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE)
                throw new IllegalArgumentException(BAD_SIZE);
            this.array = new long[(int) size];
            this.curSize = 0;
        }

        LongArrayNode(long[] array) {
            this.array = array;
            this.curSize = array.length;
        }

        @Override
        public Spliterator.OfLong spliterator() {
            return Arrays.spliterator(array, 0, curSize);
        }

        @Override
        public long[] asPrimitiveArray() {
            if (array.length == curSize) {
                return array;
            } else {
                return Arrays.copyOf(array, curSize);
            }
        }

        @Override
        public void copyInto(long[] dest, int destOffset) {
            System.arraycopy(array, 0, dest, destOffset, curSize);
        }

        @Override
        public long count() {
            return curSize;
        }

        @Override
        public void forEach(LongConsumer consumer) {
            for (int i = 0; i < curSize; i++) {
                consumer.accept(array[i]);
            }
        }

        @Override
        public String toString() {
            return String.format("LongArrayNode[%d][%s]",
                    array.length - curSize, Arrays.toString(array));
        }
    }

    private static class CharArrayNode implements Node.OfChar {
        public final char[] array;
        public int curSize;

        public CharArrayNode(long size) {
            if (size >= it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE) {
                throw new IllegalArgumentException(BAD_SIZE);
            }

            this.array = new char[(int) size];
            this.curSize = 0;
        }

        public CharArrayNode(char[] array) {
            this.array = array;
            this.curSize = array.length;
        }

        @Override
        public CharSpliterator spliterator() {
            return ArrayUtil.spliterator(array, 0, curSize);
        }

        @Override
        public char[] asPrimitiveArray() {
            if (array.length == curSize) {
                return array;
            } else {
                return Arrays.copyOf(array, curSize);
            }
        }

        @Override
        public void copyInto(char[] dest, int destOffset) {
            System.arraycopy(array, 0, dest, destOffset, curSize);
        }

        @Override
        public long count() {
            return curSize;
        }

        @Override
        public void forEach(CharConsumer consumer) {
            for (int i = 0; i < curSize; i++) {
                consumer.accept(array[i]);
            }
        }

        @Override
        public String toString() {
            return String.format("CharArrayNode[%d][%s]", array.length - curSize, Arrays.toString(array));
        }
    }

    private static class FloatArrayNode implements Node.OfFloat {
        public final float[] array;
        public int curSize;

        public FloatArrayNode(long size) {
            if (size >= it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE)
                throw new IllegalArgumentException(BAD_SIZE);
            this.array = new float[(int) size];
            this.curSize = 0;
        }

        public FloatArrayNode(float[] array) {
            this.array = array;
            this.curSize = array.length;
        }

        @Override
        public FloatSpliterator spliterator() {
            return ArrayUtil.spliterator(array, 0, curSize);
        }

        @Override
        public float[] asPrimitiveArray() {
            if (array.length == curSize) {
                return array;
            } else {
                return Arrays.copyOf(array, curSize);
            }
        }

        @Override
        public void copyInto(float[] dest, int destOffset) {
            System.arraycopy(array, 0, dest, destOffset, curSize);
        }

        @Override
        public long count() {
            return curSize;
        }

        @Override
        public void forEach(FloatConsumer consumer) {
            for (int i = 0; i < curSize; i++) {
                consumer.accept(array[i]);
            }
        }

        @Override
        public String toString() {
            return String.format("FloatArrayNode[%d][%s]", array.length - curSize, Arrays.toString(array));
        }
    }

    private static class DoubleArrayNode implements Node.OfDouble {
        final double[] array;
        int curSize;

        DoubleArrayNode(long size) {
            if (size >= it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE)
                throw new IllegalArgumentException(BAD_SIZE);
            this.array = new double[(int) size];
            this.curSize = 0;
        }

        DoubleArrayNode(double[] array) {
            this.array = array;
            this.curSize = array.length;
        }

        @Override
        public Spliterator.OfDouble spliterator() {
            return Arrays.spliterator(array, 0, curSize);
        }

        @Override
        public double[] asPrimitiveArray() {
            if (array.length == curSize) {
                return array;
            } else {
                return Arrays.copyOf(array, curSize);
            }
        }

        @Override
        public void copyInto(double[] dest, int destOffset) {
            System.arraycopy(array, 0, dest, destOffset, curSize);
        }

        @Override
        public long count() {
            return curSize;
        }

        @Override
        public void forEach(DoubleConsumer consumer) {
            for (int i = 0; i < curSize; i++) {
                consumer.accept(array[i]);
            }
        }

        @Override
        public String toString() {
            return String.format("DoubleArrayNode[%d][%s]",
                    array.length - curSize, Arrays.toString(array));
        }
    }

    private static final class BooleanFixedNodeBuilder extends BooleanArrayNode implements Node.Builder.OfBoolean {
        public BooleanFixedNodeBuilder(long size) {
            super(size);
            assert size < it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE;
        }

        @Override
        public Node.OfBoolean build() {
            if (curSize < array.length) {
                throw new IllegalStateException(String.format("Current size %d is less than fixed size %d",
                        curSize, array.length));
            }

            return this;
        }

        @Override
        public void begin(long size) {
            if (size != array.length) {
                throw new IllegalStateException(String.format("Begin size %d is not equal to fixed size %d",
                        size, array.length));
            }

            curSize = 0;
        }

        @Override
        public void accept(boolean i) {
            if (curSize < array.length) {
                array[curSize++] = i;
            } else {
                throw new IllegalStateException(String.format("Accept exceeded fixed size of %d",
                        array.length));
            }
        }

        @Override
        public void end() {
            if (curSize < array.length) {
                throw new IllegalStateException(String.format("End size %d is less than fixed size %d",
                        curSize, array.length));
            }
        }

        @Override
        public String toString() {
            return String.format("BooleanFixedNodeBuilder[%d][%s]",
                    array.length - curSize, Arrays.toString(array));
        }
    }

    private static final class ByteFixedNodeBuilder extends ByteArrayNode implements Node.Builder.OfByte {
        public ByteFixedNodeBuilder(long size) {
            super(size);
            assert size < it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE;
        }

        @Override
        public Node.OfByte build() {
            if (curSize < array.length) {
                throw new IllegalStateException(String.format("Current size %d is less than fixed size %d",
                        curSize, array.length));
            }

            return this;
        }

        @Override
        public void begin(long size) {
            if (size != array.length) {
                throw new IllegalStateException(String.format("Begin size %d is not equal to fixed size %d",
                        size, array.length));
            }

            curSize = 0;
        }

        @Override
        public void accept(byte i) {
            if (curSize < array.length) {
                array[curSize++] = i;
            } else {
                throw new IllegalStateException(String.format("Accept exceeded fixed size of %d",
                        array.length));
            }
        }

        @Override
        public void end() {
            if (curSize < array.length) {
                throw new IllegalStateException(String.format("End size %d is less than fixed size %d",
                        curSize, array.length));
            }
        }

        @Override
        public String toString() {
            return String.format("ByteFixedNodeBuilder[%d][%s]",
                    array.length - curSize, Arrays.toString(array));
        }
    }

    private static final class ShortFixedNodeBuilder extends ShortArrayNode implements Node.Builder.OfShort {
        public ShortFixedNodeBuilder(long size) {
            super(size);
            assert size < it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE;
        }

        @Override
        public Node.OfShort build() {
            if (curSize < array.length) {
                throw new IllegalStateException(String.format("Current size %d is less than fixed size %d",
                        curSize, array.length));
            }

            return this;
        }

        @Override
        public void begin(long size) {
            if (size != array.length) {
                throw new IllegalStateException(String.format("Begin size %d is not equal to fixed size %d",
                        size, array.length));
            }

            curSize = 0;
        }

        @Override
        public void accept(short i) {
            if (curSize < array.length) {
                array[curSize++] = i;
            } else {
                throw new IllegalStateException(String.format("Accept exceeded fixed size of %d",
                        array.length));
            }
        }

        @Override
        public void end() {
            if (curSize < array.length) {
                throw new IllegalStateException(String.format("End size %d is less than fixed size %d",
                        curSize, array.length));
            }
        }

        @Override
        public String toString() {
            return String.format("ShortFixedNodeBuilder[%d][%s]",
                    array.length - curSize, Arrays.toString(array));
        }
    }

    private static final class IntFixedNodeBuilder
            extends IntArrayNode
            implements Node.Builder.OfInt {

        IntFixedNodeBuilder(long size) {
            super(size);
            assert size < it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE;
        }

        @Override
        public Node.OfInt build() {
            if (curSize < array.length) {
                throw new IllegalStateException(String.format("Current size %d is less than fixed size %d",
                        curSize, array.length));
            }

            return this;
        }

        @Override
        public void begin(long size) {
            if (size != array.length) {
                throw new IllegalStateException(String.format("Begin size %d is not equal to fixed size %d",
                        size, array.length));
            }

            curSize = 0;
        }

        @Override
        public void accept(int i) {
            if (curSize < array.length) {
                array[curSize++] = i;
            } else {
                throw new IllegalStateException(String.format("Accept exceeded fixed size of %d",
                        array.length));
            }
        }

        @Override
        public void end() {
            if (curSize < array.length) {
                throw new IllegalStateException(String.format("End size %d is less than fixed size %d",
                        curSize, array.length));
            }
        }

        @Override
        public String toString() {
            return String.format("IntFixedNodeBuilder[%d][%s]",
                    array.length - curSize, Arrays.toString(array));
        }
    }

    private static final class LongFixedNodeBuilder
            extends LongArrayNode
            implements Node.Builder.OfLong {

        LongFixedNodeBuilder(long size) {
            super(size);
            assert size < it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE;
        }

        @Override
        public Node.OfLong build() {
            if (curSize < array.length) {
                throw new IllegalStateException(String.format("Current size %d is less than fixed size %d",
                        curSize, array.length));
            }

            return this;
        }

        @Override
        public void begin(long size) {
            if (size != array.length) {
                throw new IllegalStateException(String.format("Begin size %d is not equal to fixed size %d",
                        size, array.length));
            }

            curSize = 0;
        }

        @Override
        public void accept(long i) {
            if (curSize < array.length) {
                array[curSize++] = i;
            } else {
                throw new IllegalStateException(String.format("Accept exceeded fixed size of %d",
                        array.length));
            }
        }

        @Override
        public void end() {
            if (curSize < array.length) {
                throw new IllegalStateException(String.format("End size %d is less than fixed size %d",
                        curSize, array.length));
            }
        }

        @Override
        public String toString() {
            return String.format("LongFixedNodeBuilder[%d][%s]",
                    array.length - curSize, Arrays.toString(array));
        }
    }

    private static final class CharFixedNodeBuilder extends CharArrayNode implements Node.Builder.OfChar {
        public CharFixedNodeBuilder(long size) {
            super(size);
            assert size < it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE;
        }

        @Override
        public Node.OfChar build() {
            if (curSize < array.length) {
                throw new IllegalStateException(String.format("Current size %d is less than fixed size %d",
                        curSize, array.length));
            }

            return this;
        }

        @Override
        public void begin(long size) {
            if (size != array.length) {
                throw new IllegalStateException(String.format("Begin size %d is not equal to fixed size %d",
                        size, array.length));
            }

            curSize = 0;
        }

        @Override
        public void accept(char i) {
            if (curSize < array.length) {
                array[curSize++] = i;
            } else {
                throw new IllegalStateException(String.format("Accept exceeded fixed size of %d",
                        array.length));
            }
        }

        @Override
        public void end() {
            if (curSize < array.length) {
                throw new IllegalStateException(String.format("End size %d is less than fixed size %d",
                        curSize, array.length));
            }
        }

        @Override
        public String toString() {
            return String.format("CharFixedNodeBuilder[%d][%s]",
                    array.length - curSize, Arrays.toString(array));
        }
    }

    private static final class FloatFixedNodeBuilder extends FloatArrayNode implements Node.Builder.OfFloat {
        public FloatFixedNodeBuilder(long size) {
            super(size);
            assert size < it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE;
        }

        @Override
        public Node.OfFloat build() {
            if (curSize < array.length) {
                throw new IllegalStateException(String.format("Current size %d is less than fixed size %d",
                        curSize, array.length));
            }

            return this;
        }

        @Override
        public void begin(long size) {
            if (size != array.length) {
                throw new IllegalStateException(String.format("Begin size %d is not equal to fixed size %d",
                        size, array.length));
            }

            curSize = 0;
        }

        @Override
        public void accept(float i) {
            if (curSize < array.length) {
                array[curSize++] = i;
            } else {
                throw new IllegalStateException(String.format("Accept exceeded fixed size of %d",
                        array.length));
            }
        }

        @Override
        public void end() {
            if (curSize < array.length) {
                throw new IllegalStateException(String.format("End size %d is less than fixed size %d",
                        curSize, array.length));
            }
        }

        @Override
        public String toString() {
            return String.format("FloatFixedNodeBuilder[%d][%s]",
                    array.length - curSize, Arrays.toString(array));
        }
    }

    private static final class DoubleFixedNodeBuilder
            extends DoubleArrayNode
            implements Node.Builder.OfDouble {

        DoubleFixedNodeBuilder(long size) {
            super(size);
            assert size < it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE;
        }

        @Override
        public Node.OfDouble build() {
            if (curSize < array.length) {
                throw new IllegalStateException(String.format("Current size %d is less than fixed size %d",
                        curSize, array.length));
            }

            return this;
        }

        @Override
        public void begin(long size) {
            if (size != array.length) {
                throw new IllegalStateException(String.format("Begin size %d is not equal to fixed size %d",
                        size, array.length));
            }

            curSize = 0;
        }

        @Override
        public void accept(double i) {
            if (curSize < array.length) {
                array[curSize++] = i;
            } else {
                throw new IllegalStateException(String.format("Accept exceeded fixed size of %d",
                        array.length));
            }
        }

        @Override
        public void end() {
            if (curSize < array.length) {
                throw new IllegalStateException(String.format("End size %d is less than fixed size %d",
                        curSize, array.length));
            }
        }

        @Override
        public String toString() {
            return String.format("DoubleFixedNodeBuilder[%d][%s]",
                    array.length - curSize, Arrays.toString(array));
        }
    }

    private static final class BooleanSpinedNodeBuilder extends SpinedBuffer.OfBoolean implements Node.OfBoolean,
            Node.Builder.OfBoolean {
        private boolean building = false;

        public BooleanSpinedNodeBuilder() {} // Avoid creation of special accessor

        @Override
        public BooleanSpliterator spliterator() {
            assert !building : "during building";
            return super.spliterator();
        }

        @Override
        public void forEach(BooleanConsumer consumer) {
            assert !building : "during building";
            super.forEach(consumer);
        }

        //
        @Override
        public void begin(long size) {
            assert !building : "was already building";
            building = true;
            clear();
            ensureCapacity(size);
        }

        @Override
        public void accept(boolean i) {
            assert building : "not building";
            super.accept(i);
        }

        @Override
        public void end() {
            assert building : "was not building";
            building = false;
            // @@@ check begin(size) and size
        }

        @Override
        public void copyInto(boolean[] array, int offset) {
            assert !building : "during building";
            super.copyInto(array, offset);
        }

        @Override
        public boolean[] asPrimitiveArray() {
            assert !building : "during building";
            return super.asPrimitiveArray();
        }

        @Override
        public Node.OfBoolean build() {
            assert !building : "during building";
            return this;
        }

        @Override
        public Node.OfBoolean truncate(long from, long to, IntFunction<Boolean[]> generator) {
            return Node.OfBoolean.super.truncate(from, to, generator);
        }
    }

    private static final class ByteSpinedNodeBuilder extends SpinedBuffer.OfByte implements Node.OfByte,
            Node.Builder.OfByte {
        private boolean building = false;

        public ByteSpinedNodeBuilder() {} // Avoid creation of special accessor

        @Override
        public ByteSpliterator spliterator() {
            assert !building : "during building";
            return super.spliterator();
        }

        @Override
        public void forEach(ByteConsumer consumer) {
            assert !building : "during building";
            super.forEach(consumer);
        }

        //
        @Override
        public void begin(long size) {
            assert !building : "was already building";
            building = true;
            clear();
            ensureCapacity(size);
        }

        @Override
        public void accept(byte i) {
            assert building : "not building";
            super.accept(i);
        }

        @Override
        public void end() {
            assert building : "was not building";
            building = false;
            // @@@ check begin(size) and size
        }

        @Override
        public void copyInto(byte[] array, int offset) {
            assert !building : "during building";
            super.copyInto(array, offset);
        }

        @Override
        public byte[] asPrimitiveArray() {
            assert !building : "during building";
            return super.asPrimitiveArray();
        }

        @Override
        public Node.OfByte build() {
            assert !building : "during building";
            return this;
        }

        @Override
        public Node.OfByte truncate(long from, long to, IntFunction<Byte[]> generator) {
            return Node.OfByte.super.truncate(from, to, generator);
        }
    }

    private static final class ShortSpinedNodeBuilder extends SpinedBuffer.OfShort implements Node.OfShort,
            Node.Builder.OfShort {
        private boolean building = false;

        public ShortSpinedNodeBuilder() {} // Avoid creation of special accessor

        @Override
        public ShortSpliterator spliterator() {
            assert !building : "during building";
            return super.spliterator();
        }

        @Override
        public void forEach(ShortConsumer consumer) {
            assert !building : "during building";
            super.forEach(consumer);
        }

        //
        @Override
        public void begin(long size) {
            assert !building : "was already building";
            building = true;
            clear();
            ensureCapacity(size);
        }

        @Override
        public void accept(short i) {
            assert building : "not building";
            super.accept(i);
        }

        @Override
        public void end() {
            assert building : "was not building";
            building = false;
            // @@@ check begin(size) and size
        }

        @Override
        public void copyInto(short[] array, int offset) {
            assert !building : "during building";
            super.copyInto(array, offset);
        }

        @Override
        public short[] asPrimitiveArray() {
            assert !building : "during building";
            return super.asPrimitiveArray();
        }

        @Override
        public Node.OfShort build() {
            assert !building : "during building";
            return this;
        }

        @Override
        public Node.OfShort truncate(long from, long to, IntFunction<Short[]> generator) {
            return Node.OfShort.super.truncate(from, to, generator);
        }
    }

    private static final class IntSpinedNodeBuilder
            extends SpinedBuffer.OfInt
            implements Node.OfInt, Node.Builder.OfInt {
        private boolean building = false;

        IntSpinedNodeBuilder() {} // Avoid creation of special accessor

        @Override
        public Spliterator.OfInt spliterator() {
            assert !building : "during building";
            return super.spliterator();
        }

        @Override
        public void forEach(IntConsumer consumer) {
            assert !building : "during building";
            super.forEach(consumer);
        }

        //
        @Override
        public void begin(long size) {
            assert !building : "was already building";
            building = true;
            clear();
            ensureCapacity(size);
        }

        @Override
        public void accept(int i) {
            assert building : "not building";
            super.accept(i);
        }

        @Override
        public void end() {
            assert building : "was not building";
            building = false;
            // @@@ check begin(size) and size
        }

        @Override
        public void copyInto(int[] array, int offset) throws IndexOutOfBoundsException {
            assert !building : "during building";
            super.copyInto(array, offset);
        }

        @Override
        public int[] asPrimitiveArray() {
            assert !building : "during building";
            return super.asPrimitiveArray();
        }

        @Override
        public Node.OfInt build() {
            assert !building : "during building";
            return this;
        }
    }

    private static final class LongSpinedNodeBuilder
            extends SpinedBuffer.OfLong
            implements Node.OfLong, Node.Builder.OfLong {
        private boolean building = false;

        LongSpinedNodeBuilder() {} // Avoid creation of special accessor

        @Override
        public Spliterator.OfLong spliterator() {
            assert !building : "during building";
            return super.spliterator();
        }

        @Override
        public void forEach(LongConsumer consumer) {
            assert !building : "during building";
            super.forEach(consumer);
        }

        //
        @Override
        public void begin(long size) {
            assert !building : "was already building";
            building = true;
            clear();
            ensureCapacity(size);
        }

        @Override
        public void accept(long i) {
            assert building : "not building";
            super.accept(i);
        }

        @Override
        public void end() {
            assert building : "was not building";
            building = false;
            // @@@ check begin(size) and size
        }

        @Override
        public void copyInto(long[] array, int offset) {
            assert !building : "during building";
            super.copyInto(array, offset);
        }

        @Override
        public long[] asPrimitiveArray() {
            assert !building : "during building";
            return super.asPrimitiveArray();
        }

        @Override
        public Node.OfLong build() {
            assert !building : "during building";
            return this;
        }
    }

    private static final class CharSpinedNodeBuilder extends SpinedBuffer.OfChar implements Node.OfChar,
            Node.Builder.OfChar {
        private boolean building = false;

        public CharSpinedNodeBuilder() {} // Avoid creation of special accessor

        @Override
        public CharSpliterator spliterator() {
            assert !building : "during building";
            return super.spliterator();
        }

        @Override
        public void forEach(CharConsumer consumer) {
            assert !building : "during building";
            super.forEach(consumer);
        }

        //
        @Override
        public void begin(long size) {
            assert !building : "was already building";
            building = true;
            clear();
            ensureCapacity(size);
        }

        @Override
        public void accept(char i) {
            assert building : "not building";
            super.accept(i);
        }

        @Override
        public void end() {
            assert building : "was not building";
            building = false;
            // @@@ check begin(size) and size
        }

        @Override
        public void copyInto(char[] array, int offset) {
            assert !building : "during building";
            super.copyInto(array, offset);
        }

        @Override
        public char[] asPrimitiveArray() {
            assert !building : "during building";
            return super.asPrimitiveArray();
        }

        @Override
        public Node.OfChar build() {
            assert !building : "during building";
            return this;
        }

        @Override
        public Node.OfChar truncate(long from, long to, IntFunction<Character[]> generator) {
            return Node.OfChar.super.truncate(from, to, generator);
        }
    }

    private static final class FloatSpinedNodeBuilder extends SpinedBuffer.OfFloat
            implements Node.OfFloat, Node.Builder.OfFloat {
        private boolean building = false;

        public FloatSpinedNodeBuilder() {} // Avoid creation of special accessor

        @Override
        public FloatSpliterator spliterator() {
            assert !building : "during building";
            return super.spliterator();
        }

        @Override
        public void forEach(FloatConsumer consumer) {
            assert !building : "during building";
            super.forEach(consumer);
        }

        //
        @Override
        public void begin(long size) {
            assert !building : "was already building";
            building = true;
            clear();
            ensureCapacity(size);
        }

        @Override
        public void accept(float i) {
            assert building : "not building";
            super.accept(i);
        }

        @Override
        public void end() {
            assert building : "was not building";
            building = false;
            // @@@ check begin(size) and size
        }

        @Override
        public void copyInto(float[] array, int offset) {
            assert !building : "during building";
            super.copyInto(array, offset);
        }

        @Override
        public float[] asPrimitiveArray() {
            assert !building : "during building";
            return super.asPrimitiveArray();
        }

        @Override
        public Node.OfFloat build() {
            assert !building : "during building";
            return this;
        }

        @Override
        public Node.OfFloat truncate(long from, long to, IntFunction<Float[]> generator) {
            return Node.OfFloat.super.truncate(from, to, generator);
        }
    }

    private static final class DoubleSpinedNodeBuilder
            extends SpinedBuffer.OfDouble implements Node.OfDouble, Node.Builder.OfDouble {
        private boolean building = false;

        DoubleSpinedNodeBuilder() {} // Avoid creation of special accessor

        @Override
        public Spliterator.OfDouble spliterator() {
            assert !building : "during building";
            return super.spliterator();
        }

        @Override
        public void forEach(DoubleConsumer consumer) {
            assert !building : "during building";
            super.forEach(consumer);
        }

        //
        @Override
        public void begin(long size) {
            assert !building : "was already building";
            building = true;
            clear();
            ensureCapacity(size);
        }

        @Override
        public void accept(double i) {
            assert building : "not building";
            super.accept(i);
        }

        @Override
        public void end() {
            assert building : "was not building";
            building = false;
            // @@@ check begin(size) and size
        }

        @Override
        public void copyInto(double[] array, int offset) {
            assert !building : "during building";
            super.copyInto(array, offset);
        }

        @Override
        public double[] asPrimitiveArray() {
            assert !building : "during building";
            return super.asPrimitiveArray();
        }

        @Override
        public Node.OfDouble build() {
            assert !building : "during building";
            return this;
        }
    }

    /*
     * This and subclasses are not intended to be serializable
     */
    @SuppressWarnings("serial")
    private abstract static class SizedCollectorTask<P_IN, P_OUT, T_SINK extends Sink<P_OUT>,
            K extends SizedCollectorTask<P_IN, P_OUT, T_SINK, K>>
            extends CountedCompleter<Void>
            implements Sink<P_OUT> {
        protected final Spliterator<P_IN> spliterator;
        protected final PipelineHelper<P_OUT> helper;
        protected final long targetSize;
        protected long offset;
        protected long length;
        // For Sink implementation
        protected int index, fence;

        public SizedCollectorTask(Spliterator<P_IN> spliterator,
                           PipelineHelper<P_OUT> helper,
                           int arrayLength) {
            assert spliterator.hasCharacteristics(Spliterator.SUBSIZED);
            this.spliterator = spliterator;
            this.helper = helper;
            this.targetSize = AbstractTask.suggestTargetSize(spliterator.estimateSize());
            this.offset = 0;
            this.length = arrayLength;
        }

        public SizedCollectorTask(K parent, Spliterator<P_IN> spliterator,
                           long offset, long length, int arrayLength) {
            super(parent);
            assert spliterator.hasCharacteristics(Spliterator.SUBSIZED);
            this.spliterator = spliterator;
            this.helper = parent.helper;
            this.targetSize = parent.targetSize;
            this.offset = offset;
            this.length = length;

            if (offset < 0 || length < 0 || (offset + length - 1 >= arrayLength)) {
                throw new IllegalArgumentException(
                        String.format("offset and length interval [%d, %d + %d) is not within array size interval [0, %d)",
                                offset, offset, length, arrayLength));
            }
        }

        @Override
        public void compute() {
            SizedCollectorTask<P_IN, P_OUT, T_SINK, K> task = this;
            Spliterator<P_IN> rightSplit = spliterator, leftSplit;
            while (rightSplit.estimateSize() > task.targetSize &&
                    (leftSplit = rightSplit.trySplit()) != null) {
                task.setPendingCount(1);
                long leftSplitSize = leftSplit.estimateSize();
                task.makeChild(leftSplit, task.offset, leftSplitSize).fork();
                task = task.makeChild(rightSplit, task.offset + leftSplitSize,
                        task.length - leftSplitSize);
            }

            assert task.offset + task.length < it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE;
            @SuppressWarnings("unchecked")
            T_SINK sink = (T_SINK) task;
            task.helper.wrapAndCopyInto(sink, rightSplit);
            task.propagateCompletion();
        }

        public abstract K makeChild(Spliterator<P_IN> spliterator, long offset, long size);

        @Override
        public void begin(long size) {
            if (size > length)
                throw new IllegalStateException("size passed to Sink.begin exceeds array length");
            // Casts to ints are safe since absolute size is verified to be within
            // bounds when the root concrete SizedCollectorTask is constructed
            // with the shared array
            index = (int) offset;
            fence = index + (int) length;
        }

        public static final class OfRef<P_IN, P_OUT>
                extends SizedCollectorTask<P_IN, P_OUT, Sink<P_OUT>, OfRef<P_IN, P_OUT>>
                implements Sink<P_OUT> {
            private final P_OUT[] array;

            public OfRef(Spliterator<P_IN> spliterator, PipelineHelper<P_OUT> helper, P_OUT[] array) {
                super(spliterator, helper, array.length);
                this.array = array;
            }

            public OfRef(OfRef<P_IN, P_OUT> parent, Spliterator<P_IN> spliterator,
                         long offset, long length) {
                super(parent, spliterator, offset, length, parent.array.length);
                this.array = parent.array;
            }

            @Override
            public OfRef<P_IN, P_OUT> makeChild(Spliterator<P_IN> spliterator,
                                                                                   long offset, long size) {
                return new OfRef<>(this, spliterator, offset, size);
            }

            @Override
            public void accept(P_OUT value) {
                if (index >= fence) {
                    throw new IndexOutOfBoundsException(Integer.toString(index));
                }
                array[index++] = value;
            }
        }

        public static final class OfBoolean<P_IN> extends SizedCollectorTask<P_IN, Boolean, Sink.OfBoolean,
                OfBoolean<P_IN>> implements Sink.OfBoolean {
            private final boolean[] array;

            public OfBoolean(Spliterator<P_IN> spliterator, PipelineHelper<Boolean> helper, boolean[] array) {
                super(spliterator, helper, array.length);
                this.array = array;
            }

            public OfBoolean(SizedCollectorTask.OfBoolean<P_IN> parent, Spliterator<P_IN> spliterator, long offset,
                          long length) {
                super(parent, spliterator, offset, length, parent.array.length);
                this.array = parent.array;
            }

            @Override
            public SizedCollectorTask.OfBoolean<P_IN> makeChild(Spliterator<P_IN> spliterator, long offset, long size) {
                return new SizedCollectorTask.OfBoolean<>(this, spliterator, offset, size);
            }

            @Override
            public void accept(boolean value) {
                if (index >= fence) {
                    throw new IndexOutOfBoundsException(Integer.toString(index));
                }

                array[index++] = value;
            }
        }

        public static final class OfByte<P_IN> extends SizedCollectorTask<P_IN, Byte, Sink.OfByte, OfByte<P_IN>>
                implements Sink.OfByte {
            private final byte[] array;

            public OfByte(Spliterator<P_IN> spliterator, PipelineHelper<Byte> helper, byte[] array) {
                super(spliterator, helper, array.length);
                this.array = array;
            }

            public OfByte(SizedCollectorTask.OfByte<P_IN> parent, Spliterator<P_IN> spliterator, long offset,
                          long length) {
                super(parent, spliterator, offset, length, parent.array.length);
                this.array = parent.array;
            }

            @Override
            public SizedCollectorTask.OfByte<P_IN> makeChild(Spliterator<P_IN> spliterator, long offset, long size) {
                return new SizedCollectorTask.OfByte<>(this, spliterator, offset, size);
            }

            @Override
            public void accept(byte value) {
                if (index >= fence) {
                    throw new IndexOutOfBoundsException(Integer.toString(index));
                }

                array[index++] = value;
            }
        }

        public static final class OfShort<P_IN> extends SizedCollectorTask<P_IN, Short, Sink.OfShort, OfShort<P_IN>>
                implements Sink.OfShort {
            private final short[] array;

            public OfShort(Spliterator<P_IN> spliterator, PipelineHelper<Short> helper, short[] array) {
                super(spliterator, helper, array.length);
                this.array = array;
            }

            public OfShort(SizedCollectorTask.OfShort<P_IN> parent, Spliterator<P_IN> spliterator, long offset,
                          long length) {
                super(parent, spliterator, offset, length, parent.array.length);
                this.array = parent.array;
            }

            @Override
            public SizedCollectorTask.OfShort<P_IN> makeChild(Spliterator<P_IN> spliterator, long offset, long size) {
                return new SizedCollectorTask.OfShort<>(this, spliterator, offset, size);
            }

            @Override
            public void accept(short value) {
                if (index >= fence) {
                    throw new IndexOutOfBoundsException(Integer.toString(index));
                }

                array[index++] = value;
            }
        }

        public static final class OfInt<P_IN>
                extends SizedCollectorTask<P_IN, Integer, Sink.OfInt, OfInt<P_IN>>
                implements Sink.OfInt {
            private final int[] array;

            public OfInt(Spliterator<P_IN> spliterator, PipelineHelper<Integer> helper, int[] array) {
                super(spliterator, helper, array.length);
                this.array = array;
            }

            public OfInt(SizedCollectorTask.OfInt<P_IN> parent, Spliterator<P_IN> spliterator,
                         long offset, long length) {
                super(parent, spliterator, offset, length, parent.array.length);
                this.array = parent.array;
            }

            @Override
            public SizedCollectorTask.OfInt<P_IN> makeChild(Spliterator<P_IN> spliterator,
                                                                            long offset, long size) {
                return new SizedCollectorTask.OfInt<>(this, spliterator, offset, size);
            }

            @Override
            public void accept(int value) {
                if (index >= fence) {
                    throw new IndexOutOfBoundsException(Integer.toString(index));
                }
                array[index++] = value;
            }
        }

        public static final class OfLong<P_IN>
                extends SizedCollectorTask<P_IN, Long, Sink.OfLong, OfLong<P_IN>>
                implements Sink.OfLong {
            private final long[] array;

            public OfLong(Spliterator<P_IN> spliterator, PipelineHelper<Long> helper, long[] array) {
                super(spliterator, helper, array.length);
                this.array = array;
            }

            public OfLong(SizedCollectorTask.OfLong<P_IN> parent, Spliterator<P_IN> spliterator,
                          long offset, long length) {
                super(parent, spliterator, offset, length, parent.array.length);
                this.array = parent.array;
            }

            @Override
            public SizedCollectorTask.OfLong<P_IN> makeChild(Spliterator<P_IN> spliterator,
                                                                             long offset, long size) {
                return new SizedCollectorTask.OfLong<>(this, spliterator, offset, size);
            }

            @Override
            public void accept(long value) {
                if (index >= fence) {
                    throw new IndexOutOfBoundsException(Integer.toString(index));
                }
                array[index++] = value;
            }
        }

        public static final class OfChar<P_IN> extends SizedCollectorTask<P_IN, Character, Sink.OfChar, OfChar<P_IN>>
                implements Sink.OfChar {
            private final char[] array;

            public OfChar(Spliterator<P_IN> spliterator, PipelineHelper<Character> helper, char[] array) {
                super(spliterator, helper, array.length);
                this.array = array;
            }

            public OfChar(SizedCollectorTask.OfChar<P_IN> parent, Spliterator<P_IN> spliterator, long offset,
                          long length) {
                super(parent, spliterator, offset, length, parent.array.length);
                this.array = parent.array;
            }

            @Override
            public SizedCollectorTask.OfChar<P_IN> makeChild(Spliterator<P_IN> spliterator, long offset, long size) {
                return new SizedCollectorTask.OfChar<>(this, spliterator, offset, size);
            }

            @Override
            public void accept(char value) {
                if (index >= fence) {
                    throw new IndexOutOfBoundsException(Integer.toString(index));
                }

                array[index++] = value;
            }
        }

        public static final class OfFloat<P_IN> extends SizedCollectorTask<P_IN, Float, Sink.OfFloat,
                OfFloat<P_IN>> implements Sink.OfFloat {
            private final float[] array;

            public OfFloat(Spliterator<P_IN> spliterator, PipelineHelper<Float> helper, float[] array) {
                super(spliterator, helper, array.length);
                this.array = array;
            }

            public OfFloat(SizedCollectorTask.OfFloat<P_IN> parent, Spliterator<P_IN> spliterator,
                           long offset, long length) {
                super(parent, spliterator, offset, length, parent.array.length);
                this.array = parent.array;
            }

            @Override
            public SizedCollectorTask.OfFloat<P_IN> makeChild(Spliterator<P_IN> spliterator,
                                                              long offset, long size) {
                return new SizedCollectorTask.OfFloat<>(this, spliterator, offset, size);
            }

            @Override
            public void accept(float value) {
                if (index >= fence) {
                    throw new IndexOutOfBoundsException(Integer.toString(index));
                }

                array[index++] = value;
            }
        }

        public static final class OfDouble<P_IN>
                extends SizedCollectorTask<P_IN, Double, Sink.OfDouble, OfDouble<P_IN>>
                implements Sink.OfDouble {
            private final double[] array;

            public OfDouble(Spliterator<P_IN> spliterator, PipelineHelper<Double> helper, double[] array) {
                super(spliterator, helper, array.length);
                this.array = array;
            }

            public OfDouble(SizedCollectorTask.OfDouble<P_IN> parent, Spliterator<P_IN> spliterator,
                            long offset, long length) {
                super(parent, spliterator, offset, length, parent.array.length);
                this.array = parent.array;
            }

            @Override
            public SizedCollectorTask.OfDouble<P_IN> makeChild(Spliterator<P_IN> spliterator,
                                                                     long offset, long size) {
                return new SizedCollectorTask.OfDouble<>(this, spliterator, offset, size);
            }

            @Override
            public void accept(double value) {
                if (index >= fence) {
                    throw new IndexOutOfBoundsException(Integer.toString(index));
                }
                array[index++] = value;
            }
        }
    }

    @SuppressWarnings("serial")
    private abstract static class ToArrayTask<T, T_NODE extends Node<T>,
            K extends ToArrayTask<T, T_NODE, K>>
            extends CountedCompleter<Void> {
        protected final T_NODE node;
        protected final int offset;

        public ToArrayTask(T_NODE node, int offset) {
            this.node = node;
            this.offset = offset;
        }

        public ToArrayTask(K parent, T_NODE node, int offset) {
            super(parent);
            this.node = node;
            this.offset = offset;
        }

        abstract void copyNodeToArray();

        abstract K makeChild(int childIndex, int offset);

        @Override
        public void compute() {
            ToArrayTask<T, T_NODE, K> task = this;
            while (true) {
                if (task.node.getChildCount() == 0) {
                    task.copyNodeToArray();
                    task.propagateCompletion();
                    return;
                }
                else {
                    task.setPendingCount(task.node.getChildCount() - 1);

                    int size = 0;
                    int i = 0;
                    for (;i < task.node.getChildCount() - 1; i++) {
                        K leftTask = task.makeChild(i, task.offset + size);
                        size += leftTask.node.count();
                        leftTask.fork();
                    }
                    task = task.makeChild(i, task.offset + size);
                }
            }
        }

        private static final class OfRef<T>
                extends ToArrayTask<T, Node<T>, OfRef<T>> {
            private final T[] array;

            private OfRef(Node<T> node, T[] array, int offset) {
                super(node, offset);
                this.array = array;
            }

            private OfRef(OfRef<T> parent, Node<T> node, int offset) {
                super(parent, node, offset);
                this.array = parent.array;
            }

            @Override
            OfRef<T> makeChild(int childIndex, int offset) {
                return new OfRef<>(this, node.getChild(childIndex), offset);
            }

            @Override
            void copyNodeToArray() {
                node.copyInto(array, offset);
            }
        }

        private static class OfPrimitive<T, T_CONS, T_ARR,
                T_SPLITR extends Spliterator.OfPrimitive<T, T_CONS, T_SPLITR>,
                T_NODE extends Node.OfPrimitive<T, T_CONS, T_ARR, T_SPLITR, T_NODE>>
                extends ToArrayTask<T, T_NODE, OfPrimitive<T, T_CONS, T_ARR, T_SPLITR, T_NODE>> {
            private final T_ARR array;

            private OfPrimitive(T_NODE node, T_ARR array, int offset) {
                super(node, offset);
                this.array = array;
            }

            private OfPrimitive(OfPrimitive<T, T_CONS, T_ARR, T_SPLITR, T_NODE> parent, T_NODE node, int offset) {
                super(parent, node, offset);
                this.array = parent.array;
            }

            @Override
            OfPrimitive<T, T_CONS, T_ARR, T_SPLITR, T_NODE> makeChild(int childIndex, int offset) {
                return new OfPrimitive<>(this, node.getChild(childIndex), offset);
            }

            @Override
            void copyNodeToArray() {
                node.copyInto(array, offset);
            }
        }

        private static final class OfBoolean
                extends OfPrimitive<Boolean, BooleanConsumer, boolean[], BooleanSpliterator, Node.OfBoolean> {
            private OfBoolean(Node.OfBoolean node, boolean[] array, int offset) {
                super(node, array, offset);
            }
        }

        private static final class OfByte
                extends OfPrimitive<Byte, ByteConsumer, byte[], ByteSpliterator, Node.OfByte> {
            private OfByte(Node.OfByte node, byte[] array, int offset) {
                super(node, array, offset);
            }
        }

        private static final class OfShort
                extends OfPrimitive<Short, ShortConsumer, short[], ShortSpliterator, Node.OfShort> {
            private OfShort(Node.OfShort node, short[] array, int offset) {
                super(node, array, offset);
            }
        }

        private static final class OfInt
                extends OfPrimitive<Integer, IntConsumer, int[], Spliterator.OfInt, Node.OfInt> {
            private OfInt(Node.OfInt node, int[] array, int offset) {
                super(node, array, offset);
            }
        }

        private static final class OfLong
                extends OfPrimitive<Long, LongConsumer, long[], Spliterator.OfLong, Node.OfLong> {
            private OfLong(Node.OfLong node, long[] array, int offset) {
                super(node, array, offset);
            }
        }

        private static final class OfChar
                extends OfPrimitive<Character, CharConsumer, char[], CharSpliterator, Node.OfChar> {
            private OfChar(Node.OfChar node, char[] array, int offset) {
                super(node, array, offset);
            }
        }

        private static final class OfFloat extends OfPrimitive<Float, FloatConsumer, float[],
                FloatSpliterator, Node.OfFloat> {
            private OfFloat(Node.OfFloat node, float[] array, int offset) {
                super(node, array, offset);
            }
        }

        private static final class OfDouble
                extends OfPrimitive<Double, DoubleConsumer, double[], Spliterator.OfDouble, Node.OfDouble> {
            private OfDouble(Node.OfDouble node, double[] array, int offset) {
                super(node, array, offset);
            }
        }
    }

    public static class CollectorTask<P_IN, P_OUT, T_NODE extends Node<P_OUT>, T_BUILDER extends Node.Builder<P_OUT>>
            extends AbstractTask<P_IN, P_OUT, T_NODE, CollectorTask<P_IN, P_OUT, T_NODE, T_BUILDER>> {
        protected final PipelineHelper<P_OUT> helper;
        protected final LongFunction<T_BUILDER> builderFactory;
        protected final BinaryOperator<T_NODE> concFactory;

        public CollectorTask(PipelineHelper<P_OUT> helper,
                      Spliterator<P_IN> spliterator,
                      LongFunction<T_BUILDER> builderFactory,
                      BinaryOperator<T_NODE> concFactory) {
            super(helper, spliterator);
            this.helper = helper;
            this.builderFactory = builderFactory;
            this.concFactory = concFactory;
        }

        public CollectorTask(CollectorTask<P_IN, P_OUT, T_NODE, T_BUILDER> parent,
                             Spliterator<P_IN> spliterator) {
            super(parent, spliterator);
            helper = parent.helper;
            builderFactory = parent.builderFactory;
            concFactory = parent.concFactory;
        }

        @Override
        protected CollectorTask<P_IN, P_OUT, T_NODE, T_BUILDER> makeChild(Spliterator<P_IN> spliterator) {
            return new CollectorTask<>(this, spliterator);
        }

        @Override
        @SuppressWarnings("unchecked")
        protected T_NODE doLeaf() {
            T_BUILDER builder = builderFactory.apply(helper.exactOutputSizeIfKnown(spliterator));
            return (T_NODE) helper.wrapAndCopyInto(builder, spliterator).build();
        }

        @Override
        public void onCompletion(CountedCompleter<?> caller) {
            if (!isLeaf()) {
                setLocalResult(concFactory.apply(leftChild.getLocalResult(), rightChild.getLocalResult()));
            }

            super.onCompletion(caller);
        }

        public static final class OfRef<P_IN, P_OUT>
                extends CollectorTask<P_IN, P_OUT, Node<P_OUT>, Node.Builder<P_OUT>> {
            public OfRef(PipelineHelper<P_OUT> helper,
                  IntFunction<P_OUT[]> generator,
                  Spliterator<P_IN> spliterator) {
                super(helper, spliterator, s -> builder(s, generator), ConcNode::new);
            }
        }

        public static final class OfBoolean<P_IN> extends CollectorTask<P_IN, Boolean, Node.OfBoolean,
                Node.Builder.OfBoolean> {
            public OfBoolean(PipelineHelper<Boolean> helper, Spliterator<P_IN> spliterator) {
                super(helper, spliterator, Nodes::booleanBuilder, ConcNode.OfBoolean::new);
            }
        }

        public static final class OfByte<P_IN> extends CollectorTask<P_IN, Byte, Node.OfByte, Node.Builder.OfByte> {
            public OfByte(PipelineHelper<Byte> helper, Spliterator<P_IN> spliterator) {
                super(helper, spliterator, Nodes::byteBuilder, ConcNode.OfByte::new);
            }
        }

        public static final class OfShort<P_IN> extends CollectorTask<P_IN, Short, Node.OfShort,
                Node.Builder.OfShort> {
            public OfShort(PipelineHelper<Short> helper, Spliterator<P_IN> spliterator) {
                super(helper, spliterator, Nodes::shortBuilder, ConcNode.OfShort::new);
            }
        }

        private static final class OfInt<P_IN>
                extends CollectorTask<P_IN, Integer, Node.OfInt, Node.Builder.OfInt> {
            OfInt(PipelineHelper<Integer> helper, Spliterator<P_IN> spliterator) {
                super(helper, spliterator, Nodes::intBuilder, ConcNode.OfInt::new);
            }
        }

        private static final class OfLong<P_IN>
                extends CollectorTask<P_IN, Long, Node.OfLong, Node.Builder.OfLong> {
            OfLong(PipelineHelper<Long> helper, Spliterator<P_IN> spliterator) {
                super(helper, spliterator, Nodes::longBuilder, ConcNode.OfLong::new);
            }
        }

        private static final class OfChar<P_IN> extends CollectorTask<P_IN, Character, Node.OfChar,
                Node.Builder.OfChar> {
            OfChar(PipelineHelper<Character> helper, Spliterator<P_IN> spliterator) {
                super(helper, spliterator, Nodes::charBuilder, ConcNode.OfChar::new);
            }
        }

        private static final class OfFloat<P_IN>
                extends CollectorTask<P_IN, Float, Node.OfFloat, Node.Builder.OfFloat> {
            public OfFloat(PipelineHelper<Float> helper, Spliterator<P_IN> spliterator) {
                super(helper, spliterator, Nodes::floatBuilder, ConcNode.OfFloat::new);
            }
        }

        private static final class OfDouble<P_IN>
                extends CollectorTask<P_IN, Double, Node.OfDouble, Node.Builder.OfDouble> {
            OfDouble(PipelineHelper<Double> helper, Spliterator<P_IN> spliterator) {
                super(helper, spliterator, Nodes::doubleBuilder, ConcNode.OfDouble::new);
            }
        }
    }
}


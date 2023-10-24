package com.github.svegon.utils.fast.util.objects.transform.objects;

import com.github.svegon.utils.fast.util.objects.transform.TransformingObject2IntMap;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectSet;

import java.util.function.Function;
import java.util.function.IntUnaryOperator;

public final class L2LKI2IVTranformingMap<K_IN, K_OUT>
        extends TransformingObject2IntMap<K_IN, Integer, Object2IntMap<K_IN>, K_OUT> {
    private final Function<? super K_IN, ? extends K_OUT> forwardingKeyTransformer;
    private final Function<? super K_OUT, ? extends K_IN> backingKeyTransformer;
    private final IntUnaryOperator forwardingValueTransformer;
    private final IntUnaryOperator backingValueTransformer;
    private final ObjectSet<Entry<K_OUT>> entrySet;

    @SuppressWarnings("unchecked")
    public L2LKI2IVTranformingMap(Object2IntMap<K_IN> k_inv_inMap,
                                  Function<? super K_IN, ? extends K_OUT> forwardingKeyTransformer,
                                  Function<? super K_OUT, ? extends K_IN> backingKeyTransformer,
                                  IntUnaryOperator forwardingValueTransformer,
                                  IntUnaryOperator backingValueTransformer) {
        super(k_inv_inMap);
        this.forwardingKeyTransformer = Preconditions.checkNotNull(forwardingKeyTransformer);
        this.backingKeyTransformer = Preconditions.checkNotNull(backingKeyTransformer);
        this.forwardingValueTransformer = Preconditions.checkNotNull(forwardingValueTransformer);
        this.backingValueTransformer = Preconditions.checkNotNull(backingValueTransformer);
        this.entrySet = (ObjectSet) new EntrySet<>(k_inv_inMap.object2IntEntrySet(), forwardingKeyTransformer,
                forwardingValueTransformer, backingValueTransformer);
    }

    @Override
    public void defaultReturnValue(int rv) {
        m.defaultReturnValue(backingValueTransformer.applyAsInt(rv));
    }

    @Override
    public int defaultReturnValue() {
        return forwardingValueTransformer.applyAsInt(m.defaultReturnValue());
    }

    @Override
    public ObjectSet<Entry<K_OUT>> object2IntEntrySet() {
        return entrySet;
    }

    @Override
    @SuppressWarnings("unchecked")
    public int removeInt(Object key) {
        try {
            return forwardingValueTransformer.applyAsInt(m.removeInt(backingKeyTransformer.apply((K_OUT) key)));
        } catch (ClassCastException e) {
            return defaultReturnValue();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public int getInt(Object key) {
        try {
            return forwardingValueTransformer.applyAsInt(m.getInt(backingKeyTransformer.apply((K_OUT) key)));
        } catch (ClassCastException e) {
            return defaultReturnValue();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public int put(Object key, int value) {
        try {
            return forwardingValueTransformer.applyAsInt(m.put(backingKeyTransformer.apply((K_OUT) key),
                    backingValueTransformer.applyAsInt(value)));
        } catch (ClassCastException e) {
            return defaultReturnValue();
        }
    }

    public Function<? super K_IN, ? extends K_OUT> getForwardingKeyTransformer() {
        return forwardingKeyTransformer;
    }

    private static class EntrySet<K_IN, K_OUT> extends L2LTransformingSet<Entry<K_IN>,
            L2LKI2IVTranformingEntry<K_IN, K_OUT>> {
        public EntrySet(final ObjectSet<Entry<K_IN>> set,
                        final Function<? super K_IN, ? extends K_OUT> forwardingKeyTransformer,
                        final IntUnaryOperator forwardingValueTransformer,
                        final IntUnaryOperator backingValueTransformer) {
            super(set, e -> new L2LKI2IVTranformingEntry<>(e, forwardingKeyTransformer, forwardingValueTransformer,
                    backingValueTransformer), e -> e.e);
        }
    }

    private static class L2LKI2IVTranformingEntry<K_IN, K_OUT>
            extends TransformingEntry<K_IN, Integer, Entry<K_IN>, K_OUT> {
        private final Function<? super K_IN, ? extends K_OUT> forwardingKeyTransformer;
        private final IntUnaryOperator forwardingValueTranformer;
        private final IntUnaryOperator backingValueTranformer;

        public L2LKI2IVTranformingEntry(Entry<K_IN> e,
                                        Function<? super K_IN, ? extends K_OUT> forwardingKeyTransformer,
                                        IntUnaryOperator forwardingValueTransformer,
                                        IntUnaryOperator backingValueTransformer) {
            // no checks necessary
            super(e);
            this.forwardingKeyTransformer = forwardingKeyTransformer;
            this.forwardingValueTranformer = forwardingValueTransformer;
            this.backingValueTranformer = backingValueTransformer;
        }

        @Override
        public int getIntValue() {
            return forwardingValueTranformer.applyAsInt(e.getIntValue());
        }

        @Override
        public int setValue(int value) {
            return forwardingValueTranformer.applyAsInt(e.setValue(backingValueTranformer.applyAsInt(value)));
        }

        @Override
        public K_OUT getKey() {
            return forwardingKeyTransformer.apply(e.getKey());
        }
    }
}

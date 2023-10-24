package com.github.svegon.utils.fast.util.ints.transform.ints;

import com.github.svegon.utils.fast.util.ints.transform.TransformingInt2IntMap;
import com.github.svegon.utils.fast.util.objects.transform.objects.L2LTransformingSet;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.IntUnaryOperator;

import java.util.Set;

public class I2IKI2IVTranformingMap extends TransformingInt2IntMap<Integer, Integer, Int2IntMap> {
    private final IntUnaryOperator forwardingKeyTransformer;
    private final IntUnaryOperator backingKeyTransformer;
    private final IntUnaryOperator forwardingValueTransformer;
    private final IntUnaryOperator backingValueTransformer;
    private final ObjectSet<Entry> entrySet;

    @SuppressWarnings("unchecked")
    public I2IKI2IVTranformingMap(Int2IntMap map,
                                  IntUnaryOperator forwardingKeyTransformer,
                                  IntUnaryOperator backingKeyTransformer,
                                  IntUnaryOperator forwardingValueTransformer,
                                  IntUnaryOperator backingValueTransformer) {
        super(map);
        this.forwardingKeyTransformer = Preconditions.checkNotNull(forwardingKeyTransformer);
        this.backingKeyTransformer = Preconditions.checkNotNull(backingKeyTransformer);
        this.forwardingValueTransformer = Preconditions.checkNotNull(forwardingValueTransformer);
        this.backingValueTransformer = Preconditions.checkNotNull(backingValueTransformer);
        this.entrySet = (ObjectSet) new EntrySet(map.int2IntEntrySet(), forwardingKeyTransformer,
                forwardingValueTransformer, backingValueTransformer);
    }

    @Override
    public final ObjectSet<Entry> int2IntEntrySet() {
        return entrySet;
    }

    @Override
    public int put(int key, int value) {
        return forwardingValueTransformer.apply(m.put(backingKeyTransformer.apply(key),
                backingValueTransformer.apply(value)));
    }

    @Override
    public int remove(int key) {
        return forwardingValueTransformer.apply(m.remove(backingKeyTransformer.apply(key)));
    }

    @Override
    public int get(int key) {
        return forwardingValueTransformer.apply(m.get(backingKeyTransformer.apply(key)));
    }

    @Override
    public int defaultReturnValue() {
        return getForwardingValueTransformer().apply(m.defaultReturnValue());
    }

    @Override
    public void defaultReturnValue(int rv) {
        m.defaultReturnValue(getBackingValueTransformer().apply(rv));
    }

    public IntUnaryOperator getForwardingKeyTransformer() {
        return forwardingKeyTransformer;
    }

    public IntUnaryOperator getBackingKeyTransformer() {
        return backingKeyTransformer;
    }

    public IntUnaryOperator getForwardingValueTransformer() {
        return forwardingValueTransformer;
    }

    public IntUnaryOperator getBackingValueTransformer() {
        return backingValueTransformer;
    }

    private static class EntrySet extends L2LTransformingSet<Entry, I2IKI2IVTransformingEntry> {
        public EntrySet(final Set<Entry> set,
                        final IntUnaryOperator forwardingKeyTransformer,
                        final IntUnaryOperator forwardingValueTransformer,
                        final IntUnaryOperator backingValueTransformer) {
            super(set, e -> new I2IKI2IVTransformingEntry(e, forwardingKeyTransformer, forwardingValueTransformer,
                    backingValueTransformer), e -> e.e);
        }
    }

    private static class I2IKI2IVTransformingEntry extends TransformingEntry<Integer, Integer, Entry> {
        private final IntUnaryOperator forwardingKeyTransformer;
        private final IntUnaryOperator forwardingValueTransformer;
        private final IntUnaryOperator backingValueTransformer;

        public I2IKI2IVTransformingEntry(final Entry e,
                                         final IntUnaryOperator forwardingKeyTransformer,
                                         final IntUnaryOperator forwardingValueTransformer,
                                         final IntUnaryOperator backingValueTransformer) {
            // no checks necessary
            super(e);
            this.forwardingKeyTransformer = forwardingKeyTransformer;
            this.forwardingValueTransformer = forwardingValueTransformer;
            this.backingValueTransformer = backingValueTransformer;
        }

        @Override
        public int getIntKey() {
            return forwardingKeyTransformer.apply(e.getIntKey());
        }

        @Override
        public int getIntValue() {
            return forwardingValueTransformer.apply(e.getIntKey());
        }

        @Override
        public int setValue(int value) {
            return forwardingValueTransformer.apply(e.setValue(backingValueTransformer.apply(value)));
        }
    }
}

package io.github.svegon.utils.fast.util.shorts.transform.shorts;

import io.github.svegon.utils.fast.util.objects.transform.objects.L2LTransformingSet;
import io.github.svegon.utils.fast.util.shorts.transform.TransformingShort2ShortMap;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.shorts.Short2ShortMap;
import it.unimi.dsi.fastutil.shorts.ShortUnaryOperator;

import java.util.Set;

public class S2SKS2SVTranformingMap extends TransformingShort2ShortMap<Short, Short, Short2ShortMap> {
    private final ShortUnaryOperator forwardingKeyTransformer;
    private final ShortUnaryOperator backingKeyTransformer;
    private final ShortUnaryOperator forwardingValueTransformer;
    private final ShortUnaryOperator backingValueTransformer;
    private final ObjectSet<Entry> entrySet;

    @SuppressWarnings("unchecked")
    public S2SKS2SVTranformingMap(Short2ShortMap map,
                                  ShortUnaryOperator forwardingKeyTransformer,
                                  ShortUnaryOperator backingKeyTransformer,
                                  ShortUnaryOperator forwardingValueTransformer,
                                  ShortUnaryOperator backingValueTransformer) {
        super(map);
        this.forwardingKeyTransformer = Preconditions.checkNotNull(forwardingKeyTransformer);
        this.backingKeyTransformer = Preconditions.checkNotNull(backingKeyTransformer);
        this.forwardingValueTransformer = Preconditions.checkNotNull(forwardingValueTransformer);
        this.backingValueTransformer = Preconditions.checkNotNull(backingValueTransformer);
        this.entrySet = (ObjectSet) new EntrySet(map.short2ShortEntrySet(), forwardingKeyTransformer,
                forwardingValueTransformer, backingValueTransformer);
    }

    @Override
    public final ObjectSet<Entry> short2ShortEntrySet() {
        return entrySet;
    }

    @Override
    public short put(short key, short value) {
        return forwardingValueTransformer.apply(m.put(backingKeyTransformer.apply(key),
                backingValueTransformer.apply(value)));
    }

    @Override
    public short remove(short key) {
        return forwardingValueTransformer.apply(m.remove(backingKeyTransformer.apply(key)));
    }

    @Override
    public short get(short key) {
        return forwardingValueTransformer.apply(m.get(backingKeyTransformer.apply(key)));
    }

    @Override
    public short defaultReturnValue() {
        return getForwardingValueTransformer().apply(m.defaultReturnValue());
    }

    @Override
    public void defaultReturnValue(short rv) {
        m.defaultReturnValue(getBackingValueTransformer().apply(rv));
    }

    public ShortUnaryOperator getForwardingKeyTransformer() {
        return forwardingKeyTransformer;
    }

    public ShortUnaryOperator getBackingKeyTransformer() {
        return backingKeyTransformer;
    }

    public ShortUnaryOperator getForwardingValueTransformer() {
        return forwardingValueTransformer;
    }

    public ShortUnaryOperator getBackingValueTransformer() {
        return backingValueTransformer;
    }

    private static class EntrySet extends L2LTransformingSet<Entry, S2SKS2SVTransformingEntry> {
        public EntrySet(final Set<Entry> set,
                        final ShortUnaryOperator forwardingKeyTransformer,
                        final ShortUnaryOperator forwardingValueTransformer,
                        final ShortUnaryOperator backingValueTransformer) {
            super(set, e -> new S2SKS2SVTransformingEntry(e, forwardingKeyTransformer, forwardingValueTransformer,
                    backingValueTransformer), e -> e.e);
        }
    }

    private static class S2SKS2SVTransformingEntry extends TransformingEntry<Short, Short, Entry> {
        private final ShortUnaryOperator forwardingKeyTransformer;
        private final ShortUnaryOperator forwardingValueTransformer;
        private final ShortUnaryOperator backingValueTransformer;

        public S2SKS2SVTransformingEntry(final Entry e,
                                         final ShortUnaryOperator forwardingKeyTransformer,
                                         final ShortUnaryOperator forwardingValueTransformer,
                                         final ShortUnaryOperator backingValueTransformer) {
            // no checks necessary
            super(e);
            this.forwardingKeyTransformer = forwardingKeyTransformer;
            this.forwardingValueTransformer = forwardingValueTransformer;
            this.backingValueTransformer = backingValueTransformer;
        }

        @Override
        public short getShortKey() {
            return forwardingKeyTransformer.apply(e.getShortKey());
        }

        @Override
        public short getShortValue() {
            return forwardingValueTransformer.apply(e.getShortKey());
        }

        @Override
        public short setValue(short value) {
            return forwardingValueTransformer.apply(e.setValue(backingValueTransformer.apply(value)));
        }
    }
}

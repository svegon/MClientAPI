package io.github.svegon.utils.fast.util.shorts.transform.objects;

import io.github.svegon.utils.fast.util.objects.transform.objects.L2LTransformingSet;
import io.github.svegon.utils.fast.util.shorts.transform.TransformingShort2ShortMap;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.Object2ShortFunction;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.shorts.Short2ObjectFunction;

import java.util.Map;
import java.util.Set;

public class L2SKL2SVTranformingMap<K_IN, V_IN> extends TransformingShort2ShortMap<K_IN, V_IN, Map<K_IN, V_IN>> {
    private final Object2ShortFunction<? super K_IN> forwardingKeyTransformer;
    private final Short2ObjectFunction<? extends K_IN> backingKeyTransformer;
    private final Object2ShortFunction<? super V_IN> forwardingValueTransformer;
    private final Short2ObjectFunction<? extends V_IN> backingValueTransformer;
    private final ObjectSet<Entry> entrySet;

    @SuppressWarnings("unchecked")
    public L2SKL2SVTranformingMap(Map<K_IN, V_IN> map,
                                  Object2ShortFunction<? super K_IN> forwardingKeyTransformer,
                                  Short2ObjectFunction<? extends K_IN> backingKeyTransformer,
                                  Object2ShortFunction<? super V_IN> forwardingValueTransformer,
                                  Short2ObjectFunction<? extends V_IN> backingValueTransformer) {
        super(map);
        this.forwardingKeyTransformer = Preconditions.checkNotNull(forwardingKeyTransformer);
        this.backingKeyTransformer = Preconditions.checkNotNull(backingKeyTransformer);
        this.forwardingValueTransformer = Preconditions.checkNotNull(forwardingValueTransformer);
        this.backingValueTransformer = Preconditions.checkNotNull(backingValueTransformer);
        this.entrySet = (ObjectSet) new EntrySet<>(map.entrySet(), forwardingKeyTransformer,
                forwardingValueTransformer, backingValueTransformer);
    }

    @Override
    public final ObjectSet<Entry> short2ShortEntrySet() {
        return entrySet;
    }

    @Override
    public short put(short key, short value) {
        return forwardingValueTransformer.apply(m.put(backingKeyTransformer.get(key),
                backingValueTransformer.get(value)));
    }

    @Override
    public short remove(short key) {
        return forwardingValueTransformer.apply(m.remove(backingKeyTransformer.get(key)));
    }

    @Override
    public short get(short key) {
        return forwardingValueTransformer.apply(m.get(backingKeyTransformer.get(key)));
    }

    public Object2ShortFunction<? super K_IN> getForwardingKeyTransformer() {
        return forwardingKeyTransformer;
    }

    public Short2ObjectFunction<? extends K_IN> getBackingKeyTransformer() {
        return backingKeyTransformer;
    }

    public Object2ShortFunction<? super V_IN> getForwardingValueTransformer() {
        return forwardingValueTransformer;
    }

    public Short2ObjectFunction<? extends V_IN> getBackingValueTransformer() {
        return backingValueTransformer;
    }

    private static class EntrySet<K_IN, V_IN> extends L2LTransformingSet<Map.Entry<K_IN, V_IN>,
                L2SKL2LVTransformingEntry<K_IN, V_IN>> {
        public EntrySet(final Set<Map.Entry<K_IN, V_IN>> set,
                        final Object2ShortFunction<? super K_IN> forwardingKeyTransformer,
                        final Object2ShortFunction<? super V_IN> forwardingValueTransformer,
                        final Short2ObjectFunction<? extends V_IN> backingValueTransformer) {
            super(set, e -> new L2SKL2LVTransformingEntry<>(e, forwardingKeyTransformer, forwardingValueTransformer,
                    backingValueTransformer), e -> e.e);
        }
    }

    private static class L2SKL2LVTransformingEntry<K_IN, V_IN>
            extends TransformingEntry<K_IN, V_IN, Map.Entry<K_IN, V_IN>> {
        private final Object2ShortFunction<? super K_IN> forwardingKeyTransformer;
        private final Object2ShortFunction<? super V_IN> forwardingValueTranformer;
        private final Short2ObjectFunction<? extends V_IN> backingValueTranformer;

        public L2SKL2LVTransformingEntry(Map.Entry<K_IN, V_IN> e,
                                         Object2ShortFunction<? super K_IN> forwardingKeyTransformer,
                                         Object2ShortFunction<? super V_IN> forwardingValueTransformer,
                                         Short2ObjectFunction<? extends V_IN> backingValueTransformer) {
            // no checks necessary
            super(e);
            this.forwardingKeyTransformer = forwardingKeyTransformer;
            this.forwardingValueTranformer = forwardingValueTransformer;
            this.backingValueTranformer = backingValueTransformer;
        }

        @Override
        public short getShortKey() {
            return forwardingKeyTransformer.apply(e.getKey());
        }

        @Override
        public short getShortValue() {
            return forwardingValueTranformer.apply(e.getValue());
        }

        @Override
        public short setValue(short value) {
            return forwardingValueTranformer.apply(e.setValue(backingValueTranformer.get(value)));
        }
    }
}

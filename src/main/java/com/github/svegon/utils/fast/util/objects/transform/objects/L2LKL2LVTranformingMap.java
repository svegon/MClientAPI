package com.github.svegon.utils.fast.util.objects.transform.objects;

import com.github.svegon.utils.fast.util.objects.transform.TransformingObject2ObjectMap;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.ObjectSet;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class L2LKL2LVTranformingMap<K_IN, V_IN, K_OUT, V_OUT>
        extends TransformingObject2ObjectMap<K_IN, V_IN, Map<K_IN, V_IN>, K_OUT, V_OUT> {
    private final Function<? super K_IN, ? extends K_OUT> forwardingKeyTransformer;
    private final Function<? super K_OUT, ? extends K_IN> backingKeyTransformer;
    private final Function<? super V_IN, ? extends V_OUT> forwardingValueTransformer;
    private final Function<? super V_OUT, ? extends V_IN> backingValueTransformer;
    private final ObjectSet<Entry<K_OUT, V_OUT>> entrySet;

    @SuppressWarnings("unchecked")
    public L2LKL2LVTranformingMap(Map<K_IN, V_IN> k_inv_inMap,
                                  Function<? super K_IN, ? extends K_OUT> forwardingKeyTransformer,
                                  Function<? super K_OUT, ? extends K_IN> backingKeyTransformer,
                                  Function<? super V_IN, ? extends V_OUT> forwardingValueTransformer,
                                  Function<? super V_OUT, ? extends V_IN> backingValueTransformer) {
        super(k_inv_inMap);
        this.forwardingKeyTransformer = Preconditions.checkNotNull(forwardingKeyTransformer);
        this.backingKeyTransformer = Preconditions.checkNotNull(backingKeyTransformer);
        this.forwardingValueTransformer = Preconditions.checkNotNull(forwardingValueTransformer);
        this.backingValueTransformer = Preconditions.checkNotNull(backingValueTransformer);
        this.entrySet = (ObjectSet) new EntrySet<>(k_inv_inMap.entrySet(), forwardingKeyTransformer, forwardingValueTransformer,
                backingValueTransformer);
    }

    @Override
    public final ObjectSet<Entry<K_OUT, V_OUT>> object2ObjectEntrySet() {
        return entrySet;
    }

    @Override
    public V_OUT put(K_OUT key, V_OUT value) {
        return forwardingValueTransformer.apply(m.put(backingKeyTransformer.apply(key),
                backingValueTransformer.apply(value)));
    }

    @Override
    @SuppressWarnings("unchecked")
    public V_OUT remove(Object key) {
        try {
            return forwardingValueTransformer.apply(m.remove(backingKeyTransformer.apply((K_OUT) key)));
        } catch (ClassCastException e) {
            return defaultReturnValue();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public V_OUT get(Object key) {
        try {
            return forwardingValueTransformer.apply(m.get(backingKeyTransformer.apply((K_OUT) key)));
        } catch (ClassCastException e) {
            return defaultReturnValue();
        }
    }

    public Function<? super K_IN, ? extends K_OUT> getForwardingKeyTransformer() {
        return forwardingKeyTransformer;
    }

    public Function<? super K_OUT, ? extends K_IN> getBackingKeyTransformer() {
        return backingKeyTransformer;
    }

    public Function<? super V_IN, ? extends V_OUT> getForwardingValueTransformer() {
        return forwardingValueTransformer;
    }

    public Function<? super V_OUT, ? extends V_IN> getBackingValueTransformer() {
        return backingValueTransformer;
    }

    private static class EntrySet<K_IN, V_IN, K_OUT, V_OUT> extends L2LTransformingSet<Map.Entry<K_IN, V_IN>,
            L2LKL2LVTransformingEntry<K_IN, V_IN, K_OUT, V_OUT>> {
        public EntrySet(final Set<Map.Entry<K_IN, V_IN>> set,
                        final Function<? super K_IN, ? extends K_OUT> forwardingKeyTransformer,
                        final Function<? super V_IN, ? extends V_OUT> forwardingValueTransformer,
                        final Function<? super V_OUT, ? extends V_IN> backingValueTransformer) {
            super(set, e -> new L2LKL2LVTransformingEntry<>(e, forwardingKeyTransformer, forwardingValueTransformer,
                    backingValueTransformer), e -> e.e);
        }
    }

    private static class L2LKL2LVTransformingEntry<K_IN, V_IN, K_OUT, V_OUT>
            extends TransformingEntry<K_IN, V_IN, Map.Entry<K_IN, V_IN>, K_OUT, V_OUT> {
        private final Function<? super K_IN, ? extends K_OUT> forwardingKeyTransformer;
        private final Function<? super V_IN, ? extends V_OUT> forwardingValueTranformer;
        private final Function<? super V_OUT, ? extends V_IN> backingValueTranformer;

        public L2LKL2LVTransformingEntry(Map.Entry<K_IN, V_IN> e,
                                         Function<? super K_IN, ? extends K_OUT> forwardingKeyTransformer,
                                         Function<? super V_IN, ? extends V_OUT> forwardingValueTransformer,
                                         Function<? super V_OUT, ? extends V_IN> backingValueTransformer) {
            // no checks necessary
            super(e);
            this.forwardingKeyTransformer = forwardingKeyTransformer;
            this.forwardingValueTranformer = forwardingValueTransformer;
            this.backingValueTranformer = backingValueTransformer;
        }

        @Override
        public K_OUT getKey() {
            return forwardingKeyTransformer.apply(e.getKey());
        }

        @Override
        public V_OUT getValue() {
            return forwardingValueTranformer.apply(e.getValue());
        }

        @Override
        public V_OUT setValue(V_OUT value) {
            return forwardingValueTranformer.apply(e.setValue(backingValueTranformer.apply(value)));
        }
    }
}

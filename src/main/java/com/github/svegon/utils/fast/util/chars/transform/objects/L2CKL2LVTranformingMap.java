package com.github.svegon.utils.fast.util.chars.transform.objects;

import com.github.svegon.utils.fast.util.objects.transform.objects.L2LTransformingSet;
import com.github.svegon.utils.fast.util.chars.transform.TransformingChar2ObjectMap;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.Object2CharFunction;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.chars.Char2ObjectFunction;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class L2CKL2LVTranformingMap<K_IN, V_IN, V_OUT>
        extends TransformingChar2ObjectMap<K_IN, V_IN, Map<K_IN, V_IN>, V_OUT> {
    private final Object2CharFunction<? super K_IN> forwardingKeyTransformer;
    private final Char2ObjectFunction<? extends K_IN> backingKeyTransformer;
    private final Function<? super V_IN, ? extends V_OUT> forwardingValueTransformer;
    private final Function<? super V_OUT, ? extends V_IN> backingValueTransformer;
    private final ObjectSet<Entry<V_OUT>> entrySet;

    @SuppressWarnings("unchecked")
    public L2CKL2LVTranformingMap(Map<K_IN, V_IN> map,
                                  Object2CharFunction<? super K_IN> forwardingKeyTransformer,
                                  Char2ObjectFunction<? extends K_IN> backingKeyTransformer,
                                  Function<? super V_IN, ? extends V_OUT> forwardingValueTransformer,
                                  Function<? super V_OUT, ? extends V_IN> backingValueTransformer) {
        super(map);
        this.forwardingKeyTransformer = Preconditions.checkNotNull(forwardingKeyTransformer);
        this.backingKeyTransformer = Preconditions.checkNotNull(backingKeyTransformer);
        this.forwardingValueTransformer = Preconditions.checkNotNull(forwardingValueTransformer);
        this.backingValueTransformer = Preconditions.checkNotNull(backingValueTransformer);
        this.entrySet = (ObjectSet) new EntrySet<>(map.entrySet(), forwardingKeyTransformer,
                forwardingValueTransformer, backingValueTransformer);
    }

    @Override
    public final ObjectSet<Entry<V_OUT>> char2ObjectEntrySet() {
        return entrySet;
    }

    @Override
    public V_OUT put(char key, V_OUT value) {
        return forwardingValueTransformer.apply(m.put(backingKeyTransformer.apply(key),
                backingValueTransformer.apply(value)));
    }

    @Override
    public V_OUT remove(char key) {
        return forwardingValueTransformer.apply(m.remove(backingKeyTransformer.apply(key)));
    }

    @Override
    public V_OUT get(char key) {
        return forwardingValueTransformer.apply(m.get(backingKeyTransformer.apply(key)));
    }

    public Object2CharFunction<? super K_IN> getForwardingKeyTransformer() {
        return forwardingKeyTransformer;
    }

    public Char2ObjectFunction<? extends K_IN> getBackingKeyTransformer() {
        return backingKeyTransformer;
    }

    public Function<? super V_IN, ? extends V_OUT> getForwardingValueTransformer() {
        return forwardingValueTransformer;
    }

    public Function<? super V_OUT, ? extends V_IN> getBackingValueTransformer() {
        return backingValueTransformer;
    }

    private static class EntrySet<K_IN, V_IN, V_OUT> extends L2LTransformingSet<Map.Entry<K_IN, V_IN>,
            L2SKL2LVTransformingEntry<K_IN, V_IN, V_OUT>> {
        public EntrySet(final Set<Map.Entry<K_IN, V_IN>> set,
                        final Object2CharFunction<? super K_IN> forwardingKeyTransformer,
                        final Function<? super V_IN, ? extends V_OUT> forwardingValueTransformer,
                        final Function<? super V_OUT, ? extends V_IN> backingValueTransformer) {
            super(set, e -> new L2SKL2LVTransformingEntry<>(e, forwardingKeyTransformer, forwardingValueTransformer,
                    backingValueTransformer), e -> e.e);
        }
    }

    private static class L2SKL2LVTransformingEntry<K_IN, V_IN, V_OUT>
            extends TransformingEntry<K_IN, V_IN, Map.Entry<K_IN, V_IN>, V_OUT> {
        private final Object2CharFunction<? super K_IN> forwardingKeyTransformer;
        private final Function<? super V_IN, ? extends V_OUT> forwardingValueTranformer;
        private final Function<? super V_OUT, ? extends V_IN> backingValueTranformer;

        public L2SKL2LVTransformingEntry(Map.Entry<K_IN, V_IN> e,
                                         Object2CharFunction<? super K_IN> forwardingKeyTransformer,
                                         Function<? super V_IN, ? extends V_OUT> forwardingValueTransformer,
                                         Function<? super V_OUT, ? extends V_IN> backingValueTransformer) {
            // no checks necessary
            super(e);
            this.forwardingKeyTransformer = forwardingKeyTransformer;
            this.forwardingValueTranformer = forwardingValueTransformer;
            this.backingValueTranformer = backingValueTransformer;
        }

        @Override
        public char getCharKey() {
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

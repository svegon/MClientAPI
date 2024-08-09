package io.github.svegon.utils.fast.util.bytes.transform.bytes;

import io.github.svegon.utils.fast.util.objects.transform.objects.L2LTransformingSet;
import io.github.svegon.utils.fast.util.bytes.transform.TransformingByte2ByteMap;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.Object2ByteFunction;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectFunction;

import java.util.Map;
import java.util.Set;

public class L2BKL2BVTranformingMap<K_IN, V_IN>
        extends TransformingByte2ByteMap<K_IN, V_IN, Map<K_IN, V_IN>> {
    private final Object2ByteFunction<? super K_IN> forwardingKeyTransformer;
    private final Byte2ObjectFunction<? extends K_IN> backingKeyTransformer;
    private final Object2ByteFunction<? super V_IN> forwardingValueTransformer;
    private final Byte2ObjectFunction<? extends V_IN> backingValueTransformer;
    private final ObjectSet<Entry> entrySet;

    @SuppressWarnings("unchecked")
    public L2BKL2BVTranformingMap(Map<K_IN, V_IN> map,
                                  Object2ByteFunction<? super K_IN> forwardingKeyTransformer,
                                  Byte2ObjectFunction<? extends K_IN> backingKeyTransformer,
                                  Object2ByteFunction<? super V_IN> forwardingValueTransformer,
                                  Byte2ObjectFunction<? extends V_IN>backingValueTransformer) {
        super(map);
        this.forwardingKeyTransformer = Preconditions.checkNotNull(forwardingKeyTransformer);
        this.backingKeyTransformer = Preconditions.checkNotNull(backingKeyTransformer);
        this.forwardingValueTransformer = Preconditions.checkNotNull(forwardingValueTransformer);
        this.backingValueTransformer = Preconditions.checkNotNull(backingValueTransformer);
        this.entrySet = (ObjectSet) new EntrySet<>(map.entrySet(), forwardingKeyTransformer,
                forwardingValueTransformer, backingValueTransformer);
    }

    @Override
    public final ObjectSet<Entry> byte2ByteEntrySet() {
        return entrySet;
    }

    @Override
    public byte put(byte key, byte value) {
        return forwardingValueTransformer.apply(m.put(backingKeyTransformer.get(key),
                backingValueTransformer.get(value)));
    }

    @Override
    public byte remove(byte key) {
        return forwardingValueTransformer.apply(m.remove(backingKeyTransformer.get(key)));
    }

    @Override
    public byte get(byte key) {
        return forwardingValueTransformer.apply(m.get(backingKeyTransformer.get(key)));
    }

    public Object2ByteFunction<? super K_IN> getForwardingKeyTransformer() {
        return forwardingKeyTransformer;
    }

    public Byte2ObjectFunction<? extends K_IN> getBackingKeyTransformer() {
        return backingKeyTransformer;
    }

    public Object2ByteFunction<? super V_IN> getForwardingValueTransformer() {
        return forwardingValueTransformer;
    }

    public Byte2ObjectFunction<? extends V_IN> getBackingValueTransformer() {
        return backingValueTransformer;
    }

    private static class EntrySet<K_IN, V_IN> extends L2LTransformingSet<Map.Entry<K_IN, V_IN>,
                L2SKL2LVTransformingEntry<K_IN, V_IN>> {
        public EntrySet(final Set<Map.Entry<K_IN, V_IN>> set,
                        final Object2ByteFunction<? super K_IN> forwardingKeyTransformer,
                        final Object2ByteFunction<? super V_IN> forwardingValueTransformer,
                        final Byte2ObjectFunction<? extends V_IN> backingValueTransformer) {
            super(set, e -> new L2SKL2LVTransformingEntry<>(e, forwardingKeyTransformer, forwardingValueTransformer,
                    backingValueTransformer), e -> e.e);
        }
    }

    private static class L2SKL2LVTransformingEntry<K_IN, V_IN>
            extends TransformingEntry<K_IN, V_IN, Map.Entry<K_IN, V_IN>> {
        private final Object2ByteFunction<? super K_IN> forwardingKeyTransformer;
        private final Object2ByteFunction<? super V_IN> forwardingValueTransformer;
        private final Byte2ObjectFunction<? extends V_IN> backingValueTransformer;

        public L2SKL2LVTransformingEntry(Map.Entry<K_IN, V_IN> e,
                final Object2ByteFunction<? super K_IN> forwardingKeyTransformer,
                final Object2ByteFunction<? super V_IN> forwardingValueTransformer,
                final Byte2ObjectFunction<? extends V_IN> backingValueTransformer) {
            // no checks necessary
            super(e);
            this.forwardingKeyTransformer = forwardingKeyTransformer;
            this.forwardingValueTransformer = forwardingValueTransformer;
            this.backingValueTransformer = backingValueTransformer;
        }

        @Override
        public byte getByteKey() {
            return forwardingKeyTransformer.apply(e.getKey());
        }

        @Override
        public byte getByteValue() {
            return forwardingValueTransformer.apply(e.getValue());
        }

        @Override
        public byte setValue(byte value) {
            return forwardingValueTransformer.apply(e.setValue(backingValueTransformer.get(value)));
        }
    }
}

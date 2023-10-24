package com.github.svegon.utils.fast.util.bytes.transform.bytes;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ByteFunction;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectFunction;

public final class L2BKL2BVDefRetValueRedirectingTranformingMap<K_IN, V_IN>
        extends L2BKL2BVTranformingMap<K_IN, V_IN> {
    public L2BKL2BVDefRetValueRedirectingTranformingMap(Object2ObjectMap<K_IN, V_IN> backingMap,
                                                        Object2ByteFunction<? super K_IN> forwardingKeyTransformer,
                                                        Byte2ObjectFunction<? extends K_IN> backingKeyTransformer,
                                                        Object2ByteFunction<? super V_IN> forwardingValueTransformer,
                                                        Byte2ObjectFunction<? extends V_IN> backingValueTransformer) {
        super(backingMap, forwardingKeyTransformer, backingKeyTransformer, forwardingValueTransformer,
                backingValueTransformer);
    }

    @Override
    public byte defaultReturnValue() {
        return getForwardingValueTransformer().apply(getBackingMap().defaultReturnValue());
    }

    @Override
    public void defaultReturnValue(byte rv) {
        getBackingMap().defaultReturnValue(getBackingValueTransformer().apply(rv));
    }

    private Object2ObjectMap<K_IN, V_IN> getBackingMap() {
        return (Object2ObjectMap<K_IN, V_IN>) m;
    }
}

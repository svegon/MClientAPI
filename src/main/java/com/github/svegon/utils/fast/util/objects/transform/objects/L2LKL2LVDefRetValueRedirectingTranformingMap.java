package com.github.svegon.utils.fast.util.objects.transform.objects;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;

import java.util.function.Function;

public final class L2LKL2LVDefRetValueRedirectingTranformingMap<K_IN, V_IN, K_OUT, V_OUT>
        extends L2LKL2LVTranformingMap<K_IN, V_IN, K_OUT, V_OUT> {
    public L2LKL2LVDefRetValueRedirectingTranformingMap(Object2ObjectMap<K_IN, V_IN> k_inv_inMap,
                                            Function<? super K_IN, ? extends K_OUT> forwardingKeyTransformer,
                                            Function<? super K_OUT, ? extends K_IN> backingKeyTransformer,
                                            Function<? super V_IN, ? extends V_OUT> forwardingValueTransformer,
                                            Function<? super V_OUT, ? extends V_IN> backingValueTransformer) {
        super(k_inv_inMap, forwardingKeyTransformer, backingKeyTransformer, forwardingValueTransformer,
                backingValueTransformer);
    }

    @Override
    public V_OUT defaultReturnValue() {
        return getForwardingValueTransformer().apply(getOriginal().defaultReturnValue());
    }

    @Override
    public void defaultReturnValue(V_OUT rv) {
        getOriginal().defaultReturnValue(getBackingValueTransformer().apply(rv));
    }

    private Object2ObjectMap<K_IN, V_IN> getOriginal() {
        return (Object2ObjectMap<K_IN, V_IN>) m;
    }
}

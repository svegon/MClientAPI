package com.github.svegon.utils.fast.util.chars.transform.objects;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2CharFunction;
import it.unimi.dsi.fastutil.chars.Char2ObjectFunction;

import java.util.function.Function;

public final class L2CKL2LVDefRetValueRedirectingTranformingMap<K_IN, V_IN, V_OUT>
        extends L2CKL2LVTranformingMap<K_IN, V_IN, V_OUT> {
    public L2CKL2LVDefRetValueRedirectingTranformingMap(Object2ObjectMap<K_IN, V_IN> backingMap,
                                                        Object2CharFunction<? super K_IN> forwardingKeyTransformer,
                                                        Char2ObjectFunction<? extends K_IN> backingKeyTransformer,
                                                        Function<? super V_IN, ? extends V_OUT> forwardingValueTransformer,
                                                        Function<? super V_OUT, ? extends V_IN> backingValueTransformer) {
        super(backingMap, forwardingKeyTransformer, backingKeyTransformer, forwardingValueTransformer,
                backingValueTransformer);
    }

    @Override
    public V_OUT defaultReturnValue() {
        return getForwardingValueTransformer().apply(getBackingMap().defaultReturnValue());
    }

    @Override
    public void defaultReturnValue(V_OUT rv) {
        getBackingMap().defaultReturnValue(getBackingValueTransformer().apply(rv));
    }

    private Object2ObjectMap<K_IN, V_IN> getBackingMap() {
        return (Object2ObjectMap<K_IN, V_IN>) m;
    }
}

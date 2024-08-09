package io.github.svegon.utils.fast.util.shorts.transform.objects;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ShortFunction;
import it.unimi.dsi.fastutil.shorts.Short2ObjectFunction;

import java.util.function.Function;

public final class L2SKL2LVDefRetValueRedirectingTransformingMap<K_IN, V_IN, V_OUT>
        extends L2SKL2LVTranformingMap<K_IN, V_IN, V_OUT> {
    public L2SKL2LVDefRetValueRedirectingTransformingMap(Object2ObjectMap<K_IN, V_IN> backingMap,
                                                         Object2ShortFunction<? super K_IN> forwardingKeyTransformer,
                                                         Short2ObjectFunction<? extends K_IN> backingKeyTransformer,
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

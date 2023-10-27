package io.github.svegon.utils.fast.util.shorts.transform.objects;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ShortFunction;
import it.unimi.dsi.fastutil.shorts.Short2ObjectFunction;

public final class L2SKL2SVDefRetValueRedirectingTransformingMap<K_IN, V_IN> extends L2SKL2SVTranformingMap<K_IN, V_IN> {
    public L2SKL2SVDefRetValueRedirectingTransformingMap(Object2ObjectMap<K_IN, V_IN> backingMap,
                                                         Object2ShortFunction<? super K_IN> forwardingKeyTransformer,
                                                         Short2ObjectFunction<? extends K_IN> backingKeyTransformer,
                                                         Object2ShortFunction<? super V_IN> forwardingValueTransformer,
                                                         Short2ObjectFunction<? extends V_IN> backingValueTransformer) {
        super(backingMap, forwardingKeyTransformer, backingKeyTransformer, forwardingValueTransformer,
                backingValueTransformer);
    }

    @Override
    public short defaultReturnValue() {
        return getForwardingValueTransformer().apply(getBackingMap().defaultReturnValue());
    }

    @Override
    public void defaultReturnValue(short rv) {
        getBackingMap().defaultReturnValue(getBackingValueTransformer().apply(rv));
    }

    private Object2ObjectMap<K_IN, V_IN> getBackingMap() {
        return (Object2ObjectMap<K_IN, V_IN>) m;
    }
}

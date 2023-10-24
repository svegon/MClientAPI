package com.github.svegon.capi.util;

import com.google.common.base.Preconditions;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.FogShape;

public final class FogContext {
    private final BackgroundRenderer.FogData fogData;

    public FogContext(BackgroundRenderer.FogData fogData) {
        this.fogData = fogData;
    }

    public BackgroundRenderer.FogType fogType() {
        return fogData.fogType;
    }

    public float fogStart() {
        return fogData.fogStart;
    }

    public void fogStart(float start) {
        fogData.fogStart = start;
    }

    public float fogEnd() {
        return fogData.fogEnd;
    }

    public void fogEnd(float end) {
        fogData.fogEnd = end;
    }

    public FogShape fogShape() {
        return fogData.fogShape;
    }

    public void fogShape(FogShape shape) {
        fogData.fogShape = Preconditions.checkNotNull(shape);
    }
}

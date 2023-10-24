package com.github.svegon.capi.event.input;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface MousePosListener {
    Event<MousePosListener> EVENT = EventFactory.createArrayBacked(MousePosListener.class,
            (prevX, prevY, x, y, deltaX, deltaY) -> {}, listeners -> (prevX, prevY, x, y, deltaX, deltaY) -> {
        for (MousePosListener listener : listeners) {
            listener.onMousePos(prevX, prevY, x, y, deltaX, deltaY);
        }
            });

    void onMousePos(double prevX, double prevY, double x, double y, double deltaX, double deltaY);
}

package io.github.svegon.capi.event.render;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.DrawContext;

public interface GUIRenderListener {
    Event<GUIRenderListener> EVENT = EventFactory.createArrayBacked(GUIRenderListener.class,
            (startTime, tick, context, mouseX, mouseY, tickDelta) -> {},
            listeners -> (startTime, tick, context, mouseX, mouseY, tickDelta) -> {
                for (GUIRenderListener listener : listeners) {
                    listener.onGUIRender(startTime, tick, context, mouseX, mouseY, tickDelta);
                }
            });

    void onGUIRender(long startTime, boolean tick, DrawContext context, int mouseX, int mouseY, float tickDelta);
}

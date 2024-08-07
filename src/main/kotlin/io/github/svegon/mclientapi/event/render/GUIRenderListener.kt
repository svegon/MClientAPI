package io.github.svegon.capi.event.render

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.gui.DrawContext
import java.util.function.Function

interface GUIRenderListener {
    fun onGUIRender(startTime: Long, tick: Boolean, context: DrawContext?, mouseX: Int, mouseY: Int, tickDelta: Float)

    companion object {
        val EVENT: Event<GUIRenderListener?> = EventFactory.createArrayBacked<GUIRenderListener?>(
            GUIRenderListener::class.java,
            GUIRenderListener { startTime: Long, tick: Boolean, context: DrawContext?, mouseX: Int, mouseY: Int, tickDelta: Float -> },
            Function<Array<GUIRenderListener?>, GUIRenderListener?> { listeners: Array<GUIRenderListener?>? ->
                GUIRenderListener { startTime: Long, tick: Boolean, context: DrawContext?, mouseX: Int, mouseY: Int, tickDelta: Float ->
                    for (listener in listeners) {
                        listener.onGUIRender(startTime, tick, context, mouseX, mouseY, tickDelta)
                    }
                }
            })
    }
}

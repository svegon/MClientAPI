package io.github.svegon.mclientapi.client.event.render

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.gui.DrawContext

fun interface GUIRenderListener {
    fun onGUIRender(startTime: Long, tick: Boolean, context: DrawContext, mouseX: Int, mouseY: Int, tickDelta: Float)

    companion object {
        @JvmField
        val EVENT: Event<GUIRenderListener> = EventFactory.createArrayBacked(
            GUIRenderListener::class.java,
            GUIRenderListener { startTime: Long, tick: Boolean, context: DrawContext, mouseX: Int, mouseY: Int,
                                tickDelta: Float -> }
        ) { listeners: Array<GUIRenderListener> ->
            GUIRenderListener { startTime: Long, tick: Boolean, context: DrawContext, mouseX: Int, mouseY: Int,
                                tickDelta: Float ->
                for (listener in listeners) {
                    listener.onGUIRender(startTime, tick, context, mouseX, mouseY, tickDelta)
                }
            }
        }
    }
}

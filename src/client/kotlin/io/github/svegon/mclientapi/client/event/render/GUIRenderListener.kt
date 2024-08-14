package io.github.svegon.mclientapi.client.event.render

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.render.RenderTickCounter

fun interface GUIRenderListener {
    fun onGUIRender(gameRenderer: GameRenderer, tickCounter: RenderTickCounter, tick: Boolean,
                    context: DrawContext, mouseX: Int, mouseY: Int, tickDelta: Float)

    companion object {
        @JvmField
        val EVENT: Event<GUIRenderListener> = EventFactory.createArrayBacked(
            GUIRenderListener::class.java,
            GUIRenderListener { gameRenderer: GameRenderer, _: RenderTickCounter, tick: Boolean, context: DrawContext,
                                mouseX: Int, mouseY: Int, tickDelta: Float -> }
        ) { listeners: Array<GUIRenderListener> ->
            GUIRenderListener { gameRenderer: GameRenderer, tickCounter: RenderTickCounter, tick: Boolean,
                                context: DrawContext, mouseX: Int, mouseY: Int, tickDelta: Float ->
                for (listener in listeners) {
                    listener.onGUIRender(gameRenderer, tickCounter, tick, context, mouseX, mouseY, tickDelta)
                }
            }
        }
    }
}

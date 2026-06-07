package io.github.svegon.mclientapi.client.event.render

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.Minecraft
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

fun interface RenderListener {
    fun onRender(client: Minecraft, tick: Boolean, callback: CallbackInfo)

    companion object {
        val EVENT: Event<RenderListener> = EventFactory.createArrayBacked(
            RenderListener::class.java,
            RenderListener { client: Minecraft, tick: Boolean, callback: CallbackInfo -> }
        ) { listeners: Array<RenderListener> ->
            RenderListener { client: Minecraft, tick: Boolean, callback: CallbackInfo ->
                for (listener in listeners) {
                    listener.onRender(client, tick, callback)

                    if (callback.isCancelled) {
                        return@RenderListener
                    }
                }
            }
        }
    }
}

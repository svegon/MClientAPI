package io.github.svegon.mclientapi.client.event.render

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.MinecraftClient
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import java.util.function.Function
import kotlin.Array
import kotlin.Boolean
import kotlin.invoke

fun interface RenderListener {
    fun onRender(client: MinecraftClient, tick: Boolean, callback: CallbackInfo)

    companion object {
        @JvmField
        val EVENT: Event<RenderListener> = EventFactory.createArrayBacked(
            RenderListener::class.java,
            RenderListener { client: MinecraftClient, tick: Boolean, callback: CallbackInfo -> }
        ) { listeners: Array<RenderListener> ->
            RenderListener { client: MinecraftClient, tick: Boolean, callback: CallbackInfo ->
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

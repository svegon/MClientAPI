package io.github.svegon.mclientapi.client.event.input

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.MinecraftClient

fun interface CrosshairTargetUpdateCallback {
    fun onCrosshairTargetUpdate(client: MinecraftClient, tickDelta: Float)

    companion object {
        @JvmField
        val EVENT: Event<CrosshairTargetUpdateCallback> = EventFactory.createArrayBacked(
                CrosshairTargetUpdateCallback::class.java,
                CrosshairTargetUpdateCallback { client: MinecraftClient, tickDelta: Float -> }
            ) { listeners: Array<CrosshairTargetUpdateCallback> ->
                CrosshairTargetUpdateCallback { client: MinecraftClient, tickDelta: Float ->
                    for (listener in listeners) {
                        listener.onCrosshairTargetUpdate(client, tickDelta)
                    }
                }
            }
    }
}

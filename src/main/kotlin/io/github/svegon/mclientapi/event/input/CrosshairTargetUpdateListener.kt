package io.github.svegon.capi.event.input

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.MinecraftClient
import java.util.function.Function

interface CrosshairTargetUpdateListener {
    fun onCrosshairTargetUpdate(client: MinecraftClient?, tickDelta: Float)

    companion object {
        @JvmField
        val EVENT: Event<CrosshairTargetUpdateListener?> =
            EventFactory.createArrayBacked<CrosshairTargetUpdateListener?>(CrosshairTargetUpdateListener::class.java,
                CrosshairTargetUpdateListener { client: MinecraftClient?, tickDelta: Float -> },
                Function<Array<CrosshairTargetUpdateListener?>, CrosshairTargetUpdateListener?> { listeners: Array<CrosshairTargetUpdateListener?>? ->
                    CrosshairTargetUpdateListener { client: MinecraftClient?, tickDelta: Float ->
                        for (listener in listeners) {
                            listener.onCrosshairTargetUpdate(client, tickDelta)
                        }
                    }
                })
    }
}

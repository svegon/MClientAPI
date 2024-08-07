package io.github.svegon.capi.event.network

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.MinecraftClient

fun interface ServerConnectCallback {
    fun onServerConnect(
        screen: Screen?, client: MinecraftClient?, address: ServerAddress?, info: ServerInfo?,
        quickPlay: Boolean, ci: CallbackInfo?
    )

    companion object {
        @JvmField
        val EVENT: Event<ServerConnectCallback> = EventFactory.createArrayBacked<ServerConnectCallback>(
            ServerConnectCallback::class.java,
            ServerConnectCallback { screen: Screen?, client: MinecraftClient?, address: ServerAddress?, info: ServerInfo?, quickPlay: Boolean, ci: CallbackInfo? -> }
        ) { listeners: Array<ServerConnectCallback> ->
            ServerConnectCallback { screen: Screen?, client: MinecraftClient?, address: ServerAddress?, info: ServerInfo?, quickPlay: Boolean, ci: CallbackInfo ->
                for (listener in listeners) {
                    listener.onServerConnect(screen, client, address, info, quickPlay, ci)

                    if (ci.isCancelled()) {
                        return@ServerConnectCallback
                    }
                }
            }
        }
    }
}

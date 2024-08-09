package io.github.svegon.mclientapi.client.event.network

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.network.CookieStorage
import net.minecraft.client.network.ServerAddress
import net.minecraft.client.network.ServerInfo
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

fun interface ServerConnectCallback {
    fun onServerConnect(
        screen: Screen, client: MinecraftClient, address: ServerAddress, info: ServerInfo, quickPlay: Boolean,
        cookieJar: CookieStorage?, ci: CallbackInfo,
    )

    companion object {
        @JvmField
        val EVENT: Event<ServerConnectCallback> = EventFactory.createArrayBacked(
            ServerConnectCallback::class.java,
            ServerConnectCallback { screen, client, address, info, quickPlay, cookieJar, ci -> }
        ) { listeners: Array<ServerConnectCallback> ->
            ServerConnectCallback { screen, client, address, info, quickPlay, cookieJar, ci ->
                for (listener in listeners) {
                    listener.onServerConnect(screen, client, address, info, quickPlay, cookieJar, ci)
                }
            }
        }
    }
}

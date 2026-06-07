package io.github.svegon.mclientapi.client.event.network

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ServerData
import net.minecraft.client.multiplayer.TransferState
import net.minecraft.client.multiplayer.resolver.ServerAddress
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

fun interface ServerConnectCallback {
    fun onServerConnect(
        minecraft: Minecraft, hostAndPort: ServerAddress, server: ServerData,
        transferState: TransferState, ci: CallbackInfo
    )

    companion object {
        val EVENT: Event<ServerConnectCallback> = EventFactory.createArrayBacked(
            ServerConnectCallback::class.java,
            ServerConnectCallback { minecraft: Minecraft, hostAndPort: ServerAddress, server: ServerData,
                                    transferState: TransferState, ci: CallbackInfo -> }
        ) { listeners: Array<ServerConnectCallback> ->
            ServerConnectCallback { minecraft: Minecraft, hostAndPort: ServerAddress, server: ServerData,
                                    transferState: TransferState, ci: CallbackInfo ->
                for (listener in listeners) {
                    listener.onServerConnect(minecraft, hostAndPort, server, transferState, ci)
                }
            }
        }
    }
}

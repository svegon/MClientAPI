package io.github.svegon.capi.event.network

import io.github.svegon.capi.event.network.PacketSendCallback
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.network.ClientConnection
import net.minecraft.network.packet.Packet
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import java.util.function.Function

interface PacketReceiveCallback {
    fun onPacketReceive(connection: ClientConnection?, packet: Packet<*>?, callback: CallbackInfo?)

    companion object {
        @JvmField
        val EVENT: Event<PacketSendCallback> = EventFactory.createArrayBacked(PacketSendCallback::class.java,
            T { connection, packet, callback -> }, Function<Array<T?>, T> { listeners: Array<T?> ->
                label@ T { connection, packet, callback ->
                    for (listener in listeners) {
                        listener.onPacketSend(connection, packet, callback)

                        if (callback.isCancelled()) {
                            return@label
                        }
                    }
                }
            })
    }
}

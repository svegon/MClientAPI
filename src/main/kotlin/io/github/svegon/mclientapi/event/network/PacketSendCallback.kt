package io.github.svegon.capi.event.network

import io.github.svegon.capi.event.network.PacketSendCallback
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.network.ClientConnection
import net.minecraft.network.packet.Packet
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import java.util.function.Function
import kotlin.Array
import kotlin.invoke

interface PacketSendCallback {
    fun onPacketSend(connection: ClientConnection?, packet: Packet<*>?, callback: CallbackInfo?)

    companion object {
        @JvmField
        val EVENT: Event<PacketSendCallback?> = EventFactory.createArrayBacked(
            PacketSendCallback::class.java,
            PacketSendCallback { connection: ClientConnection?, packet: Packet<*>?, callback: CallbackInfo? -> },
            Function { listeners: Array<PacketSendCallback?>? ->
                PacketSendCallback { connection: ClientConnection?, packet: Packet<*>?, callback: CallbackInfo? ->
                    for (listener in listeners) {
                        listener.onPacketSend(connection, packet, callback)

                        if (callback.isCancelled()) {
                            return@PacketSendCallback
                        }
                    }
                }
            })
    }
}

package io.github.svegon.mclientapi.event.network

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.network.Connection
import net.minecraft.network.protocol.Packet
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

fun interface PacketSendListener {
    fun onPacketSend(connection: Connection, packet: Packet<*>, callback: CallbackInfo, flush: Boolean)

    companion object {
        val EVENT: Event<PacketSendListener> = EventFactory.createArrayBacked(
            PacketSendListener::class.java,
            PacketSendListener { connection, packet, callback, flush -> }) { listeners ->
            PacketSendListener { connection, packet, callback, flush ->
                for (listener in listeners) {
                    listener.onPacketSend(connection, packet, callback, flush)
                }
            }
        }
    }
}

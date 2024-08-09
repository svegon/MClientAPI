package io.github.svegon.mclientapi.event.network.packet_direct

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.network.ClientConnection
import net.minecraft.network.packet.Packet
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import java.util.function.Function
import kotlin.Array
import kotlin.invoke

fun interface PacketSendListener {
    fun onPacketSend(connection: ClientConnection, packet: Packet<*>, callback: CallbackInfo, flush: Boolean)

    companion object {
        @JvmField
        val EVENT: Event<PacketSendListener?> = EventFactory.createArrayBacked(
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

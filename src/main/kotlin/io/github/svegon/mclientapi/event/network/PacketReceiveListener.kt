package io.github.svegon.mclientapi.event.network

import io.netty.channel.ChannelHandlerContext
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.network.ClientConnection
import net.minecraft.network.packet.Packet
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

fun interface PacketReceiveListener {
    fun onPacketReceive(connection: ClientConnection, context: ChannelHandlerContext,
                        packet: Packet<*>, callback: CallbackInfo)

    companion object {
        @JvmField
        val EVENT: Event<PacketReceiveListener> = EventFactory.createArrayBacked(
                PacketReceiveListener::class.java,
            PacketReceiveListener { connection, context, packet, callback -> }
            ) { listeners: Array<PacketReceiveListener> ->
            PacketReceiveListener { connection, context, packet, callback ->
                for (listener in listeners) {
                    listener.onPacketReceive(connection, context, packet, callback)

                    if (callback.isCancelled) {
                        return@PacketReceiveListener
                    }
                }
            }
        }
    }
}

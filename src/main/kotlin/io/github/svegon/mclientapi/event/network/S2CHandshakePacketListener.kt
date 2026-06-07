package io.github.svegon.mclientapi.event.network

import net.minecraft.network.ConnectionProtocol
import net.minecraft.network.PacketListener
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.PacketFlow
import net.minecraft.network.protocol.handshake.ClientIntentionPacket
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import java.util.function.Function

interface S2CHandshakePacketListener : S2CPacketListener {
    override fun flow(): PacketFlow { return PacketFlow.CLIENTBOUND }

    override fun protocol(): ConnectionProtocol { return ConnectionProtocol.HANDSHAKING }

    fun handleIntention(packet: ClientIntentionPacket) {}

    object EmptyInvoker : S2CHandshakePacketListener

    object InvokerFactory : Function<Array<S2CHandshakePacketListener>, S2CHandshakePacketListener> {
        override fun apply(listeners: Array<S2CHandshakePacketListener>): S2CHandshakePacketListener {
            return object : S2CHandshakePacketListener {
                override fun intercept(packet: Packet<out PacketListener>, callback: CallbackInfo) {
                    for (listener in listeners) {
                        listener.intercept(packet, callback)

                        if (callback.isCancelled) {
                            return
                        }
                    }
                }
            }
        }
    }
}
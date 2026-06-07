package io.github.svegon.mclientapi.event.network

import net.minecraft.network.PacketListener
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.handshake.ClientIntentionPacket
import net.minecraft.network.protocol.handshake.ServerHandshakePacketListener
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import java.util.function.Function

interface C2SHandshakePacketListener : C2SPacketListener, ServerHandshakePacketListener {
    override fun handleIntention(packet: ClientIntentionPacket) {}

    object EmptyInvoker : C2SHandshakePacketListener

    object InvokerFactory : Function<Array<C2SHandshakePacketListener>, C2SHandshakePacketListener> {
        override fun apply(listeners: Array<C2SHandshakePacketListener>): C2SHandshakePacketListener {
            return object : C2SHandshakePacketListener {
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
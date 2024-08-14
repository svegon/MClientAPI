package io.github.svegon.mclientapi.event.network

import net.minecraft.network.listener.PacketListener
import net.minecraft.network.listener.ServerHandshakePacketListener
import net.minecraft.network.listener.ServerQueryPacketListener
import net.minecraft.network.packet.Packet
import net.minecraft.network.packet.c2s.handshake.HandshakeC2SPacket
import net.minecraft.network.packet.c2s.query.QueryPingC2SPacket
import net.minecraft.network.packet.c2s.query.QueryRequestC2SPacket
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import java.util.function.Function

interface C2SHandshakePacketListener : C2SPacketListener, ServerHandshakePacketListener {
    override fun onHandshake(packet: HandshakeC2SPacket) {}

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
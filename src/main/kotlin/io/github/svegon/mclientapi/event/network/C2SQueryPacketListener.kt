package io.github.svegon.mclientapi.event.network

import net.minecraft.network.listener.PacketListener
import net.minecraft.network.listener.ServerQueryPacketListener
import net.minecraft.network.packet.Packet
import net.minecraft.network.packet.c2s.query.QueryPingC2SPacket
import net.minecraft.network.packet.c2s.query.QueryRequestC2SPacket
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import java.util.function.Function

interface C2SQueryPacketListener : C2SPacketListener, ServerQueryPacketListener {
    override fun onQueryPing(packet: QueryPingC2SPacket) {}

    override fun onRequest(packet: QueryRequestC2SPacket) {}

    object EmptyInvoker : C2SQueryPacketListener

    object InvokerFactory : Function<Array<C2SQueryPacketListener>, C2SQueryPacketListener> {
        override fun apply(listeners: Array<C2SQueryPacketListener>): C2SQueryPacketListener {
            return object : C2SQueryPacketListener {
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
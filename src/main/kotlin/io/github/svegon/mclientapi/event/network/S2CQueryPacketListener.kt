package io.github.svegon.mclientapi.event.network

import net.minecraft.network.listener.ClientQueryPacketListener
import net.minecraft.network.listener.PacketListener
import net.minecraft.network.packet.Packet
import net.minecraft.network.packet.s2c.query.PingResultS2CPacket
import net.minecraft.network.packet.s2c.query.QueryResponseS2CPacket
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import java.util.function.Function

interface S2CQueryPacketListener : S2CPacketListener, ClientQueryPacketListener {
    override fun onPingResult(packet: PingResultS2CPacket) {}

    override fun onResponse(packet: QueryResponseS2CPacket) {}

    object EmptyInvoker : S2CQueryPacketListener

    object InvokerFactory : Function<Array<S2CQueryPacketListener>, S2CQueryPacketListener> {
        override fun apply(listeners: Array<S2CQueryPacketListener>): S2CQueryPacketListener {
            return object : S2CQueryPacketListener {
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
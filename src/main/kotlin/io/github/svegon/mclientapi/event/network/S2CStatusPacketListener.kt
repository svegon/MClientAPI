package io.github.svegon.mclientapi.event.network

import net.minecraft.network.PacketListener
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.ping.ClientboundPongResponsePacket
import net.minecraft.network.protocol.status.ClientStatusPacketListener
import net.minecraft.network.protocol.status.ClientboundStatusResponsePacket
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import java.util.function.Function

interface S2CStatusPacketListener : S2CPacketListener, ClientStatusPacketListener {
    override fun handleStatusResponse(packet: ClientboundStatusResponsePacket) {}

    override fun handlePongResponse(packet: ClientboundPongResponsePacket) {}
    
    object EmptyInvoker : S2CStatusPacketListener

    object InvokerFactory : Function<Array<S2CStatusPacketListener>, S2CStatusPacketListener> {
        override fun apply(listeners: Array<S2CStatusPacketListener>): S2CStatusPacketListener {
            return object : S2CStatusPacketListener {
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
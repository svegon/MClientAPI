package io.github.svegon.mclientapi.event.network

import net.minecraft.network.PacketListener
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.ping.ServerboundPingRequestPacket
import net.minecraft.network.protocol.status.ServerStatusPacketListener
import net.minecraft.network.protocol.status.ServerboundStatusRequestPacket
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import java.util.function.Function

interface C2SStatusPacketListener : C2SPacketListener, ServerStatusPacketListener {
    override fun handleStatusRequest(packet: ServerboundStatusRequestPacket) {}

    override fun handlePingRequest(packet: ServerboundPingRequestPacket) {}
    
    object EmptyInvoker : C2SStatusPacketListener

    object InvokerFactory : Function<Array<C2SStatusPacketListener>, C2SStatusPacketListener> {
        override fun apply(listeners: Array<C2SStatusPacketListener>): C2SStatusPacketListener {
            return object : C2SStatusPacketListener {
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
package io.github.svegon.mclientapi.event.network

import net.minecraft.network.listener.PacketListener
import net.minecraft.network.listener.ServerConfigurationPacketListener
import net.minecraft.network.packet.Packet
import net.minecraft.network.packet.c2s.common.*
import net.minecraft.network.packet.c2s.config.ReadyC2SPacket
import net.minecraft.network.packet.c2s.config.SelectKnownPacksC2SPacket
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import java.util.function.Function

interface C2SConfigurationPacketListener : C2SPacketListener, ServerConfigurationPacketListener {
    override fun onCookieResponse(packet: CookieResponseC2SPacket) {}

    override fun onKeepAlive(packet: KeepAliveC2SPacket) {}

    override fun onPong(packet: CommonPongC2SPacket) {}

    override fun onCustomPayload(packet: CustomPayloadC2SPacket) {}

    override fun onResourcePackStatus(packet: ResourcePackStatusC2SPacket) {}

    override fun onClientOptions(packet: ClientOptionsC2SPacket) {}

    override fun onReady(packet: ReadyC2SPacket) {}

    override fun onSelectKnownPacks(packet: SelectKnownPacksC2SPacket) {}

    object EmptyInvoker : C2SConfigurationPacketListener

    object InvokerFactory : Function<Array<C2SConfigurationPacketListener>, C2SConfigurationPacketListener> {
        override fun apply(listeners: Array<C2SConfigurationPacketListener>): C2SConfigurationPacketListener {
            return object : C2SConfigurationPacketListener {
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
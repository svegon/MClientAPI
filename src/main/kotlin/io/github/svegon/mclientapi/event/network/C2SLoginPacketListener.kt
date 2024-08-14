package io.github.svegon.mclientapi.event.network

import net.minecraft.network.listener.PacketListener
import net.minecraft.network.listener.ServerCommonPacketListener
import net.minecraft.network.listener.ServerLoginPacketListener
import net.minecraft.network.packet.Packet
import net.minecraft.network.packet.c2s.common.*
import net.minecraft.network.packet.c2s.login.EnterConfigurationC2SPacket
import net.minecraft.network.packet.c2s.login.LoginHelloC2SPacket
import net.minecraft.network.packet.c2s.login.LoginKeyC2SPacket
import net.minecraft.network.packet.c2s.login.LoginQueryResponseC2SPacket
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import java.util.function.Function

interface C2SLoginPacketListener : C2SPacketListener, ServerLoginPacketListener,
    ServerCommonPacketListener {
    override fun onCookieResponse(packet: CookieResponseC2SPacket) {}
    
    override fun onKeepAlive(packet: KeepAliveC2SPacket) {}

    override fun onPong(packet: CommonPongC2SPacket) {}

    override fun onCustomPayload(packet: CustomPayloadC2SPacket) {}

    override fun onResourcePackStatus(packet: ResourcePackStatusC2SPacket) {}

    override fun onClientOptions(packet: ClientOptionsC2SPacket) {}

    override fun onHello(packet: LoginHelloC2SPacket) {}

    override fun onKey(packet: LoginKeyC2SPacket) {}

    override fun onQueryResponse(packet: LoginQueryResponseC2SPacket) {}

    override fun onEnterConfiguration(packet: EnterConfigurationC2SPacket) {}

    object EmptyInvoker : C2SLoginPacketListener

    object InvokerFactory : Function<Array<C2SLoginPacketListener>, C2SLoginPacketListener> {
        override fun apply(listeners: Array<C2SLoginPacketListener>): C2SLoginPacketListener {
            return object : C2SLoginPacketListener {
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
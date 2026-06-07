package io.github.svegon.mclientapi.event.network

import net.minecraft.network.PacketListener
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.common.*
import net.minecraft.network.protocol.cookie.ServerboundCookieResponsePacket
import net.minecraft.network.protocol.login.*
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import java.util.function.Function

interface C2SLoginPacketListener : C2SPacketListener, ServerLoginPacketListener, ServerCommonPacketListener {
    override fun handleCookieResponse(packet: ServerboundCookieResponsePacket) {}

    override fun handleHello(packet: ServerboundHelloPacket) {}

    override fun handleKey(packet: ServerboundKeyPacket) {}

    override fun handleCustomQueryPacket(packet: ServerboundCustomQueryAnswerPacket) {}

    override fun handleLoginAcknowledgement(packet: ServerboundLoginAcknowledgedPacket) {}
    
    override fun handleKeepAlive(packet: ServerboundKeepAlivePacket) {}

    override fun handlePong(serverboundPongPacket: ServerboundPongPacket) {}

    override fun handleCustomPayload(packet: ServerboundCustomPayloadPacket) {}

    override fun handleResourcePackResponse(packet: ServerboundResourcePackPacket) {}

    override fun handleClientInformation(packet: ServerboundClientInformationPacket) {}

    override fun handleCustomClickAction(packet: ServerboundCustomClickActionPacket) {}

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
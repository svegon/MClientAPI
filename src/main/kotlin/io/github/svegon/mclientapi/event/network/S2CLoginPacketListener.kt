package io.github.svegon.mclientapi.event.network

import net.minecraft.network.PacketListener
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.cookie.ClientboundCookieRequestPacket
import net.minecraft.network.protocol.login.ClientLoginPacketListener
import net.minecraft.network.protocol.login.ClientboundCustomQueryPacket
import net.minecraft.network.protocol.login.ClientboundHelloPacket
import net.minecraft.network.protocol.login.ClientboundLoginCompressionPacket
import net.minecraft.network.protocol.login.ClientboundLoginDisconnectPacket
import net.minecraft.network.protocol.login.ClientboundLoginFinishedPacket
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import java.util.function.Function

interface S2CLoginPacketListener : S2CPacketListener, ClientLoginPacketListener {
    override fun handleHello(packet: ClientboundHelloPacket) {}

    override fun handleLoginFinished(packet: ClientboundLoginFinishedPacket) {}

    override fun handleDisconnect(packet: ClientboundLoginDisconnectPacket) {}

    override fun handleCompression(packet: ClientboundLoginCompressionPacket) {}

    override fun handleCustomQuery(packet: ClientboundCustomQueryPacket) {}

    override fun handleRequestCookie(packet: ClientboundCookieRequestPacket) {}

    object EmptyInvoker : S2CLoginPacketListener

    object InvokerFactory : Function<Array<S2CLoginPacketListener>, S2CLoginPacketListener> {
        override fun apply(listeners: Array<S2CLoginPacketListener>): S2CLoginPacketListener {
            return object : S2CLoginPacketListener {
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
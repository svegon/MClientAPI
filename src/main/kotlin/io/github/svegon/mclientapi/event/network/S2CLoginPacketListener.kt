package io.github.svegon.mclientapi.event.network

import net.minecraft.network.listener.ClientLoginPacketListener
import net.minecraft.network.listener.PacketListener
import net.minecraft.network.packet.Packet
import net.minecraft.network.packet.s2c.common.CookieRequestS2CPacket
import net.minecraft.network.packet.s2c.login.*
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import java.util.function.Function

interface S2CLoginPacketListener : S2CPacketListener, ClientLoginPacketListener {
    override fun onCookieRequest(packet: CookieRequestS2CPacket) {}

    override fun onHello(packet: LoginHelloS2CPacket) {}

    override fun onSuccess(packet: LoginSuccessS2CPacket) {}

    override fun onDisconnect(packet: LoginDisconnectS2CPacket) {}

    override fun onCompression(packet: LoginCompressionS2CPacket) {}

    override fun onQueryRequest(packet: LoginQueryRequestS2CPacket) {}

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
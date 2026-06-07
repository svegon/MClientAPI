package io.github.svegon.mclientapi.event.network

import net.minecraft.network.PacketListener
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.common.*
import net.minecraft.network.protocol.configuration.ServerConfigurationPacketListener
import net.minecraft.network.protocol.configuration.ServerboundAcceptCodeOfConductPacket
import net.minecraft.network.protocol.configuration.ServerboundFinishConfigurationPacket
import net.minecraft.network.protocol.configuration.ServerboundSelectKnownPacks
import net.minecraft.network.protocol.cookie.ServerboundCookieResponsePacket
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import java.util.function.Function

interface C2SConfigurationPacketListener : C2SPacketListener, ServerConfigurationPacketListener {
    override fun handleCookieResponse(packet: ServerboundCookieResponsePacket) {}
    
    override fun handleKeepAlive(packet: ServerboundKeepAlivePacket) {}

    override fun handlePong(serverboundPongPacket: ServerboundPongPacket) {}

    override fun handleCustomPayload(packet: ServerboundCustomPayloadPacket) {}

    override fun handleResourcePackResponse(packet: ServerboundResourcePackPacket) {}

    override fun handleClientInformation(packet: ServerboundClientInformationPacket) {}

    override fun handleCustomClickAction(packet: ServerboundCustomClickActionPacket) {}

    override fun handleConfigurationFinished(packet: ServerboundFinishConfigurationPacket) {}

    override fun handleSelectKnownPacks(packet: ServerboundSelectKnownPacks) {}

    override fun handleAcceptCodeOfConduct(packet: ServerboundAcceptCodeOfConductPacket) {}

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
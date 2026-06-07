package io.github.svegon.mclientapi.event.network

import net.minecraft.network.PacketListener
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.common.ClientboundClearDialogPacket
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket
import net.minecraft.network.protocol.common.ClientboundCustomReportDetailsPacket
import net.minecraft.network.protocol.common.ClientboundDisconnectPacket
import net.minecraft.network.protocol.common.ClientboundKeepAlivePacket
import net.minecraft.network.protocol.common.ClientboundPingPacket
import net.minecraft.network.protocol.common.ClientboundResourcePackPopPacket
import net.minecraft.network.protocol.common.ClientboundResourcePackPushPacket
import net.minecraft.network.protocol.common.ClientboundServerLinksPacket
import net.minecraft.network.protocol.common.ClientboundShowDialogPacket
import net.minecraft.network.protocol.common.ClientboundStoreCookiePacket
import net.minecraft.network.protocol.common.ClientboundTransferPacket
import net.minecraft.network.protocol.common.ClientboundUpdateTagsPacket
import net.minecraft.network.protocol.configuration.ClientConfigurationPacketListener
import net.minecraft.network.protocol.configuration.ClientboundCodeOfConductPacket
import net.minecraft.network.protocol.configuration.ClientboundFinishConfigurationPacket
import net.minecraft.network.protocol.configuration.ClientboundRegistryDataPacket
import net.minecraft.network.protocol.configuration.ClientboundResetChatPacket
import net.minecraft.network.protocol.configuration.ClientboundSelectKnownPacks
import net.minecraft.network.protocol.configuration.ClientboundUpdateEnabledFeaturesPacket
import net.minecraft.network.protocol.cookie.ClientboundCookieRequestPacket
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import java.util.function.Function

interface S2CConfigurationPacketListener : S2CPacketListener, ClientConfigurationPacketListener {
    override fun handleCodeOfConduct(packet: ClientboundCodeOfConductPacket) {}

    override fun handleConfigurationFinished(packet: ClientboundFinishConfigurationPacket) {}

    override fun handleRegistryData(packet: ClientboundRegistryDataPacket) {}

    override fun handleEnabledFeatures(packet: ClientboundUpdateEnabledFeaturesPacket) {}

    override fun handleSelectKnownPacks(packet: ClientboundSelectKnownPacks) {}

    override fun handleResetChat(packet: ClientboundResetChatPacket) {}

    override fun handleKeepAlive(packet: ClientboundKeepAlivePacket) {}

    override fun handlePing(packet: ClientboundPingPacket) {}

    override fun handleCustomPayload(packet: ClientboundCustomPayloadPacket) {}

    override fun handleDisconnect(packet: ClientboundDisconnectPacket) {}

    override fun handleResourcePackPush(packet: ClientboundResourcePackPushPacket) {}

    override fun handleResourcePackPop(packet: ClientboundResourcePackPopPacket) {}

    override fun handleUpdateTags(packet: ClientboundUpdateTagsPacket) {}

    override fun handleStoreCookie(packet: ClientboundStoreCookiePacket) {}

    override fun handleTransfer(packet: ClientboundTransferPacket) {}

    override fun handleCustomReportDetails(packet: ClientboundCustomReportDetailsPacket) {}

    override fun handleServerLinks(packet: ClientboundServerLinksPacket) {}

    override fun handleClearDialog(packet: ClientboundClearDialogPacket) {}

    override fun handleShowDialog(packet: ClientboundShowDialogPacket) {}

    override fun handleRequestCookie(packet: ClientboundCookieRequestPacket) {}

    object EmptyInvoker : S2CConfigurationPacketListener

    object InvokerFactory : Function<Array<S2CConfigurationPacketListener>, S2CConfigurationPacketListener> {
        override fun apply(listeners: Array<S2CConfigurationPacketListener>): S2CConfigurationPacketListener {
            return object : S2CConfigurationPacketListener {
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
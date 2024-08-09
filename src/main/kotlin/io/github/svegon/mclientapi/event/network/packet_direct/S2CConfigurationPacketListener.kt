package io.github.svegon.mclientapi.event.network.packet_direct

import net.minecraft.network.listener.ClientConfigurationPacketListener
import net.minecraft.network.packet.s2c.common.*
import net.minecraft.network.packet.s2c.config.*

interface S2CConfigurationPacketListener : S2CPacketListener, ClientConfigurationPacketListener {
    override fun onCookieRequest(packet: CookieRequestS2CPacket) {}

    override fun onKeepAlive(packet: KeepAliveS2CPacket) {}

    override fun onPing(packet: CommonPingS2CPacket) {}

    override fun onCustomPayload(packet: CustomPayloadS2CPacket) {}

    override fun onDisconnect(packet: DisconnectS2CPacket) {}

    override fun onResourcePackSend(packet: ResourcePackSendS2CPacket) {}

    override fun onResourcePackRemove(packet: ResourcePackRemoveS2CPacket) {}

    override fun onSynchronizeTags(packet: SynchronizeTagsS2CPacket) {}

    override fun onStoreCookie(packet: StoreCookieS2CPacket) {}

    override fun onServerTransfer(packet: ServerTransferS2CPacket) {}

    override fun onCustomReportDetails(packet: CustomReportDetailsS2CPacket) {}

    override fun onServerLinks(packet: ServerLinksS2CPacket) {}

    override fun onReady(packet: ReadyS2CPacket) {}

    override fun onDynamicRegistries(packet: DynamicRegistriesS2CPacket) {}

    override fun onFeatures(packet: FeaturesS2CPacket) {}

    override fun onSelectKnownPacks(packet: SelectKnownPacksS2CPacket) {}

    override fun onResetChat(packet: ResetChatS2CPacket) {}
}
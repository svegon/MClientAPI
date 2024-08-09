package io.github.svegon.mclientapi.event.network.packet_direct

import net.minecraft.network.listener.ServerConfigurationPacketListener
import net.minecraft.network.packet.c2s.common.*
import net.minecraft.network.packet.c2s.config.ReadyC2SPacket
import net.minecraft.network.packet.c2s.config.SelectKnownPacksC2SPacket

interface C2SConfigurationPacketListener : C2SPacketListener, ServerConfigurationPacketListener {
    override fun onCookieResponse(packet: CookieResponseC2SPacket) {}

    override fun onKeepAlive(packet: KeepAliveC2SPacket) {}

    override fun onPong(packet: CommonPongC2SPacket) {}

    override fun onCustomPayload(packet: CustomPayloadC2SPacket) {}

    override fun onResourcePackStatus(packet: ResourcePackStatusC2SPacket) {}

    override fun onClientOptions(packet: ClientOptionsC2SPacket) {}

    override fun onReady(packet: ReadyC2SPacket) {}

    override fun onSelectKnownPacks(packet: SelectKnownPacksC2SPacket) {}
}
package io.github.svegon.mclientapi.event.network.packet_direct

import net.minecraft.network.listener.ClientQueryPacketListener
import net.minecraft.network.packet.s2c.query.PingResultS2CPacket
import net.minecraft.network.packet.s2c.query.QueryResponseS2CPacket

interface S2CQueryPacketListener : S2CPacketListener, ClientQueryPacketListener {
    override fun onPingResult(packet: PingResultS2CPacket) {}

    override fun onResponse(packet: QueryResponseS2CPacket) {}
}
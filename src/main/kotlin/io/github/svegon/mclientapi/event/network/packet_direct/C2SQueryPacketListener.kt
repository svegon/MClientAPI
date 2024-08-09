package io.github.svegon.mclientapi.event.network.packet_direct

import net.minecraft.network.listener.ServerQueryPacketListener
import net.minecraft.network.packet.c2s.query.QueryPingC2SPacket
import net.minecraft.network.packet.c2s.query.QueryRequestC2SPacket

interface C2SQueryPacketListener : C2SPacketListener, ServerQueryPacketListener {
    override fun onQueryPing(packet: QueryPingC2SPacket) {}

    override fun onRequest(packet: QueryRequestC2SPacket) {}
}
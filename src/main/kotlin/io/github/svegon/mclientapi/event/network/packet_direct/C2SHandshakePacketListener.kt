package io.github.svegon.mclientapi.event.network.packet_direct

import net.minecraft.network.listener.ServerHandshakePacketListener
import net.minecraft.network.listener.ServerQueryPacketListener
import net.minecraft.network.packet.c2s.handshake.HandshakeC2SPacket
import net.minecraft.network.packet.c2s.query.QueryPingC2SPacket
import net.minecraft.network.packet.c2s.query.QueryRequestC2SPacket

interface C2SHandshakePacketListener : C2SPacketListener, ServerHandshakePacketListener {
    override fun onHandshake(packet: HandshakeC2SPacket) {}
}
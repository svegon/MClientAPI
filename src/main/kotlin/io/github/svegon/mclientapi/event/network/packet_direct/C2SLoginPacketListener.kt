package io.github.svegon.mclientapi.event.network.packet_direct

import net.minecraft.network.listener.ServerLoginPacketListener
import net.minecraft.network.packet.c2s.common.CookieResponseC2SPacket
import net.minecraft.network.packet.c2s.login.EnterConfigurationC2SPacket
import net.minecraft.network.packet.c2s.login.LoginHelloC2SPacket
import net.minecraft.network.packet.c2s.login.LoginKeyC2SPacket
import net.minecraft.network.packet.c2s.login.LoginQueryResponseC2SPacket

interface C2SLoginPacketListener : C2SPacketListener, ServerLoginPacketListener {
    override fun onCookieResponse(packet: CookieResponseC2SPacket) {}

    override fun onHello(packet: LoginHelloC2SPacket) {}

    override fun onKey(packet: LoginKeyC2SPacket) {}

    override fun onQueryResponse(packet: LoginQueryResponseC2SPacket) {}

    override fun onEnterConfiguration(packet: EnterConfigurationC2SPacket) {}
}
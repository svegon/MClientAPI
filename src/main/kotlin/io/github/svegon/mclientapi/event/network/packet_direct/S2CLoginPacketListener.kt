package io.github.svegon.mclientapi.event.network.packet_direct

import net.minecraft.network.listener.ClientLoginPacketListener
import net.minecraft.network.packet.s2c.common.CookieRequestS2CPacket
import net.minecraft.network.packet.s2c.login.*

interface S2CLoginPacketListener : S2CPacketListener, ClientLoginPacketListener {
    override fun onCookieRequest(packet: CookieRequestS2CPacket) {}

    override fun onHello(packet: LoginHelloS2CPacket) {}

    override fun onSuccess(packet: LoginSuccessS2CPacket) {}

    override fun onDisconnect(packet: LoginDisconnectS2CPacket) {}

    override fun onCompression(packet: LoginCompressionS2CPacket) {}

    override fun onQueryRequest(packet: LoginQueryRequestS2CPacket) {}
}
package io.github.svegon.mclientapi.event.network.packet_direct

import net.minecraft.network.DisconnectionInfo
import net.minecraft.network.listener.PacketListener
import net.minecraft.network.packet.Packet
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

interface InterceptingPacketListener : PacketListener {
    fun intercept(packet: Packet<out PacketListener>, ci: CallbackInfo) {
        packet.apply(this as Nothing?) // cast is checked by callers
    }

    override fun onDisconnected(info: DisconnectionInfo) {}

    override fun isConnectionOpen(): Boolean {return false}
}
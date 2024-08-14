package io.github.svegon.mclientapi.event.network

import net.minecraft.network.DisconnectionInfo
import net.minecraft.network.listener.PacketListener
import net.minecraft.network.packet.Packet
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

interface InterceptingPacketListener : PacketListener {
    fun intercept(packet: Packet<out PacketListener>, callback: CallbackInfo) {
        apply(packet) // cast is checked by callers
    }

    override fun onDisconnected(info: DisconnectionInfo) {}

    override fun isConnectionOpen(): Boolean = false

    companion object {
        fun <T : PacketListener> PacketListener.apply(packet: Packet<T>) {
            packet.apply(this as T)
        }
    }
}

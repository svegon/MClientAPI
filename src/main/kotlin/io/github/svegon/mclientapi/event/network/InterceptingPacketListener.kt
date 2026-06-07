package io.github.svegon.mclientapi.event.network

import net.minecraft.network.DisconnectionDetails
import net.minecraft.network.PacketListener
import net.minecraft.network.protocol.Packet
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

interface InterceptingPacketListener : PacketListener {
    fun intercept(packet: Packet<out PacketListener>, callback: CallbackInfo) {
        apply(packet) // cast is checked by callers
    }

    override fun onDisconnect(details: DisconnectionDetails) {}

    override fun isAcceptingMessages(): Boolean { return false }

    companion object {
        fun <T : PacketListener> PacketListener.apply(packet: Packet<T>) {
            packet.handle(this as T)
        }
    }
}

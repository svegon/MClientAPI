package io.github.svegon.capi.event.network

import io.github.svegon.capi.event.ListenerCollectionFactory
import io.github.svegon.capi.event.ListenerSet
import io.github.svegon.capi.event.network.S2CLoginPacketListener
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.network.listener.ClientLoginPacketListener
import net.minecraft.network.packet.Packet
import net.minecraft.network.packet.s2c.login.*
import net.minecraft.text.Text
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import java.util.function.Function

interface S2CLoginPacketListener : ClientLoginPacketListener {
    fun apply(packet: Packet<ClientLoginPacketListener?>, callback: CallbackInfo?) {
        packet.apply(this)
    }

    override fun isConnectionOpen(): Boolean {
        return false
    }

    override fun onHello(packet: LoginHelloS2CPacket) {
    }

    override fun onSuccess(packet: LoginSuccessS2CPacket) {
    }

    override fun onDisconnect(packet: LoginDisconnectS2CPacket) {
    }

    override fun onCompression(packet: LoginCompressionS2CPacket) {
    }

    override fun onQueryRequest(packet: LoginQueryRequestS2CPacket) {
    }

    fun onDisconnected(reason: Text?) {
    }

    companion object {
        @JvmField
        val CLIENT_PACKET_SENT_EVENT: Event<S2CLoginPacketListener> = EventFactory.createArrayBacked(
            S2CLoginPacketListener::class.java
        ) { listeners: Array<S2CLoginPacketListener> ->
            object : S2CLoginPacketListener {
                override fun apply(packet: Packet<ClientLoginPacketListener?>, info: CallbackInfo) {
                    for (listener in listeners) {
                        listener.apply(packet, info)

                        if (info.isCancelled) {
                            return@createArrayBacked
                        }
                    }
                }
            }
        }
        val LISTENER_LIST: io.github.svegon.capi.event.ListenerList<S2CLoginPacketListener> =
            ListenerCollectionFactory.Companion.listenersVector<Any>(
                Function<List<Any>, Any> { l: List<Any> ->
                    object : S2CLoginPacketListener {
                        override fun apply(packet: Packet<ClientLoginPacketListener?>, info: CallbackInfo) {
                            for (listener in l) {
                                listener.apply(packet, info)

                                if (info.isCancelled) {
                                    return@listenersVector
                                }
                            }
                        }
                    }
                })
        val LISTENER_SET: ListenerSet<S2CLoginPacketListener> =
            ListenerCollectionFactory.Companion.listenersLinkedSet<Any>(
                Function<Set<Any>, Any> { s: Set<Any> ->
                    object : S2CLoginPacketListener {
                        override fun apply(packet: Packet<ClientLoginPacketListener?>, info: CallbackInfo) {
                            for (listener in s) {
                                listener.apply(packet, info)

                                if (info.isCancelled) {
                                    return@listenersLinkedSet
                                }
                            }
                        }
                    }
                })
    }
}

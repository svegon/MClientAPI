package io.github.svegon.mclientapi.mixininterface.network

import io.github.svegon.mclientapi.event.network.InterceptingPacketListener
import net.fabricmc.fabric.api.event.Event

interface MClientAPIPacketListener<R : InterceptingPacketListener, S : InterceptingPacketListener> {
    val packetReceivedEvent: Event<R>

    val packetSendEvent: Event<S>
}
package io.github.svegon.mclientapi.mixininterface

import io.github.svegon.mclientapi.event.network.packet_direct.InterceptingPacketListener
import net.fabricmc.fabric.api.event.Event

interface IPacketListener<T : InterceptingPacketListener> {
    val packetReceivedEvent: Event<T>
}
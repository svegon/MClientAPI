package io.github.svegon.mclientapi.event.network.packet_direct

import net.minecraft.network.listener.ServerPacketListener

interface C2SPacketListener : InterceptingPacketListener, ServerPacketListener
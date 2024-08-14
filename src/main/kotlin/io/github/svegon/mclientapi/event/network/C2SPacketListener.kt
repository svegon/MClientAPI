package io.github.svegon.mclientapi.event.network

import net.minecraft.network.listener.ServerPacketListener

interface C2SPacketListener : InterceptingPacketListener, ServerPacketListener
package io.github.svegon.mclientapi.event.network

import net.minecraft.network.protocol.game.ServerPacketListener

interface C2SPacketListener : InterceptingPacketListener, ServerPacketListener

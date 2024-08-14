package io.github.svegon.mclientapi.event.network

import net.minecraft.network.listener.ClientPacketListener

interface S2CPacketListener : InterceptingPacketListener, ClientPacketListener
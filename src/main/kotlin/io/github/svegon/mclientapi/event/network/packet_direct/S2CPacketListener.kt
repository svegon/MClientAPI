package io.github.svegon.mclientapi.event.network.packet_direct

import net.minecraft.network.listener.ClientPacketListener

interface S2CPacketListener : InterceptingPacketListener, ClientPacketListener
package io.github.svegon.mclientapi.client.mixinterface

import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientPacketListener

interface IClientWorld {
    val `mClientAPI$minecraft`: Minecraft

    val `mClientAPI$packetListener`: ClientPacketListener
}

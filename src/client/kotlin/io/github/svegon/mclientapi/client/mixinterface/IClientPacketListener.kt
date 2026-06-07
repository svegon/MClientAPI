package io.github.svegon.mclientapi.client.mixinterface

import io.github.svegon.mclientapi.mixininterface.network.IClientPlayPacketListener
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.multiplayer.PlayerInfo
import net.minecraft.core.RegistryAccess
import java.util.*

interface IClientPacketListener : IClientPlayPacketListener {
    val `mClientAPI$playerInfoMap`: Map<UUID, PlayerInfo>

    val `mClientAPI$levelData`: ClientLevel.ClientLevelData?

    var `mClientAPI$registries`: RegistryAccess.Frozen
}

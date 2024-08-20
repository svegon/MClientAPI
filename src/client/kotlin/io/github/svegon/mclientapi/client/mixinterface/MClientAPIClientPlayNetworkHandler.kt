package io.github.svegon.mclientapi.client.mixinterface

import io.github.svegon.mclientapi.mixininterface.network.MClientAPIClientPlayPacketListener
import net.minecraft.client.network.PlayerListEntry
import net.minecraft.client.world.ClientWorld
import net.minecraft.registry.DynamicRegistryManager
import java.util.*

interface MClientAPIClientPlayNetworkHandler : MClientAPIClientPlayPacketListener {
    val playerListEntries: Map<UUID, PlayerListEntry>

    var world: ClientWorld?

    var worldProperties: ClientWorld.Properties?

    var combinedDynamicRegistries: DynamicRegistryManager.Immutable
}

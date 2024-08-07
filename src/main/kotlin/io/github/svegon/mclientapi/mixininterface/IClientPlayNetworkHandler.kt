package io.github.svegon.capi.mixininterface

import java.util.*

interface IClientPlayNetworkHandler {
    fun playerListEntries(): Map<UUID?, PlayerListEntry?>?

    fun setWorld(world: ClientWorld?)

    fun getWorldProperties(): ClientWorld.Properties?

    fun setWorldProperties(properties: ClientWorld.Properties?)

    fun setCombinedDynamicRegistries(combinedDynamicRegistries: DynamicRegistryManager.Immutable?)
}

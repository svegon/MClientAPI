package io.github.svegon.capi.mixininterface;

import net.minecraft.client.network.ClientDynamicRegistryType;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.CombinedDynamicRegistries;
import net.minecraft.registry.DynamicRegistryManager;

import java.util.Map;
import java.util.UUID;

public interface IClientPlayNetworkHandler {
    Map<UUID, PlayerListEntry> playerListEntries();

    void setWorld(ClientWorld world);

    ClientWorld.Properties getWorldProperties();

    void setWorldProperties(ClientWorld.Properties properties);

    void setCombinedDynamicRegistries(DynamicRegistryManager.Immutable combinedDynamicRegistries);
}

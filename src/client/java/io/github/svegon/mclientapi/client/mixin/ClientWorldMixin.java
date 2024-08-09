package io.github.svegon.mclientapi.client.mixin;

import io.github.svegon.mclientapi.client.mixinterface.IClientWorld;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PendingUpdateManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Supplier;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin extends World implements IClientWorld {
    @Shadow
    @Final
    private ClientPlayNetworkHandler networkHandler;
    @Shadow
    @Final
    private PendingUpdateManager pendingUpdateManager;

    private ClientWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef,
                             DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry,
                             Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long biomeAccess,
                             int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess,
                maxChainedNeighborUpdates);
    }

    @Override
    public ClientPlayNetworkHandler getNetworkHandler() {
        return networkHandler;
    }

    @NotNull
    @Override
    public PendingUpdateManager getPendingUpdateManager() {
        return pendingUpdateManager;
    }
}

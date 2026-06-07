package io.github.svegon.mclientapi.client.mixin.world;

import io.github.svegon.mclientapi.client.mixinterface.IClientWorld;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.CacheSlot;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin  extends Level implements BlockAndTintGetter, CacheSlot.Cleaner<ClientLevel>,
        IClientWorld {
    @Shadow private @Final ClientPacketListener connection;
    @Shadow private @Final Minecraft minecraft;

    @Override
    public @NotNull Minecraft getMClientAPI$minecraft() {
        return minecraft;
    }

    @Override
    public @NotNull ClientPacketListener getMClientAPI$packetListener() {
        return connection;
    }

    private ClientLevelMixin(WritableLevelData levelData, ResourceKey<Level> dimension, RegistryAccess registryAccess,
                             Holder<DimensionType> dimensionTypeRegistration, boolean isClientSide, boolean isDebug,
                             long biomeZoomSeed, int maxChainedNeighborUpdates) {
        super(levelData, dimension, registryAccess, dimensionTypeRegistration, isClientSide, isDebug, biomeZoomSeed,
                maxChainedNeighborUpdates);
    }
}

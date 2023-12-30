package io.github.svegon.capi.mixin;

import io.github.svegon.capi.event.world.ClientWorldLifecycleEvents;
import io.github.svegon.capi.mixininterface.IClientPlayNetworkHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.listener.TickablePacketListener;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.registry.DynamicRegistryManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.UUID;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin extends ClientCommonNetworkHandler
        implements TickablePacketListener, ClientPlayPacketListener, IClientPlayNetworkHandler {
    @Shadow
    @Final
    private Map<UUID, PlayerListEntry> playerListEntries;
    @Shadow
    private ClientWorld world;
    @Shadow
    private ClientWorld.Properties worldProperties;
    @Mutable
    @Final
    @Shadow
    private DynamicRegistryManager.Immutable combinedDynamicRegistries;

    @Inject(method = "onGameJoin", at = @At("RETURN"))
    private void onGameJoinMixin(GameJoinS2CPacket packet, CallbackInfo callback) {
        ClientWorldLifecycleEvents.JOIN_WORLD.invoker().onWorldJoin(client, world);
    }

    @Override
    public Map<UUID, PlayerListEntry> playerListEntries() {
        return playerListEntries;
    }

    @Override
    public void setWorld(ClientWorld world) {
        this.world = world;
    }

    @Override
    public ClientWorld.Properties getWorldProperties() {
        return worldProperties;
    }

    @Override
    public void setWorldProperties(ClientWorld.Properties worldProperties) {
        this.worldProperties = worldProperties;
    }

    @Override
    public void setCombinedDynamicRegistries(DynamicRegistryManager.Immutable combinedDynamicRegistries) {
        this.combinedDynamicRegistries = combinedDynamicRegistries;
    }

    private ClientPlayNetworkHandlerMixin(MinecraftClient client, ClientConnection connection, ClientConnectionState connectionState) {
        super(client, connection, connectionState);
    }
}

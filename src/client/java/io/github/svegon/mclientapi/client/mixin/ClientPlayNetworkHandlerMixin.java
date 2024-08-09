package io.github.svegon.mclientapi.client.mixin;

import io.github.svegon.mclientapi.client.event.input.CrosshairTargetTypeCallback;
import io.github.svegon.mclientapi.client.event.world.ClientWorldLifecycleEvents;
import io.github.svegon.mclientapi.client.mixinterface.IClientPlayNetworkHandler;
import io.github.svegon.mclientapi.event.network.packet_direct.S2CPlayPacketListener;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.listener.TickablePacketListener;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.registry.DynamicRegistryManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.UUID;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin extends ClientCommonNetworkHandler
        implements TickablePacketListener, ClientPlayPacketListener,
        IClientPlayNetworkHandler {
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

    @Unique
    private final Event<S2CPlayPacketListener> packetReceivedEvent =
            EventFactory.createArrayBacked(S2CPlayPacketListener.class, S2CPlayPacketListener.Companion.emptyInvoker(),
                    S2CPlayPacketListener.Companion.invokerFactory());

    @Inject(method = "onGameJoin", at = @At("RETURN"))
    private void onGameJoinMixin(GameJoinS2CPacket packet, CallbackInfo callback) {
        ClientWorldLifecycleEvents.JOIN_WORLD.invoker().onWorldJoin(client, world);
    }

    @NotNull
    @Override
    public Event<S2CPlayPacketListener> getPacketReceivedEvent() {
        return packetReceivedEvent;
    }

    @Override
    public Map<UUID, PlayerListEntry> getPlayerListEntries() {
        return playerListEntries;
    }

    @Nullable
    @Override
    public ClientWorld getWorld() {
        return world;
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

    @NotNull
    @Override
    public DynamicRegistryManager.Immutable getCombinedDynamicRegistries() {
        return combinedDynamicRegistries;
    }

    @Override
    public void setCombinedDynamicRegistries(DynamicRegistryManager.@NotNull Immutable combinedDynamicRegistries) {
        this.combinedDynamicRegistries = combinedDynamicRegistries;
    }

    private ClientPlayNetworkHandlerMixin(MinecraftClient client, ClientConnection connection,
                                          ClientConnectionState connectionState) {
        super(client, connection, connectionState);
    }
}

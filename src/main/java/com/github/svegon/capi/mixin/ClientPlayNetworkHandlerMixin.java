package com.github.svegon.capi.mixin;

import com.github.svegon.capi.event.network.C2SPlayPacketListener;
import com.github.svegon.capi.event.world.ClientWorldLifecycleEvents;
import com.github.svegon.capi.mixininterface.IClientPlayNetworkHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientDynamicRegistryType;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.registry.CombinedDynamicRegistries;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.UUID;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin implements ClientPlayPacketListener, IClientPlayNetworkHandler {
    @Shadow
    @Final
    private Map<UUID, PlayerListEntry> playerListEntries;
    @Shadow
    private ClientWorld world;
    @Shadow
    private ClientWorld.Properties worldProperties;
    @Shadow
    private CombinedDynamicRegistries<ClientDynamicRegistryType> combinedDynamicRegistries;

    @Shadow @Final private MinecraftClient client;

    @Inject(method = "sendPacket", at = @At("HEAD"), cancellable = true)
    @SuppressWarnings("unchecked")
    public void onSendPacket(Packet<?> packet, CallbackInfo info) {
        try {
            C2SPlayPacketListener.CLIENT_PACKET_SENT_EVENT.invoker().apply((Packet<ServerPlayPacketListener>) packet,
                    info);
        } catch (ClassCastException ignored) {

        }
    }

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
    public void setCombinedDynamicRegistries(CombinedDynamicRegistries<ClientDynamicRegistryType>
                                                         combinedDynamicRegistries) {
        this.combinedDynamicRegistries = combinedDynamicRegistries;
    }
}

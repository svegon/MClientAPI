package io.github.svegon.mclientapi.client.mixin.network;

import io.github.svegon.mclientapi.client.event.world.ClientWorldLifecycleEvents;
import io.github.svegon.mclientapi.client.mixinterface.IClientPacketListener;
import io.github.svegon.mclientapi.event.network.C2SPlayPacketListener;
import io.github.svegon.mclientapi.event.network.S2CGamePacketListener;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.*;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.Connection;
import net.minecraft.network.TickablePacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.UUID;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin extends ClientCommonPacketListenerImpl
        implements ClientGamePacketListener, TickablePacketListener, IClientPacketListener {
    @Shadow private @Final Map<UUID, PlayerInfo> playerInfoMap;
    @Shadow private ClientLevel.ClientLevelData levelData;
    @Mutable @Shadow private @Final RegistryAccess.Frozen registryAccess;

    @Shadow public abstract ClientLevel getLevel();

    @Unique
    private @Final Event<@NotNull S2CGamePacketListener> packetReceivedEvent;
    @Unique
    private @Final Event<@NotNull C2SPlayPacketListener> packetSendEvent;

    @Inject(at = @At("RETURN"), method = "<init>")
    private void init(final Minecraft minecraft, final Connection connection, final CommonListenerCookie cookie,
                      CallbackInfo ci) {
        packetReceivedEvent = EventFactory.createArrayBacked(S2CGamePacketListener.class,
                S2CGamePacketListener.EmptyInvoker.INSTANCE, S2CGamePacketListener.InvokerFactory.INSTANCE);
        packetSendEvent =
                EventFactory.createArrayBacked(C2SPlayPacketListener.class, C2SPlayPacketListener.EmptyInvoker.INSTANCE,
                        C2SPlayPacketListener.InvokerFactory.INSTANCE);
    }

    @Override
    public void send(Packet<?> packet) {
        CallbackInfo callback = new CallbackInfo(getClass().getCanonicalName()
                + "sendPacket(Lnet/minecraft/network/packet;)V", true);

        packetSendEvent.invoker().intercept(packet, callback);

        if (!callback.isCancelled()) {
            super.send(packet);
        }
    }

    @Inject(method = "handleLogin", at = @At("RETURN"))
    private void onGameJoinMixin(final ClientboundLoginPacket packet, CallbackInfo callback) {
        ClientWorldLifecycleEvents.INSTANCE.getJOIN_WORLD().invoker().onWorldJoin(minecraft, getLevel());
    }

    @NotNull
    @Override
    public Event<S2CGamePacketListener> getPacketReceivedEvent() {
        return packetReceivedEvent;
    }

    @Override
    @NotNull
    public Event<C2SPlayPacketListener> getPacketSendEvent() {
        return packetSendEvent;
    }

    @Override
    public @NotNull Map<@NotNull UUID, @NotNull PlayerInfo> getMClientAPI$playerInfoMap() {
        return playerInfoMap;
    }

    @Override
    public ClientLevel.@Nullable ClientLevelData getMClientAPI$levelData() {
        return levelData;
    }

    @Override
    public RegistryAccess.@NotNull Frozen getMClientAPI$registries() {
        return registryAccess;
    }

    @Override
    public void setMClientAPI$registries(RegistryAccess.@NotNull Frozen registries) {
        registryAccess = registries;
    }

    private ClientPacketListenerMixin(Minecraft minecraft, Connection connection, CommonListenerCookie cookie) {
        super(minecraft, connection, cookie);
    }
}

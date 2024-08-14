package io.github.svegon.mclientapi.mixin;

import io.github.svegon.mclientapi.event.network.C2SQueryPacketListener;
import io.github.svegon.mclientapi.event.network.S2CQueryPacketListener;
import io.github.svegon.mclientapi.mixininterface.network.IServerConfigurationPacketListener;
import io.github.svegon.mclientapi.mixininterface.network.IServerQueryPacketListener;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.listener.ServerLoginPacketListener;
import net.minecraft.network.listener.ServerQueryPacketListener;
import net.minecraft.network.listener.TickablePacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerMetadata;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerQueryNetworkHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerQueryNetworkHandler.class)
public abstract class ServerQueryPacketHandlerMixin implements ServerQueryPacketListener,
        IServerQueryPacketListener {
    @Unique
    private @Final Event<C2SQueryPacketListener> packetReceivedEvent;
    @Unique
    private @Final Event<S2CQueryPacketListener> packetSentEvent;

    @Inject(at = @At("RETURN"), method = "<init>")
    private void init(ServerMetadata metadata, ClientConnection connection, CallbackInfo ci) {
        packetSentEvent = EventFactory.createArrayBacked(S2CQueryPacketListener.class,
                S2CQueryPacketListener.EmptyInvoker.INSTANCE,
                S2CQueryPacketListener.InvokerFactory.INSTANCE);
        packetReceivedEvent = EventFactory.createArrayBacked(C2SQueryPacketListener.class,
                C2SQueryPacketListener.EmptyInvoker.INSTANCE,
                        C2SQueryPacketListener.InvokerFactory.INSTANCE);
    }

    @NotNull
    @Override
    public Event<C2SQueryPacketListener> getPacketReceivedEvent() {
        return packetReceivedEvent;
    }

    @Override
    @NotNull
    public Event<S2CQueryPacketListener> getPacketSendEvent() {
        return packetSentEvent;
    }

    @Redirect(method = "onRequest", target = @Desc(owner = ClientConnection.class, value = "send",
            args = Packet.class), at = @At(value = "INVOKE", target = "Lnet/minecraft/network/ClientConnection;" +
            "send(Lnet/minecraft/network/packet/Packet;)V"))
    private void packetSendOnRequest(ClientConnection instance, Packet<?> packet) {
        CallbackInfo callback = new CallbackInfo("packet send", true);

        getPacketSendEvent().invoker().intercept(packet, callback);

        if (!callback.isCancelled()) {
            instance.send(packet);
        }
    }

    @Redirect(method = "onQueryPing", target = @Desc(owner = ClientConnection.class, value = "send",
            args = Packet.class), at = @At(value = "INVOKE", target = "Lnet/minecraft/network/ClientConnection;" +
            "send(Lnet/minecraft/network/packet/Packet;)V"))
    private void packetSendOnQueryPing(ClientConnection instance, Packet<?> packet) {
        CallbackInfo callback = new CallbackInfo("packet send", true);

        getPacketSendEvent().invoker().intercept(packet, callback);

        if (!callback.isCancelled()) {
            instance.send(packet);
        }
    }
}

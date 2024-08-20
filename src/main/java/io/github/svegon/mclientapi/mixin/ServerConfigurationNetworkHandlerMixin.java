package io.github.svegon.mclientapi.mixin;

import io.github.svegon.mclientapi.event.network.C2SConfigurationPacketListener;
import io.github.svegon.mclientapi.event.network.S2CConfigurationPacketListener;
import io.github.svegon.mclientapi.mixininterface.network.MClientAPIServerConfigurationPacketListener;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.networking.v1.FabricServerConfigurationNetworkHandler;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.ServerConfigurationPacketListener;
import net.minecraft.network.listener.TickablePacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.*;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerConfigurationNetworkHandler.class)
public abstract class ServerConfigurationNetworkHandlerMixin extends ServerCommonNetworkHandler
        implements ServerConfigurationPacketListener, TickablePacketListener, FabricServerConfigurationNetworkHandler,
        MClientAPIServerConfigurationPacketListener {
    @Unique
    private @Final Event<C2SConfigurationPacketListener> packetReceivedEvent;
    @Unique
    private @Final Event<S2CConfigurationPacketListener> packetSentEvent;

    @Inject(at = @At("RETURN"), method = "<init>")
    private void init(MinecraftServer minecraftServer, ClientConnection clientConnection,
                      ConnectedClientData connectedClientData, CallbackInfo ci) {
        packetSentEvent = EventFactory.createArrayBacked(S2CConfigurationPacketListener.class,
                S2CConfigurationPacketListener.EmptyInvoker.INSTANCE,
                S2CConfigurationPacketListener.InvokerFactory.INSTANCE);
        packetReceivedEvent = EventFactory.createArrayBacked(C2SConfigurationPacketListener.class,
                C2SConfigurationPacketListener.EmptyInvoker.INSTANCE,
                        C2SConfigurationPacketListener.InvokerFactory.INSTANCE);
    }

    @NotNull
    @Override
    public Event<C2SConfigurationPacketListener> getPacketReceivedEvent() {
        return packetReceivedEvent;
    }

    @Override
    @NotNull
    public Event<S2CConfigurationPacketListener> getPacketSendEvent() {
        return packetSentEvent;
    }

    @Override
    public void sendPacket(Packet<?> packet) {
        CallbackInfo callback = new CallbackInfo("packet send", true);

        getPacketSendEvent().invoker().intercept(packet, callback);

        if (!callback.isCancelled()) {
            super.sendPacket(packet);
        }
    }

    @Redirect(method = "onReady", target = @Desc(owner = ClientConnection.class, value = "send",
            args = Packet.class), at = @At(value = "INVOKE", target = "Lnet/minecraft/network/ClientConnection;" +
            "send(Lnet/minecraft/network/packet/Packet;)V"))
    private void packetSendOnReady(ClientConnection instance, Packet<?> packet) {
        CallbackInfo callback = new CallbackInfo("packet send", true);

        getPacketSendEvent().invoker().intercept(packet, callback);

        if (!callback.isCancelled()) {
            connection.send(packet);
        }
    }

    private ServerConfigurationNetworkHandlerMixin(MinecraftServer server, ClientConnection connection,
                                                   ConnectedClientData clientData) {
        super(server, connection, clientData);
    }
}

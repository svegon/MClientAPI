package io.github.svegon.mclientapi.mixin;

import io.github.svegon.mclientapi.event.network.C2SPlayPacketListener;
import io.github.svegon.mclientapi.event.network.S2CPlayPacketListener;
import io.github.svegon.mclientapi.mixininterface.network.MClientAPIServerPlayPacketListener;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.ServerPlayPacketListener;
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

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin
        extends ServerCommonNetworkHandler
        implements ServerPlayPacketListener,
        PlayerAssociatedNetworkHandler,
        TickablePacketListener,
        MClientAPIServerPlayPacketListener {
    @Unique
    private @Final Event<C2SPlayPacketListener> packetReceivedEvent;
    @Unique
    private @Final Event<S2CPlayPacketListener> packetSentEvent;

    @Inject(at = @At("RETURN"), method = "<init>")
    private void init(MinecraftServer server, ClientConnection connection,
                      ServerPlayerEntity player, ConnectedClientData clientData, CallbackInfo ci) {
        packetSentEvent = EventFactory.createArrayBacked(S2CPlayPacketListener.class,
                S2CPlayPacketListener.EmptyInvoker.INSTANCE,
                S2CPlayPacketListener.InvokerFactory.INSTANCE);
        packetReceivedEvent = EventFactory.createArrayBacked(C2SPlayPacketListener.class,
                C2SPlayPacketListener.EmptyInvoker.INSTANCE,
                        C2SPlayPacketListener.InvokerFactory.INSTANCE);
    }

    @NotNull
    @Override
    public Event<C2SPlayPacketListener> getPacketReceivedEvent() {
        return packetReceivedEvent;
    }

    @Override
    @NotNull
    public Event<S2CPlayPacketListener> getPacketSendEvent() {
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

    @Redirect(method = "onQueryPing", target = @Desc(owner = ClientConnection.class, value = "send",
            args = Packet.class), at = @At(value = "INVOKE", target = "Lnet/minecraft/network/ClientConnection;" +
                    "send(Lnet/minecraft/network/packet/Packet;)V"))
    private void packetSendOnReady(ClientConnection instance, Packet<?> packet) {
        CallbackInfo callback = new CallbackInfo("packet send", true);

        getPacketSendEvent().invoker().intercept(packet, callback);

        if (!callback.isCancelled()) {
            connection.send(packet);
        }
    }

    private ServerPlayNetworkHandlerMixin(MinecraftServer server, ClientConnection connection,
                                          ConnectedClientData clientData) {
        super(server, connection, clientData);
    }
}

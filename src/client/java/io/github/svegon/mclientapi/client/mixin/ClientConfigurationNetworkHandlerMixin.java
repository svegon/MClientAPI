package io.github.svegon.mclientapi.client.mixin;

import io.github.svegon.mclientapi.event.network.C2SConfigurationPacketListener;
import io.github.svegon.mclientapi.event.network.S2CConfigurationPacketListener;
import io.github.svegon.mclientapi.mixininterface.network.MClientAPIClientConfigurationPacketListener;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.client.network.ClientConfigurationNetworkHandler;
import net.minecraft.client.network.ClientConnectionState;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.ClientConfigurationPacketListener;
import net.minecraft.network.listener.TickablePacketListener;
import net.minecraft.network.packet.Packet;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConfigurationNetworkHandler.class)
public abstract class ClientConfigurationNetworkHandlerMixin extends ClientCommonNetworkHandler
        implements ClientConfigurationPacketListener, TickablePacketListener,
        MClientAPIClientConfigurationPacketListener {
    @Unique
    private @Final Event<S2CConfigurationPacketListener> packetReceivedEvent;
    @Unique
    private @Final Event<C2SConfigurationPacketListener> packetSendEvent;

    @Inject(at = @At("RETURN"), method = "<init>")
    private void init(MinecraftClient client, ClientConnection clientConnection,
                      ClientConnectionState clientConnectionState, CallbackInfo ci) {
        packetReceivedEvent = EventFactory.createArrayBacked(S2CConfigurationPacketListener.class,
                        S2CConfigurationPacketListener.EmptyInvoker.INSTANCE,
                        S2CConfigurationPacketListener.InvokerFactory.INSTANCE);
        packetSendEvent = EventFactory.createArrayBacked(C2SConfigurationPacketListener.class,
                        C2SConfigurationPacketListener.EmptyInvoker.INSTANCE,
                        C2SConfigurationPacketListener.InvokerFactory.INSTANCE);
    }
    @Override
    public @NotNull Event<C2SConfigurationPacketListener> getPacketSendEvent() {
        return packetSendEvent;
    }

    @Override
    public @NotNull Event<S2CConfigurationPacketListener> getPacketReceivedEvent() {
        return packetReceivedEvent;
    }

    @Override
    public void sendPacket(Packet<?> packet) {
        CallbackInfo callback = new CallbackInfo("packet send", true);

        getPacketSendEvent().invoker().intercept(packet, callback);

        if (!callback.isCancelled()) {
            super.sendPacket(packet);
        }
    }

    @Redirect(method = "onReady", target = @Desc(owner = ClientConnection.class, value = "send", args = Packet.class),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/network/ClientConnection;" +
                    "send(Lnet/minecraft/network/packet/Packet;)V"))
    private void packetSendOnReady(ClientConnection instance, Packet<?> packet) {
        CallbackInfo callback = new CallbackInfo("packet send", true);

        getPacketSendEvent().invoker().intercept(packet, callback);

        if (!callback.isCancelled()) {
            connection.send(packet);
        }
    }

    private ClientConfigurationNetworkHandlerMixin(MinecraftClient client, ClientConnection connection,
                                                   ClientConnectionState connectionState) {
        super(client, connection, connectionState);
    }
}

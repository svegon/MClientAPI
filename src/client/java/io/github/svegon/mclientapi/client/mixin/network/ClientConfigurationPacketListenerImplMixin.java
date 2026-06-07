package io.github.svegon.mclientapi.client.mixin.network;

import io.github.svegon.mclientapi.event.network.C2SConfigurationPacketListener;
import io.github.svegon.mclientapi.event.network.S2CConfigurationPacketListener;
import io.github.svegon.mclientapi.mixininterface.network.IClientConfigurationPacketListener;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.client.multiplayer.ClientConfigurationPacketListenerImpl;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.network.Connection;
import net.minecraft.network.TickablePacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.configuration.ClientConfigurationPacketListener;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConfigurationPacketListenerImpl.class)
public abstract class ClientConfigurationPacketListenerImplMixin extends ClientCommonPacketListenerImpl
        implements ClientConfigurationPacketListener, TickablePacketListener,
        IClientConfigurationPacketListener {
    @Unique
    private @Final Event<S2CConfigurationPacketListener> packetReceivedEvent;
    @Unique
    private @Final Event<C2SConfigurationPacketListener> packetSendEvent;

    @Inject(at = @At("RETURN"), method = "<init>")
    private void init(Minecraft minecraft, Connection connection, CommonListenerCookie cookie,
                      CallbackInfo ci) {
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
    public void send(Packet<?> packet) {
        CallbackInfo callback = new CallbackInfo("packet send", true);

        getPacketSendEvent().invoker().intercept(packet, callback);

        if (!callback.isCancelled()) {
            super.send(packet);
        }
    }

    @Redirect(method = "handleConfigurationFinished", target = @Desc(owner = Connection.class, value = "send",
            args = Packet.class), at = @At(value = "INVOKE", target = "Lnet/minecraft/network/Connection;send(" +
            "Lnet/minecraft/network/protocol/Packet;)V"))
    private void packetSendOnReady(Connection instance, Packet<?> packet) {
        CallbackInfo callback = new CallbackInfo("packet send", true);

        getPacketSendEvent().invoker().intercept(packet, callback);

        if (!callback.isCancelled()) {
            connection.send(packet);
        }
    }

    private ClientConfigurationPacketListenerImplMixin(Minecraft minecraft, Connection connection,
                                                       CommonListenerCookie cookie) {
        super(minecraft, connection, cookie);
    }
}

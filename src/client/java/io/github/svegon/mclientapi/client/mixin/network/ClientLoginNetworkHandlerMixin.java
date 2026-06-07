package io.github.svegon.mclientapi.client.mixin.network;

import io.github.svegon.mclientapi.event.network.C2SLoginPacketListener;
import io.github.svegon.mclientapi.event.network.S2CLoginPacketListener;
import io.github.svegon.mclientapi.mixininterface.network.IClientLoginPacketListener;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
import net.minecraft.client.multiplayer.LevelLoadTracker;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.TransferState;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.login.ClientLoginPacketListener;
import net.minecraft.network.protocol.login.ServerboundKeyPacket;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.crypto.Cipher;
import java.time.Duration;
import java.util.function.Consumer;

@Mixin(ClientHandshakePacketListenerImpl.class)
public abstract class ClientLoginNetworkHandlerMixin implements ClientLoginPacketListener,
        IClientLoginPacketListener {
    @Unique
    private @Final Event<S2CLoginPacketListener> packetReceivedEvent;
    @Unique
    private @Final Event<C2SLoginPacketListener> packetSendEvent;

    @Inject(at = @At("RETURN"), method = "<init>")
    private void init(Connection connection, Minecraft minecraft, ServerData serverData, Screen parent,
                      boolean newWorld, Duration worldLoadDuration, Consumer updateStatus,
                      LevelLoadTracker levelLoadTracker, TransferState transferState, CallbackInfo ci) {
        packetReceivedEvent = EventFactory.createArrayBacked(
                S2CLoginPacketListener.class, S2CLoginPacketListener.EmptyInvoker.INSTANCE,
                S2CLoginPacketListener.InvokerFactory.INSTANCE);
        packetSendEvent = EventFactory.createArrayBacked(
                C2SLoginPacketListener.class, C2SLoginPacketListener.EmptyInvoker.INSTANCE,
                C2SLoginPacketListener.InvokerFactory.INSTANCE);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/network/Connection;send(" +
            "Lnet/minecraft/network/protocol/Packet;Lio/netty/channel/ChannelFutureListener;)V"),
            method = "setEncryption", cancellable = true)
    private void onSetupEncryption(ServerboundKeyPacket setKeyPacket, Cipher decryptCipher,
                                   Cipher encryptCipher, CallbackInfo ci) {
        getPacketSendEvent().invoker().intercept(setKeyPacket, ci);
    }

    @Redirect(method = "handleLoginFinished", target = @Desc(owner = Connection.class, value = "send",
            args = Packet.class), at = @At(value = "INVOKE", target = "Lnet/minecraft/network/Connection;send(" +
            "Lnet/minecraft/network/protocol/Packet;)V"))
    private void packetSendOnSuccess(Connection instance, Packet<?> packet) {
        CallbackInfo callback = new CallbackInfo("client login packet send", true);

        getPacketSendEvent().invoker().intercept(packet, callback);

        if (!callback.isCancelled()) {
            instance.send(packet);
        }
    }

    @Redirect(method = "handleCustomQuery", target = @Desc(owner = Connection.class, value = "send",
            args = Packet.class), at = @At(value = "INVOKE", target = "Lnet/minecraft/network/Connection;" +
            "send(Lnet/minecraft/network/protocol/Packet;)V"))
    private void packetSendOnQueryRequest(Connection instance, Packet<?> packet) {
        CallbackInfo callback = new CallbackInfo("client login packet send", true);

        getPacketSendEvent().invoker().intercept(packet, callback);

        if (!callback.isCancelled()) {
            instance.send(packet);
        }
    }

    @Redirect(method = "handleRequestCookie", target = @Desc(owner = Connection.class, value = "send",
            args = Packet.class), at = @At(value = "INVOKE", target = "Lnet/minecraft/network/Connection;" +
            "send(Lnet/minecraft/network/protocol/Packet;)V"))
    private void packetSendOnCookieRequest(Connection instance, Packet<?> packet) {
        CallbackInfo callback = new CallbackInfo("client login packet send", true);

        getPacketSendEvent().invoker().intercept(packet, callback);

        if (!callback.isCancelled()) {
            instance.send(packet);
        }
    }

    @Override
    @NotNull
    public Event<S2CLoginPacketListener> getPacketReceivedEvent() {
        return packetReceivedEvent;
    }

    @Override
    @NotNull
    public Event<C2SLoginPacketListener> getPacketSendEvent() {
        return packetSendEvent;
    }
}

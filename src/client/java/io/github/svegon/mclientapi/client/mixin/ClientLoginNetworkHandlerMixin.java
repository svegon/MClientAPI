package io.github.svegon.mclientapi.client.mixin;

import io.github.svegon.mclientapi.event.network.C2SLoginPacketListener;
import io.github.svegon.mclientapi.event.network.S2CLoginPacketListener;
import io.github.svegon.mclientapi.mixininterface.network.MClientAPIClientLoginPacketListener;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientLoginNetworkHandler;
import net.minecraft.client.network.CookieStorage;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.ClientLoginPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.login.LoginKeyC2SPacket;
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

@Mixin(ClientLoginNetworkHandler.class)
public abstract class ClientLoginNetworkHandlerMixin implements ClientLoginPacketListener,
        MClientAPIClientLoginPacketListener {
    @Unique
    private @Final Event<S2CLoginPacketListener> packetReceivedEvent;
    @Unique
    private @Final Event<C2SLoginPacketListener> packetSendEvent;

    @Inject(at = @At("RETURN"), method = "<init>")
    private void init(ClientConnection connection, MinecraftClient client, ServerInfo serverInfo,
                      Screen parentScreen, boolean newWorld, Duration worldLoadTime,
                      Consumer statusConsumer, CookieStorage cookieStorage, CallbackInfo ci) {
        packetReceivedEvent = EventFactory.createArrayBacked(
                S2CLoginPacketListener.class, S2CLoginPacketListener.EmptyInvoker.INSTANCE,
                S2CLoginPacketListener.InvokerFactory.INSTANCE);
        packetSendEvent = EventFactory.createArrayBacked(
                C2SLoginPacketListener.class, C2SLoginPacketListener.EmptyInvoker.INSTANCE,
                C2SLoginPacketListener.InvokerFactory.INSTANCE);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/network/ClientConnection;send" +
            "(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/PacketCallbacks;)V"),
            method = "setupEncryption", cancellable = true)
    private void onSetupEncryption(LoginKeyC2SPacket keyPacket, Cipher decryptionCipher, Cipher encryptionCipher,
                                   CallbackInfo ci) {
        getPacketSendEvent().invoker().intercept(keyPacket, ci);
    }

    @Redirect(method = "onSuccess", target = @Desc(owner = ClientConnection.class, value = "send",
            args = Packet.class), at = @At(value = "INVOKE", target = "Lnet/minecraft/network/ClientConnection;" +
            "send(Lnet/minecraft/network/packet/Packet;)V"))
    private void packetSendOnSuccess(ClientConnection instance, Packet<?> packet) {
        CallbackInfo callback = new CallbackInfo("client login packet send", true);

        getPacketSendEvent().invoker().intercept(packet, callback);

        if (!callback.isCancelled()) {
            instance.send(packet);
        }
    }

    @Redirect(method = "onQueryRequest", target = @Desc(owner = ClientConnection.class, value = "send",
            args = Packet.class), at = @At(value = "INVOKE", target = "Lnet/minecraft/network/ClientConnection;" +
            "send(Lnet/minecraft/network/packet/Packet;)V"))
    private void packetSendOnQueryRequest(ClientConnection instance, Packet<?> packet) {
        CallbackInfo callback = new CallbackInfo("client login packet send", true);

        getPacketSendEvent().invoker().intercept(packet, callback);

        if (!callback.isCancelled()) {
            instance.send(packet);
        }
    }

    @Redirect(method = "onCookieRequest", target = @Desc(owner = ClientConnection.class, value = "send",
            args = Packet.class), at = @At(value = "INVOKE", target = "Lnet/minecraft/network/ClientConnection;" +
            "send(Lnet/minecraft/network/packet/Packet;)V"))
    private void packetSendOnCookieRequest(ClientConnection instance, Packet<?> packet) {
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

package io.github.svegon.mclientapi.mixin;

import io.github.svegon.mclientapi.event.network.C2SLoginPacketListener;
import io.github.svegon.mclientapi.event.network.S2CLoginPacketListener;
import io.github.svegon.mclientapi.mixininterface.network.IServerLoginPacketListener;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.listener.ServerLoginPacketListener;
import net.minecraft.network.listener.TickablePacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.*;
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

@Mixin(ServerLoginNetworkHandler.class)
public abstract class ServerLoginNetworkHandlerMixin  implements ServerLoginPacketListener, TickablePacketListener,
        IServerLoginPacketListener {
    @Unique
    private @Final Event<C2SLoginPacketListener> packetReceivedEvent;
    @Unique
    private @Final Event<S2CLoginPacketListener> packetSentEvent;

    @Inject(at = @At("RETURN"), method = "<init>")
    private void init(MinecraftServer server, ClientConnection connection, boolean transferred, CallbackInfo ci) {
        packetSentEvent = EventFactory.createArrayBacked(S2CLoginPacketListener.class,
                S2CLoginPacketListener.EmptyInvoker.INSTANCE,
                S2CLoginPacketListener.InvokerFactory.INSTANCE);
        packetReceivedEvent = EventFactory.createArrayBacked(C2SLoginPacketListener.class,
                C2SLoginPacketListener.EmptyInvoker.INSTANCE,
                        C2SLoginPacketListener.InvokerFactory.INSTANCE);
    }

    @NotNull
    @Override
    public Event<C2SLoginPacketListener> getPacketReceivedEvent() {
        return packetReceivedEvent;
    }

    @Override
    @NotNull
    public Event<S2CLoginPacketListener> getPacketSendEvent() {
        return packetSentEvent;
    }

    @Redirect(method = "onHello", target = @Desc(owner = ClientConnection.class, value = "send",
            args = Packet.class), at = @At(value = "INVOKE", target = "Lnet/minecraft/network/ClientConnection;" +
            "send(Lnet/minecraft/network/packet/Packet;)V"))
    public void packetSendOnHello(ClientConnection instance, Packet<?> packet) {
        CallbackInfo callback = new CallbackInfo("packet send", true);

        getPacketSendEvent().invoker().intercept(packet, callback);

        if (!callback.isCancelled()) {
            instance.send(packet);
        }
    }

    @Redirect(method = "disconnect", target = @Desc(owner = ClientConnection.class, value = "send",
            args = Packet.class), at = @At(value = "INVOKE", target = "Lnet/minecraft/network/ClientConnection;" +
            "send(Lnet/minecraft/network/packet/Packet;)V"))
    public void packetSendDisconnect(ClientConnection instance, Packet<?> packet) {
        CallbackInfo callback = new CallbackInfo("packet send", true);

        getPacketSendEvent().invoker().intercept(packet, callback);

        if (!callback.isCancelled()) {
            instance.send(packet);
        }
    }

    @Redirect(method = "tickVerify", target = @Desc(owner = ClientConnection.class, value = "send",
            args = Packet.class), at = @At(value = "INVOKE", target = "Lnet/minecraft/network/ClientConnection;" +
            "send(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/PacketCallbacks;)V"))
    public void onTickVerify(ClientConnection instance, Packet<?> packet, @Nullable PacketCallbacks callbacks) {
        CallbackInfo callback = new CallbackInfo("packet send", true);

        getPacketSendEvent().invoker().intercept(packet, callback);

        if (!callback.isCancelled()) {
            instance.send(packet);
        }
    }

    @Redirect(method = "sendSuccessPacket", target = @Desc(owner = ClientConnection.class, value = "send",
            args = Packet.class), at = @At(value = "INVOKE", target = "Lnet/minecraft/network/ClientConnection;" +
            "send(Lnet/minecraft/network/packet/Packet;)V"))
    public void onSendSuccessPacket(ClientConnection instance, Packet<?> packet) {
        CallbackInfo callback = new CallbackInfo("packet send", true);

        getPacketSendEvent().invoker().intercept(packet, callback);

        if (!callback.isCancelled()) {
            instance.send(packet);
        }
    }
}

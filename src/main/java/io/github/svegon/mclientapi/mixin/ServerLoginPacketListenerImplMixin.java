package io.github.svegon.mclientapi.mixin;

import io.github.svegon.mclientapi.event.network.C2SLoginPacketListener;
import io.github.svegon.mclientapi.event.network.S2CLoginPacketListener;
import io.github.svegon.mclientapi.mixininterface.network.IServerLoginPacketListener;
import io.netty.channel.ChannelFutureListener;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.networking.v1.context.PacketContextProvider;
import net.minecraft.network.Connection;
import net.minecraft.network.TickablePacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.login.ServerLoginPacketListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.*;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLoginPacketListenerImpl.class)
public abstract class ServerLoginPacketListenerImplMixin implements ServerLoginPacketListener, TickablePacketListener,
        PacketContextProvider, IServerLoginPacketListener {
    @Unique
    private @Final Event<@NotNull C2SLoginPacketListener> packetReceivedEvent;
    @Unique
    private @Final Event<@NotNull S2CLoginPacketListener> packetSentEvent;

    @Inject(at = @At("RETURN"), method = "<init>")
    private void init(final MinecraftServer minecraftserver, final Connection connection, final boolean transferred,
                      CallbackInfo ci) {
        packetSentEvent = EventFactory.createArrayBacked(S2CLoginPacketListener.class,
                S2CLoginPacketListener.EmptyInvoker.INSTANCE,
                S2CLoginPacketListener.InvokerFactory.INSTANCE);
        packetReceivedEvent = EventFactory.createArrayBacked(C2SLoginPacketListener.class,
                C2SLoginPacketListener.EmptyInvoker.INSTANCE,
                        C2SLoginPacketListener.InvokerFactory.INSTANCE);
    }

    @NotNull
    @Override
    public Event<@NotNull C2SLoginPacketListener> getPacketReceivedEvent() {
        return packetReceivedEvent;
    }

    @Override
    @NotNull
    public Event<@NotNull S2CLoginPacketListener> getPacketSendEvent() {
        return packetSentEvent;
    }

    @Redirect(method = "disconnect", target = @Desc(owner = Connection.class, value = "send",
            args = Packet.class), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/network/Connection;send(Lnet/minecraft/network/protocol/Packet;)V"))
    public void packetSendDisconnect(Connection instance, Packet<?> packet) {
        CallbackInfo callback = new CallbackInfo("packet send", true);

        getPacketSendEvent().invoker().intercept(packet, callback);

        if (!callback.isCancelled()) {
            instance.send(packet);
        }
    }

    @Redirect(method = "handleHello", target = @Desc(owner = Connection.class, value = "send",
            args = Packet.class), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/network/Connection;send(Lnet/minecraft/network/protocol/Packet;)V"))
    public void packetSendOnHello(Connection instance, Packet<?> packet) {
        CallbackInfo callback = new CallbackInfo("packet send", true);

        getPacketSendEvent().invoker().intercept(packet, callback);

        if (!callback.isCancelled()) {
            instance.send(packet);
        }
    }

    @Redirect(method = "verifyLoginAndFinishConnectionSetup", target = @Desc(owner = Connection.class, value = "send",
            args = Packet.class), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/network/Connection;send(Lnet/minecraft/network/protocol/Packet;Lio/netty/channel/ChannelFutureListener;)V"))
    public void onTickVerify(Connection instance, Packet<?> packet, final @Nullable ChannelFutureListener listener) {
        CallbackInfo callback = new CallbackInfo("packet send", true);

        getPacketSendEvent().invoker().intercept(packet, callback);

        if (!callback.isCancelled()) {
            instance.send(packet);
        }
    }

    @Redirect(method = "finishLoginAndWaitForClient", target = @Desc(owner = Connection.class, value = "send",
            args = Packet.class), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/network/Connection;send(Lnet/minecraft/network/protocol/Packet;)V"))
    public void onSendSuccessPacket(Connection instance, Packet<?> packet) {
        CallbackInfo callback = new CallbackInfo("packet send", true);

        getPacketSendEvent().invoker().intercept(packet, callback);

        if (!callback.isCancelled()) {
            instance.send(packet);
        }
    }
}

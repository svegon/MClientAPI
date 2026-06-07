package io.github.svegon.mclientapi.mixin;

import io.github.svegon.mclientapi.event.network.C2SHandshakePacketListener;
import io.github.svegon.mclientapi.event.network.S2CHandshakePacketListener;
import io.github.svegon.mclientapi.mixininterface.network.IServerHandshakePacketListener;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.networking.v1.context.PacketContextProvider;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.handshake.ServerHandshakePacketListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerHandshakePacketListenerImpl;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerHandshakePacketListenerImpl.class)
public abstract class ServerHandshakePacketListenerImplMixin implements ServerHandshakePacketListener,
        PacketContextProvider, IServerHandshakePacketListener {
    @Unique
    private @Final Event<@NotNull C2SHandshakePacketListener> packetReceivedEvent;
    @Unique
    private @Final Event<@NotNull S2CHandshakePacketListener> packetSentEvent;

    @Inject(at = @At("RETURN"), method = "<init>")
    private void init(final MinecraftServer server, final Connection connection, CallbackInfo ci) {
        packetSentEvent = EventFactory.createArrayBacked(S2CHandshakePacketListener.class,
                S2CHandshakePacketListener.EmptyInvoker.INSTANCE,
                S2CHandshakePacketListener.InvokerFactory.INSTANCE);
        packetReceivedEvent = EventFactory.createArrayBacked(C2SHandshakePacketListener.class,
                C2SHandshakePacketListener.EmptyInvoker.INSTANCE,
                C2SHandshakePacketListener.InvokerFactory.INSTANCE);
    }

    @NotNull
    @Override
    public Event<@NotNull C2SHandshakePacketListener> getPacketReceivedEvent() {
        return packetReceivedEvent;
    }

    @Override
    @NotNull
    public Event<@NotNull S2CHandshakePacketListener> getPacketSendEvent() {
        return packetSentEvent;
    }

    @Redirect(method = "handleIntention", target = @Desc(owner = Connection.class, value = "send",
            args = Packet.class), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/network/Connection;send(Lnet/minecraft/network/protocol/Packet;)V"))
    private void packetSendOnHandleIntention(Connection instance, Packet<?> packet) {
        CallbackInfo callback = new CallbackInfo("packet send", true);

        getPacketSendEvent().invoker().intercept(packet, callback);

        if (!callback.isCancelled()) {
            instance.send(packet);
        }
    }

    @Redirect(method = "beginLogin", target = @Desc(owner = Connection.class, value = "send",
            args = Packet.class), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/network/Connection;send(Lnet/minecraft/network/protocol/Packet;)V"))
    private void packetSendOnLogin(Connection instance, Packet<?> packet) {
        CallbackInfo callback = new CallbackInfo("packet send", true);

        getPacketSendEvent().invoker().intercept(packet, callback);

        if (!callback.isCancelled()) {
            instance.send(packet);
        }
    }
}

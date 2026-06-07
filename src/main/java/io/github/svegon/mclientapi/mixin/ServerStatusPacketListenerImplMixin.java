package io.github.svegon.mclientapi.mixin;

import io.github.svegon.mclientapi.event.network.C2SStatusPacketListener;
import io.github.svegon.mclientapi.event.network.S2CStatusPacketListener;
import io.github.svegon.mclientapi.mixininterface.network.IServerStatusPacketListener;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.status.ServerStatus;
import net.minecraft.network.protocol.status.ServerStatusPacketListener;
import net.minecraft.server.network.ServerStatusPacketListenerImpl;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerStatusPacketListenerImpl.class)
public abstract class ServerStatusPacketListenerImplMixin implements ServerStatusPacketListener,
        IServerStatusPacketListener {
    @Unique
    private @Final Event<@NotNull C2SStatusPacketListener> packetReceivedEvent;
    @Unique
    private @Final Event<@NotNull S2CStatusPacketListener> packetSentEvent;

    @Inject(at = @At("RETURN"), method = "<init>")
    private void init(final ServerStatus status, final Connection connection, CallbackInfo ci) {
        packetSentEvent = EventFactory.createArrayBacked(S2CStatusPacketListener.class,
                S2CStatusPacketListener.EmptyInvoker.INSTANCE,
                S2CStatusPacketListener.InvokerFactory.INSTANCE);
        packetReceivedEvent = EventFactory.createArrayBacked(C2SStatusPacketListener.class,
                C2SStatusPacketListener.EmptyInvoker.INSTANCE,
                        C2SStatusPacketListener.InvokerFactory.INSTANCE);
    }

    @NotNull
    @Override
    public Event<@NotNull C2SStatusPacketListener> getPacketReceivedEvent() {
        return packetReceivedEvent;
    }

    @Override
    @NotNull
    public Event<@NotNull S2CStatusPacketListener> getPacketSendEvent() {
        return packetSentEvent;
    }

    @Redirect(method = "handleStatusRequest", target = @Desc(owner = Connection.class, value = "send",
            args = Packet.class), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/network/Connection;send(Lnet/minecraft/network/protocol/Packet;)V"))
    private void packetSendOnRequest(Connection instance, Packet<?> packet) {
        CallbackInfo callback = new CallbackInfo("packet send", true);

        getPacketSendEvent().invoker().intercept(packet, callback);

        if (!callback.isCancelled()) {
            instance.send(packet);
        }
    }

    @Redirect(method = "handlePingRequest", target = @Desc(owner = Connection.class, value = "send",
            args = Packet.class), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/network/Connection;send(Lnet/minecraft/network/protocol/Packet;)V"))
    private void packetSendOnQueryPing(Connection instance, Packet<?> packet) {
        CallbackInfo callback = new CallbackInfo("packet send", true);

        getPacketSendEvent().invoker().intercept(packet, callback);

        if (!callback.isCancelled()) {
            instance.send(packet);
        }
    }
}

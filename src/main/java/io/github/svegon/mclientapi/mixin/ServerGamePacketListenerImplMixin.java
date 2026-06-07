package io.github.svegon.mclientapi.mixin;

import io.github.svegon.mclientapi.event.network.C2SPlayPacketListener;
import io.github.svegon.mclientapi.event.network.S2CGamePacketListener;
import io.github.svegon.mclientapi.mixininterface.network.IServerGamePacketListener;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.network.Connection;
import net.minecraft.network.TickablePacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.GameProtocols;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
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

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin extends ServerCommonPacketListenerImpl
        implements ServerGamePacketListener, ServerPlayerConnection, TickablePacketListener, GameProtocols.Context,
        IServerGamePacketListener {
    @Unique
    private @Final Event<@NotNull C2SPlayPacketListener> packetReceivedEvent;
    @Unique
    private @Final Event<@NotNull S2CGamePacketListener> packetSentEvent;

    @Inject(at = @At("RETURN"), method = "<init>")
    private void init(final MinecraftServer server, final Connection connection, final ServerPlayer player,
                      final CommonListenerCookie cookie, CallbackInfo ci) {
        packetSentEvent = EventFactory.createArrayBacked(S2CGamePacketListener.class,
                S2CGamePacketListener.EmptyInvoker.INSTANCE,
                S2CGamePacketListener.InvokerFactory.INSTANCE);
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
    public Event<S2CGamePacketListener> getPacketSendEvent() {
        return packetSentEvent;
    }

    @Override
    public void send(Packet<?> packet) {
        CallbackInfo callback = new CallbackInfo("packet send", true);

        getPacketSendEvent().invoker().intercept(packet, callback);

        if (!callback.isCancelled()) {
            super.send(packet);
        }
    }

    @Redirect(method = "handlePingRequest", target = @Desc(owner = Connection.class, value = "send",
            args = Packet.class), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/network/Connection;send(Lnet/minecraft/network/protocol/Packet;)V"))
    private void packetSendOnReady(Connection instance, Packet<?> packet) {
        CallbackInfo callback = new CallbackInfo("packet send", true);

        getPacketSendEvent().invoker().intercept(packet, callback);

        if (!callback.isCancelled()) {
            connection.send(packet);
        }
    }

    private ServerGamePacketListenerImplMixin(MinecraftServer server, Connection connection, CommonListenerCookie cookie) {
        super(server, connection, cookie);
    }
}

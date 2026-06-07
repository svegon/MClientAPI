package io.github.svegon.mclientapi.mixin;

import io.github.svegon.mclientapi.event.network.C2SConfigurationPacketListener;
import io.github.svegon.mclientapi.event.network.S2CConfigurationPacketListener;
import io.github.svegon.mclientapi.mixininterface.network.IServerConfigurationPacketListener;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.network.Connection;
import net.minecraft.network.TickablePacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.configuration.ServerConfigurationPacketListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.*;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerConfigurationPacketListenerImpl.class)
public abstract class ServerConfigurationPacketListenerImplMixin extends ServerCommonPacketListenerImpl
        implements ServerConfigurationPacketListener, TickablePacketListener, IServerConfigurationPacketListener {
    @Unique
    private @Final Event<@NotNull C2SConfigurationPacketListener> packetReceivedEvent;
    @Unique
    private @Final Event<@NotNull S2CConfigurationPacketListener> packetSentEvent;

    @Inject(at = @At("RETURN"), method = "<init>")
    private void init(final MinecraftServer server, final Connection connection, final CommonListenerCookie cookie,
                      CallbackInfo ci) {
        packetSentEvent = EventFactory.createArrayBacked(S2CConfigurationPacketListener.class,
                S2CConfigurationPacketListener.EmptyInvoker.INSTANCE,
                S2CConfigurationPacketListener.InvokerFactory.INSTANCE);
        packetReceivedEvent = EventFactory.createArrayBacked(C2SConfigurationPacketListener.class,
                C2SConfigurationPacketListener.EmptyInvoker.INSTANCE,
                        C2SConfigurationPacketListener.InvokerFactory.INSTANCE);
    }

    @NotNull
    @Override
    public Event<C2SConfigurationPacketListener> getPacketReceivedEvent() {
        return packetReceivedEvent;
    }

    @Override
    @NotNull
    public Event<S2CConfigurationPacketListener> getPacketSendEvent() {
        return packetSentEvent;
    }

    @Override
    public void send(@NotNull Packet<?> packet) {
        CallbackInfo callback = new CallbackInfo("packet send", true);

        getPacketSendEvent().invoker().intercept(packet, callback);

        if (!callback.isCancelled()) {
            super.send(packet);
        }
    }

    private ServerConfigurationPacketListenerImplMixin(MinecraftServer server, Connection connection,
                                                       CommonListenerCookie cookie) {
        super(server, connection, cookie);
    }
}

package io.github.svegon.mclientapi.mixin;

import io.github.svegon.mclientapi.event.network.packet_direct.C2SPlayPacketListener;
import io.github.svegon.mclientapi.event.network.packet_direct.S2CPlayPacketListener;
import io.github.svegon.mclientapi.mixininterface.IServerPlayPacketListener;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.listener.TickablePacketListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.PlayerAssociatedNetworkHandler;
import net.minecraft.server.network.ServerCommonNetworkHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin
        extends ServerCommonNetworkHandler
        implements ServerPlayPacketListener,
        PlayerAssociatedNetworkHandler,
        TickablePacketListener,
        IServerPlayPacketListener {
    @Unique
    private final Event<C2SPlayPacketListener> packetReceivedEvent =
            EventFactory.createArrayBacked(C2SPlayPacketListener.class, C2SPlayPacketListener.Companion.emptyInvoker(),
                    C2SPlayPacketListener.Companion.invokerFactory());

    private ServerPlayNetworkHandlerMixin(MinecraftServer server, ClientConnection connection, ConnectedClientData clientData) {
        super(server, connection, clientData);
    }

    @NotNull
    @Override
    public Event<C2SPlayPacketListener> getPacketReceivedEvent() {
        return packetReceivedEvent;
    }
}

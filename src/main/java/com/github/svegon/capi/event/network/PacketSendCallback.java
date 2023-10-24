package com.github.svegon.capi.event.network;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public interface PacketSendCallback {
    Event<PacketSendCallback> EVENT = EventFactory.createArrayBacked(PacketSendCallback.class,
            (connection, packet, callback) -> {}, listeners -> (connection, packet, callback) -> {
        for (PacketSendCallback listener : listeners) {
            listener.onPacketSend(connection, packet, callback);

            if (callback.isCancelled()) {
                return;
            }
        }
            });

    void onPacketSend(ClientConnection connection, Packet<?> packet, CallbackInfo callback);
}

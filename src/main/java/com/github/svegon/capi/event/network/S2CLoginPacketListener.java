package com.github.svegon.capi.event.network;

import com.github.svegon.capi.event.ListenerCollectionFactory;
import com.github.svegon.capi.event.ListenerList;
import com.github.svegon.capi.event.ListenerSet;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.ClientLoginPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.login.*;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public interface S2CLoginPacketListener extends ClientLoginPacketListener {
    Event<S2CLoginPacketListener> CLIENT_PACKET_SENT_EVENT = EventFactory.createArrayBacked(
            S2CLoginPacketListener.class,
            (listeners) -> new S2CLoginPacketListener() {
                @Override
                public void apply(Packet<ClientLoginPacketListener> packet, CallbackInfo info) {
                    for (S2CLoginPacketListener listener : listeners) {
                        listener.apply(packet, info);

                        if (info.isCancelled()) {
                            return;
                        }
                    }
                }
            });
    ListenerList<S2CLoginPacketListener> LISTENER_LIST = ListenerCollectionFactory.listenersVector(
            (l) -> new S2CLoginPacketListener() {
                @Override
                public void apply(Packet<ClientLoginPacketListener> packet, CallbackInfo info) {
                    for (S2CLoginPacketListener listener : l) {
                        listener.apply(packet, info);

                        if (info.isCancelled()) {
                            return;
                        }
                    }
                }
            });
    ListenerSet<S2CLoginPacketListener> LISTENER_SET = ListenerCollectionFactory.listenersLinkedSet(
            (s) -> new S2CLoginPacketListener() {
                @Override
                public void apply(Packet<ClientLoginPacketListener> packet, CallbackInfo info) {
                    for (S2CLoginPacketListener listener : s) {
                        listener.apply(packet, info);

                        if (info.isCancelled()) {
                            return;
                        }
                    }
                }
            });

    default void apply(Packet<ClientLoginPacketListener> packet, CallbackInfo callback) {
        packet.apply(this);
    }

    @Override
    default boolean isConnectionOpen() {
        return false;
    }

    @Override
    default void onHello(LoginHelloS2CPacket packet) {

    }

    @Override
    default void onSuccess(LoginSuccessS2CPacket packet) {

    }

    @Override
    default void onDisconnect(LoginDisconnectS2CPacket packet) {

    }

    @Override
    default void onCompression(LoginCompressionS2CPacket packet) {

    }

    @Override
    default void onQueryRequest(LoginQueryRequestS2CPacket packet) {

    }

    @Override
    default void onDisconnected(Text reason) {

    }
}

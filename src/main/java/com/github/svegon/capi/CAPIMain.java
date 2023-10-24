package com.github.svegon.capi;

import com.github.svegon.capi.event.network.C2SPlayPacketListener;
import com.github.svegon.capi.event.network.S2CPlayPacketListener;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;

public final class CAPIMain implements ClientModInitializer, DedicatedServerModInitializer, ModInitializer {
    @Override
    public void onInitializeClient() {
        // static initialization which couldn't be done in interfaces
        C2SPlayPacketListener.CLIENT_PACKET_SENT_EVENT.register(C2SPlayPacketListener.LISTENER_LIST.invoker());
        C2SPlayPacketListener.CLIENT_PACKET_SENT_EVENT.register(C2SPlayPacketListener.LISTENER_SET.invoker());
        S2CPlayPacketListener.CLIENT_PACKET_RECEIVED_EVENT.register(S2CPlayPacketListener.LISTENER_LIST.invoker());
        S2CPlayPacketListener.CLIENT_PACKET_RECEIVED_EVENT.register(S2CPlayPacketListener.LISTENER_SET.invoker());

        ClientAPI.LOGGER.info("initialized Client API");
    }

    @Override
    public void onInitializeServer() {

    }

    @Override
    public void onInitialize() {
    }
}

package io.github.svegon.mclientapi

import io.github.svegon.capi.event.network.C2SPlayPacketListener
import io.github.svegon.capi.event.network.S2CPlayPacketListener
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.DedicatedServerModInitializer
import net.fabricmc.api.ModInitializer

class CAPIMain : ClientModInitializer, DedicatedServerModInitializer, ModInitializer {
    override fun onInitializeClient() {
        // static initialization which couldn't be done in interfaces
        C2SPlayPacketListener.Companion.CLIENT_PACKET_SENT_EVENT.register(C2SPlayPacketListener.Companion.LISTENER_LIST.invoker())
        C2SPlayPacketListener.Companion.CLIENT_PACKET_SENT_EVENT.register(C2SPlayPacketListener.Companion.LISTENER_SET.invoker())
        S2CPlayPacketListener.Companion.CLIENT_PACKET_RECEIVED_EVENT.register(S2CPlayPacketListener.Companion.LISTENER_LIST.invoker())
        S2CPlayPacketListener.Companion.CLIENT_PACKET_RECEIVED_EVENT.register(S2CPlayPacketListener.Companion.LISTENER_SET.invoker())

        ClientAPI.LOGGER.info("initialized Client API")
    }

    override fun onInitializeServer() {
    }

    override fun onInitialize() {
    }
}

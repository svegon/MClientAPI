package io.github.svegon.mclientapi.mixininterface.network

import io.github.svegon.mclientapi.event.network.C2SConfigurationPacketListener
import io.github.svegon.mclientapi.event.network.C2SLoginPacketListener
import io.github.svegon.mclientapi.event.network.S2CConfigurationPacketListener
import io.github.svegon.mclientapi.event.network.S2CLoginPacketListener

interface IClientConfigurationPacketListener : IClientPacketListener<S2CConfigurationPacketListener,
        C2SConfigurationPacketListener>
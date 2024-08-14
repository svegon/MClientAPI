package io.github.svegon.mclientapi.mixininterface.network

import io.github.svegon.mclientapi.event.network.C2SConfigurationPacketListener
import io.github.svegon.mclientapi.event.network.C2SHandshakePacketListener
import io.github.svegon.mclientapi.event.network.S2CConfigurationPacketListener
import io.github.svegon.mclientapi.event.network.S2CLoginPacketListener

interface IServerConfigurationPacketListener : IServerPacketListener<C2SConfigurationPacketListener,
        S2CConfigurationPacketListener>
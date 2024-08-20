package io.github.svegon.mclientapi.mixininterface.network

import io.github.svegon.mclientapi.event.network.C2SConfigurationPacketListener
import io.github.svegon.mclientapi.event.network.S2CConfigurationPacketListener

interface MClientAPIServerConfigurationPacketListener : MClientAPIServerPacketListener<C2SConfigurationPacketListener,
        S2CConfigurationPacketListener>
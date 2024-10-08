package io.github.svegon.mclientapi.mixininterface.network

import io.github.svegon.mclientapi.event.network.C2SQueryPacketListener
import io.github.svegon.mclientapi.event.network.S2CQueryPacketListener

interface IServerQueryPacketListener : IServerPacketListener<C2SQueryPacketListener, S2CQueryPacketListener>
package io.github.svegon.mclientapi.mixininterface.network

import io.github.svegon.mclientapi.event.network.C2SQueryPacketListener
import io.github.svegon.mclientapi.event.network.S2CQueryPacketListener

interface IClientQueryPacketListener : IClientPacketListener<S2CQueryPacketListener, C2SQueryPacketListener>
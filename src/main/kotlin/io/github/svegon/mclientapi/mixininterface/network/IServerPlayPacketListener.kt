package io.github.svegon.mclientapi.mixininterface.network

import io.github.svegon.mclientapi.event.network.C2SPlayPacketListener
import io.github.svegon.mclientapi.event.network.S2CPlayPacketListener

interface IServerPlayPacketListener : IServerPacketListener<C2SPlayPacketListener, S2CPlayPacketListener>
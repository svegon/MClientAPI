package io.github.svegon.mclientapi.mixininterface.network

import io.github.svegon.mclientapi.event.network.C2SPlayPacketListener
import io.github.svegon.mclientapi.event.network.S2CGamePacketListener

interface IClientPlayPacketListener : IClientPacketListener<S2CGamePacketListener, C2SPlayPacketListener>
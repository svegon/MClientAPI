package io.github.svegon.mclientapi.mixininterface.network

import io.github.svegon.mclientapi.event.network.C2SPlayPacketListener
import io.github.svegon.mclientapi.event.network.S2CPlayPacketListener

interface MClientAPIServerPlayPacketListener : MClientAPIServerPacketListener<C2SPlayPacketListener, S2CPlayPacketListener>
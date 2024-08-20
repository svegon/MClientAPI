package io.github.svegon.mclientapi.mixininterface.network

import io.github.svegon.mclientapi.event.network.C2SPacketListener
import io.github.svegon.mclientapi.event.network.S2CPacketListener

interface MClientAPIServerPacketListener<R : C2SPacketListener, S : S2CPacketListener> : MClientAPIPacketListener<R, S>
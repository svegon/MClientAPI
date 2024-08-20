package io.github.svegon.mclientapi.mixininterface.network

import io.github.svegon.mclientapi.event.network.C2SPacketListener
import io.github.svegon.mclientapi.event.network.S2CPacketListener

interface MClientAPIClientPacketListener<R : S2CPacketListener, S : C2SPacketListener> : MClientAPIPacketListener<R, S>
package io.github.svegon.mclientapi.mixininterface.network

import io.github.svegon.mclientapi.event.network.C2SPacketListener
import io.github.svegon.mclientapi.event.network.S2CPacketListener

interface IServerPacketListener<R : C2SPacketListener, S : S2CPacketListener> : IPacketListener<R, S>
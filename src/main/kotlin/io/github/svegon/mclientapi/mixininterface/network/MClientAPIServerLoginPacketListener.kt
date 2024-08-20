package io.github.svegon.mclientapi.mixininterface.network

import io.github.svegon.mclientapi.event.network.*

interface MClientAPIServerLoginPacketListener : MClientAPIServerPacketListener<C2SLoginPacketListener, S2CLoginPacketListener>
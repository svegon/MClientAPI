package io.github.svegon.mclientapi.mixininterface.network

import io.github.svegon.mclientapi.event.network.C2SHandshakePacketListener
import io.github.svegon.mclientapi.event.network.S2CLoginPacketListener

interface IServerHandshakePacketListener : IServerPacketListener<C2SHandshakePacketListener, S2CLoginPacketListener>
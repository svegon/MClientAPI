package io.github.svegon.mclientapi.mixininterface.network

import io.github.svegon.mclientapi.event.network.*

interface IServerLoginPacketListener : IServerPacketListener<C2SLoginPacketListener, S2CLoginPacketListener>
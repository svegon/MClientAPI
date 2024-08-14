package io.github.svegon.mclientapi.mixininterface.network

import io.github.svegon.mclientapi.event.network.C2SLoginPacketListener
import io.github.svegon.mclientapi.event.network.S2CLoginPacketListener

interface IClientLoginPacketListener : IClientPacketListener<S2CLoginPacketListener, C2SLoginPacketListener>
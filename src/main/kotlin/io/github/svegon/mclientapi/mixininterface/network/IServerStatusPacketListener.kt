package io.github.svegon.mclientapi.mixininterface.network

import io.github.svegon.mclientapi.event.network.C2SStatusPacketListener
import io.github.svegon.mclientapi.event.network.S2CStatusPacketListener

interface IServerStatusPacketListener : IServerPacketListener<C2SStatusPacketListener, S2CStatusPacketListener>
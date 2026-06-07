package io.github.svegon.mclientapi.mixininterface.network

import io.github.svegon.mclientapi.event.network.C2SStatusPacketListener
import io.github.svegon.mclientapi.event.network.S2CStatusPacketListener

interface IClientStatusPacketListener : IClientPacketListener<S2CStatusPacketListener, C2SStatusPacketListener>

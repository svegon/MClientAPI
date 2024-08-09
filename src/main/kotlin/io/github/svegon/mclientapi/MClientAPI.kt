package io.github.svegon.mclientapi

import net.fabricmc.api.ModInitializer
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class MClientAPI : ModInitializer {
    companion object {
        val LOGGER: Logger = LogManager.getLogger("MClientAPI")
    }

    override fun onInitialize() {}
}

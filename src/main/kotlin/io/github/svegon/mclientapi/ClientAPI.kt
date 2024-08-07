package io.github.svegon.mclientapi

import io.github.svegon.mclientapi.ClientAPI
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object ClientAPI {
    var LOGGER: Logger = LogManager.getLogger("Client API")

    val instance: ClientAPI = ClientAPI()
}

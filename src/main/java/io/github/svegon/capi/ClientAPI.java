package io.github.svegon.capi;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ClientAPI {
    public static Logger LOGGER = LogManager.getLogger("Client API");

    private static final ClientAPI INSTANCE = new ClientAPI();

    private ClientAPI() {
    }

    public static ClientAPI getInstance() {
        return INSTANCE;
    }
}

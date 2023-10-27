package io.github.svegon.capi.event.network;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@FunctionalInterface
public interface ServerConnectCallback {
    Event<ServerConnectCallback> EVENT = EventFactory.createArrayBacked(ServerConnectCallback.class,
            (screen, client, address, info, quickPlay, ci) -> {},
            listeners -> (screen, client, address, info, quickPlay, ci) -> {
        for (ServerConnectCallback listener : listeners) {
            listener.onServerConnect(screen, client, address, info, quickPlay, ci);

            if (ci.isCancelled()) {
                return;
            }
        }
    });

    void onServerConnect(Screen screen, MinecraftClient client, ServerAddress address, ServerInfo info,
                         boolean quickPlay, CallbackInfo ci);
}

package com.github.svegon.capi.event.world;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.world.ClientWorld;

public final class ClientWorldLifecycleEvents {
    private ClientWorldLifecycleEvents() {
        throw new UnsupportedOperationException();
    }

    public static final Event<JoinWorld> JOIN_WORLD = EventFactory.createArrayBacked(JoinWorld.class,
            listeners -> (client, world) -> {
                for (JoinWorld listener : listeners) {
                    listener.onWorldJoin(client, world);
                }
            });

    public static final Event<LeaveWorld> LEAVE_WORLD = EventFactory.createArrayBacked(LeaveWorld.class,
            listeners -> (client, screen) -> {
                for (LeaveWorld listener : listeners) {
                    listener.onWorldLeave(client, screen);
                }
            });

    public interface JoinWorld {
        void onWorldJoin(MinecraftClient client, ClientWorld world);
    }

    public interface LeaveWorld {
        void onWorldLeave(MinecraftClient client, Screen screen);
    }
}

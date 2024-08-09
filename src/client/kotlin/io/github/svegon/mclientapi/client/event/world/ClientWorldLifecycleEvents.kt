package io.github.svegon.mclientapi.client.event.world

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.world.ClientWorld
import java.util.function.Function

object ClientWorldLifecycleEvents {
    fun interface JoinWorld {
        fun onWorldJoin(client: MinecraftClient, world: ClientWorld)
    }

    fun interface LeaveWorld {
        fun onWorldLeave(client: MinecraftClient, screen: Screen)
    }

    @JvmField
    val JOIN_WORLD: Event<JoinWorld> = EventFactory.createArrayBacked(
        JoinWorld::class.java,
        JoinWorld { client, world -> }
    ) { listeners: Array<JoinWorld> ->
        JoinWorld { client, world ->
            for (listener in listeners) {
                listener.onWorldJoin(client, world)
            }
        }
    }

    @JvmField
    val LEAVE_WORLD: Event<LeaveWorld> = EventFactory.createArrayBacked(
        LeaveWorld::class.java,
        LeaveWorld { client, screen -> }
    ) { listeners: Array<LeaveWorld> ->
        LeaveWorld { client, screen ->
            for (listener in listeners) {
                listener.onWorldLeave(client, screen)
            }
        }
    }
}

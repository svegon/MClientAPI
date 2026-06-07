package io.github.svegon.mclientapi.client.event.world

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.multiplayer.ClientLevel
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

object ClientWorldLifecycleEvents {
    fun interface JoinWorld {
        fun onWorldJoin(client: Minecraft, world: ClientLevel)
    }

    fun interface LeaveWorld {
        fun onWorldLeave(client: Minecraft, screen: Screen, keepResourcePacks: Boolean, stopSound: Boolean,
                         ci: CallbackInfo)
    }

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

    val LEAVE_WORLD: Event<LeaveWorld> = EventFactory.createArrayBacked(LeaveWorld::class.java,
        LeaveWorld { client: Minecraft, screen: Screen, keepResourcePacks: Boolean, stopSound: Boolean,
                     ci: CallbackInfo -> }
    ) { listeners: Array<LeaveWorld> ->
        LeaveWorld { client: Minecraft, screen: Screen, keepResourcePacks: Boolean, stopSound: Boolean,
                     ci: CallbackInfo ->
            for (listener in listeners) {
                listener.onWorldLeave(client, screen, keepResourcePacks, stopSound, ci)

                if (ci.isCancelled) {
                    return@LeaveWorld
                }
            }
        }
    }
}

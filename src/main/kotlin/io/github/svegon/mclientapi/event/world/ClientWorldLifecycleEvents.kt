package io.github.svegon.capi.event.world

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.MinecraftClient
import java.util.function.Function

class ClientWorldLifecycleEvents private constructor() {
    init {
        throw UnsupportedOperationException()
    }

    interface JoinWorld {
        fun onWorldJoin(client: MinecraftClient?, world: ClientWorld?)
    }

    interface LeaveWorld {
        fun onWorldLeave(client: MinecraftClient?, screen: Screen?)
    }

    companion object {
        @JvmField
        val JOIN_WORLD: Event<JoinWorld> = EventFactory.createArrayBacked<JoinWorld>(
            JoinWorld::class.java,
            Function<Array<JoinWorld>, JoinWorld> { listeners: Array<JoinWorld> ->
                JoinWorld { client: MinecraftClient?, world: ClientWorld? ->
                    for (listener in listeners) {
                        listener.onWorldJoin(client, world)
                    }
                }
            })

        @JvmField
        val LEAVE_WORLD: Event<LeaveWorld> = EventFactory.createArrayBacked<LeaveWorld>(
            LeaveWorld::class.java,
            Function<Array<LeaveWorld>, LeaveWorld> { listeners: Array<LeaveWorld> ->
                LeaveWorld { client: MinecraftClient?, screen: Screen? ->
                    for (listener in listeners) {
                        listener.onWorldLeave(client, screen)
                    }
                }
            })
    }
}

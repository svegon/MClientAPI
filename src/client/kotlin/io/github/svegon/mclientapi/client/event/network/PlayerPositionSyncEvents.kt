package io.github.svegon.mclientapi.client.event.network

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.player.LocalPlayer
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

object PlayerPositionSyncEvents {
    fun interface BeforeInputSync {
        fun beforeInputSync(player: LocalPlayer)
    }

    fun interface AfterInputSync {
        fun afterInputSync(player: LocalPlayer)
    }

    fun interface BeforeMovementPacketsSend {
        fun beforePacketsSend(player: LocalPlayer, callback: CallbackInfo)
    }

    fun interface AfterMovementPacketsSend {
        fun afterPacketsSend(player: LocalPlayer)
    }

    fun interface BeforeVehiclePacketsSend {
        fun beforeVehiclePacketsSend(player: LocalPlayer, callback: CallbackInfo)
    }

    fun interface AfterVehiclePacketsSend {
        fun afterVehiclePacketsSend(player: LocalPlayer)
    }

    val BEFORE_INPUT_SYNC: Event<BeforeInputSync> =
        EventFactory.createArrayBacked(
            BeforeInputSync::class.java,
            BeforeInputSync { player: LocalPlayer -> }
        ) { listeners: Array<BeforeInputSync> ->
            BeforeInputSync { player: LocalPlayer ->
                for (listener in listeners) {
                    listener.beforeInputSync(player)
                }
            }
        }

    val AFTER_INPUT_SYNC: Event<AfterInputSync> =
        EventFactory.createArrayBacked(
            AfterInputSync::class.java,
            AfterInputSync { player: LocalPlayer -> }
        ) { listeners: Array<AfterInputSync> ->
            AfterInputSync { player: LocalPlayer ->
                for (listener in listeners) {
                    listener.afterInputSync(player)
                }
            }
        }

    val BEFORE_VEHICLE_PACKETS_SEND_EVENT: Event<BeforeVehiclePacketsSend> =
        EventFactory.createArrayBacked(BeforeVehiclePacketsSend::class.java,
            BeforeVehiclePacketsSend { player: LocalPlayer, callback: CallbackInfo -> }
        ) { listeners: Array<BeforeVehiclePacketsSend> ->
            BeforeVehiclePacketsSend { player: LocalPlayer, callback: CallbackInfo ->
                for (listener in listeners) {
                    listener.beforeVehiclePacketsSend(player, callback)

                    if (callback.isCancelled) {
                        return@BeforeVehiclePacketsSend
                    }
                }
            }
        }

    val BEFORE_MOVEMENT_PACKETS_SEND_EVENT: Event<BeforeMovementPacketsSend> = EventFactory.createArrayBacked(
            BeforeMovementPacketsSend::class.java,
            BeforeMovementPacketsSend { player: LocalPlayer, callback: CallbackInfo -> }
        ) { listeners: Array<BeforeMovementPacketsSend> ->
            BeforeMovementPacketsSend { player: LocalPlayer, callback: CallbackInfo ->
                for (listener in listeners) {
                    listener.beforePacketsSend(player, callback)

                    if (callback.isCancelled) {
                        return@BeforeMovementPacketsSend
                    }
                }
            }
        }

    val AFTER_MOVEMENT_PACKETS_SEND_EVENT: Event<AfterMovementPacketsSend> =
        EventFactory.createArrayBacked(AfterMovementPacketsSend::class.java,
            AfterMovementPacketsSend { player: LocalPlayer -> }
        ) { listeners: Array<AfterMovementPacketsSend> ->
            AfterMovementPacketsSend { player: LocalPlayer ->
                for (listener in listeners) {
                    listener.afterPacketsSend(player)
                }
            }
        }
}

package io.github.svegon.capi.event.network

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.network.ClientPlayerEntity
import java.util.function.Function

class PlayerPositionSyncEvents private constructor() {
    init {
        throw UnsupportedOperationException()
    }

    interface BeforeVehiclePacketsSend {
        fun beforeVehiclePacketsSend(player: ClientPlayerEntity?, callback: CallbackInfo?)
    }

    interface AfterVehiclePacketsSend {
        fun afterVehiclePacketsSend(player: ClientPlayerEntity?)
    }

    interface BeforeMovementPacketsSend {
        fun beforePacketsSend(player: ClientPlayerEntity?, callback: CallbackInfo?)
    }

    interface AfterMovementPacketsSend {
        fun afterPacketsSend(player: ClientPlayerEntity?)
    }

    companion object {
        @JvmField
        val BEFORE_VEHICLE_PACKETS_SEND_EVENT: Event<BeforeVehiclePacketsSend> =
            EventFactory.createArrayBacked<BeforeVehiclePacketsSend>(
                BeforeVehiclePacketsSend::class.java,
                BeforeVehiclePacketsSend { player: ClientPlayerEntity?, callback: CallbackInfo? -> },
                Function<Array<BeforeVehiclePacketsSend>, BeforeVehiclePacketsSend> { listeners: Array<BeforeVehiclePacketsSend> ->
                    BeforeVehiclePacketsSend { player: ClientPlayerEntity?, callback: CallbackInfo ->
                        for (listener in listeners) {
                            listener.beforeVehiclePacketsSend(player, callback)

                            if (callback.isCancelled()) {
                                return@BeforeVehiclePacketsSend
                            }
                        }
                    }
                })

        val AFTER_VEHICLE_PACKETS_SEND_EVENT: Event<AfterVehiclePacketsSend> =
            EventFactory.createArrayBacked<AfterVehiclePacketsSend>(
                AfterVehiclePacketsSend::class.java, AfterVehiclePacketsSend { player: ClientPlayerEntity? -> },
                Function<Array<AfterVehiclePacketsSend>, AfterVehiclePacketsSend> { listeners: Array<AfterVehiclePacketsSend> ->
                    AfterVehiclePacketsSend { player: ClientPlayerEntity? ->
                        for (listener in listeners) {
                            listener.afterVehiclePacketsSend(player)
                        }
                    }
                })

        @JvmField
        val BEFORE_MOVEMENT_PACKETS_SEND_EVENT: Event<BeforeMovementPacketsSend> =
            EventFactory.createArrayBacked<BeforeMovementPacketsSend>(
                BeforeMovementPacketsSend::class.java,
                BeforeMovementPacketsSend { player: ClientPlayerEntity?, callback: CallbackInfo? -> },
                Function<Array<BeforeMovementPacketsSend>, BeforeMovementPacketsSend> { listeners: Array<BeforeMovementPacketsSend> ->
                    BeforeMovementPacketsSend { player: ClientPlayerEntity?, callback: CallbackInfo ->
                        for (listener in listeners) {
                            listener.beforePacketsSend(player, callback)

                            if (callback.isCancelled()) {
                                return@BeforeMovementPacketsSend
                            }
                        }
                    }
                })

        @JvmField
        val AFTER_MOVEMENT_PACKETS_SEND_EVENT: Event<AfterMovementPacketsSend> =
            EventFactory.createArrayBacked<AfterMovementPacketsSend>(
                AfterMovementPacketsSend::class.java,
                AfterMovementPacketsSend { player: ClientPlayerEntity? -> },
                Function<Array<AfterMovementPacketsSend>, AfterMovementPacketsSend> { listeners: Array<AfterMovementPacketsSend> ->
                    AfterMovementPacketsSend { player: ClientPlayerEntity? ->
                        for (listener in listeners) {
                            listener.afterPacketsSend(player)
                        }
                    }
                })
    }
}
